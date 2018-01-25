import javax.swing.*;

public class AdmissionPanel extends JPanel {
    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, medicineColumns;

    public AdmissionPanel() {/*

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Hospital Admission");
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
        medicineColumns = new String[]{"Admission", "Release", "Patient Fiscal Code", "Patient Name", "Patient Surname", "Admission Cause", "Hospital ID", "Ward ID", "Room No"};
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
        columnModel.getColumn(0).setPreferredWidth(25);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(95);
        columnModel.getColumn(3).setPreferredWidth(60);
        columnModel.getColumn(4).setPreferredWidth(60);
        columnModel.getColumn(5).setPreferredWidth(110);
        columnModel.getColumn(6).setPreferredWidth(20);
        columnModel.getColumn(7).setPreferredWidth(10);
        columnModel.getColumn(8).setPreferredWidth(10);


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

    public Object[][] getAllAdmissionData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital_admission a INNER JOIN hospital h ON h.hospitalid = a.hospitalid " +
                "INNER JOIN patient p ON p.patientfiscalcode = a.patientfiscalcode";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                        rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                        rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

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
    public Object[][] getAdmissionDataFromInteger(String column, int number) {
        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital_admission a INNER JOIN hospital h ON h.hospitalid = a.hospitalid " +
                "INNER JOIN patient p ON p.patientfiscalcode = a.patientfiscalcode WHERE " + column + " = " + number;
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                        rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                        rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

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

    //Get all data when a string is inserted as query string
    public Object[][] getAdmissionDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital_admission a INNER JOIN hospital h ON h.hospitalid = a.hospitalid " +
                "INNER JOIN patient p ON p.patientfiscalcode = a.patientfiscalcode WHERE UPPER(" + column + ") = UPPER('" + stringToBeMatched + "')";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                        rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                        rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

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

    //Get all data when date is inserted as query string
    public Object[][] getAdmissionDataFromDate(String column, Date dateToBeMatched) {
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
                do {
                    Object[] row = {rs.getDate("admissiondate"), rs.getDate("releasedate"), rs.getString("patientfiscalcode"),
                            rs.getString("patientname"), rs.getString("patientsurname"), rs.getString("cause"),
                            rs.getInt("hospitalid"), rs.getInt("wardid"), rs.getInt("roomnumber")};

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
            int index;

            index = tab.getSelectedRow();

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
            firstRow.add(Box.createRigidArea(new Dimension(68, 0)));
            firstRow.add(admissionDateField);

            addPanel.add(firstRow);

            // Second row: Release Date
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel releaseDate = new JLabel("Release Date");
            releaseDate.setFont(new Font("Verdana", Font.PLAIN, 18));

            secondRow.add(releaseDate);
            secondRow.add(Box.createRigidArea(new Dimension(90, 0)));

            //If release date has not yet been inserted, the date can be choosen, otherwise the JTextField is disabled
            String dateExists = tab.getModel().getValueAt(index, 1).toString();
            boolean changed = false;
            JDatePanelImpl datePanel;
            JDatePickerImpl datePicker = null;
            JTextField releaseDateField = null;


            if (dateExists == "") {
                UtilDateModel model = new UtilDateModel();
                Properties p = new Properties();
                p.put("text.today", "Today");
                p.put("text.month", "Month");
                p.put("text.year", "Year");

                datePanel = new JDatePanelImpl(model, p);
                datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

                if (datePicker.getModel().getValue() != null)
                    changed = true;

                secondRow.add(datePicker);
            }

            else {
                releaseDateField = new JTextField(dateExists);
                releaseDateField.setEditable(false);
                secondRow.add(releaseDateField);
            }

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
            fourthRow.add(Box.createRigidArea(new Dimension(57, 0)));
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
            fifthRow.add(Box.createRigidArea(new Dimension(102, 0)));
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
            sixthRow.add(Box.createRigidArea(new Dimension(127, 0)));
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
            seventhRow.add(Box.createRigidArea(new Dimension(117, 0)));
            seventhRow.add(roomField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);


            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update hospital admission", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                java.util.Date admission = (java.util.Date) tab.getModel().getValueAt(index, 0);
                java.sql.Date sqlAdmission  = new java.sql.Date(admission.getTime());
                java.sql.Date sqlReleaseDate = null;

                if (changed) {
                    java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
                    sqlReleaseDate = new java.sql.Date(selectedDate.getTime());

                    if (sqlReleaseDate.after(Calendar.getInstance().getTime())) {
                        JOptionPane.showMessageDialog(container, "The release date cannot be later than today's date.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (sqlAdmission.after(sqlReleaseDate)){
                        JOptionPane.showMessageDialog(container, "The release date cannot be before the admission date.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
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

                if (wardIdField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Ward ID cannot be empty.\n " +
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
                                "both the hospital ID and the Ward ID are correct.", "No ward found!", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                } catch (NumberFormatException n){
                    JOptionPane.showMessageDialog(container, "Ward ID must be an integer.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                int checkRoom = 0;
                try {
                    checkRoom = Integer.parseInt(roomField.getText());

                    String checkDoctorExists = "SELECT * FROM bedroom WHERE bedroomnumber = " + checkRoom + " AND wardid = " + checkWardId + " AND hospitalid = " + checkHospitalId;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement s = conn.createStatement();

                    ResultSet res = s.executeQuery(checkDoctorExists);

                    if (!res.next()) {
                        JOptionPane.showMessageDialog(container, "No room found for the given ID in that hospital and ward. Please check that  \n" +
                                "the hospital ID, the Ward ID and the room No. are correct.", "No room found", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (res.getInt("noavailablebeds") == 0) {
                        JOptionPane.showMessageDialog(container, "This room is full and it cannot host another patient.", "Full room", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException n){
                    JOptionPane.showMessageDialog(container, "Ward ID must be an integer.\n " +
                            "No admission will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                String updateAdmission = "UPDATE hospital_admission SET releasedate = ?, wardid = ?, roomnumber = ? " +
                        "WHERE admissiondate = ? AND patientfiscalcode = ?";
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(updateAdmission);
                    if (sqlReleaseDate != null)
                        stat.setDate(1, sqlReleaseDate);
                    else
                        stat.setDate(1, Date.valueOf(releaseDateField.getText()));
                    stat.setInt(2, checkWardId);
                    stat.setInt(3, checkRoom);
                    stat.setDate(4, sqlAdmission);
                    stat.setString(5, patientFiscalCodeField.getText());

                    System.out.println(stat);

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Hospital admission updated successfully.");
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

    private class insertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {/*

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Admission Date
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel admissionDate = new JLabel("Admission Date");
            admissionDate.setFont(new Font("Verdana", Font.PLAIN, 18));

            UtilDateModel model = new UtilDateModel();
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");

            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            firstRow.add(admissionDate);
            firstRow.add(Box.createRigidArea(new Dimension(68, 0)));
            firstRow.add(datePicker);

            addPanel.add(firstRow);

            // Second row: Release Date
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel releaseDate = new JLabel("Release Date");
            releaseDate.setFont(new Font("Verdana", Font.PLAIN, 18));

            UtilDateModel model2 = new UtilDateModel();
            Properties p2 = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");

            JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
            JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

            firstRow.add(admissionDate);
            firstRow.add(Box.createRigidArea(new Dimension(68, 0)));
            firstRow.add(datePicker2);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Patient Fiscal Code
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel patientFiscalCode = new JLabel("Patient Fiscal Code");
            patientFiscalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField patientFiscalCodeField = new JTextField();
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
            JTextField admissionCauseField = new JTextField();
            admissionCauseField.setEditable(false);
            fourthRow.add(admissionCause);
            fourthRow.add(Box.createRigidArea(new Dimension(57, 0)));
            fourthRow.add(admissionCauseField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Hospital ID
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel hospitalId = new JLabel("Hospital ID ");
            hospitalId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField hospitalIdField = new JTextField();
            hospitalIdField.setEditable(false);
            fifthRow.add(hospitalId);
            fifthRow.add(Box.createRigidArea(new Dimension(102, 0)));
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
            sixthRow.add(Box.createRigidArea(new Dimension(127, 0)));
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
            seventhRow.add(Box.createRigidArea(new Dimension(117, 0)));
            seventhRow.add(roomField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update medicine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result
/*
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
            }*/
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
        columnModel.getColumn(0).setPreferredWidth(25);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(95);
        columnModel.getColumn(3).setPreferredWidth(60);
        columnModel.getColumn(4).setPreferredWidth(60);
        columnModel.getColumn(5).setPreferredWidth(110);
        columnModel.getColumn(6).setPreferredWidth(20);
        columnModel.getColumn(7).setPreferredWidth(10);
        columnModel.getColumn(8).setPreferredWidth(10);
    }*/

    }
}

