
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class PatientPanel extends JPanel {
    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, patientColumns;

    public PatientPanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Patient");
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
        boxColumns = new String[]{"Show all", "Fiscal Code", "Name", "Surname", "Birth Date", "Gender", "Family Doctor ID"};
        patientColumns = new String[]{"Fiscal Code", "Name", "Surname", "Birth Date", "Gender", "Family Doctor ID", "Family Doctor Name", "Family Doctor Surname"};
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

        Object[][] myData = getAllPatientsData();
        tab = new JTable() {        //this deselects a row when the user clicks on a selected row
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };
        tab.setModel(new CustomTableModel(myData, patientColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(95);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(10);
        columnModel.getColumn(5).setPreferredWidth(20);
        columnModel.getColumn(6).setPreferredWidth(70);
        columnModel.getColumn(7).setPreferredWidth(70);

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
        findButton.addActionListener(new PatientPanel.findListener());

        updateButton = new JButton("Update");
        updateButton.setEnabled(false);
        updateButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        updateButton.setMaximumSize(d);
        updateButton.setIcon(new ImageIcon("update.png"));
        updateButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        updateButton.addActionListener(new PatientPanel.updateListener());

        insertButton = new JButton("Insert");
        insertButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        insertButton.setMaximumSize(d);
        insertButton.setIcon(new ImageIcon("insert.png"));
        insertButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        insertButton.addActionListener(new PatientPanel.insertListener());

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
        goBackButton.addActionListener(new PatientPanel.goBackListener());

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        buttonPanel.add(findButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(insertButton);
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

    //Get all the data from the patient table
    private Object[][] getAllPatientsData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * " +
                "FROM patient INNER JOIN doctor ON patient.familydoctorid = doctor.doctorid";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getString("patientfiscalcode"), rs.getString("patientname"), rs.getString("patientsurname"),
                        rs.getString("birthdate"), rs.getString("gender"), rs.getInt("familydoctorid"),
                        rs.getString("doctorname"), rs.getString("doctorsurname")};

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

    //Get all data when the doctor id is inserted as query string
    private Object[][] getPatientDataFromDoctorId(int id) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM patient INNER JOIN doctor ON patient.familydoctorid = doctor.doctorid WHERE familydoctorid = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given doctor ID.");

            else {
                do {
                    Object[] row = {rs.getInt("patientfisclacode"), rs.getString("patientname"), rs.getString("patientsurname"),
                            rs.getString("birthdate"), rs.getString("gender"), rs.getInt("familydoctorid"), rs.getString("doctorname"), rs.getString("doctorsurname")};
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

    //Get all data when ID is inserted as query string
    private Object[][] getPatientDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM patient INNER JOIN doctor ON patient.familydoctorid = doctor.doctorid WHERE UPPER(" + column + ") = UPPER(?)";
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
                    Object[] row = {rs.getString("patientfiscalcode"), rs.getString("patientname"), rs.getString("patientsurname"),
                            rs.getDate("birthdate"), rs.getString("gender"), rs.getInt("familydoctorid"), rs.getString("doctorname"), rs.getString("doctorsurname")};
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

    //Get all data when birth date is inserted as query string
    private Object[][] getPatientDataFromDate(Date dateToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findDateQuery = "SELECT * FROM patient INNER JOIN doctor ON patient.familydoctorid = doctor.doctorid WHERE birthdate = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findDateQuery);
            stmt.setDate(1, dateToBeMatched);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given date.");

            else {
                do {
                    Object[] row = {rs.getString("patientfiscalcode"), rs.getString("patientname"), rs.getString("patientsurname"),
                            rs.getDate("birthdate"), rs.getString("gender"), rs.getInt("familydoctorid"),
                            rs.getString("doctorname"), rs.getString("doctorsurname")};

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

    private class findListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedColumn = (String) columnsList.getSelectedItem();
            String stringToBeMatched = textField.getText();
            Object[][] myData;
            Object[][] allData;

            if (stringToBeMatched.length() != 0) {

                if (selectedColumn == "Fiscal Code") {
                    if (stringToBeMatched.length() == 16) {
                        myData = getPatientDataFromString("patientfiscalcode", stringToBeMatched);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPatientsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient fiscal code must be 16 characters long.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Name") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getPatientDataFromString("patientname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPatientsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Surname") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getPatientDataFromString("patientsurname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPatientsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient surname name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Birth Date") {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    Date birthDate;

                    try {
                        dateFormat.parse(stringToBeMatched);
                        birthDate = Date.valueOf(stringToBeMatched);
                        myData = getPatientDataFromDate(birthDate);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPatientsData();
                            repaintTable(allData);
                        }

                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(container, "Wrong patient's birth date format.\nDate format must be the following: \"yyyy-mm-dd\"",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Gender") {
                    if (stringToBeMatched.toUpperCase().equals("M") || stringToBeMatched.toUpperCase().equals("F")) {
                        myData = getPatientDataFromString("gender", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPatientsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Gender must be either \"M\" or \"F\".", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Family Doctor ID") {
                    try {
                        myData = getPatientDataFromDoctorId(Integer.parseInt(stringToBeMatched));

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPatientsData();
                            repaintTable(allData);
                        }
                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Family doctor  ID name must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                textField.setText("");

            } else {

                if (selectedColumn == "Show all") {
                    repaintTable(getAllPatientsData());
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

            // First row: Patient Fiscal Code
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel fiscalCode = new JLabel("Fiscal Code");
            fiscalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField fiscalCodeField = new JTextField(String.valueOf(tab.getModel().getValueAt(index, 0)));
            fiscalCodeField.setEditable(false);
            firstRow.add(fiscalCode);
            firstRow.add(Box.createRigidArea(new Dimension(90, 0)));
            firstRow.add(fiscalCodeField);

            addPanel.add(firstRow);

            // Second row: Patient Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Patient Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField(tab.getModel().getValueAt(index, 1).toString());
            nameField.setEditable(false);
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(70, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Patient Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel surname = new JLabel("Patient Surname");
            surname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField surnameField = new JTextField(tab.getModel().getValueAt(index, 2).toString());
            surnameField.setEditable(false);
            thirdRow.add(surname);
            thirdRow.add(Box.createRigidArea(new Dimension(41, 0)));
            thirdRow.add(surnameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Birth date
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel birthDate = new JLabel("Birth Date");
            birthDate.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField birthDateField = new JTextField(tab.getModel().getValueAt(index, 3).toString());
            birthDateField.setEditable(false);
            fourthRow.add(birthDate);
            fourthRow.add(Box.createRigidArea(new Dimension(101, 0)));
            fourthRow.add(birthDateField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Gender
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel gender = new JLabel("Gender");
            gender.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField genderField = new JTextField(tab.getModel().getValueAt(index, 4).toString());
            genderField.setEditable(false);
            fifthRow.add(gender);
            fifthRow.add(Box.createRigidArea(new Dimension(127, 0)));
            fifthRow.add(genderField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Family Doctor
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel familyDoctorId = new JLabel("Family Doctor ID");
            familyDoctorId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField familyDoctorIdField = new JTextField(tab.getModel().getValueAt(index, 5).toString());
            sixthRow.add(familyDoctorId);
            sixthRow.add(Box.createRigidArea(new Dimension(39, 0)));
            sixthRow.add(familyDoctorIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);
            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                //we add the patient

                //Family Doctor Id check

                Connection conn;
                String findDoctor = "SELECT * FROM doctor WHERE doctorid = " + familyDoctorIdField.getText();
                try {
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
                    } else {
                        int doctorId = rs.getInt("doctorid");

                        String updateQuery = "UPDATE patient SET familydoctorid = ? WHERE patientfiscalcode = ?";
                        PreparedStatement st = conn.prepareStatement(updateQuery);
                        st.setInt(1, doctorId);
                        st.setString(2, tab.getModel().getValueAt(index, 0).toString());

                        int res = st.executeUpdate();

                        if (res > 0)
                            JOptionPane.showMessageDialog(container, "Patient updated successfully.");
                    }
                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new PatientPanel());
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

            // First row: Patient Fiscal Code
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel fiscCode = new JLabel("Fiscal Code");
            fiscCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField fiscCodeField = new JTextField();
            firstRow.add(fiscCode);
            firstRow.add(Box.createRigidArea(new Dimension(90, 0)));
            firstRow.add(fiscCodeField);

            addPanel.add(firstRow);

            // Second row: Patient Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Patient Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField();
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(70, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Patient Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel surname = new JLabel("Patient Surname");
            surname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField surnameField = new JTextField();
            thirdRow.add(surname);
            thirdRow.add(Box.createRigidArea(new Dimension(41, 0)));
            thirdRow.add(surnameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: BirthDate
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel birthDate = new JLabel("Birth Date");
            birthDate.setFont(new Font("Verdana", Font.PLAIN, 18));

            UtilDateModel model = new UtilDateModel();
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");

            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            fourthRow.add(birthDate);
            fourthRow.add(Box.createRigidArea(new Dimension(101, 0)));
            fourthRow.add(datePicker);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Gender
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel gender = new JLabel("Gender");
            gender.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField genderField = new JTextField();
            fifthRow.add(gender);
            fifthRow.add(Box.createRigidArea(new Dimension(127, 0)));
            fifthRow.add(genderField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Family Doctor
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel familyDoctorId = new JLabel("Family Doctor ID");
            familyDoctorId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField familyDoctorIdField = new JTextField();
            sixthRow.add(familyDoctorId);
            sixthRow.add(Box.createRigidArea(new Dimension(39, 0)));
            sixthRow.add(familyDoctorIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);
            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            //add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Add patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {

                Connection conn;

                if (fiscCodeField.getText().length() != 16) {
                    JOptionPane.showMessageDialog(container, "Fiscal code should be 16 characters.\n " +
                            "No patient will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Name field cannot be empty.\n " +
                            "No patient will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nameField.getText().length() > 30) {
                    JOptionPane.showMessageDialog(container, "Name field should be less than 30 characters.\n " +
                            "No patient will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (surnameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Surname field cannot be empty.\n " +
                            "No patient will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (surnameField.getText().length() > 30) {
                    JOptionPane.showMessageDialog(container, "Surname field should be less than 30 characters.\n " +
                            "No patient will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();

                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(container, "No date selected. Please select a birth date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

                if (sqlDate.after(Calendar.getInstance().getTime())) {
                    JOptionPane.showMessageDialog(container, "The patient's birth date cannot be later than today's date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!genderField.getText().toUpperCase().equals("M") && !genderField.getText().toUpperCase().equals("F")) {
                    JOptionPane.showMessageDialog(container, "Gender field must be either \"M\" or \"F\".\n" +
                            "No patient will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int id = Integer.parseInt(familyDoctorIdField.getText());

                    String findDoctor = "SELECT * FROM doctor WHERE doctorid = " + id;

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
                    JOptionPane.showMessageDialog(container, "Doctor ID must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    String addPatient = "INSERT INTO patient(patientfiscalcode, patientname, patientsurname, birthdate, gender, familydoctorid) values (?,?,?,?,?,?)";

                    PreparedStatement stat = conn.prepareStatement(addPatient);
                    stat.setString(1, fiscCodeField.getText().toUpperCase());
                    stat.setString(2, nameField.getText());
                    stat.setString(3, surnameField.getText());
                    stat.setDate(4, sqlDate);
                    stat.setString(5, genderField.getText().toUpperCase());
                    stat.setInt(6, Integer.parseInt(familyDoctorIdField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that patient record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Patient added successfully.");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new PatientPanel());
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
        tab.setModel(new CustomTableModel(dataToBeInserted, patientColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(95);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(10);
        columnModel.getColumn(5).setPreferredWidth(20);
        columnModel.getColumn(6).setPreferredWidth(70);
        columnModel.getColumn(7).setPreferredWidth(70);
    }
}



