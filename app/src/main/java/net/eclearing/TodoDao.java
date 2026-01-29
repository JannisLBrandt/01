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
    
    /**
     * Function to display todos, based on a limit and offset
     *
     * @param limit amount of tasks to display
     * @param offset start for displaying until limit
     * @return List<Task> a List of the tasks that fit
     */
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
    
    /**
     * Function to add a single task with a custom string.
     *
     * @param titleString the title of the task
     */
    public void addTask(String titleString) {

        // validate input
        if (titleString == null || titleString.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty or spaces only!");
        }

        if (titleString.length() > 255) {
            throw new IllegalArgumentException("Title cannot exceed 250 characters!");
        }
        
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
    
    /**
     * Function to delete a single task by id
     *
     * @param id the tasks id to be delete
     */
    public void deleteTask(long id) {

        // input validation
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be zero or smaller!");
        }

        String sql = "UPDATE todos SET deleted_at=? WHERE id=?";

        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(2, id);

            // check if data for the id even exists
            int rowsEffected = pstmt.executeUpdate();

            if (rowsEffected == 0 ) {
                throw new IllegalArgumentException("Task with id " + id + " not found" );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    /**
     * Function to mark task as completed
     *
     * @param id the tasks id to be marked as completed
     */
    public void completeTask(long id) {

        String sql = "UPDATE todos SET completed_at=? WHERE id=?";

        try (Connection conn = DataSourceProvider.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to complete task", e);
        }
    }
    
}
