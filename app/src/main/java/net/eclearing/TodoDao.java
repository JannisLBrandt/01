package net.eclearing;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class TodoDao {

    public List<Task> findAll(int limit, int offset) {

        String sql = "SELECT id, created_at, modified_at, completed_at, deleted_at, title FROM todos ORDER BY id ASC LIMIT ? OFFSET ?";
        
        List<Task> todos = new ArrayList<>();

        try (
            Connection conn = DataSourceProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            try (ResultSet result = pstmt.executeQuery()) {
                // TODO execute statement here
                while (result.next()) {
                    Task task = new Task(
                        result.getLong("id"),
                        result.getTimestamp("created_at").toLocalDateTime(),
                        result.getTimestamp("modified_at").toLocalDateTime(),
                        result.getTimestamp("completed_at") != null ? result.getTimestamp("completed_at").toLocalDateTime() : null,
                        result.getTimestamp("deleted_at") != null ? result.getTimestamp("deleted_at").toLocalDateTime() : null,
                        result.getString("title")
                    );
                    todos.add(task);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch todos", e);
        }
        return todos;
    }
}
