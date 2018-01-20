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

public class DoctorPanel extends JPanel {

    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, doctorColumns;
    private JRadioButton practisingButton, retiredButton;
    private ButtonGroup group;
    private boolean isPractising;

    public DoctorPanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Doctor");
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
        boxColumns = new String[]{"Show all", "ID", "Name", "Surname", "Specialization", "Practising"};
        doctorColumns = new String[]{"ID", "Name", "Surname", "Specialization", "Practising"};
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

        Object[][] myData = getAllDoctorsData();
        tab = new JTable(/*myData, doctorColumns*/) {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };
        tab.setModel(new CustomTableModel(myData, doctorColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(90);
        columnModel.getColumn(4).setPreferredWidth(15);


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

    //Get all the data from the doctor table

    public Object[][] getAllDoctorsData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM doctor";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname"),
                        rs.getString("specialization"), rs.getBoolean("practising")};

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
    public Object[][] getDoctorDataFromInteger(int number) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM doctor WHERE doctorid = ?";
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
                    Object[] row = {rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname"),
                            rs.getString("specialization"), rs.getBoolean("practising")};
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
    public Object[][] getDoctorDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM doctor WHERE UPPER(" + column + ") = UPPER(?)";
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
                    Object[] row = {rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname"),
                            rs.getString("specialization"), rs.getBoolean("practising")};
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

