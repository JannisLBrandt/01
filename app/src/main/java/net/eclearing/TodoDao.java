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

    public List<Task> display(int limit, int offset) {

        String sql = "SELECT id, created_at, modified_at, completed_at, title FROM todos WHERE deleted_at IS NULL ORDER BY id ASC LIMIT ? OFFSET ?";
        
        List<Task> todos = new ArrayList<>();

        try (
            Connection conn = DataSourceProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            try (ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    Task task = new Task(
                        result.getLong("id"),
                        result.getTimestamp("created_at").toLocalDateTime(),
                        result.getTimestamp("modified_at").toLocalDateTime(),
                        result.getTimestamp("completed_at") != null ? result.getTimestamp("completed_at").toLocalDateTime() : null,
                        null,
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

    // add a task via a String
    public void addTask(String titleString) {

        String sql = "INSERT INTO todos (created_at, modified_at, title) VALUES (?,?,?)";
        
        // create connection
        try (Connection conn = DataSourceProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, titleString);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add task", e);
        }
    }

    // delete a task by id
    public void deleteTask(long id) {

        String sql = "UPDATE todos SET deleted_at=? WHERE id=?";

        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(2, id);
            pstmt.executeUpdate();            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete task", e);
        }
    }
    
}
