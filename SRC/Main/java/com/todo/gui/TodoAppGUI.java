package com.todo.gui;

import javax.swing.*;
import java.awt.*;

public class TodoAppGUI extends JFrame {
    private JTextArea descriptionArea;
    private JCheckBox completedCheckbox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JComboBox<String> filterComboBox;
    private JLabel descriptionLabel;

    public TodoAppGUI() {
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        
        completedCheckbox = new JCheckBox("Completed");
        addButton = new JButton("Add Todo");
        updateButton = new JButton("Update Todo");
        deleteButton = new JButton("Delete Todo");
        refreshButton = new JButton("Refresh Todo");
        
        String[] filterOptions = {"All", "Completed", "Pending"};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setSelectedIndex(0);
        
        // Add action listeners
        filterComboBox.addActionListener(e -> filterTodos());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Description label and text area
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(descriptionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Checkbox
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(completedCheckbox, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(filterComboBox);
        
        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);
    }
    
    private void filterTodos() {
        // Implement filtering logic here
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        // TODO: Add filtering logic based on selectedFilter
    }
}