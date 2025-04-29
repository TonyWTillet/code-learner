package db;


import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseTask {
    void execute(Connection conn) throws SQLException;
}