    //Get all data when a boolean is inserted as query string
    public Object[][] getDoctorDataFromBoolean(String column, boolean bool) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM doctor WHERE (" + column + ") = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setBoolean(1, bool);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname"),
                            rs.getString("specialization"), rs.getBoolean("practising")};
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
            int doctorIDCheck;
            Object[][] myData;
            Object[][] allData;

            if (stringToBeMatched.length() != 0) {

                if (selectedColumn == "ID") {
                    try {
                        doctorIDCheck = Integer.parseInt(textField.getText());
                        myData = getDoctorDataFromInteger(doctorIDCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllDoctorsData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor id must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Name") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getDoctorDataFromString("doctor.doctorname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllDoctorsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Doctor name must be less than 30 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (selectedColumn == "Surname") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getDoctorDataFromString("doctor.doctorsurname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllDoctorsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Doctor surname must be less than 30 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (selectedColumn == "Specialization") {
                    if (stringToBeMatched.length() < 70) {
                        myData = getDoctorDataFromString("doctor.specialization", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllDoctorsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Specialization must be less than 70 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (selectedColumn == "Practising") {

                    if (stringToBeMatched.toUpperCase().equals("YES") || stringToBeMatched.toUpperCase().equals("NO")) {
                        boolean check;

                        if (stringToBeMatched.toUpperCase().equals("YES"))
                            check = true;
                        else
                            check = false;

                        myData = getDoctorDataFromBoolean("practising", check);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllDoctorsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Practising must be either \"yes\" or \"no\".", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                textField.setText("");
            } else {

                if (selectedColumn == "Show all") {
                    repaintTable(getAllDoctorsData());
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

            // First row: Doctor Id
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel id = new JLabel("Doctor ID");
            id.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField idField = new JTextField();
            idField.setText(String.valueOf(tab.getValueAt(index, 0)));
            idField.setEditable(false);
            firstRow.add(id);
            firstRow.add(Box.createRigidArea(new Dimension(90, 0)));
            firstRow.add(idField);

            addPanel.add(firstRow);

            // Second row: Doctor Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Doctor Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField();
            nameField.setText(String.valueOf(tab.getValueAt(index, 1)));
            nameField.setEditable(false);
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(60, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Doctor Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel surname = new JLabel("Doctor Surname");
            surname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField surnameField = new JTextField();
            surnameField.setText(String.valueOf(tab.getValueAt(index, 2)));
            surnameField.setEditable(false);
            thirdRow.add(surname);
            thirdRow.add(Box.createRigidArea(new Dimension(30, 0)));
            thirdRow.add(surnameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Specialization
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel specialization = new JLabel("Specialization");
            specialization.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField specializationField = new JTextField();
            specializationField.setText(String.valueOf(tab.getValueAt(index, 3)));
            fourthRow.add(specialization);
            fourthRow.add(Box.createRigidArea(new Dimension(55, 0)));
            fourthRow.add(specializationField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Practising
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel practising = new JLabel("Practising");
            practising.setFont(new Font("Verdana", Font.PLAIN, 18));

            practisingButton = new JRadioButton("Practising");
            retiredButton = new JRadioButton("Retired");
            group = new ButtonGroup();

            group.add(practisingButton);
            group.add(retiredButton);

            ActivityListener listener = new ActivityListener();
            practisingButton.addActionListener(listener);
            retiredButton.addActionListener(listener);

            fifthRow.add(practising);
            fifthRow.add(Box.createRigidArea(new Dimension(150, 0)));
            fifthRow.add(practisingButton);
            fifthRow.add(Box.createRigidArea(new Dimension(80, 0)));
            fifthRow.add(retiredButton);
            fifthRow.add(Box.createRigidArea(new Dimension(100,0)));

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update doctor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                //we add the doctor

                //doctor checks


                if (specializationField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Specialization field cannot be empty.\n" +
                            "No doctor will be updated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (specializationField.getText().length() > 70) {
                    JOptionPane.showMessageDialog(container, "Specialization should have less than 70 characters.\n " +
                            "No doctor will be updated.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (group.getSelection() == null) {
                    JOptionPane.showMessageDialog(container, "Practising or retired button not select.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    //we add the doctor

                    String addDoctor = " UPDATE doctor SET specialization = ?, practising = ?  WHERE doctorid = ?";

                    PreparedStatement updateDoctorStat = conn.prepareStatement(addDoctor);
                    updateDoctorStat.setString(1, specializationField.getText());
                    updateDoctorStat.setBoolean(2, isPractising);
                    updateDoctorStat.setInt(3, Integer.parseInt(idField.getText()));

                    int res = updateDoctorStat.executeUpdate();

                    //Confirm that doctor record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Doctor updated successfully.");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new DoctorPanel());
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

            // First row: Doctor Id
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel id = new JLabel("Doctor ID");
            id.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField idField = new JTextField();
            firstRow.add(id);
            firstRow.add(Box.createRigidArea(new Dimension(90, 0)));
            firstRow.add(idField);

            addPanel.add(firstRow);

            // Second row: Doctor Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Doctor Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField();
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(60, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Doctor Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel surname = new JLabel("Doctor Surname");
            surname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField surnameField = new JTextField();
            thirdRow.add(surname);
            thirdRow.add(Box.createRigidArea(new Dimension(30, 0)));
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
            fourthRow.add(Box.createRigidArea(new Dimension(55, 0)));
            fourthRow.add(specializationField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: Practising
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel practising = new JLabel("Practising");
            practising.setFont(new Font("Verdana", Font.PLAIN, 18));

            practisingButton = new JRadioButton("Practising");
            retiredButton = new JRadioButton("Retired");
            group = new ButtonGroup();

            group.add(practisingButton);
            group.add(retiredButton);

            ActivityListener listener = new ActivityListener();
            practisingButton.addActionListener(listener);
            retiredButton.addActionListener(listener);

            fifthRow.add(practising);
            fifthRow.add(Box.createRigidArea(new Dimension(150, 0)));
            fifthRow.add(practisingButton);
            fifthRow.add(Box.createRigidArea(new Dimension(80, 0)));
            fifthRow.add(retiredButton);
            fifthRow.add(Box.createRigidArea(new Dimension(100,0)));

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            //add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Add Doctor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                //first we need to check if the Doctor already exists, if not we have to add it

                boolean doctorAlreadyExists = checkDoctorExists(Integer.parseInt(idField.getText()));

                if (doctorAlreadyExists) {
                    JOptionPane.showMessageDialog(container, "This doctor ID already exists. Insert another doctor ID or modify the existing one.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    int doctorId;

                    //Doctor checks
                    try {
                        doctorId = Integer.parseInt(idField.getText());

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.\n" +
                                "No doctor will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nameField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "Doctor name field cannot be empty.\n" +
                                "No doctor will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nameField.getText().length() > 30) {
                        JOptionPane.showMessageDialog(container, "Doctor name should be less than 30 characters.\n" +
                                "No doctor will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (surnameField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "Doctor surname field cannot be empty.\n" +
                                "No doctor will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (surnameField.getText().length() > 30) {
                        JOptionPane.showMessageDialog(container, "Doctor surname should be less than 30 characters. \n" +
                                "No doctor will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (specializationField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "specialization field cannot be empty.\n" +
                                "No doctor will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (specializationField.getText().length() > 70) {
                        JOptionPane.showMessageDialog(container, "Specialization should have less then 70 characters.\n " +
                                "No doctor will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    //Add the doctor

                    String addDoctor = " INSERT INTO Doctor (doctorid, doctorname, doctorsurname, specialization, practising) VALUES (?,?,?,?,?)";

                    PreparedStatement addDoctorStat = conn.prepareStatement(addDoctor);

                    addDoctorStat.setInt(1, doctorId);
                    addDoctorStat.setString(2, nameField.getText());
                    addDoctorStat.setString(3, surnameField.getText());
                    addDoctorStat.setString(4, specializationField.getText());
                    addDoctorStat.setBoolean(5, isPractising);

                    int res = addDoctorStat.executeUpdate();

                    //Confirm that doctor record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Doctor added successfully");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new DoctorPanel());
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

    public void repaintTable(Object[][] dataToBeInserted) {
        //Show the found rows
        tab.setModel(new CustomTableModel(dataToBeInserted, doctorColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(90);
        columnModel.getColumn(4).setPreferredWidth(15);
    }

    private boolean checkDoctorExists (int id) {
        boolean doctorExists = false;

        String query = "SELECT * FROM doctor WHERE doctorid = ?";

        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement s = conn.prepareStatement(query);

            s.setInt(1, id);

            ResultSet res = s.executeQuery();

            if (res.next())
                doctorExists = true;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorExists;
    }

    private class ActivityListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == practisingButton)
                isPractising = true;

            else
                isPractising = false;
        }
    }
}