import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }


    // Create a new table
    public static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                 "name TEXT UNIQUE," +
                 "level INTEGER," +
                 "xp INTEGER" +
                 ");";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log("ERROR", "Erreur création table : " + e.getMessage());
        }
    }

    public static void createQuizTable() {
        String sql = "CREATE TABLE IF NOT EXISTS quizzes (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "title TEXT NOT NULL," +
                     "language TEXT NOT NULL" +
                     ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log("ERROR", "Erreur création table quizzes : " + e.getMessage());
        }
    }
    
    public static void createQuestionTable() {
        String sql = "CREATE TABLE IF NOT EXISTS questions (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "quiz_id INTEGER," +
                     "question_text TEXT NOT NULL," +
                     "options TEXT NOT NULL," + // ex: "réponse1;réponse2;réponse3"
                     "correct_answer_index INTEGER," +
                     "FOREIGN KEY (quiz_id) REFERENCES quizzes(id)" +
                     ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log("ERROR", "Erreur création table questions : " + e.getMessage());
        }
    }
    

    // Create a new player
    public static boolean createPlayer(String name) {
        String sql = "INSERT INTO players(name, level, xp) VALUES (?, 1, 0)";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            log("ERROR", "Erreur création joueur : " + e.getMessage());
            return false;
        }
    }
    
    // Save a player
    public static void savePlayer(Player player) {
        String sql = "INSERT OR REPLACE INTO players(name, level, xp) VALUES (?, ?, ?)";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getLevel());
            pstmt.setInt(3, player.getXp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log("ERROR", "Erreur insertion joueur : " + e.getMessage());
        }
    }

    // Get a player by name
    public static Player getPlayer(String name) {
        String sql = "SELECT * FROM players WHERE name = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Player player = new Player(rs.getString("name"));
                player.setLevel(rs.getInt("level"));
                player.setXp(rs.getInt("xp"));
                return player;
            }
        } catch (SQLException e) {
            log("ERROR", "Erreur récupération joueur : " + e.getMessage());
        }
        return null;
    }

    private static void log(String level, String message) {
        try (FileWriter fw = new FileWriter("logs.txt", true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write("[" + timestamp + "] [" + level + "] " + message + "\n");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier de log : " + e.getMessage());
        }
    }

    // Load quizzes by language
    public static List<QuizInfo> loadQuizzesByLanguage(String language) {
        List<QuizInfo> quizzes = new ArrayList<>();
        String sql = "SELECT id, title FROM quizzes WHERE language = ?";
    
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, language);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                quizzes.add(new QuizInfo(id, title));
            }
        } catch (SQLException e) {
            log("ERROR", "Erreur chargement quizzes : " + e.getMessage());
        }
        return quizzes;
    }

    // Load questions for a quiz
    public static List<Question> loadQuestionsForQuiz(int quizId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT question_text, options, correct_answer_index FROM questions WHERE quiz_id = ?";
    
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String questionText = rs.getString("question_text");
                String optionsString = rs.getString("options");
                int correctAnswer = rs.getInt("correct_answer_index");
    
                List<String> options = List.of(optionsString.split(";"));
    
                questions.add(new Question(questionText, options, correctAnswer));
            }
        } catch (SQLException e) {
            log("ERROR", "Erreur chargement questions : " + e.getMessage());
        }
        return questions;
    }
    
}


    
    
