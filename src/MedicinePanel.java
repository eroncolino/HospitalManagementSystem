import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MedicinePanel extends JPanel {
    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, medicineColumns;

    public MedicinePanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Medicine");
        tb.setTitleFont(new Font("Verdana", Font.PLAIN, 30));
        tb.setTitleColor(Color.DARK_GRAY);
        setBorder(BorderFactory.createCompoundBorder(emptyBorder, tb));

        // Create the container panel
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Criteria (search by) Panel

        JPanel criteria = new JPanel();
        criteria.setLayout(new BoxLayout(criteria, BoxLayout.X_AXIS));
        searchLabel = new JLabel("Search by: ");
        searchLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        boxColumns = new String[]{"Show all", "Medicine Code", "Medicine Name", "Producer", "Active Substance", "Cost"};
        medicineColumns = new String[]{"Medicine Code", "Medicine Name", "Producer", "Active Substance", "Cost (in €)"};
        columnsList = new JComboBox(boxColumns);
        columnsList.setPreferredSize(new Dimension(200, 20));
        columnsList.setMaximumSize(new Dimension(200, 20));
        criteria.add(searchLabel);
        criteria.add(Box.createRigidArea(new Dimension(30, 0)));
        criteria.add(columnsList);
        container.add(criteria);

        // Parameter Panel

        JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout(new BoxLayout(parameterPanel, BoxLayout.X_AXIS));
        stringLabel = new JLabel("Enter string: ");
        stringLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 20));
        textField.setMaximumSize(new Dimension(200, 20));
        parameterPanel.add(stringLabel);
        parameterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        parameterPanel.add(textField);
        container.add(Box.createRigidArea(new Dimension(0, 50)));
        container.add(parameterPanel);

        // Main Part Panel (the row)

        JPanel mainRow = new JPanel();
        mainRow.setLayout(new BoxLayout(mainRow, BoxLayout.X_AXIS));

        // Table

        JPanel tablePanel = new JPanel();

        //we need to read data in order to fill in the table

        Object[][] myData = getAllMedicinesData();

        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };

        tab.setModel(new CustomTableModel(myData, medicineColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(250);
        columnModel.getColumn(4).setPreferredWidth(20);

        // Buttons

        JPanel buttonPanel = new JPanel();

        Dimension d = new Dimension(150, 40);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        findButton = new JButton("Find");
        findButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        findButton.setPreferredSize(d);
        findButton.setMaximumSize(new Dimension(150, 40));
        findButton.setIcon(new ImageIcon("glass.png"));
        findButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        findButton.addActionListener(new findListener());

        updateButton = new JButton("Update");
        updateButton.setEnabled(false);
        updateButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        updateButton.setMaximumSize(d);
        updateButton.setIcon(new ImageIcon("update.png"));
        updateButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        updateButton.addActionListener(new updateListener());

        insertButton = new JButton("Insert");
        insertButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        insertButton.setMaximumSize(d);
        insertButton.setIcon(new ImageIcon("insert.png"));
        insertButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        insertButton.addActionListener(new insertListener());

        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        deleteButton.setMaximumSize(d);
        deleteButton.setIcon(new ImageIcon("delete.png"));
        deleteButton.setHorizontalTextPosition(AbstractButton.RIGHT);

        goBackButton = new JButton("Go back");
        goBackButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        goBackButton.setMaximumSize(d);
        goBackButton.setIcon(new ImageIcon("goback.png"));
        goBackButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        goBackButton.addActionListener(new goBackListener());

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        buttonPanel.add(findButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(insertButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(goBackButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainRow.add(buttonPanel);

        // At the end

        container.add(Box.createRigidArea(new Dimension(0, 100)));
        container.add(mainRow);
        add(container);

        tab.setRowSelectionAllowed(true);
        ListSelectionModel rowSelectionModel = tab.getSelectionModel();
        rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        rowSelectionModel.addListSelectionListener(e -> {
            int[] indexes = tab.getSelectedRows();

            if (indexes.length == 0) {
                updateButton.setEnabled(false);
            } else {
                updateButton.setEnabled(true);
            }
        });
    }

    //Get all the data from the medicine table

    public Object[][] getAllMedicinesData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM medicine";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getInt("medicinecode"), rs.getString("medicinename"), rs.getString("producer"),
                        rs.getString("activesubstance"), rs.getString("cost")};

                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][5];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    public Object[][] getMedicineDataFromInteger(int number) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM medicine WHERE medicinecode = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setInt(1, number);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getInt("medicinecode"), rs.getString("medicinename"), rs.getString("producer"),
                            rs.getString("activesubstance"), rs.getString("cost")};

                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][5];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
        }
        return dataReturn;
    }

    //Get all data when a string is inserted as query string
    public Object[][] getMedicineDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM medicine WHERE UPPER(" + column + ") = UPPER(?)";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setString(1, stringToBeMatched);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getInt("medicinecode"), rs.getString("medicinename"), rs.getString("producer"),
                            rs.getString("activesubstance"), rs.getString("cost")};

                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][5];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
        }
        return dataReturn;
    }

    //Get all data when a money value is inserted as query string
    public Object[][] getMedicineDataFromMoney (double cost) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM medicine WHERE cost = " + cost;
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(findIdQuery);

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getInt("medicinecode"), rs.getString("medicinename"), rs.getString("producer"),
                            rs.getString("activesubstance"), rs.getString("cost")};

                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][5];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
        }
        return dataReturn;
    }

    private class findListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedColumn = (String) columnsList.getSelectedItem();
            String stringToBeMatched = textField.getText();
            Object[][] myData;
            Object[][] allData;

            if (stringToBeMatched.length() != 0) {

                if (selectedColumn == "Medicine Code") {
                    try {
                        int medicineCodeCheck = Integer.parseInt(textField.getText());
                        myData = getMedicineDataFromInteger(medicineCodeCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllMedicinesData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Medicine code must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Medicine Name") {
                    if (stringToBeMatched.length() < 80) {
                        myData = getMedicineDataFromString("medicinename", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllMedicinesData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Medicine name must be less than 80 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Producer") {
                    if (stringToBeMatched.length() < 80) {
                        myData = getMedicineDataFromString("producer", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllMedicinesData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Producer must be less than 80 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Active Substance") {
                    if (stringToBeMatched.length() < 150) {
                        myData = getMedicineDataFromString("activesubstance", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllMedicinesData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Active substance must be less than 150 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Cost") {
                    try {

                        double cost = Double.parseDouble(stringToBeMatched);

                        if (cost == 0){
                            JOptionPane.showMessageDialog(container, "The medicine cost cannot be zero.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (cost > 999.99){
                            JOptionPane.showMessageDialog(container, "The medicine cost cannot be greater than €999.99.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        myData = getMedicineDataFromMoney(cost);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllMedicinesData();
                            repaintTable(allData);
                        }
                    } catch(NumberFormatException n){
                        JOptionPane.showMessageDialog(container, "Cost must be of the format \"€€€.€€\".", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                textField.setText("");

            } else {
                if (selectedColumn == "Show all") {
                    repaintTable(getAllMedicinesData());
                    textField.setText("");
                } else
                    JOptionPane.showMessageDialog(container, "Enter the string to be found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private class updateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index;

            index = tab.getSelectedRow();

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Medicine Code
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel code = new JLabel("Medicine Code");
            code.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField codeField = new JTextField(String.valueOf(tab.getModel().getValueAt(index, 0)));
            codeField.setEditable(false);
            firstRow.add(code);
            firstRow.add(Box.createRigidArea(new Dimension(60, 0)));
            firstRow.add(codeField);

            addPanel.add(firstRow);

            // Second row: Medicine Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Medicine Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField(tab.getModel().getValueAt(index, 1).toString());
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(53, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Producer
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel producer = new JLabel("Producer");
            producer.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField producerField = new JTextField(tab.getModel().getValueAt(index, 2).toString());
            thirdRow.add(producer);
            thirdRow.add(Box.createRigidArea(new Dimension(110, 0)));
            thirdRow.add(producerField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Active Substance
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel activeSubstance = new JLabel("Active Substance");
            activeSubstance.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField activeSubstanceField = new JTextField(tab.getModel().getValueAt(index, 3).toString());
            fourthRow.add(activeSubstance);
            fourthRow.add(Box.createRigidArea(new Dimension(38, 0)));
            fourthRow.add(activeSubstanceField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Cost
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel cost = new JLabel("Cost (in €)");
            cost.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField costField = new JTextField(tab.getModel().getValueAt(index, 4).toString());
            fifthRow.add(cost);
            fifthRow.add(Box.createRigidArea(new Dimension(96, 0)));
            fifthRow.add(costField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);


            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update medicine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {

                if (nameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Medicine name field cannot be empty. \n" +
                            "No medicine will be updated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nameField.getText().length() > 80) {
                    JOptionPane.showMessageDialog(container, "Medicine name should be less than 80 characters. \n" +
                            "No medicine will be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (producerField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Producer field cannot be empty.\n" +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (producerField.getText().length() > 80) {
                    JOptionPane.showMessageDialog(container, "Producer should be less than 80 characters. \n" +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (activeSubstanceField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Active substance field cannot be empty.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (activeSubstanceField.getText().length() > 150) {
                    JOptionPane.showMessageDialog(container, "Active substance field must be less than 150 characters.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (Double.parseDouble(costField.getText()) == 0) {
                    JOptionPane.showMessageDialog(container, "The cost of the medicine cannot be zero.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (Double.parseDouble(costField.getText()) > 999.99) {
                    JOptionPane.showMessageDialog(container, "The cost of the medicine cannot be greater than 999.99.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String updateMedicine = "UPDATE medicine SET medicinename = ?, producer = ?, activesubstance = ?, cost = ? WHERE medicinecode = " + tab.getModel().getValueAt(index, 0);
                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(updateMedicine);
                    stat.setString(1, nameField.getText());
                    stat.setString(2, producerField.getText().toUpperCase());
                    stat.setString(3, activeSubstanceField.getText());
                    stat.setDouble(4, Double.parseDouble(costField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Medicine updated successfully.");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new MedicinePanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }
    }

    private class insertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Medicine Code
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel code = new JLabel("Medicine Code");
            code.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField codeField = new JTextField();
            firstRow.add(code);
            firstRow.add(Box.createRigidArea(new Dimension(60, 0)));
            firstRow.add(codeField);

            addPanel.add(firstRow);

            // Second row: Medicine Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Medicine Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField();
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(53, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Producer
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel producer = new JLabel("Producer");
            producer.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField producerField = new JTextField();
            thirdRow.add(producer);
            thirdRow.add(Box.createRigidArea(new Dimension(110, 0)));
            thirdRow.add(producerField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Active Substance
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel activeSubstance = new JLabel("Active Substance");
            activeSubstance.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField activeSubstanceField = new JTextField();
            fourthRow.add(activeSubstance);
            fourthRow.add(Box.createRigidArea(new Dimension(38, 0)));
            fourthRow.add(activeSubstanceField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Cost
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel cost = new JLabel("Cost (in €)");
            cost.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField costField = new JTextField();
            fifthRow.add(cost);
            fifthRow.add(Box.createRigidArea(new Dimension(96, 0)));
            fifthRow.add(costField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update medicine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {

                try {
                    Integer.parseInt(codeField.getText());

                } catch (NumberFormatException n){
                    JOptionPane.showMessageDialog(container, "Medicine code must be an integer. \n" +
                            "No medicine will be updated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Medicine name field cannot be empty. \n" +
                            "No medicine will be updated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nameField.getText().length() > 80) {
                    JOptionPane.showMessageDialog(container, "Medicine name should be less than 80 characters. \n" +
                            "No medicine will be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (producerField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Producer field cannot be empty.\n" +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (producerField.getText().length() > 80) {
                    JOptionPane.showMessageDialog(container, "Producer should be less than 80 characters. \n" +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (activeSubstanceField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Active substance field cannot be empty.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (activeSubstanceField.getText().length() > 150) {
                    JOptionPane.showMessageDialog(container, "Active substance field must be less than 150 characters.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (Double.parseDouble(costField.getText()) == 0) {
                    JOptionPane.showMessageDialog(container, "The cost of the medicine cannot be zero.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (Double.parseDouble(costField.getText()) > 999.99) {
                    JOptionPane.showMessageDialog(container, "The cost of the medicine cannot be greater than 999.99.\n " +
                            "The medicine will not be updated.","Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String updateMedicine = "INSERT INTO medicine (medicinecode, medicinename, producer, activesubstance, cost) VALUES (?,?,?,?,?)";
                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(updateMedicine);
                    stat.setInt(1, Integer.parseInt(codeField.getText()));
                    stat.setString(2, nameField.getText());
                    stat.setString(3, producerField.getText().toUpperCase());
                    stat.setString(4, activeSubstanceField.getText());
                    stat.setDouble(5, Double.parseDouble(costField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Medicine added successfully.");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new MedicinePanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }
    }

    private class goBackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new DashboardPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private void repaintTable(Object[][] dataToBeInserted) {
        //Show the found rows
        tab.setModel(new CustomTableModel(dataToBeInserted, medicineColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(250);
        columnModel.getColumn(4).setPreferredWidth(20);
    }
}
