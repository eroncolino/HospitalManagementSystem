import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class AdmissionPanel extends JPanel {
    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, medicineColumns;

    public AdmissionPanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Hospital Admissions");
        tb.setTitleFont(new Font("Verdana", Font.PLAIN, 30));
        tb.setTitleColor(Color.DARK_GRAY);
        setBorder(BorderFactory.createCompoundBorder(emptyBorder, tb));

        // Create the container panel
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Criteria (search by) Panel

        JPanel criteria = new JPanel();
        criteria.setLayout(new BoxLayout(criteria, BoxLayout.X_AXIS));
        searchLabel = new JLabel("Search by:");
        searchLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        boxColumns = new String[]{"Show all", "Admission Date", "Release Date", "Patient Fiscal Code", "Admission Cause", "Hospital ID"};
        medicineColumns = new String[]{"#", "Admission", "Release", "Patient Fiscal Code", "Patient Name", "Patient Surname", "Admission Cause", "Hospital ID", "Ward ID", "Room No"};
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

        //we need to read data in order to fill in the table

        Object[][] myData = getAllAdmissionData();

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
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(30);
        columnModel.getColumn(3).setPreferredWidth(95);
        columnModel.getColumn(4).setPreferredWidth(60);
        columnModel.getColumn(5).setPreferredWidth(60);
        columnModel.getColumn(6).setPreferredWidth(110);
        columnModel.getColumn(7).setPreferredWidth(20);
        columnModel.getColumn(8).setPreferredWidth(10);
        columnModel.getColumn(9).setPreferredWidth(10);

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

            if (indexes.length == 0 || tab.getModel().getValueAt(tab.getSelectedRow(), 1) !=  null) {
                updateButton.setEnabled(false);
            } else {
                updateButton.setEnabled(true);
            }
        });
    }

    //Get all the data from the medicine table
    private Object[][] getAllAdmissionData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital_admission a INNER JOIN hospital h ON h.hospitalid = a.hospitalid " +
                "INNER JOIN patient p ON p.patientfiscalcode = a.patientfiscalcode";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            int count = 0;

            while (rs.next()) {
                count++;

                Object[] row = {count, rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                        rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                        rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][10];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            dataReturn[i][7] = data.get(i)[7];
            dataReturn[i][8] = data.get(i)[8];
            dataReturn[i][9] = data.get(i)[9];
        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    private Object[][] getAdmissionDataFromInteger(String column, int number) {
        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital_admission a INNER JOIN hospital h ON h.hospitalid = a.hospitalid " +
                "INNER JOIN patient p ON p.patientfiscalcode = a.patientfiscalcode WHERE " + column + " = " + number;
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            int count = 0;

            while (rs.next()) {
                count++;
                Object[] row = {count, rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                        rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                        rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][10];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            dataReturn[i][7] = data.get(i)[7];
            dataReturn[i][8] = data.get(i)[8];
            dataReturn[i][9] = data.get(i)[9];
        }
        return dataReturn;
    }

    //Get all data when a string is inserted as query string
    private Object[][] getAdmissionDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital_admission a INNER JOIN hospital h ON h.hospitalid = a.hospitalid " +
                "INNER JOIN patient p ON p.patientfiscalcode = a.patientfiscalcode WHERE UPPER(" + column + ") = UPPER('" + stringToBeMatched + "')";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            int count = 0;

            while (rs.next()) {
                count++;
                Object[] row = {count, rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                        rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                        rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][10];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            dataReturn[i][7] = data.get(i)[7];
            dataReturn[i][8] = data.get(i)[8];
            dataReturn[i][9] = data.get(i)[9];
        }
        return dataReturn;
    }

    //Get all data when date is inserted as query string
    private Object[][] getAdmissionDataFromDate(String column, Date dateToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findDateQuery = "SELECT * FROM hospital_admission a INNER JOIN hospital h ON h.hospitalid = a.hospitalid " +
                "INNER JOIN patient p ON p.patientfiscalcode = a.patientfiscalcode WHERE " + column + " = ?";

        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findDateQuery);
            stmt.setDate(1, dateToBeMatched);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given date.");

            else {
                int count = 0;
                do {
                    count++;
                    Object[] row = {count, rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                            rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                            rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][10];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            dataReturn[i][7] = data.get(i)[7];
            dataReturn[i][8] = data.get(i)[8];
            dataReturn[i][9] = data.get(i)[9];
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

                if (selectedColumn == "Admission Date") {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    Date admissionDate;

                    try {
                        dateFormat.parse(stringToBeMatched);
                        admissionDate = Date.valueOf(stringToBeMatched);
                        myData = getAdmissionDataFromDate("admissiondate", admissionDate);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllAdmissionData();
                            repaintTable(allData);
                        }

                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(container, "Wrong date format.\nDate format must be the following: \"yyyy-mm-dd\"",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Release Date") {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    Date admissionDate;

                    try {
                        dateFormat.parse(stringToBeMatched);
                        admissionDate = Date.valueOf(stringToBeMatched);
                        myData = getAdmissionDataFromDate("releasedate", admissionDate);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllAdmissionData();
                            repaintTable(allData);
                        }

                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(container, "Wrong date format.\nDate format must be the following: \"yyyy-mm-dd\"",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Patient Fiscal Code") {
                    if (stringToBeMatched.length() == 16) {
                        myData = getAdmissionDataFromString("a.patientfiscalcode", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllAdmissionData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient fiscal code must be 16 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Admission Cause") {
                    if (stringToBeMatched.length() < 75) {
                        myData = getAdmissionDataFromString("cause", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllAdmissionData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Admission cause must be less than 75 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Hospital ID") {
                    try {
                        int hospitalIdCheck = Integer.parseInt(textField.getText());
                        myData = getAdmissionDataFromInteger("a.hospitalid", hospitalIdCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllAdmissionData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Hospital ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                textField.setText("");
            } else {
                if (selectedColumn == "Show all") {
                    repaintTable(getAllAdmissionData());
                    textField.setText("");
                } else
                    JOptionPane.showMessageDialog(container, "Enter the string to be found.", "Error", JOptionPane.ERROR_MESSAGE);
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

            // First row: Admission Date
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel admissionDate = new JLabel("Admission Date");
            admissionDate.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField admissionDateField = new JTextField(String.valueOf(tab.getModel().getValueAt(index, 0)));
            admissionDateField.setEditable(false);
            firstRow.add(admissionDate);
            firstRow.add(Box.createRigidArea(new Dimension(69, 0)));
            firstRow.add(admissionDateField);

            addPanel.add(firstRow);

            // Second row: Release Date
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel releaseDate = new JLabel("Release Date");
            releaseDate.setFont(new Font("Verdana", Font.PLAIN, 18));

            JDatePanelImpl datePanel;
            JDatePickerImpl datePicker;

            UtilDateModel model = new UtilDateModel();
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");

            datePanel = new JDatePanelImpl(model, p);
            datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            secondRow.add(releaseDate);
            secondRow.add(Box.createRigidArea(new Dimension(90, 0)));
            secondRow.add(datePicker);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Patient Fiscal Code
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel patientFiscalCode = new JLabel("Patient Fiscal Code");
            patientFiscalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField patientFiscalCodeField = new JTextField(tab.getModel().getValueAt(index, 2).toString());
            patientFiscalCodeField.setEditable(false);
            thirdRow.add(patientFiscalCode);
            thirdRow.add(Box.createRigidArea(new Dimension(40, 0)));
            thirdRow.add(patientFiscalCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Admission Cause
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel admissionCause = new JLabel("Admission Cause");
            admissionCause.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField admissionCauseField = new JTextField(tab.getModel().getValueAt(index, 5).toString());
            admissionCauseField.setEditable(false);
            fourthRow.add(admissionCause);
            fourthRow.add(Box.createRigidArea(new Dimension(58, 0)));
            fourthRow.add(admissionCauseField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Hospital ID
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel hospitalId = new JLabel("Hospital ID ");
            hospitalId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField hospitalIdField = new JTextField(tab.getModel().getValueAt(index, 6).toString());
            hospitalIdField.setEditable(false);
            fifthRow.add(hospitalId);
            fifthRow.add(Box.createRigidArea(new Dimension(103, 0)));
            fifthRow.add(hospitalIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Ward ID
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel wardId = new JLabel("Ward ID ");
            wardId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField wardIdField = new JTextField(tab.getModel().getValueAt(index, 7).toString());
            sixthRow.add(wardId);
            sixthRow.add(Box.createRigidArea(new Dimension(128, 0)));
            sixthRow.add(wardIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            // Sixth row: Room No.
            JPanel seventhRow = new JPanel();
            seventhRow.setLayout(new BoxLayout(seventhRow, BoxLayout.X_AXIS));

            JLabel room = new JLabel("Room No.");
            room.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField roomField = new JTextField(tab.getModel().getValueAt(index, 8).toString());
            seventhRow.add(room);
            seventhRow.add(Box.createRigidArea(new Dimension(120, 0)));
            seventhRow.add(roomField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update hospital admission", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                java.util.Date admission = (java.util.Date) tab.getModel().getValueAt(index, 0);
                java.sql.Date sqlAdmission = new java.sql.Date(admission.getTime());
                java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
                java.sql.Date sqlRelease = null;
                
                if (selectedDate != null){
                    sqlRelease = new java.sql.Date(selectedDate.getTime());
                    
                    if (sqlRelease.after(Calendar.getInstance().getTime())) {
                        JOptionPane.showMessageDialog(container, "The release date cannot be later than today's date.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (sqlAdmission.after(sqlRelease)) {
                        JOptionPane.showMessageDialog(container, "The release date cannot be before the admission date.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (wardIdField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Ward ID cannot be empty.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (wardIdField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Ward ID cannot be empty.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn;
                int checkWardId = 0;
                try {
                    checkWardId = Integer.parseInt(wardIdField.getText());

                    String checkDoctorExists = "SELECT * FROM ward WHERE wardid = " + checkWardId + " AND hospitalid = " + tab.getModel().getValueAt(index, 6);

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement s = conn.createStatement();

                    ResultSet res = s.executeQuery(checkDoctorExists);

                    if (!res.next()) {
                        JOptionPane.showMessageDialog(container, "No ward found for the given ID in that hospital. Please check that\n" +
                                "the ward ID is correct.", "No ward found!", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Ward ID must be an integer.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                int checkRoom = 0;
                try {
                    checkRoom = Integer.parseInt(roomField.getText());

                    String checkDoctorExists = "SELECT * FROM bedroom WHERE bedroomnumber = " + checkRoom + " AND wardid = " + checkWardId
                            + " AND hospitalid = " + tab.getModel().getValueAt(index, 6);

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement s = conn.createStatement();

                    ResultSet res = s.executeQuery(checkDoctorExists);

                    if (!res.next()) {
                        JOptionPane.showMessageDialog(container, "No room found for the given ID in that hospital and ward. Please check that\n" +
                                "the ward ID and the room No. are correct.", "No room found", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (res.getInt("noavailablebeds") == 0) {
                        JOptionPane.showMessageDialog(container, "This room is full and it cannot host another patient.", "Full room", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Ward ID must be an integer.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                //We update the ward and/or the room number only if the release date is still null, otherwise we just set the release date and ignore the
                //updated ward and id since it makes no sense to release a patient and update the room in which he/she stays at the same time.
                int oldRoomNo = (int) tab.getModel().getValueAt(index, 7);
                int oldWardId = (int) tab.getModel().getValueAt(index, 8);

                if (sqlRelease == null) {   //i.e. the patient is just changed room or ward
                    String updateWardAndRoom = "UPDATE hospital_admission SET wardid = ?, roomnumber = ? " +
                            "WHERE admissiondate = ? AND patientfiscalcode = ?";
                    try {
                        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                        PreparedStatement stat = conn.prepareStatement(updateWardAndRoom);
                        stat.setInt(1, checkWardId);
                        stat.setInt(2, checkRoom);
                        stat.setDate(3, sqlAdmission);
                        stat.setString(4, patientFiscalCodeField.getText());

                        int res = stat.executeUpdate();

                        //Confirm that hospital record has been added successfully
                        if (res > 0) {
                            JOptionPane.showMessageDialog(container, "Ward and room updated successfully.");

                            //If room or ward have changed, we update the number of available beds
                            if (Integer.parseInt(wardIdField.getText()) != oldWardId || Integer.parseInt(roomField.getText()) != oldRoomNo) {

                                //The old room has one new free bed
                                String updateOldRoom = "UPDATE bedroom SET noavailablebeds = noavailablebeds + 1 WHERE hospitalid = ? AND wardid = ? AND bedroomnumber = ?";
                                PreparedStatement s = conn.prepareStatement(updateOldRoom);
                                s.setInt(1, (Integer) tab.getModel().getValueAt(index, 6));
                                s.setInt(2, (Integer) tab.getModel().getValueAt(index, 7));
                                s.setInt(3, (Integer) tab.getModel().getValueAt(index, 8));

                                s.executeUpdate();
                                System.out.println("old room");

                                //The new room has one less available bed
                                String updateNewRoom = "UPDATE bedroom SET noavailablebeds = noavailablebeds - 1 WHERE hospitalid = ? AND wardid = ? AND bedroomnumber = ?";
                                PreparedStatement s2 = conn.prepareStatement(updateNewRoom);
                                s2.setInt(1, Integer.parseInt(hospitalIdField.getText()));
                                s2.setInt(2, Integer.parseInt(wardIdField.getText()));
                                s2.setInt(3, Integer.parseInt(roomField.getText()));

                                s2.executeUpdate();
                                System.out.println("new room");
                            }
                        }
                    } catch (SQLException s) {
                        s.printStackTrace();
                    }
                }
                else {  //i.e. patient has been released
                    String updateReleaseDate = "UPDATE hospital_admission SET releasedate = ? " +
                        "WHERE admissiondate = ? AND patientfiscalcode = ?";
                    try {
                        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                        PreparedStatement stat = conn.prepareStatement(updateReleaseDate);
                        stat.setDate(1, sqlRelease);
                        stat.setDate(2, sqlAdmission);
                        stat.setString(3, patientFiscalCodeField.getText());

                        int res = stat.executeUpdate();

                        //Confirm that hospital record has been added successfully
                        if (res > 0) {
                            JOptionPane.showMessageDialog(container, "Patient released from admission.\n" +
                                    "Note: if you changed the ward and/or room number \n" +
                                    "they will not be updated.");

                            //Update the number of available beds in the rooom where the patient was hosted
                            String updateRoom = "UPDATE bedroom SET noavailablebeds = noavailablebeds + 1 WHERE hospitalid = ? AND wardid = ? AND bedroomnumber = ?";
                            PreparedStatement s = conn.prepareStatement(updateRoom);
                            s.setInt(1, (Integer) tab.getModel().getValueAt(index, 6));
                            s.setInt(2, oldWardId);
                            s.setInt(3, oldRoomNo);

                            s.executeUpdate();
                            System.out.println("bed freed");
                        }
                    } catch (SQLException s) {
                        s.printStackTrace();
                    }
                }

                //Repaint the table

                AppFrame.frame.getContentPane().setVisible(false);
                AppFrame.frame.setContentPane(new AdmissionPanel());
                AppFrame.frame.getContentPane().setVisible(true);
            }
        }
        private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

            private String datePattern = "yyyy-MM-dd";
            private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

            @Override
            public Object stringToValue(String text) throws ParseException {
                return dateFormatter.parseObject(text);
            }

            @Override
            public String valueToString(Object value) throws ParseException {
                if (value != null) {
                    Calendar cal = (Calendar) value;
                    return dateFormatter.format(cal.getTime());
                }

                return "";
            }

        }
    }

    private class insertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = tab.getSelectedRow();
            boolean isReleased = true; //this boolean is need to update the number of available beds in the room

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Admission Date
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel admissionDate = new JLabel("Admission Date");
            admissionDate.setFont(new Font("Verdana", Font.PLAIN, 18));

            JDatePanelImpl datePanel;
            JDatePickerImpl datePicker;

            UtilDateModel model = new UtilDateModel();
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");

            datePanel = new JDatePanelImpl(model, p);
            datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            firstRow.add(admissionDate);
            firstRow.add(Box.createRigidArea(new Dimension(69, 0)));
            firstRow.add(datePicker);

            addPanel.add(firstRow);

            // Second row: Release Date
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel releaseDate = new JLabel("Release Date");
            releaseDate.setFont(new Font("Verdana", Font.PLAIN, 18));

            JDatePanelImpl datePanel2;
            JDatePickerImpl datePicker2;

            UtilDateModel model2 = new UtilDateModel();
            Properties p2 = new Properties();
            p2.put("text.today", "Today");
            p2.put("text.month", "Month");
            p2.put("text.year", "Year");

            datePanel2 = new JDatePanelImpl(model2, p2);
            datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

            secondRow.add(releaseDate);
            secondRow.add(Box.createRigidArea(new Dimension(90, 0)));
            secondRow.add(datePicker2);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Patient Fiscal Code
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel patientFiscalCode = new JLabel("Patient Fiscal Code");
            patientFiscalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField patientFiscalCodeField = new JTextField();
            thirdRow.add(patientFiscalCode);
            thirdRow.add(Box.createRigidArea(new Dimension(40, 0)));
            thirdRow.add(patientFiscalCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Admission Cause
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel admissionCause = new JLabel("Admission Cause");
            admissionCause.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField admissionCauseField = new JTextField();
            fourthRow.add(admissionCause);
            fourthRow.add(Box.createRigidArea(new Dimension(58, 0)));
            fourthRow.add(admissionCauseField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Hospital ID
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel hospitalId = new JLabel("Hospital ID ");
            hospitalId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField hospitalIdField = new JTextField();
            fifthRow.add(hospitalId);
            fifthRow.add(Box.createRigidArea(new Dimension(103, 0)));
            fifthRow.add(hospitalIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Ward ID
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel wardId = new JLabel("Ward ID ");
            wardId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField wardIdField = new JTextField();
            sixthRow.add(wardId);
            sixthRow.add(Box.createRigidArea(new Dimension(128, 0)));
            sixthRow.add(wardIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            // Sixth row: Room No.
            JPanel seventhRow = new JPanel();
            seventhRow.setLayout(new BoxLayout(seventhRow, BoxLayout.X_AXIS));

            JLabel room = new JLabel("Room No.");
            room.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField roomField = new JTextField();
            seventhRow.add(room);
            seventhRow.add(Box.createRigidArea(new Dimension(119, 0)));
            seventhRow.add(roomField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Insert hospital admission", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {

                java.util.Date selectedAdmission = (java.util.Date) datePicker.getModel().getValue();

                if (selectedAdmission == null) {
                    JOptionPane.showMessageDialog(container, "You must select an admission date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.sql.Date sqlAdmissionDate = new java.sql.Date(selectedAdmission.getTime());

                if (sqlAdmissionDate.after(Calendar.getInstance().getTime())) {
                    JOptionPane.showMessageDialog(container, "The admission date cannot be later than today's date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                isReleased = false;

                java.util.Date selectedRelease = (java.util.Date) datePicker2.getModel().getValue();
                java.sql.Date sqlReleaseDate = null;

                if (selectedRelease != null) {
                    sqlReleaseDate = new java.sql.Date(selectedRelease.getTime());

                    if (sqlReleaseDate.after(Calendar.getInstance().getTime())) {
                        JOptionPane.showMessageDialog(container, "The release date cannot be later than today's date.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (sqlAdmissionDate.after(sqlReleaseDate)) {
                        JOptionPane.showMessageDialog(container, "The release date cannot be before the admission date.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    isReleased = true;
                }

                if (hospitalIdField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Ward ID cannot be empty.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn;
                int checkHospitalId = 0;
                try {
                    checkHospitalId = Integer.parseInt(hospitalIdField.getText());

                    String checkDoctorExists = "SELECT * FROM hospital WHERE hospitalid = " + checkHospitalId;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement s = conn.createStatement();

                    ResultSet res = s.executeQuery(checkDoctorExists);

                    if (!res.next()) {
                        int addHospital = JOptionPane.showConfirmDialog(container, "No hospital found for the given ID. Please check if the hospital ID is correct or" +
                                "add a new hospital in the hospital section.\n" +
                                "Do you want to add a new hospital now?", "No hospital found!", JOptionPane.INFORMATION_MESSAGE);

                        if (addHospital == JOptionPane.YES_OPTION) {
                            AppFrame.frame.getContentPane().setVisible(false);
                            AppFrame.frame.setContentPane(new HospitalPanel());
                            AppFrame.frame.getContentPane().setVisible(true);
                        }
                    }
                }catch (SQLException e1) {
                    e1.printStackTrace();
                }

                if (hospitalIdField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Hospital ID cannot be empty.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int checkWardId = 0;
                try {
                    checkWardId = Integer.parseInt(wardIdField.getText());

                    String checkDoctorExists = "SELECT * FROM ward WHERE wardid = " + checkWardId + " AND hospitalid = " + checkHospitalId;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement s = conn.createStatement();

                    ResultSet res = s.executeQuery(checkDoctorExists);

                    if (!res.next()) {
                        JOptionPane.showMessageDialog(container, "No ward found for the given ID in that hospital. Please check that  \n" +
                                "both the hospital ID and the ward ID are correct.", "No ward found!", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                } catch (NumberFormatException n){
                    JOptionPane.showMessageDialog(container, "Ward ID must be an integer.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                int checkRoom;
                try {
                    checkRoom = Integer.parseInt(roomField.getText());

                    String checkDoctorExists = "SELECT * FROM bedroom WHERE bedroomnumber = " + checkRoom + " AND wardid = " + checkWardId + " AND hospitalid = " + checkHospitalId;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement s = conn.createStatement();

                    ResultSet res = s.executeQuery(checkDoctorExists);

                    if (!res.next()) {
                        JOptionPane.showMessageDialog(container, "No room found for the given ID in that hospital and ward. Please check that  \n" +
                                "the hospital ID, the ward ID and the room No. are correct.", "No room found", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (res.getInt("noavailablebeds") == 0) {
                        JOptionPane.showMessageDialog(container, "This room is full and it cannot host another patient.", "Full room", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException n){
                    JOptionPane.showMessageDialog(container, "Room ID must be an integer.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                String insertAdmission = "INSERT INTO hospital_admission (admissiondate, patientfiscalcode, cause, releasedate, hospitalid, " +
                        "wardid, roomnumber) VALUES (?,?,?,?,?,?,?)";
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(insertAdmission);
                    stat.setDate(1, sqlAdmissionDate);
                    stat.setString(2, patientFiscalCodeField.getText().toUpperCase());
                    stat.setString(3, admissionCauseField.getText());
                    stat.setDate(4, sqlReleaseDate);
                    stat.setInt(5, Integer.parseInt(hospitalIdField.getText()));
                    stat.setInt(6, Integer.parseInt(wardIdField.getText()));
                    stat.setInt(7, Integer.parseInt(roomField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Hospital admission updated successfully.");

                        //If the insertion has been completed successfully, we update the number of beds in the room
                        if(isReleased == false) {
                            String updateRoomNumber = "UPDATE bedroom SET noavailablebeds = noavailablebeds - 1 WHERE bedroomnumber = " + roomField.getText() +
                                    " AND wardid = " + wardIdField.getText() + " AND hospitalid = " + hospitalIdField.getText();

                            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                            Statement stmt = conn.createStatement();

                            int res2 = stmt.executeUpdate(updateRoomNumber);

                            if (res2 > 0)
                                JOptionPane.showMessageDialog(container, "The number of available beds in that room has also been updated.");
                        }
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new AdmissionPanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }
        private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

            private String datePattern = "yyyy-MM-dd";
            private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

            @Override
            public Object stringToValue(String text) throws ParseException {
                return dateFormatter.parseObject(text);
            }

            @Override
            public String valueToString(Object value) throws ParseException {
                if (value != null) {
                    Calendar cal = (Calendar) value;
                    return dateFormatter.format(cal.getTime());
                }

                return "";
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
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(30);
        columnModel.getColumn(3).setPreferredWidth(95);
        columnModel.getColumn(4).setPreferredWidth(60);
        columnModel.getColumn(5).setPreferredWidth(60);
        columnModel.getColumn(6).setPreferredWidth(110);
        columnModel.getColumn(7).setPreferredWidth(20);
        columnModel.getColumn(8).setPreferredWidth(10);
        columnModel.getColumn(9).setPreferredWidth(10);
    }
}


