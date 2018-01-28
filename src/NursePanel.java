import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class NursePanel extends JPanel {

    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, nurseColumns;

    public NursePanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Nurses");
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
        boxColumns = new String[]{"Show all", "Nurse ID", "Name", "Surname", "Specialization", "Hospital ID", "Ward ID"};
        nurseColumns = new String[]{"Nurse ID", "Name", "Surname", "Specialization", "Hospital ID", "Hospital Name", "Ward ID", "Ward Name"};
        columnsList = new JComboBox(boxColumns);
        columnsList.setPreferredSize(new Dimension(200, 20));
        columnsList.setMaximumSize(new Dimension(200, 20));
        criteria.add(searchLabel);
        criteria.add(Box.createRigidArea(new Dimension(30, 0)));
        criteria.add(columnsList);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
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

        // we need to read data in order to fill in the table

        Object[][] myData = getAllNurseData();
        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };
        tab.setModel(new CustomTableModel(myData, nurseColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(25);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(110);
        columnModel.getColumn(4).setPreferredWidth(15);
        columnModel.getColumn(5).setPreferredWidth(190);
        columnModel.getColumn(6).setPreferredWidth(5);
        columnModel.getColumn(7).setPreferredWidth(135);

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
        findButton.addActionListener(new NursePanel.findListener());

        updateButton = new JButton("Update");
        updateButton.setEnabled(false);
        updateButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        updateButton.setMaximumSize(d);
        updateButton.setIcon(new ImageIcon("update.png"));
        updateButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        updateButton.addActionListener(new NursePanel.updateListener());

        insertButton = new JButton("Insert");
        insertButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        insertButton.setMaximumSize(d);
        insertButton.setIcon(new ImageIcon("insert.png"));
        insertButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        insertButton.addActionListener(new NursePanel.insertListener());

        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        deleteButton.setMaximumSize(d);
        deleteButton.setIcon(new ImageIcon("delete.png"));
        deleteButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        deleteButton.addActionListener(new NursePanel.deleteListener());

        goBackButton = new JButton("Go back");
        goBackButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        goBackButton.setMaximumSize(d);
        goBackButton.setIcon(new ImageIcon("goback.png"));
        goBackButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        goBackButton.addActionListener(new NursePanel.goBackListener());

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

        rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] indexes = tab.getSelectedRows();

                if (indexes.length == 0) {
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else {
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });
    }

    //Get all the data from the patient table
    public Object[][] getAllNurseData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * " +
                "FROM nurse n INNER JOIN ward w ON n.hospitalid = w.hospitalid AND n.wardid = w.wardid  " +
                "INNER JOIN hospital h ON w.hospitalid = h.hospitalid";

        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getInt("nurseid"), rs.getString("nursename"), rs.getString("nursesurname"),
                        rs.getString("specialization"), rs.getInt("hospitalid"), rs.getString("hospitalname"), rs.getInt("wardid"), rs.getString("wardname")};

                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][8];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            dataReturn[i][7] = data.get(i)[7];

        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    public Object[][] getNurseDataFromInteger(String column, int number) {
        ArrayList<Object[]> data = new ArrayList();
        String findIntQuery = "SELECT * " +
                " FROM nurse n INNER JOIN ward w ON n.hospitalid = w.hospitalid AND n.wardid = w.wardid " +
                " INNER JOIN hospital h ON w.hospitalid = h.hospitalid WHERE (" + column + ") = (?)";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIntQuery);
            stmt.setInt(1, number);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getInt("nurseid"), rs.getString("nursename"), rs.getString("nursesurname"),
                            rs.getString("specialization"), rs.getInt("hospitalid"), rs.getString("hospitalname"), rs.getInt("wardid"), rs.getString("wardname")};
                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][8];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            dataReturn[i][7] = data.get(i)[7];
        }
        return dataReturn;
    }

    //Get all data when a String is inserted as query string
    public Object[][] getNurseDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * " +
                "FROM nurse n INNER JOIN ward w ON n.hospitalid = w.hospitalid AND n.wardid = w.wardid " +
                "INNER JOIN hospital h ON w.hospitalid = h.hospitalid WHERE UPPER(" + column + ") = UPPER(?)";
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
                    Object[] row = {rs.getInt("nurseid"), rs.getString("nursename"), rs.getString("nursesurname"),
                            rs.getString("specialization"), rs.getInt("hospitalid"), rs.getString("hospitalname"), rs.getInt("wardid"), rs.getString("wardname")};
                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][8];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            dataReturn[i][7] = data.get(i)[7];

            // System.out.print(dataReturn[i][0] + "\n " + dataReturn[i][1] + " " + dataReturn[i][2] + "\n");
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

                if (selectedColumn == "Nurse ID") {
                    try {
                        int nurseIdCheck = Integer.parseInt(textField.getText());
                        myData = getNurseDataFromInteger("nurseid", nurseIdCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllNurseData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Nurse ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Name") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getNurseDataFromString("nursename", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllNurseData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Error: Nurse name must be less than 30 characters.");
                    }
                }
                if (selectedColumn == "Surname") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getNurseDataFromString("nursesurname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllNurseData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Error: Nurse Surname name must be less than 30 characters.");
                    }
                }
                if (selectedColumn == "Specialization") {
                    if (stringToBeMatched.length() < 70) {
                        myData = getNurseDataFromString("specialization", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllNurseData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Error: Specialization must be less than 70 characters.");
                    }
                }
                if (selectedColumn == "Hospital ID") {
                    try {
                        int hospitalIdCheck = Integer.parseInt(textField.getText());
                        myData = getNurseDataFromInteger("n.hospitalid", hospitalIdCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllNurseData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Hospital ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Ward ID") {
                    try {
                        int wardIdcheck = Integer.parseInt(textField.getText());
                        myData = getNurseDataFromInteger("n.wardid", wardIdcheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllNurseData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Ward ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                textField.setText("");
            } else {
                if (selectedColumn == "Show all") {
                    repaintTable(getAllNurseData());
                    textField.setText("");
                } else
                    JOptionPane.showMessageDialog(container, "Error: Enter the string to be found.");
            }
        }
    }

    private class updateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = tab.getSelectedRow();

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Nurse ID
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel nurseId = new JLabel("Nurse ID");
            nurseId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nurseIDField = new JTextField((tab.getModel().getValueAt(index, 0).toString()));
            nurseIDField.setEditable(false);
            firstRow.add(nurseId);
            firstRow.add(Box.createRigidArea(new Dimension(111, 0)));
            firstRow.add(nurseIDField);

            addPanel.add(firstRow);

            // Second row: Nurse Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Nurse Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField((tab.getModel().getValueAt(index, 1).toString()));
            nameField.setEditable(false);
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(80, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Nurse Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel surname = new JLabel("Nurse Surname");
            surname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField surnameField = new JTextField((tab.getModel().getValueAt(index, 2).toString()));
            surnameField.setEditable(false);
            thirdRow.add(surname);
            thirdRow.add(Box.createRigidArea(new Dimension(51, 0)));
            thirdRow.add(surnameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Specialization
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel specialization = new JLabel("Specialization");
            specialization.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField specializationField = new JTextField((tab.getModel().getValueAt(index, 3).toString()));
            fourthRow.add(specialization);
            fourthRow.add(Box.createRigidArea(new Dimension(68, 0)));
            fourthRow.add(specializationField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Hospital ID
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel hospId = new JLabel("Hospital ID");
            hospId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField hospIdField = new JTextField((tab.getModel().getValueAt(index, 4).toString()));
            fifthRow.add(hospId);
            fifthRow.add(Box.createRigidArea(new Dimension(91, 0)));
            fifthRow.add(hospIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Ward ID
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel wardId = new JLabel("Ward ID");
            wardId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField wardIdField = new JTextField((tab.getModel().getValueAt(index, 6).toString()));
            sixthRow.add(wardId);
            sixthRow.add(Box.createRigidArea(new Dimension(116, 0)));
            sixthRow.add(wardIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update nurse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);


            if (result == JOptionPane.YES_OPTION) {

                Connection conn;
                try {
                    int hospID = Integer.parseInt(hospIdField.getText());

                    String findHospital = "SELECT * FROM hospital WHERE hospitalid = " + hospID;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(findHospital);

                    if (!rs.next()) {
                        int addHospital = JOptionPane.showConfirmDialog(container, "No hospital found for the given ID. Please check if the hospital ID is correct or" +
                                "add a new hospital in the doctor section.\n" +
                                "Do you want to add a new hospital now?", "No hospital found!", JOptionPane.INFORMATION_MESSAGE);

                        if (addHospital == JOptionPane.YES_OPTION) {
                            AppFrame.frame.getContentPane().setVisible(false);
                            AppFrame.frame.setContentPane(new HospitalPanel());
                            AppFrame.frame.getContentPane().setVisible(true);
                        }
                        return;
                    }
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Hospital ID must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    int wardID = Integer.parseInt(wardIdField.getText());

                    String findWard = "SELECT * FROM ward WHERE wardid = " + wardID;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(findWard);

                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(container, "No ward found for the given ID.\n " +
                                "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                if (specializationField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Specialization field cannot be empty.\n " +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (specializationField.getText().length() > 70) {
                    JOptionPane.showMessageDialog(container, "Specialization field cannot be empty.\n " +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String updateMedicine = "UPDATE nurse SET specialization = ?, hospitalid = ?, wardid = ? WHERE nurseid = " + tab.getModel().getValueAt(index, 0);
                Connection con;
                try {
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = con.prepareStatement(updateMedicine);
                    stat.setString(1, specializationField.getText());
                    stat.setInt(2, Integer.parseInt(hospIdField.getText()));
                    stat.setInt(3, Integer.parseInt(wardIdField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Nurse updated successfully.");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new NursePanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException e1) {
                    e1.printStackTrace();
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

            // First row: Nurse ID
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel nurseId = new JLabel("Nurse ID");
            nurseId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nurseIdField = new JTextField();
            firstRow.add(nurseId);
            firstRow.add(Box.createRigidArea(new Dimension(111, 0)));
            firstRow.add(nurseIdField);

            addPanel.add(firstRow);

            // Second row: Nurse Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Nurse Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField();
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(80, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Nurse Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel surname = new JLabel("Nurse Surname");
            surname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField surnameField = new JTextField();
            thirdRow.add(surname);
            thirdRow.add(Box.createRigidArea(new Dimension(51, 0)));
            thirdRow.add(surnameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Specialization
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel specialization = new JLabel("Specialization");
            specialization.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField specializationField = new JTextField();
            fourthRow.add(specialization);
            fourthRow.add(Box.createRigidArea(new Dimension(68, 0)));
            fourthRow.add(specializationField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Hospital ID
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel hospId = new JLabel("Hospital ID");
            hospId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField hospIdField = new JTextField();
            fifthRow.add(hospId);
            fifthRow.add(Box.createRigidArea(new Dimension(91, 0)));
            fifthRow.add(hospIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Ward ID
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel wardId = new JLabel("Ward ID");
            wardId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField wardIdField = new JTextField();
            sixthRow.add(wardId);
            sixthRow.add(Box.createRigidArea(new Dimension(116, 0)));
            sixthRow.add(wardIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            //add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Add patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                Connection conn;

                //Nurse ID check

                try {
                    Integer.parseInt(nurseIdField.getText());

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Nurse ID must be an integer.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Name field cannot be empty.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nameField.getText().length() > 30) {
                    JOptionPane.showMessageDialog(container, "Name field should be less than 30 characters.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (surnameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Surname field cannot be empty.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (surnameField.getText().length() > 30) {
                    JOptionPane.showMessageDialog(container, "Surname field should be less than 30 characters.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (specializationField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Specialization field cannot be empty.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (specializationField.getText().length() > 70) {
                    JOptionPane.showMessageDialog(container, "Specialization field cannot be empty.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //Hospital check
                try {
                    int hospID = Integer.parseInt(hospIdField.getText());

                    String findHospital = "SELECT * FROM hospital WHERE hospitalid = " + hospID;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(findHospital);

                    if (!rs.next()) {
                        int addHospital = JOptionPane.showConfirmDialog(container, "No hospital found for the given ID. Please check if the hospital ID is correct or" +
                                "add a new hospital in the doctor section.\n" +
                                "Do you want to add a new hospital now?", "No hospital found!", JOptionPane.INFORMATION_MESSAGE);

                        if (addHospital == JOptionPane.YES_OPTION) {
                            AppFrame.frame.getContentPane().setVisible(false);
                            AppFrame.frame.setContentPane(new HospitalPanel());
                            AppFrame.frame.getContentPane().setVisible(true);
                        }
                        return;
                    }
                } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Hospital ID must be an integer.\n" +
                                "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                //Ward check

                try {
                    int wardID = Integer.parseInt(wardIdField.getText());

                    String findWard = "SELECT * FROM ward WHERE wardid = " + wardID;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(findWard);

                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(container, "No ward found for the given ID.\n" +
                                "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Ward ID must be an integer.\n" +
                            "Nurse will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }catch (SQLException e1) {
                    e1.printStackTrace();
                }

                String insertPatient = "INSERT INTO nurse(nurseid, nursename, nursesurname, specialization, hospitalid, wardid) values (?,?,?,?,?,?)";

                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(insertPatient);
                    stat.setInt(1, Integer.parseInt(nurseIdField.getText()));
                    stat.setString(2, nameField.getText());
                    stat.setString(3, surnameField.getText());
                    stat.setString(4, specializationField.getText());
                    stat.setInt(5, Integer.parseInt(hospIdField.getText()));
                    stat.setInt(6, Integer.parseInt(wardIdField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that patient record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Nurse added successfully.");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new NursePanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private class deleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = tab.getSelectedRow();
            int nurseID = (int) tab.getModel().getValueAt(index, 0);

            int result = JOptionPane.showConfirmDialog(container, "Are you sure you want to permanently delete the selected nurse?", "Warning", JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                String deleteNurse = "DELETE FROM nurse WHERE nurseid = ?";

                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement s = conn.prepareStatement(deleteNurse);
                    s.setInt(1, nurseID);

                    int res = s.executeUpdate();

                    //Confirm that nurse record has been deleted successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Nurse deleted successfully.");
                    }

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new NursePanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException e1) {
                    e1.printStackTrace();
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

    public void repaintTable(Object[][] dataToBeInserted) {
        //Show the found rows
        tab.setModel(new CustomTableModel(dataToBeInserted, nurseColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(25);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(110);
        columnModel.getColumn(4).setPreferredWidth(15);
        columnModel.getColumn(5).setPreferredWidth(190);
        columnModel.getColumn(6).setPreferredWidth(5);
        columnModel.getColumn(7).setPreferredWidth(135);

    }
}
