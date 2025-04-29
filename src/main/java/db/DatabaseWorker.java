package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DatabaseWorker extends Thread {
    private static final String URL = "jdbc:sqlite:database.db";
    private static final BlockingQueue<DatabaseTask> queue = new LinkedBlockingQueue<>();

    private static DatabaseWorker instance;

    private DatabaseWorker() {}

    public static synchronized void startWorker() {
        if (instance == null) {
            instance = new DatabaseWorker();
            instance.start();
        }
    }

    public static void submit(DatabaseTask task) {
        queue.offer(task);
    }

    @Override
    public void run() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.createStatement().execute("PRAGMA journal_mode=WAL;");
            conn.createStatement().execute("PRAGMA busy_timeout=5000;");
            conn.createStatement().execute("PRAGMA synchronous=NORMAL;");

            while (true) {
                DatabaseTask task = queue.take(); // Attend une tâche
                try {
                    conn.setAutoCommit(false);
                    task.execute(conn);
                    conn.commit();
                } catch (SQLException e) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
