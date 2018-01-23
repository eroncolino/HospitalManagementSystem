import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class PrescriptionPanel extends JPanel {

    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private ArrayList<String> allPrescriptionNoList;
    private JPanel container;
    private String[] boxColumns, prescriptionColumns;

    public PrescriptionPanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Prescription");
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
        boxColumns = new String[]{"Show all", "Prescrpition No", "Medicine Name", "Patient Fiscal Code", "Patient Name", "Patient Surname", "Doctor ID", "Doctor Name", "Doctor Surname"};
        prescriptionColumns = new String[]{"Prescrpition No", "Medicine Name", "Qty", "Patient Fiscal Code", "Patient Name", "Surname", "Doctor ID", "Doctor Name", "Doctor Surname"};
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

        Object[][] myData = getAllPrescriptionData();

        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };

        tab.setModel(new CustomTableModel(myData, prescriptionColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(125);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(5);
        columnModel.getColumn(3).setPreferredWidth(130);
        columnModel.getColumn(4).setPreferredWidth(50);
        columnModel.getColumn(5).setPreferredWidth(47);
        columnModel.getColumn(6).setPreferredWidth(50);
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
        deleteButton.addActionListener(new deleteListener());

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
                deleteButton.setEnabled(false);
            } else {
                updateButton.setEnabled(false);
                deleteButton.setEnabled(true);
            }
        });
    }

    //Get all the data from the medicine table

    public Object[][] getAllPrescriptionData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM prescription p INNER JOIN medicine m ON p.medicinecode = m.medicinecode " +
                "INNER JOIN doctor d ON p.doctorid = d.doctorid INNER JOIN patient pat ON p.patientfiscalcode = pat.patientfiscalcode";
        Connection conn;
        allPrescriptionNoList = new ArrayList();

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getString("prescriptionno"), rs.getString("medicinename"), rs.getInt("medicinequantity"), rs.getString("patientfiscalcode"), rs.getString("patientname"),
                        rs.getString("patientsurname"), rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname")};

                data.add(row);
                allPrescriptionNoList.add(rs.getString("prescriptionno"));
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
    public Object[][] getPrescriptionDataFromInteger(String column, int number) {
        ArrayList<Object[]> data = new ArrayList();
        String findPrescNoQuery = "SELECT p.prescriptionno, m.patientfiscalcode, m.medicinequantity, m.patientfisclacode, m.patientname, m.patientsurname, d.doctorid, d.doctorname, d.doctorsurname " +
                "FROM prescription p INNER JOIN medicine m ON p.medicinecode = m.medicinecode " +
                "INNER JOIN doctor d ON p.doctorid = d.doctorid INNER JOIN patient pat ON p.patientfiscalcode = pat.patientfiscalcode" +
                " WHERE UPPER(" + column + ") = UPPER(?)";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findPrescNoQuery);
            stmt.setInt(1, number);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getString("prescriptionno"), rs.getString("medicinename"), rs.getInt("medicinequantity"), rs.getString("patientfiscalcode"), rs.getString("patientname"),
                            rs.getString("patientsurname"), rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname")};

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
    public Object[][] getPrescriptionDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findPrescriptionQuery = "SELECT * FROM prescription p INNER JOIN medicine m ON p.medicinecode = m.medicinecode " +
                "INNER JOIN doctor d ON p.doctorid = d.doctorid INNER JOIN patient pat ON p.patientfiscalcode = pat.patientfiscalcode" +
                " WHERE UPPER (" + column + ") = UPPER(?)";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findPrescriptionQuery);
            stmt.setString(1, stringToBeMatched);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given Integer.");

            else {
                do {
                    Object[] row = {rs.getString("prescriptionno"), rs.getString("medicinename"), rs.getInt("medicinequantity"), rs.getString("patientfiscalcode"), rs.getString("patientname"),
                            rs.getString("patientsurname"), rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname")};

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

                if (selectedColumn == "Prescription") {
                    if (stringToBeMatched.length() < 17) {
                        myData = getPrescriptionDataFromString("prescriptionno", stringToBeMatched.toUpperCase());

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Prescription No must be less than 17 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Patient Fiscal Code") {
                    if (stringToBeMatched.length() != 16) {
                        myData = getPrescriptionDataFromString("patientfiscalcode", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient Fiscal Code must be 16 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textField.setText("");
                }

                if (selectedColumn == "Medicine Quantity") {
                    try {
                        int medicineQuaCheck = Integer.parseInt(textField.getText());
                        myData = getPrescriptionDataFromInteger("medicinequantity", medicineQuaCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Medicine Quantity must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }


                if (selectedColumn == "Medicine Code") {
                    try {
                        int medicineCheck = Integer.parseInt(textField.getText());
                        myData = getPrescriptionDataFromInteger("mediciencode", medicineCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Medicine Code must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Medicine Name") {
                    if (stringToBeMatched.length() < 80) {
                        myData = getPrescriptionDataFromString("patientfiscalcode", stringToBeMatched);

                        if (myData.length != 0) {
                            repaintTable(myData);
                        } else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Patient Fiscal Code must be less than 80 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textField.setText("");
                }

                if (selectedColumn == "Doctor ID") {
                    try {
                        int doctorIDCheck = Integer.parseInt(textField.getText());
                        myData = getPrescriptionDataFromInteger("doctorid", doctorIDCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (selectedColumn == "Doctor Name") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getPrescriptionDataFromString("doctorname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Doctor Name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textField.setText("");
                }

                if (selectedColumn == "Doctor Surname") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getPrescriptionDataFromString("doctorsurname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllPrescriptionData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Doctor Surname must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textField.setText("");
                }

                textField.setText("");
            } else {
                if (selectedColumn == "Show all") {
                    repaintTable(getAllPrescriptionData());
                    textField.setText("");
                } else
                    JOptionPane.showMessageDialog(container, "Enter the string to be found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private class updateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    private class insertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = 0;
            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Prescription No
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel prescNo = new JLabel("Prescription No");
            prescNo.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField prescNoField = new JTextField();
            firstRow.add(prescNo);
            firstRow.add(Box.createRigidArea(new Dimension(60, 0)));
            firstRow.add(prescNoField);

            addPanel.add(firstRow);

            // Second row: Doctor ID
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel docId = new JLabel("Doctor ID");
            docId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField docIdField = new JTextField();
            secondRow.add(docId);
            secondRow.add(Box.createRigidArea(new Dimension(96, 0)));
            secondRow.add(docIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // third row: Patient fiscal code
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel patFisCode = new JLabel("Patient Fiscal Code");
            patFisCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField patFisCodeField = new JTextField();
            thirdRow.add(patFisCode);
            thirdRow.add(Box.createRigidArea(new Dimension(38, 0)));
            thirdRow.add(patFisCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Medicine code
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel medCode = new JLabel("Medicine Code");
            medCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField medCodeField = new JTextField();
            fourthRow.add(medCode);
            fourthRow.add(Box.createRigidArea(new Dimension(38, 0)));
            fourthRow.add(medCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Medicine Quantity
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel medQuan = new JLabel("Medicine Quantity");
            medQuan.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField medQuanField = new JTextField();
            fifthRow.add(medQuan);
            fifthRow.add(Box.createRigidArea(new Dimension(110, 0)));
            fifthRow.add(medQuanField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Insert Prescription", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //doctor check
                int doctorId;
                Connection conn;
                try {
                    int id = Integer.parseInt(docIdField.getText());

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

                //patient check

                try {
                    String fisCode = (patFisCodeField.getText().toUpperCase());

                    String findPatient = "SELECT * FROM patient WHERE patientfiscalcode = " + fisCode;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(findPatient);

                    if (!rs.next()) {
                        int addPatient = JOptionPane.showConfirmDialog(container, "No patient found for the given fiscal code. Please check if the fiscal code is correct or" +
                                "add a new patient in the patient section.\n" +
                                "Do you want to add a new patient now?", "No patient found!", JOptionPane.INFORMATION_MESSAGE);

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

                //medicine check
                try {
                    String medicine = (medCodeField.getText());

                    String findMedicine = "SELECT * FROM medicine WHERE medicinecode = " + medicine;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(findMedicine);

                    if (!rs.next()) {
                        int addPatient = JOptionPane.showConfirmDialog(container, "No medicine found for the given medicine code. Please check if the medicine code is correct or" +
                                "add a new medicine in the medicine section.\n" +
                                "Do you want to add a new medicine now?", "No medicine found!", JOptionPane.INFORMATION_MESSAGE);

                        if (addPatient == JOptionPane.YES_OPTION) {
                            AppFrame.frame.getContentPane().setVisible(false);
                            AppFrame.frame.setContentPane(new MedicinePanel());
                            AppFrame.frame.getContentPane().setVisible(true);
                        }
                        return;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                if (prescNoField.getText().length() != 17) {
                    JOptionPane.showMessageDialog(container, "Prescription No must be 17 characters. \n" +
                            "Prescription will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (patFisCodeField.getText().length() != 16) {
                    JOptionPane.showMessageDialog(container, "Fiscal code should be 16 characters.\n " +
                            "Prescription will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int medCodeInt = Integer.parseInt(medCodeField.getText());

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Medicine Code must be an integer.\n" +
                            "Prescription will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (medCodeField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Medicine Code field cannot be empty.\n " +
                            "Prescription will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int medQuant = Integer.parseInt(medQuanField.getText());

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Medicine quantity must be an integer.\n" +
                            "Prescription will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (medQuanField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Medicine quantity field cannot be empty.\n " +
                            "Prescription will not be inserted.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String insertPrescription = "INSERT INTO prescription (prescriptionno, doctorid, patientfiscalcode, medicinecode, medicinequantity) VALUES (?,?,?,?,?)";
                Connection con;
                try {
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = con.prepareStatement(insertPrescription);
                    stat.setString(1, prescNoField.getText());
                    stat.setInt(2, Integer.parseInt(docIdField.getText()));
                    stat.setString(3, patFisCodeField.getText().toUpperCase());
                    stat.setInt(4, Integer.parseInt(medCodeField.getText()));
                    stat.setInt(5, Integer.parseInt(medQuanField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Medicine added successfully.");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new PrescriptionPanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }
    }

    private class deleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = tab.getSelectedRow();
            String prescriptionNo = allPrescriptionNoList.get(index);

            int result = JOptionPane.showConfirmDialog(container, "Are you sure you want to permanently delete the selected prescription?", "Warning", JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                String deletePrescription = "DELETE FROM prescription WHERE prescriptionno = ?";

                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement s = conn.prepareStatement(deletePrescription);
                    s.setString(1, prescriptionNo);

                    int res = s.executeUpdate();

                    //Confirm that prescription record has been deleted successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Prescription deleted successfully.");
                    }

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new PrescriptionPanel());
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

    private void repaintTable(Object[][] dataToBeInserted) {
        //Show the found rows
        tab.setModel(new CustomTableModel(dataToBeInserted, prescriptionColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(125);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(5);
        columnModel.getColumn(3).setPreferredWidth(130);
        columnModel.getColumn(4).setPreferredWidth(50);
        columnModel.getColumn(5).setPreferredWidth(47);
        columnModel.getColumn(6).setPreferredWidth(50);
        columnModel.getColumn(7).setPreferredWidth(70);
    }
}

