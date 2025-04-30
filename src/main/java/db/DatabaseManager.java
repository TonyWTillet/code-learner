package db;


import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.Player;
import model.Question;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:database.db";
    private static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private static final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private static final Runnable POISON_PILL = () -> {};

    static {
        dbExecutor.submit(() -> {
            while (true) {
                try {
                    Runnable task = queue.take();
                    if (task == POISON_PILL) {
                        break; // Arrêt propre du worker
                    }
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log("ERROR", "Erreur dans le worker DB : " + e.getMessage());
                }
            }
        });
    }

    // ==================== WAIT FOR QUEUE TO BE EMPTY ====================
    private static void waitUntilQueueIsEmpty() {
        while (!queue.isEmpty()) {
            try {
                Thread.sleep(50); // Attendre un peu
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    

    // ==================== CONNEXION ====================

    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
            stmt.execute("PRAGMA busy_timeout=5000;");
            stmt.execute("PRAGMA synchronous=NORMAL;");
        }
        return conn;
    }

    // ==================== LOGGING ====================

    private static void log(String level, String message) {
        String logDir = "src/main/ressources";
        String logPath = logDir + "/logs.txt";
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(logDir));
            try (FileWriter fw = new FileWriter(logPath, true)) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                fw.write("[" + timestamp + "] [" + level + "] " + message + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erreur d'écriture log : " + e.getMessage());
        }
    }

    // ==================== TABLES CREATION ====================

    public static void createTableIfNotExists() {
        createTable("CREATE TABLE IF NOT EXISTS players (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, level INTEGER, xp INTEGER);");
    }

    public static void createQuizTable() {
        createTable("CREATE TABLE IF NOT EXISTS quizzes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, language TEXT NOT NULL);");
    }

    public static void createQuestionTable() {
        createTable("CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, quiz_id INTEGER, question_text TEXT NOT NULL, options TEXT NOT NULL, correct_answer_index INTEGER, FOREIGN KEY (quiz_id) REFERENCES quizzes(id));");
    }

    public static void createCompletedQuizTable() {
        createTable("CREATE TABLE IF NOT EXISTS completed_quizzes (id INTEGER PRIMARY KEY AUTOINCREMENT, player_id INTEGER NOT NULL, quiz_id INTEGER NOT NULL, score INTEGER, status BOOLEAN, date_completed TEXT DEFAULT (datetime('now', 'localtime')), FOREIGN KEY (player_id) REFERENCES players(id), FOREIGN KEY (quiz_id) REFERENCES quizzes(id));");
    }

    private static void createTable(String sql) {
        waitUntilQueueIsEmpty();
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log("ERROR", "Erreur création table : " + e.getMessage());
        }
    }

    // ==================== PLAYER MANAGEMENT ====================

    public static boolean createPlayer(String name) {
        waitUntilQueueIsEmpty();
        String sql = "INSERT INTO players(name, level, xp) VALUES (?, 1, 0)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            log("ERROR", "Erreur création joueur : " + e.getMessage());
            return false;
        }
    }

    public static Player getPlayer(String name) {
        waitUntilQueueIsEmpty();
        String sql = "SELECT * FROM players WHERE name = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Player player = new Player(rs.getInt("id"), rs.getString("name"));
                    player.setLevel(rs.getInt("level"));
                    player.setXp(rs.getInt("xp"));
                    return player;
                }
            }
        } catch (SQLException e) {
            log("ERROR", "Erreur récupération joueur : " + e.getMessage());
        }
        return null;
    }

    public static void savePlayer(Player player) {
        queue.add(() -> {
            String sql = "UPDATE players SET level = ?, xp = ? WHERE id = ?";
            try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, player.getLevel());
                pstmt.setInt(2, player.getXp());
                pstmt.setInt(3, player.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                log("ERROR", "Erreur sauvegarde joueur : " + e.getMessage());
            }
        });
    }

    // ==================== QUIZZES MANAGEMENT ====================

    public static List<QuizInfo> loadQuizzesByLanguage(String language) {
        waitUntilQueueIsEmpty();
        List<QuizInfo> quizzes = new ArrayList<>();
        String sql = "SELECT id, title, language FROM quizzes WHERE language = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, language);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    quizzes.add(new QuizInfo(rs.getInt("id"), rs.getString("title"), rs.getString("language")));
                }
            }
        } catch (SQLException e) {
            log("ERROR", "Erreur chargement quizzes : " + e.getMessage());
        }
        return quizzes;
    }

    public static List<Question> loadQuestionsForQuiz(int quizId) {
        waitUntilQueueIsEmpty();
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT question_text, options, correct_answer_index FROM questions WHERE quiz_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quizId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String questionText = rs.getString("question_text");
                    String optionsString = rs.getString("options");
                    int correctAnswer = rs.getInt("correct_answer_index");
                    List<String> options = List.of(optionsString.split(";"));
                    questions.add(new Question(questionText, options, correctAnswer));
                }
            }
        } catch (SQLException e) {
            log("ERROR", "Erreur chargement questions : " + e.getMessage());
        }
        return questions;
    }

    public static void saveCompletedQuiz(int playerId, int quizId, int score, boolean status) {
        queue.add(() -> {
            String sql = "INSERT INTO completed_quizzes (player_id, quiz_id, score, status) VALUES (?, ?, ?, ?)";
            try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, playerId);
                pstmt.setInt(2, quizId);
                pstmt.setInt(3, score);
                pstmt.setBoolean(4, status);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                log("ERROR", "Erreur sauvegarde quiz terminé : " + e.getMessage());
            }
        });
    }

    public static void savePlayerAndCompletedQuiz(Player player, int quizId, int score, boolean perfect) {
        queue.add(() -> {
            Connection conn = null;
            try {
                conn = connect();
                conn.setAutoCommit(false);

                String savePlayerSQL = "UPDATE players SET level = ?, xp = ? WHERE id = ?";
                try (PreparedStatement pstmt1 = conn.prepareStatement(savePlayerSQL)) {
                    pstmt1.setInt(1, player.getLevel());
                    pstmt1.setInt(2, player.getXp());
                    pstmt1.setInt(3, player.getId());
                    pstmt1.executeUpdate();
                }

                String saveQuizSQL = "INSERT INTO completed_quizzes (player_id, quiz_id, score, status) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt2 = conn.prepareStatement(saveQuizSQL)) {
                    pstmt2.setInt(1, player.getId());
                    pstmt2.setInt(2, quizId);
                    pstmt2.setInt(3, score);
                    pstmt2.setBoolean(4, perfect);
                    pstmt2.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                log("ERROR", "Erreur transaction SavePlayerAndCompletedQuiz : " + e.getMessage());
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) { log("ERROR", "Erreur rollback : " + ex.getMessage()); }
                }
            } finally {
                if (conn != null) {
                    try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { log("ERROR", "Erreur fermeture connexion : " + e.getMessage()); }
                }
            }
        });
    }

    // ==================== SHUTDOWN ====================

    public static void shutdown() {
        queue.add(POISON_PILL); // Arrêter proprement le Worker
        dbExecutor.shutdown();
    }
}
