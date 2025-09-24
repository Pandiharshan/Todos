
package com.todo.gui;
import com.todo.dao.TodoAppDAO;
import com.todo.model.Todo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TodoAppGUI extends JFrame {
    private TodoAppDAO tododao;
    private DefaultTableModel tableModel;
    private JTable todoTable;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;
    private JComboBox<String> fillterComboBox;

    public TodoAppGUI() {
        this.tododao = new TodoAppDAO();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadTodos();
    }

    private void initializeComponents() {
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID","Title","Description","Completed","Created At","Updated At"};
        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        todoTable = new JTable(tableModel);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.getSelectionModel().addListSelectionListener(
                (e) -> {
                    if(!e.getValueIsAdjusting()){
                        loadSelectedtodo();
                    }
                }
        );

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(3,20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        completedCheckBox = new JCheckBox("Completed");
        addButton = new JButton("Add Todo");
        updateButton = new JButton ("Update Todo");
        deleteButton = new JButton("Delete Todo");
        refreshButton = new JButton("Refresh Todo");

        String[] fillterOptions = {"All","Completed","Pending"};
        fillterComboBox = new JComboBox<>(fillterOptions);
        fillterComboBox.addActionListener(
                (e)->{
                    filterTodos();
                }
        );
    }

    private void setupLayout(){
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        inputPanel.add(new JLabel("Title"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(completedCheckBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);

        JPanel fillterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fillterPanel.add(new JLabel("Filter"));
        fillterPanel.add(fillterComboBox);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        northPanel.add(fillterPanel, BorderLayout.NORTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(todoTable), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select a todo to edit or delete"));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupListeners(){
        addButton.addActionListener(e -> addTodo());
        updateButton.addActionListener(e -> updateTodo());
        deleteButton.addActionListener(e -> deleteTodo());
        refreshButton.addActionListener(e -> refreshTodo());

    }
    private void ClearForms(){
        titleField.setText("");
        descriptionArea.setText("");
        completedCheckBox.setSelected(false);
    }

    private void addTodo(){
        String title =titleField.getText().trim();
        String description =descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();
        if("".equals(title)){
//           if(title.isEmpty()): you can use this also
            JOptionPane.showMessageDialog(this,"Title cannot be empty!","Warning",JOptionPane.WARNING_MESSAGE);
        }
        try {
            Todo obj1 = new Todo(title, description);
            obj1.setCompleted(completed);
            tododao.createTodo(obj1);
            JOptionPane.showMessageDialog(this,"Todo added Successfully","Success",JOptionPane.INFORMATION_MESSAGE);
            loadTodos();

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateTodo(){
           int row = todoTable.getSelectedRow();
           if(row !=-1){
               String title =(String)todoTable.getValueAt(row,1);
               String description =(String)todoTable.getValueAt(row,2);
               boolean completed =(boolean)todoTable.getValueAt(row,3);
//               titleField.getText().trim();
//               descriptionArea.getText().trim();
//               completedCheckBox.getAccessibleContext(completed);
               if(title.isEmpty()){
                   JOptionPane.showMessageDialog(this,"Title cannot be empty!","Warning",JOptionPane.WARNING_MESSAGE);
                   return ;
               }
               descriptionArea.getText().trim();
               int id = (Integer)todoTable.getValueAt(row,0);
               try{
                   Todo todo =tododao.getTodoById(id);
                   if(todo!=null){
                       todo.setTitle(titleField.getText().trim());
                       todo.setDescription(descriptionArea.getText().trim());
                       todo.setCompleted(completedCheckBox.isSelected());
                       if(tododao.updateTodo(todo)){
                           JOptionPane.showMessageDialog(this,"todo updated successfully","Success",JOptionPane.INFORMATION_MESSAGE);
                           ClearForms();
                           loadTodos();
                       }
                       else{
                           JOptionPane.showMessageDialog(this,"Failed to update todos","Update error",JOptionPane.ERROR_MESSAGE);
                       }
                   }
               }
               catch(Exception e){
                   JOptionPane.showMessageDialog(this,e.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
               }

           }
           else{
               JOptionPane.showMessageDialog(this,"Plese select rows","Warning",JOptionPane.WARNING_MESSAGE);
               return ;
           }


    }
    private void deleteTodo(){
        int row =todoTable.getSelectedRow();
        if(row !=-1){
            int id = (Integer)todoTable.getValueAt(row,0);
            try{
                try{
                    tododao.deleteRow(id);
                    ClearForms();
                    loadTodos();
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(this,e.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
                }

            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(this,"Plese select rows","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }
    private void refreshTodo(){
        ClearForms();
        fillterComboBox.setSelectedIndex(0);
        loadTodos();
    }

    private void loadTodos(){
        try {
            List<Todo> todos = tododao.getAllTodos();
            updateTable(todos);
            ClearForms();
        } catch(Exception e){
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading todos: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateTable(List<Todo> todos){
        tableModel.setRowCount(0);
        for(Todo t : todos){
            Object[] row = {
                    t.getId(),
                    t.getTitle(),
                    t.getDescription(),
                    t.isCompleted(),
                    t.getCreated_at(),
                    t.getUpdated_at()
            };
            tableModel.addRow(row);
        }
    }
    private void loadSelectedtodo(){
        int row = todoTable.getSelectedRow();
        if(row!=-1){
            String title =tableModel.getValueAt(row,1).toString();
            String description =tableModel.getValueAt(row,2).toString();
            boolean completed =(boolean)tableModel.getValueAt(row,3);
            titleField.setText(title);
            descriptionArea.setText(description);
            completedCheckBox.setSelected(completed);


        }
    }
    private void filterTodos(){
        String option = fillterComboBox.getSelectedItem().toString();
        if(option.equals("All")){
                  loadTodos();
        }
        else if(option.equals("Completed")){
            try {
                updateTable(tododao.completedTodos());
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            try {
                updateTable(tododao.pendindTodos());
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}