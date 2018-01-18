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

public class TreatmentPanel extends JPanel {

    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, treatmentColumns;

    public TreatmentPanel(){
        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Treatment");
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
        boxColumns = new String[]{"Show all", "Treatment Date", "Patient Fiscal Code", "Doctor ID", "Treatment Name", "Treatment Reason"};
        treatmentColumns = new String[]{"Date", "Patient Fiscal Code", "Patient Name", "Patient Surname", "Doctor ID", "Doctor Name", "Doctor Surname", "Treatment Name", "Treatment Reason"};
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

        // we need to read data in order to fill in the table

        Object[][] myData = getAllTreatmentsData();
        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };

        tab.setModel(new CustomTableModel(myData, treatmentColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(1000, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(110);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(75);
        columnModel.getColumn(4).setPreferredWidth(30);
        columnModel.getColumn(5).setPreferredWidth(70);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(7).setPreferredWidth(90);
        columnModel.getColumn(8).setPreferredWidth(120);


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
    }

    //Get all the data from the treatment table

    public Object[][] getAllTreatmentsData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM treatment t INNER JOIN doctor d ON d.doctorid = t.doctorid INNER JOIN patient  p ON p.patientfiscalcode = t.patientfiscalcode";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getDate("treatmentdate"), rs.getString("patientfiscalcode"), rs.getString("patientname"),
                        rs.getString("patientsurname"), rs.getInt("doctorid"), rs.getString("doctorname"),
                        rs.getString("doctorsurname"), rs.getString("treatmentname"), rs.getString("treatmentreason")};

                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][9];

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
        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    public Object[][] getTreatmentDataFromDoctorId(int doctorId) {
        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM treatment t INNER JOIN doctor d ON d.doctorid = t.doctorid INNER JOIN patient  p ON p.patientfiscalcode = t.patientfiscalcode " +
                "WHERE d.doctorid = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, doctorId);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given doctor ID.");

            else {
                do {
                    Object[] row = {rs.getDate("treatmentdate"), rs.getString("patientfiscalcode"), rs.getString("patientname"),
                            rs.getString("patientsurname"), rs.getInt("doctorid"), rs.getString("doctorname"),
                            rs.getString("doctorsurname"), rs.getString("treatmentname"), rs.getString("treatmentreason")};

                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][9];

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
        }

