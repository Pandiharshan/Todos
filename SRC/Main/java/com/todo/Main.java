package com.todo;

import com.todo.gui.TodoAppGUI;
import com.todo.util.DatabaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Initialize database connection
        DatabaseConnection dbConnection = new DatabaseConnection();
        try (Connection conn = dbConnection.getDBConnection()) {
            System.out.println("Database connection successful!");
            
            // Launch the GUI on the Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> {
                TodoAppGUI todoApp = new TodoAppGUI();
                todoApp.setVisible(true);
            });
            
        } catch (SQLException e) {
            System.err.println("The database connection has failed: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to the database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}