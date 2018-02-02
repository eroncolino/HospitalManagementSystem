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

public class OutpatientsDepartmentPanel extends JPanel {
    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, departmentColumns;
    private int hospitalId, wardId;
    private String hospitalName, wardName;

    public OutpatientsDepartmentPanel(int hId, int wId, String hName, String wName) {
        hospitalId = hId;
        wardId = wId;
        hospitalName = hName;
        wardName = wName;

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder(wardName + "'s Outpatient's Departments (Hospital ID: " + hospitalId + ")");
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
        boxColumns = new String[]{"Show all", "Department Number", "Doctor ID"};
        departmentColumns = new String[]{"#", "Department Number", "Doctor ID", "Doctor Name", "Doctor Surname", "Doctor Specialization"};
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

        Object[][] myData = getAllDepartmentsData();
        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };

        tab.setModel(new CustomTableModel(myData, departmentColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(Box.createRigidArea(new Dimension(50, 0)));
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(150);
        columnModel.getColumn(5).setPreferredWidth(150);

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
        buttonPanel.add(Box.createRigidArea(new Dimension(100, 0)));
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

                if (indexes.length == 0)
                    updateButton.setEnabled(false);
                else
                    updateButton.setEnabled(true);
            }
        });
    }

    //Get all the data from the department table of the selected hospital
    public Object[][] getAllDepartmentsData() {

        ArrayList<Object[]> data = new ArrayList();
        String departmentQuery = "SELECT * FROM outpatients_department o LEFT JOIN doctor d ON d.doctorid = o.doctorid WHERE hospitalid = ? AND wardid = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement s = conn.prepareStatement(departmentQuery);
            s.setInt(1, hospitalId);
            s.setInt(2, wardId);

            ResultSet rs = s.executeQuery();

            int count = 0;

            while (rs.next()) {
                count++;

                Object[] row = {count, rs.getInt("departmentnumber"), rs.getInt("doctorid"),
                        rs.getString("doctorname"), rs.getString("doctorsurname"), rs.getString("specialization")};

                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][6];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    public Object[][] getDepartmentDataFromInteger(String column, int number) {
        ArrayList<Object[]> data = new ArrayList();
        String departmentQuery = "SELECT * FROM outpatients_department o LEFT JOIN doctor d ON d.doctorid = o.doctorid WHERE hospitalid = ? AND wardid = ? AND " + column + " = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement s = conn.prepareStatement(departmentQuery);
            s.setInt(1, hospitalId);
            s.setInt(2, wardId);
            s.setInt(3, number);

            ResultSet rs = s.executeQuery();

            int count = 0;

            while (rs.next()) {
                count++;

                Object[] row = {count, rs.getInt("departmentnumber"), rs.getInt("doctorid"),
                        rs.getString("doctorname"), rs.getString("doctorsurname"), rs.getString("specialization")};

                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][6];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
        }
        return dataReturn;
    }

    private class findListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedColumn = (String) columnsList.getSelectedItem();
            String stringToBeMatched = textField.getText();
            int departmentCheck, doctorIdCheck;
            Object[][] myData;
            Object[][] allData;

            if (stringToBeMatched.length() != 0) {

                if (selectedColumn == "Department Number") {
                    try {
                        departmentCheck = Integer.parseInt(textField.getText());
                        myData = getDepartmentDataFromInteger("departmentnumber", departmentCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllDepartmentsData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Department number must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Doctor ID") {
                    try {
                        doctorIdCheck = Integer.parseInt(textField.getText());
                        myData = getDepartmentDataFromInteger("o.doctorid", doctorIdCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllDepartmentsData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                textField.setText("");
            } else {

                if (selectedColumn == "Show all") {
                    repaintTable(getAllDepartmentsData());
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

            // First row: Department Number
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel departmentNumber = new JLabel("Department Number");
            departmentNumber.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField departmentNumberField = new JTextField(tab.getModel().getValueAt(index, 0).toString());
            firstRow.add(departmentNumber);
            firstRow.add(Box.createRigidArea(new Dimension(61, 0)));
            firstRow.add(departmentNumberField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(firstRow);

            // Second row: Doctor ID
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel doctorId = new JLabel("Doctor ID");
            doctorId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorIdField = new JTextField(tab.getModel().getValueAt(index, 1).toString());
            secondRow.add(doctorId);
            secondRow.add(Box.createRigidArea(new Dimension(160, 0)));
            secondRow.add(doctorIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container,
                    addPanel, "Update outpatient's department", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                Connection conn;

                try {
                    Integer.parseInt(departmentNumberField.getText());

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Department number must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!doctorIdField.getText().equals("")) {
                    try {
                        int doctorIdCheck = Integer.parseInt(doctorIdField.getText());

                        String findDoctor = "SELECT * FROM doctor WHERE doctorid = " + doctorIdCheck;

                        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(findDoctor);

                        if (!rs.next()) {
                            int addDoctor = JOptionPane.showConfirmDialog(container, "No doctor found for the given ID. Please check if the doctor ID is correct or" +
                                    "add a new doctor in the doctor section.\n" +
                                    "Do you want to add a new doctor now?", "No doctor found!", JOptionPane.INFORMATION_MESSAGE);

                            if (addDoctor == JOptionPane.YES_OPTION) {
                                AppFrame.frame.getContentPane().setVisible(false);
                                AppFrame.frame.setContentPane(new DoctorPanel());
                                AppFrame.frame.getContentPane().setVisible(true);
                            }
                            return;
                        }
                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                try {
                    String findDepartmentNumber = "SELECT * FROM outpatients_department WHERE departmentnumber = ? AND wardid = ? AND hospitalid = ?";
                    String findDoctorId = "SELECT * FROM outpatients_department WHERE doctorid = ? AND wardid = ? AND hospitalid = ?";

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement stmt1 = conn.prepareStatement(findDepartmentNumber);
                    stmt1.setInt(1, Integer.parseInt(departmentNumberField.getText()));
                    stmt1.setInt(2, wardId);
                    stmt1.setInt(3, hospitalId);

                    ResultSet rs1 = stmt1.executeQuery();

                    if (rs1.next() && !tab.getModel().getValueAt(index, 0).toString().equals(departmentNumberField.getText().toString())) {
                        JOptionPane.showMessageDialog(container, "This department number already exists.\n" +
                                "Outpatient's department will not be updated.", "Ward ID error", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (!doctorIdField.getText().equals("")) {
                        PreparedStatement stmt2 = conn.prepareStatement(findDoctorId);
                        stmt2.setInt(1, Integer.parseInt(doctorIdField.getText()));
                        stmt2.setInt(2, wardId);
                        stmt2.setInt(3, hospitalId);

                        ResultSet rs2 = stmt2.executeQuery();

                        if (rs2.next() && !tab.getModel().getValueAt(index, 0).toString().equals(departmentNumberField.getText().toString())) {
                            int choice = JOptionPane.showConfirmDialog(container, "This doctor has already an outpatient's department in this ward.\n" +
                                    "Do you want to assign also this department to this doctor?", "Warning", JOptionPane.INFORMATION_MESSAGE);
                            if (choice != JOptionPane.YES_OPTION)
                                return;
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                String updateDepartment = "UPDATE outpatients_department SET departmentnumber = ?, doctorid = ? WHERE departmentnumber = ? AND wardid = ? AND hospitalid = ?";
                Connection con;
                try {
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = con.prepareStatement(updateDepartment);
                    stat.setInt(1, Integer.parseInt(departmentNumberField.getText()));
                    if (!doctorIdField.getText().equals(""))
                        stat.setInt(2, Integer.parseInt(doctorIdField.getText()));
                    else
                        stat.setNull(2, java.sql.Types.INTEGER);
                    stat.setInt(3, Integer.parseInt(tab.getModel().getValueAt(index, 0).toString()));
                    stat.setInt(4, wardId);
                    stat.setInt(5, hospitalId);

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Outpatient's department updated successfully.");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new OutpatientsDepartmentPanel(hospitalId, wardId, hospitalName, wardName));
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
            int index;

            index = tab.getSelectedRow();

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));
            // First row: Ward Id
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel departmentNumber = new JLabel("Department Number");
            departmentNumber.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField departmentNumberField = new JTextField();
            firstRow.add(departmentNumber);
            firstRow.add(Box.createRigidArea(new Dimension(61, 0)));
            firstRow.add(departmentNumberField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(firstRow);

            // Second row: Ward Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel doctorId = new JLabel("Doctor ID");
            doctorId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorIdField = new JTextField();
            secondRow.add(doctorId);
            secondRow.add(Box.createRigidArea(new Dimension(160, 0)));
            secondRow.add(doctorIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Insert outpatient's department", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                Connection conn;

                try {
                    Integer.parseInt(departmentNumberField.getText());

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Deparment number must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!doctorIdField.getText().equals("")) {
                    try {
                        int doctorIdCheck = Integer.parseInt(doctorIdField.getText());

                        String findDoctor = "SELECT * FROM doctor WHERE doctorid = " + doctorIdCheck;

                        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(findDoctor);

                        if (!rs.next()) {
                            int addDoctor = JOptionPane.showConfirmDialog(container, "No doctor found for the given ID. Please check if the doctor ID is correct or" +
                                    "add a new doctor in the doctor section.\n" +
                                    "Do you want to add a new doctor now?", "No doctor found!", JOptionPane.INFORMATION_MESSAGE);

                            if (addDoctor == JOptionPane.YES_OPTION) {
                                AppFrame.frame.getContentPane().setVisible(false);
                                AppFrame.frame.setContentPane(new DoctorPanel());
                                AppFrame.frame.getContentPane().setVisible(true);
                            }
                            return;
                        }
                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                try {
                    String findDepartmentNumber = "SELECT * FROM outpatients_department WHERE departmentnumber = ? AND wardid = ? AND hospitalid = ?";
                    String findDoctorId = "SELECT * FROM outpatients_department WHERE doctorid = ? AND wardid = ? AND hospitalid = ?";

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement stmt1 = conn.prepareStatement(findDepartmentNumber);
                    stmt1.setInt(1, Integer.parseInt(departmentNumberField.getText()));
                    stmt1.setInt(2, wardId);
                    stmt1.setInt(3, hospitalId);

                    ResultSet rs1 = stmt1.executeQuery();

                    if (rs1.next() && !tab.getModel().getValueAt(index, 0).toString().equals(departmentNumberField.getText().toString())) {
                        JOptionPane.showMessageDialog(container, "This department number already exists.\n" +
                                "Outpatient's department will not be inserted.", "Ward ID error", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (!doctorIdField.getText().equals("")) {
                        PreparedStatement stmt2 = conn.prepareStatement(findDoctorId);
                        stmt2.setInt(1, Integer.parseInt(doctorIdField.getText()));
                        stmt2.setInt(2, wardId);
                        stmt2.setInt(3, hospitalId);

                        ResultSet rs2 = stmt2.executeQuery();

                        if (rs2.next() && !tab.getModel().getValueAt(index, 0).toString().equals(departmentNumberField.getText().toString())) {
                            int choice = JOptionPane.showConfirmDialog(container, "This doctor has already an outpatient's department in this ward.\n" +
                                    "Do you want to assign also this department to this doctor?", "Warning", JOptionPane.INFORMATION_MESSAGE);
                            if (choice != JOptionPane.YES_OPTION)
                                return;
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                String updateDepartment = "INSERT INTO outpatients_department (departmentnumber, wardid, hospitalid, doctorid) VALUES (?,?,?,?)";
                Connection con;
                try {
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = con.prepareStatement(updateDepartment);
                    stat.setInt(1, Integer.parseInt(departmentNumberField.getText()));
                    stat.setInt(2, wardId);
                    stat.setInt(3, hospitalId);
                    if (!doctorIdField.getText().equals(""))
                        stat.setInt(4, Integer.parseInt(doctorIdField.getText()));
                    else
                        stat.setNull(4, java.sql.Types.INTEGER);

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Outpatient's department inserted successfully.");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new OutpatientsDepartmentPanel(hospitalId, wardId, hospitalName, wardName));
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
            AppFrame.frame.setContentPane(new WardPanel(hospitalId, hospitalName));
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private void repaintTable(Object[][] dataToBeInserted) {
        //Show the found rows
        tab.setModel(new CustomTableModel(dataToBeInserted, departmentColumns));

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(150);
        columnModel.getColumn(5).setPreferredWidth(150);
    }
}