        return dataReturn;
    }

    //Get all data when a string is inserted as query string
    public Object[][] getTreatmentDataFromString (String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findQuery = "SELECT * FROM treatment t INNER JOIN doctor d ON d.doctorid = t.doctorid INNER JOIN patient  p ON p.patientfiscalcode = t.patientfiscalcode " +
                "WHERE UPPER(" + column + ") = UPPER (?)";

        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findQuery);
            stmt.setString(1, stringToBeMatched);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getDate("treatmentdate"), rs.getString("patientfiscalcode"), rs.getString("patientname"),
                            rs.getString("patientsurname"), rs.getInt("doctorid"), rs.getString("doctorname"),
                            rs.getString("doctorsurname"), rs.getString("treatmentname"), rs.getString("treatmentreason")};

                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][9];

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

        }
        return dataReturn;
    }

    //Get all data when date is inserted as query string
    public Object[][] getTreatmentDataFromDate(String column, Date dateToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM treatment t INNER JOIN doctor d ON d.doctorid = t.doctorid INNER JOIN patient  p ON p.patientfiscalcode = t.patientfiscalcode " +
                "WHERE " + column + " = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setDate(1, dateToBeMatched);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given date.");

            else {
                do {
                    Object[] row = {rs.getDate("treatmentdate"), rs.getString("patientfiscalcode"), rs.getString("patientname"),
                            rs.getString("patientsurname"), rs.getInt("doctorid"), rs.getString("doctorname"),
                            rs.getString("doctorsurname"), rs.getString("treatmentname"), rs.getString("treatmentreason")};

                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][9];

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

                if (selectedColumn == "Treatment Date") {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    Date treatmentDate;

                    try {
                        dateFormat.parse(stringToBeMatched);
                        treatmentDate = Date.valueOf(stringToBeMatched);
                        myData = getTreatmentDataFromDate("treatmentdate", treatmentDate);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTreatmentsData();
                            repaintTable(allData);
                        }

                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(container, "Wrong treatment date format.\nDate format must be the following: \"yyyy-mm-dd\"",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Patient Fiscal Code") {
                    if (stringToBeMatched.length() == 16) {
                        myData = getTreatmentDataFromString("t.patientfiscalcode", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTreatmentsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "The patient fiscal code must be 16 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (selectedColumn == "Patient Name") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getTreatmentDataFromString("patientname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTreatmentsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (selectedColumn == "Patient Surname") {
                    if (stringToBeMatched.length() == 5) {
                        myData = getTreatmentDataFromString("patientsurname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTreatmentsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient surname must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Doctor ID") {
                    int doctorId;

                    try {
                        doctorId = Integer.parseInt(stringToBeMatched);
                        myData = getTreatmentDataFromDoctorId(doctorId);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTreatmentsData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Treatment Name") {
                    if (stringToBeMatched.length() < 75) {
                        myData = getTreatmentDataFromString("treatmentname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTreatmentsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Treatment name must be less than 75 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Treatment Reason") {
                    if (stringToBeMatched.length() < 75) {
                        myData = getTreatmentDataFromString("treatmentreason", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTreatmentsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Treatment reason must be less than 75 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                textField.setText("");
            } else {
                if (selectedColumn == "Show all") {
                    repaintTable(getAllTreatmentsData());
                    textField.setText("");
                } else
                    JOptionPane.showMessageDialog(container, "Enter the string to be found.", "Error", JOptionPane.ERROR_MESSAGE);
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

            // First row: Treatment Date
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel dateLabel = new JLabel("Treatment Date");
            dateLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

            UtilDateModel model = new UtilDateModel();
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");

            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            firstRow.add(dateLabel);
            firstRow.add(Box.createRigidArea(new Dimension(80, 0)));
            firstRow.add(datePicker);

            addPanel.add(firstRow);

            // Second row: Patient Fiscal Code
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel patientFiscalCode = new JLabel("Patient Fiscal Code");
            patientFiscalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField patientFiscalCodeField = new JTextField();
            secondRow.add(patientFiscalCode);
            secondRow.add(Box.createRigidArea(new Dimension(55, 0)));
            secondRow.add(patientFiscalCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Doctor ID
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel doctorId = new JLabel("Doctor ID");
            doctorId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorIdField = new JTextField();
            thirdRow.add(doctorId);
            thirdRow.add(Box.createRigidArea(new Dimension(137, 0)));
            thirdRow.add(doctorIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Treatment Name
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel treatmentName = new JLabel("Treatment Name");
            treatmentName.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField treatmentNameField = new JTextField();
            fourthRow.add(treatmentName);
            fourthRow.add(Box.createRigidArea(new Dimension(70, 0)));
            fourthRow.add(treatmentNameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Treatment Reason
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel treatmentReason = new JLabel("Treatment Reason");
            treatmentReason.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField treatmentReasonField = new JTextField();
            fifthRow.add(treatmentReason);
            fifthRow.add(Box.createRigidArea(new Dimension(58, 0)));
            fifthRow.add(treatmentReasonField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            //add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Add treatment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {

                java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();

                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(container, "No date selected. Please select a treatment date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

                if (sqlDate.after(Calendar.getInstance().getTime())) {
                    JOptionPane.showMessageDialog(container, "The treatment date cannot be later than today's date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (patientFiscalCodeField.getText().length() != 16) {
                    JOptionPane.showMessageDialog(container, "The patient fiscal code must be 16 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn;

                String checkPatientExists = "SELECT * FROM patient WHERE UPPER (patientfiscalcode) =  UPPER(?)";

                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement s = conn.prepareStatement(checkPatientExists);
                    s.setString(1, patientFiscalCodeField.getText());

                    ResultSet rs = s.executeQuery();

                    if (!rs.next()) {
                        int addPatient = JOptionPane.showConfirmDialog(container, "No patient found for the given ID. Please check if the patient fiscal code" +
                                " is correct or \nadd a new patient in the patient section.\n" +
                                "Do you want to add a new patient now?", "No doctor found!", JOptionPane.INFORMATION_MESSAGE);

                        if (addPatient == JOptionPane.YES_OPTION) {
                            AppFrame.frame.getContentPane().setVisible(false);
                            AppFrame.frame.setContentPane(new PatientPanel());
                            AppFrame.frame.getContentPane().setVisible(true);
                        }
                        return;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    Integer.parseInt(doctorIdField.getText());

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int checkDoctorId = Integer.parseInt(doctorIdField.getText());

                String checkDoctorExists = "SELECT * FROM doctor WHERE doctorid = ?";

                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement s = conn.prepareStatement(checkDoctorExists);
                    s.setInt(1, checkDoctorId);

                    ResultSet rs = s.executeQuery();

                    if (!rs.next()) {
                        int addDoctor = JOptionPane.showConfirmDialog(container, "No doctor found for the given ID. Please check if the doctor ID is correct or" +
                                "\nadd a new doctor in the doctor section.\n" +
                                "Do you want to add a new doctor now?", "No doctor found!", JOptionPane.INFORMATION_MESSAGE);

                        if (addDoctor == JOptionPane.YES_OPTION) {
                            AppFrame.frame.getContentPane().setVisible(false);
                            AppFrame.frame.setContentPane(new DoctorPanel());
                            AppFrame.frame.getContentPane().setVisible(true);
                        }
                        return;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


                if (treatmentNameField.getText().length() > 75) {
                    JOptionPane.showMessageDialog(container, "The treatment name must be less than 75 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (treatmentReasonField.getText().length() > 75) {
                    JOptionPane.showMessageDialog(container, "The treatment reason must be less than 75 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String addTreatment = "INSERT INTO TREATMENT (treatmentdate, patientfiscalcode, doctorid, treatmentname, treatmentreason) VALUES (?,?,?,?,?)";

                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement stmt = conn.prepareStatement(addTreatment);

                    stmt.setDate(1, sqlDate);
                    stmt.setString(2, patientFiscalCodeField.getText().toUpperCase());
                    stmt.setInt(3, Integer.parseInt(doctorIdField.getText()));
                    stmt.setString(4, treatmentNameField.getText());
                    stmt.setString(5, treatmentReasonField.getText());

                    int res = stmt.executeUpdate();

                    //Confirm that treatment record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Treatment added successfully.");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new TreatmentPanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException e1) {
                    e1.printStackTrace();
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
        tab.setModel(new CustomTableModel(dataToBeInserted, treatmentColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(110);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(75);
        columnModel.getColumn(4).setPreferredWidth(30);
        columnModel.getColumn(5).setPreferredWidth(70);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(7).setPreferredWidth(90);
        columnModel.getColumn(8).setPreferredWidth(120);
    }

}
