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

public class TimetablePanel extends JPanel {

    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, timetableColumns;
    private ArrayList<Integer> allOfficesIdList;
    private ArrayList<Integer> allTimetablesIdList;
    private SimpleDateFormat simpleDateFormat;
    private String[] daysOfTheWeek = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};

    public TimetablePanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Timetable");
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
        boxColumns = new String[]{"Show all", "Doctor ID", "Doctor Name", "Doctor Surname", "Office Street", "ZIP Code", "City", "Province", "State", "Day", "Begin Time", "End Time"};
        timetableColumns = new String[]{"Doctor ID", "Doctor Name", "Doctor Surname", "Office Street", "ZIP Code", "City", "Province", "State", "Day", "Begin Time", "End Time"};
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

        Object[][] myData = getAllTimetableData();
        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };
        tab.setModel(new CustomTableModel(myData, timetableColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(90);
        columnModel.getColumn(2).setPreferredWidth(90);
        columnModel.getColumn(3).setPreferredWidth(140);
        columnModel.getColumn(4).setPreferredWidth(20);
        columnModel.getColumn(5).setPreferredWidth(50);
        columnModel.getColumn(6).setPreferredWidth(20);
        columnModel.getColumn(7).setPreferredWidth(5);
        columnModel.getColumn(8).setPreferredWidth(20);
        columnModel.getColumn(9).setPreferredWidth(35);
        columnModel.getColumn(10).setPreferredWidth(30);

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

    //Get all the data from the timetable table joined with address, private_office and doctor tables

    public Object[][] getAllTimetableData() {

        ArrayList<Object[]> data = new ArrayList();
        allOfficesIdList = new ArrayList();
        allTimetablesIdList = new ArrayList();

        String query = "SELECT d.doctorid, d.doctorname, d.doctorsurname, a.street AS officestreet, a.postalcode, a.city, a.province, a.state, t.timetableid, t.officeid, t.dayoftheweek, t.beginningtime, t.endtime " +
                "FROM timetable t INNER JOIN doctor d ON t.doctorid = d.doctorid " +
                "INNER JOIN private_office o ON t.officeid = o.officeid " +
                "INNER JOIN address a ON o.officeaddress = a.addressid ";

        Connection conn;

        simpleDateFormat = new SimpleDateFormat("HH:mm");

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname"), rs.getString("officestreet"),
                        rs.getString("postalcode"), rs.getString("city"), rs.getString("province"),
                        rs.getString("state"), rs.getString("dayoftheweek"),
                        simpleDateFormat.format(rs.getTime("beginningtime")), simpleDateFormat.format(rs.getTime("endtime"))};

                data.add(row);

                allOfficesIdList.add(rs.getInt("officeid"));
                allTimetablesIdList.add(rs.getInt("timetableid"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][11];

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
            dataReturn[i][10] = data.get(i)[10];
        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    public Object[][] getTimetableDataFromDoctorId(int doctorId) {
        ArrayList<Object[]> data = new ArrayList();
        allOfficesIdList = new ArrayList();
        allTimetablesIdList = new ArrayList();

        String findIdQuery = "SELECT d.doctorid, d.doctorname, d.doctorsurname, a.street AS officestreet, a.postalcode, a.city, a.province, a.state, t.timetableid, t.officeid, t.dayoftheweek, t.beginningtime, t.endtime\n" +
                "FROM timetable t INNER JOIN doctor d ON t.doctorid = d.doctorid\n" +
                "INNER JOIN private_office o ON t.officeid = o.officeid\n" +
                "INNER JOIN address a ON o.officeaddress = a.addressid WHERE d.doctorid = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setInt(1, doctorId);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname"),
                            rs.getString("officestreet"), rs.getString("postalcode"), rs.getString("city"),
                            rs.getString("province"), rs.getString("state"), rs.getString("dayoftheweek"),
                            simpleDateFormat.format(rs.getTime("beginningtime")), simpleDateFormat.format(rs.getTime("endtime"))};
                    data.add(row);

                    allOfficesIdList.add(rs.getInt("officeid"));
                    allTimetablesIdList.add(rs.getInt("timetableid"));

                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][11];

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
            dataReturn[i][10] = data.get(i)[10];
        }

        return dataReturn;
    }

    //Get all data when a string is inserted as query string
    public Object[][] getTimetableDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        allOfficesIdList = new ArrayList();
        allTimetablesIdList = new ArrayList();

        String findIdQuery = "SELECT d.doctorid, d.doctorname, d.doctorsurname, a.street AS officestreet, a.postalcode, a.city, a.province, a.state, t.timetableid, t.officeid, t.dayoftheweek, t.beginningtime, t.endtime\n" +
                "FROM timetable t INNER JOIN doctor d ON t.doctorid = d.doctorid\n" +
                "INNER JOIN private_office o ON t.officeid = o.officeid\n" +
                "INNER JOIN address a ON o.officeaddress = a.addressid WHERE UPPER (" + column + ") = UPPER (?)";
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
                            rs.getString("officestreet"), rs.getString("postalcode"), rs.getString("city"),
                            rs.getString("province"), rs.getString("state"), rs.getString("dayoftheweek"),
                            simpleDateFormat.format(rs.getTime("beginningtime")), simpleDateFormat.format(rs.getTime("endtime"))};
                    data.add(row);

                    allOfficesIdList.add(rs.getInt("officeid"));
                    allTimetablesIdList.add(rs.getInt("timetableid"));
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][11];

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
            dataReturn[i][10] = data.get(i)[10];
        }
        return dataReturn;
    }

    //Get all data when a date is inserted as query string
    public Object[][] getTimetableDataFromDate(String column, String timeString) {
        ArrayList<Object[]> data = new ArrayList();
        allOfficesIdList = new ArrayList();
        allTimetablesIdList = new ArrayList();

        String findDateQuery = "SELECT d.doctorid, d.doctorname, d.doctorsurname, a.street AS officestreet, a.postalcode, a.city, a.province, a.state, t.timetableid, t.officeid, t.dayoftheweek, t.beginningtime, t.endtime\n" +
                "FROM timetable t INNER JOIN doctor d ON t.doctorid = d.doctorid\n" +
                "INNER JOIN private_office o ON t.officeid = o.officeid\n" +
                "INNER JOIN address a ON o.officeaddress = a.addressid WHERE (" + column + ") = ?";

        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findDateQuery);

            long ms = simpleDateFormat.parse(timeString).getTime();
            Time t = new Time(ms);
            stmt.setTime(1, t);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    Object[] row = {rs.getInt("doctorid"), rs.getString("doctorname"), rs.getString("doctorsurname"),
                            rs.getString("officestreet"), rs.getString("postalcode"), rs.getString("city"),
                            rs.getString("province"), rs.getString("state"), rs.getString("dayoftheweek"),
                            simpleDateFormat.format(rs.getTime("beginningtime")), simpleDateFormat.format(rs.getTime("endtime"))};
                    data.add(row);

                    allOfficesIdList.add(rs.getInt("officeid"));
                    allTimetablesIdList.add(rs.getInt("timetableid"));

                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][11];

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
            dataReturn[i][10] = data.get(i)[10];
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

                if (selectedColumn == "Doctor ID") {
                    try {
                        doctorIDCheck = Integer.parseInt(textField.getText());
                        myData = getTimetableDataFromDoctorId(doctorIDCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Doctor id must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Doctor Name") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getTimetableDataFromString("doctorname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Doctor name must be less than 60 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Doctor Surname") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getTimetableDataFromString("doctorsurname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Doctor name must be less than 60 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Office Street") {
                    if (stringToBeMatched.length() < 50) {
                        myData = getTimetableDataFromString("street", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Street name must be less than 50 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "ZIP Code") {
                    if (stringToBeMatched.length() == 5) {
                        myData = getTimetableDataFromString("postalcode", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "ZIP Code must be 5 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "City") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getTimetableDataFromString("city", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "City name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Province") {
                    if (stringToBeMatched.length() == 2) {
                        myData = getTimetableDataFromString("province", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Province name must be 2 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "State") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getTimetableDataFromString("state", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "State name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Day") {
                    if (stringToBeMatched.toUpperCase().equals(daysOfTheWeek[0]) && stringToBeMatched.toUpperCase().equals(daysOfTheWeek[1])
                            && stringToBeMatched.toUpperCase().equals(daysOfTheWeek[2]) && stringToBeMatched.toUpperCase().equals(daysOfTheWeek[3])
                            && stringToBeMatched.toUpperCase().equals(daysOfTheWeek[4]) && stringToBeMatched.toUpperCase().equals(daysOfTheWeek[5])
                            && stringToBeMatched.toUpperCase().equals(daysOfTheWeek[6])) {

                        myData = getTimetableDataFromString("dayoftheweek", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Day field must be one of the following: \"Mo\", \"Tu\", \"We\", \"Th\", \"Fr\", \"Sa\", \"Su\". " +
                                "\nThe timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "Begin Time") {

                    try {
                        simpleDateFormat.parse(stringToBeMatched);

                        myData = getTimetableDataFromDate("beginningtime", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }

                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(container, "Begin Time format must be of the type \"HH:mm\".", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                if (selectedColumn == "End Time") {

                    try {
                        simpleDateFormat.parse(stringToBeMatched);

                        myData = getTimetableDataFromDate("endtime", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllTimetableData();
                            repaintTable(allData);
                        }

                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(container, "End Time format must be of the type \"HH:mm\".", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

                textField.setText("");
            } else {

                if (selectedColumn == "Show all") {
                    repaintTable(getAllTimetableData());
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

            // First row: Doctor Name
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel doctorId = new JLabel("Doctor ID");
            doctorId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorIdField = new JTextField(String.valueOf(tab.getModel().getValueAt(index, 0)));
            doctorIdField.setEditable(false);
            firstRow.add(doctorId);
            firstRow.add(Box.createRigidArea(new Dimension(90, 0)));
            firstRow.add(doctorIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(firstRow);

            // Second row: Doctor Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel doctorName = new JLabel("Doctor Name");
            doctorName.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorNameField = new JTextField((tab.getModel().getValueAt(index, 1).toString()));
            doctorNameField.setEditable(false);
            secondRow.add(doctorName);
            secondRow.add(Box.createRigidArea(new Dimension(60, 0)));
            secondRow.add(doctorNameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Doctor Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel doctorSurname = new JLabel("Doctor Surname");
            doctorSurname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorSurnameField = new JTextField(tab.getModel().getValueAt(index, 2).toString());
            doctorSurnameField.setEditable(false);
            thirdRow.add(doctorSurname);
            thirdRow.add(Box.createRigidArea(new Dimension(30, 0)));
            thirdRow.add(doctorSurnameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Office Street
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel street = new JLabel("Office Street");
            street.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField streetField = new JTextField(tab.getModel().getValueAt(index, 3).toString());
            fourthRow.add(street);
            fourthRow.add(Box.createRigidArea(new Dimension(65, 0)));
            fourthRow.add(streetField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: PostalCode
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel postalCode = new JLabel("Postal code");
            postalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField postalCodeField = new JTextField(tab.getModel().getValueAt(index, 4).toString());
            fifthRow.add(postalCode);
            fifthRow.add(Box.createRigidArea(new Dimension(75, 0)));
            fifthRow.add(postalCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: City
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel city = new JLabel("City");
            city.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField cityField = new JTextField(tab.getModel().getValueAt(index, 5).toString());
            sixthRow.add(city);
            sixthRow.add(Box.createRigidArea(new Dimension(140, 0)));
            sixthRow.add(cityField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            // Seventh row: Province
            JPanel seventhRow = new JPanel();
            seventhRow.setLayout(new BoxLayout(seventhRow, BoxLayout.X_AXIS));

            JLabel province = new JLabel("Province");
            province.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField provinceField = new JTextField(tab.getModel().getValueAt(index, 6).toString());
            seventhRow.add(province);
            seventhRow.add(Box.createRigidArea(new Dimension(100, 0)));
            seventhRow.add(provinceField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);

            // Eighth row: Province
            JPanel eighthRow = new JPanel();
            eighthRow.setLayout(new BoxLayout(eighthRow, BoxLayout.X_AXIS));

            JLabel state = new JLabel("State");
            state.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField stateField = new JTextField(tab.getModel().getValueAt(index, 7).toString());
            eighthRow.add(state);
            eighthRow.add(Box.createRigidArea(new Dimension(130, 0)));
            eighthRow.add(stateField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(eighthRow);

            //Ninth row: Day of the week

            JPanel ninthRow = new JPanel();
            ninthRow.setLayout(new BoxLayout(ninthRow, BoxLayout.X_AXIS));

            JLabel day = new JLabel("Day of the week");
            day.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField dayField = new JTextField(tab.getModel().getValueAt(index, 8).toString());
            ninthRow.add(day);
            ninthRow.add(Box.createRigidArea(new Dimension(30, 0)));
            ninthRow.add(dayField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(ninthRow);

            // Tenth row: Begin Time

            JPanel tenthRow = new JPanel();
            tenthRow.setLayout(new BoxLayout(tenthRow, BoxLayout.X_AXIS));

            JLabel beginTime = new JLabel("Begin Time");
            beginTime.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField beginTimeField = new JTextField(tab.getModel().getValueAt(index, 9).toString());
            tenthRow.add(beginTime);
            tenthRow.add(Box.createRigidArea(new Dimension(75, 0)));
            tenthRow.add(beginTimeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(tenthRow);

            //Eleventh row: End Time

            JPanel eleventhRow = new JPanel();
            eleventhRow.setLayout(new BoxLayout(eleventhRow, BoxLayout.X_AXIS));

            JLabel endTime = new JLabel("End Time");
            endTime.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField endTimeField = new JTextField(tab.getModel().getValueAt(index, 10).toString());
            eleventhRow.add(endTime);
            eleventhRow.add(Box.createRigidArea(new Dimension(95, 0)));
            eleventhRow.add(endTimeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(eleventhRow);
            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            //Add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update timetable", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //Now we check the result

            if (result == JOptionPane.YES_OPTION) {

                //Checks

                if (streetField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Street field cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (streetField.getText().length() > 50) {
                    JOptionPane.showMessageDialog(container, "Street should be less than 50 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (postalCodeField.getText().length() != 5) {
                    JOptionPane.showMessageDialog(container, "Postal code should have 5 characters", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (cityField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "City field cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (cityField.getText().length() > 30) {
                    JOptionPane.showMessageDialog(container, "City field should be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (provinceField.getText().length() != 2) {
                    JOptionPane.showMessageDialog(container, "Province should be 2 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (stateField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "State field cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (stateField.getText().length() > 30) {
                    JOptionPane.showMessageDialog(container, "State field should be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!dayField.getText().toUpperCase().equals(daysOfTheWeek[0]) && !dayField.getText().toUpperCase().equals(daysOfTheWeek[1])
                        && !dayField.getText().toUpperCase().equals(daysOfTheWeek[2]) && !dayField.getText().toUpperCase().equals(daysOfTheWeek[3])
                        && !dayField.getText().toUpperCase().equals(daysOfTheWeek[4]) && !dayField.getText().toUpperCase().equals(daysOfTheWeek[5])
                        && !dayField.getText().toUpperCase().equals(daysOfTheWeek[6])) {
                    JOptionPane.showMessageDialog(container, "Day field must be one of the following: \"Mo\", \"Tu\", \"We\", \"Th\", \"Fr\", \"Sa\", \"Su\". ", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    simpleDateFormat.parse(beginTimeField.getText());
                } catch (ParseException e1) {
                    JOptionPane.showMessageDialog(container, "Wrong begin time format. Insert time in the following format: 'HH:mm'.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    simpleDateFormat.parse(endTimeField.getText());
                } catch (ParseException e1) {
                    JOptionPane.showMessageDialog(container, "Wrong end time format. Insert time in the following format: 'HH:mm'.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String findAddress = "SELECT * FROM address WHERE UPPER(street) = UPPER(?) and UPPER(postalcode) = UPPER(?) and UPPER(city) = UPPER(?) and UPPER(province) = UPPER(?) and UPPER(state) = UPPER(?) ";
                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(findAddress);
                    stat.setString(1, tab.getModel().getValueAt(index, 3).toString());
                    stat.setString(2, tab.getModel().getValueAt(index, 4).toString());
                    stat.setString(3, tab.getModel().getValueAt(index, 5).toString());
                    stat.setString(4, tab.getModel().getValueAt(index, 6).toString());
                    stat.setString(5, tab.getModel().getValueAt(index, 7).toString());

                    ResultSet rs = stat.executeQuery();

                    int addressId = 0;

                    //Either we already have the address and we update it immediately or we add it
                    if (rs.next()) {
                        addressId = rs.getInt("addressid");
                        String updateAddress = "UPDATE address SET street = ?, postalcode = ?, city = ?, province = ?, state = ? WHERE addressid = " + addressId;

                        PreparedStatement updateAddressStmt = conn.prepareStatement(updateAddress);

                        updateAddressStmt.setString(1, streetField.getText());
                        updateAddressStmt.setString(2, postalCodeField.getText());
                        updateAddressStmt.setString(3, cityField.getText());
                        updateAddressStmt.setString(4, provinceField.getText());
                        updateAddressStmt.setString(5, stateField.getText());

                        updateAddressStmt.executeUpdate();

                    } else {

                        String addAddress = "INSERT INTO address (street, postalcode, city, province, state) VALUES(?,?,?,?,?)";

                        PreparedStatement updateAddressStmt = conn.prepareStatement(addAddress, Statement.RETURN_GENERATED_KEYS);

                        updateAddressStmt.setString(1, streetField.getText());
                        updateAddressStmt.setString(2, postalCodeField.getText());
                        updateAddressStmt.setString(3, cityField.getText());
                        updateAddressStmt.setString(4, provinceField.getText());
                        updateAddressStmt.setString(5, stateField.getText());

                        updateAddressStmt.executeUpdate();
                        ResultSet key = updateAddressStmt.getGeneratedKeys();

                        if (key.next())
                            addressId = key.getInt(1);
                    }

                    //Now we have the address, so we check if an office at that address already exists, otherwise we create a new office

                    String findOffice = "SELECT * FROM private_office o WHERE o.officeid = " + allOfficesIdList.get(index);

                    ResultSet rs2 = stat.executeQuery();

                    int officeId = 0;

                    //Either we already have the office and we update it immediately or we add a new one
                    if (rs2.next()) {
                        officeId = allOfficesIdList.get(index);

                        String updateOffice = "UPDATE private_office SET officeaddress = ? WHERE officeid = ?";

                        PreparedStatement updateOfficeStmt = conn.prepareStatement(updateOffice);
                        updateOfficeStmt.setInt(1, addressId);
                        updateOfficeStmt.setInt(2, officeId);

                        updateOfficeStmt.executeUpdate();

                    } else {

                        String addOffice = "INSERT INTO private_office (officeaddress) VALUES(?)";

                        PreparedStatement insertOfficeStmt = conn.prepareStatement(addOffice, Statement.RETURN_GENERATED_KEYS);

                        insertOfficeStmt.setInt(1, addressId);

                        insertOfficeStmt.executeUpdate();
                        ResultSet key = insertOfficeStmt.getGeneratedKeys();

                        if (key.next())
                            officeId = key.getInt(1);
                    }

                    long ms1 = 0, ms2 = 0;
                    try {
                        ms1 = simpleDateFormat.parse(beginTimeField.getText()).getTime();
                        ms2 = simpleDateFormat.parse(endTimeField.getText()).getTime();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    Time t1 = new Time(ms1);
                    Time t2 = new Time(ms2);


                    String updateQuery = "UPDATE timetable SET officeid = ?, doctorid = ?, dayoftheweek = ?, beginningtime = ?, endtime = ? WHERE timetableid = " + allTimetablesIdList.get(index);

                    PreparedStatement updateTimetableStmt = conn.prepareStatement(updateQuery);

                    updateTimetableStmt.setInt(1, officeId);
                    updateTimetableStmt.setInt(2, Integer.parseInt(doctorIdField.getText()));
                    updateTimetableStmt.setString(3, dayField.getText());
                    updateTimetableStmt.setTime(4, t1);
                    updateTimetableStmt.setTime(5, t2);

                    int res = updateTimetableStmt.executeUpdate();

                    //Confirm that timetable record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Timetable updated successfully");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new TimetablePanel());
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

            // First row: Doctor ID
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel doctorId = new JLabel("Doctor ID");
            doctorId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorIdField = new JTextField();
            doctorIdField.setEditable(false);
            firstRow.add(doctorId);
            firstRow.add(Box.createRigidArea(new Dimension(90, 0)));
            firstRow.add(doctorIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(firstRow);

            // Second row: Doctor Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel doctorName = new JLabel("Doctor Name");
            doctorName.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorNameField = new JTextField();
            doctorNameField.setEditable(false);
            secondRow.add(doctorName);
            secondRow.add(Box.createRigidArea(new Dimension(60, 0)));
            secondRow.add(doctorNameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Doctor Surname
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel doctorSurname = new JLabel("Doctor Surname");
            doctorSurname.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField doctorSurnameField = new JTextField();
            doctorSurnameField.setEditable(false);
            thirdRow.add(doctorSurname);
            thirdRow.add(Box.createRigidArea(new Dimension(30, 0)));
            thirdRow.add(doctorSurnameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: Office Street
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel street = new JLabel("Office Street");
            street.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField streetField = new JTextField();
            fourthRow.add(street);
            fourthRow.add(Box.createRigidArea(new Dimension(65, 0)));
            fourthRow.add(streetField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: PostalCode
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel postalCode = new JLabel("Postal code");
            postalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField postalCodeField = new JTextField();
            fifthRow.add(postalCode);
            fifthRow.add(Box.createRigidArea(new Dimension(75, 0)));
            fifthRow.add(postalCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: City
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel city = new JLabel("City");
            city.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField cityField = new JTextField();
            sixthRow.add(city);
            sixthRow.add(Box.createRigidArea(new Dimension(141, 0)));
            sixthRow.add(cityField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            // Seventh row: Province
            JPanel seventhRow = new JPanel();
            seventhRow.setLayout(new BoxLayout(seventhRow, BoxLayout.X_AXIS));

            JLabel province = new JLabel("Province");
            province.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField provinceField = new JTextField();
            seventhRow.add(province);
            seventhRow.add(Box.createRigidArea(new Dimension(100, 0)));
            seventhRow.add(provinceField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);

            // Eighth row: Province
            JPanel eighthRow = new JPanel();
            eighthRow.setLayout(new BoxLayout(eighthRow, BoxLayout.X_AXIS));

            JLabel state = new JLabel("State");
            state.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField stateField = new JTextField();
            eighthRow.add(state);
            eighthRow.add(Box.createRigidArea(new Dimension(130, 0)));
            eighthRow.add(stateField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(eighthRow);

            //Ninth row: Day of the week

            JPanel ninthRow = new JPanel();
            ninthRow.setLayout(new BoxLayout(ninthRow, BoxLayout.X_AXIS));

            JLabel day = new JLabel("Day of the week");
            day.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField dayField = new JTextField();
            ninthRow.add(day);
            ninthRow.add(Box.createRigidArea(new Dimension(30, 0)));
            ninthRow.add(dayField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(ninthRow);

            // Tenth row: Begin Time

            JPanel tenthRow = new JPanel();
            tenthRow.setLayout(new BoxLayout(tenthRow, BoxLayout.X_AXIS));

            JLabel beginTime = new JLabel("Begin Time");
            beginTime.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField beginTimeField = new JTextField();
            tenthRow.add(beginTime);
            tenthRow.add(Box.createRigidArea(new Dimension(76, 0)));
            tenthRow.add(beginTimeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(tenthRow);

            //Eleventh row: End Time

            JPanel eleventhRow = new JPanel();
            eleventhRow.setLayout(new BoxLayout(eleventhRow, BoxLayout.X_AXIS));

            JLabel endTime = new JLabel("End Time");
            endTime.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField endTimeField = new JTextField();
            eleventhRow.add(endTime);
            eleventhRow.add(Box.createRigidArea(new Dimension(95, 0)));
            eleventhRow.add(endTimeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(eleventhRow);
            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            //Create JOptionPane to first check if the doctor exists. If it doesn't, the user is suggested to add the doctor first and then his timetable
            JPanel beforePanel = new JPanel();
            beforePanel.setLayout(new BoxLayout(beforePanel, BoxLayout.X_AXIS));

            JLabel beforeDoctor = new JLabel("Enter the doctor ID");
            beforeDoctor.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField beforeDoctorField = new JTextField();
            beforePanel.add(beforeDoctor);
            beforePanel.add(Box.createRigidArea(new Dimension(10, 0)));
            beforePanel.add(beforeDoctorField);

            beforePanel.add(Box.createRigidArea(new Dimension(0, 30)));

            int doctorExists = JOptionPane.showConfirmDialog(container, beforePanel, "Enter doctor ID", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (doctorExists == JOptionPane.YES_OPTION) {

                Connection conn;
                try {
                    int checkDoctorId = Integer.parseInt(beforeDoctorField.getText());

                    String checkDoctorExists = "SELECT * FROM doctor WHERE doctorid = " + checkDoctorId;

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    Statement s = conn.createStatement();

                    ResultSet res = s.executeQuery(checkDoctorExists);

                    if (!res.next()) {
                        int addDoctor = JOptionPane.showConfirmDialog(container, "No doctor found for the given ID. Please check if the doctor ID is correct or" +
                                "add a new doctor in the doctor section.\n" +
                                "Do you want to add a new doctor now?", "No doctor found!", JOptionPane.INFORMATION_MESSAGE);

                        if (addDoctor == JOptionPane.YES_OPTION) {
                            AppFrame.frame.getContentPane().setVisible(false);
                            AppFrame.frame.setContentPane(new DoctorPanel());
                            AppFrame.frame.getContentPane().setVisible(true);
                        }
                    } else {

                        doctorIdField.setText(String.valueOf(checkDoctorId));
                        doctorNameField.setText(res.getString("doctorname"));
                        doctorSurnameField.setText(res.getString("doctorsurname"));

                        int result = JOptionPane.showConfirmDialog(container, addPanel, "Add timetable", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (result == JOptionPane.YES_OPTION) {

                            //Checks

                            if (streetField.getText().length() == 0) {
                                JOptionPane.showMessageDialog(container, "Street field cannot be empty.\n" +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (streetField.getText().length() > 50) {
                                JOptionPane.showMessageDialog(container, "Street should be less than 50 characters. \n" +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (postalCodeField.getText().length() != 5) {
                                JOptionPane.showMessageDialog(container, "Postal code should have 5 characters.\n " +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (cityField.getText().length() == 0) {
                                JOptionPane.showMessageDialog(container, "City field cannot be empty.\n " +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (cityField.getText().length() > 30) {
                                JOptionPane.showMessageDialog(container, "City field should be less than 30 characters.\n " +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (provinceField.getText().length() != 2) {
                                JOptionPane.showMessageDialog(container, "Province should be 2 characters.\n " +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (stateField.getText().length() == 0) {
                                JOptionPane.showMessageDialog(container, "State field cannot be empty.\n " +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (stateField.getText().length() > 30) {
                                JOptionPane.showMessageDialog(container, "State field should be less than 30 characters.\n " +
                                        "The timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if (!dayField.getText().toUpperCase().equals(daysOfTheWeek[0]) && !dayField.getText().toUpperCase().equals(daysOfTheWeek[1])
                                    && !dayField.getText().toUpperCase().equals(daysOfTheWeek[2]) && !dayField.getText().toUpperCase().equals(daysOfTheWeek[3])
                                    && !dayField.getText().toUpperCase().equals(daysOfTheWeek[4]) && !dayField.getText().toUpperCase().equals(daysOfTheWeek[5])
                                    && !dayField.getText().toUpperCase().equals(daysOfTheWeek[6])) {
                                JOptionPane.showMessageDialog(container, "Day field must be one of the following: \"Mo\", \"Tu\", \"We\", \"Th\", \"Fr\", \"Sa\", \"Su\". " +
                                        "\nThe timetable will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            try {
                                simpleDateFormat.parse(beginTimeField.getText());
                            } catch (ParseException e1) {
                                JOptionPane.showMessageDialog(container, "Wrong begin time format. Insert time in the following format: 'HH:mm'.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            try {
                                simpleDateFormat.parse(endTimeField.getText());
                            } catch (ParseException e1) {
                                JOptionPane.showMessageDialog(container, "Wrong end time format. Insert time in the following format: 'HH:mm'.", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            long ms1 = 0, ms2 = 0;
                            try {
                                ms1 = simpleDateFormat.parse(beginTimeField.getText()).getTime();
                                ms2 = simpleDateFormat.parse(endTimeField.getText()).getTime();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            Time t1 = new Time(ms1);
                            Time t2 = new Time(ms2);

                            if (t1.after(t2))
                                JOptionPane.showMessageDialog(container, "Begin time cannot be later than end time.", "Warning", JOptionPane.WARNING_MESSAGE);

                            boolean alreadyExists = checkTimetableAlreadyExists(checkDoctorId, dayField.getText(), beginTimeField.getText(), endTimeField.getText());

                            if (alreadyExists) {
                                JOptionPane.showMessageDialog(container, "This timetable already exists for the specified doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            String findAddress = "SELECT * FROM address WHERE UPPER(street) = UPPER(?) and UPPER(postalcode) = UPPER(?) and UPPER(city) = UPPER(?) and UPPER(province) = UPPER(?) and UPPER(state) = UPPER(?) ";

                            try {
                                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                                PreparedStatement stat = conn.prepareStatement(findAddress);
                                stat.setString(1, streetField.getText());
                                stat.setString(2, postalCodeField.getText());
                                stat.setString(3, cityField.getText());
                                stat.setString(4, provinceField.getText());
                                stat.setString(5, stateField.getText());

                                ResultSet rs = stat.executeQuery();

                                int addressId = 0;

                                //Either we already have the address and we save the id, or we add it
                                if (rs.next()) {
                                    addressId = rs.getInt("addressid");

                                } else {

                                    String addAddress = "INSERT INTO address (street, postalcode, city, province, state) VALUES(?,?,?,?,?)";

                                    PreparedStatement insertAddressStmt = conn.prepareStatement(addAddress, Statement.RETURN_GENERATED_KEYS);

                                    insertAddressStmt.setString(1, streetField.getText());
                                    insertAddressStmt.setString(2, postalCodeField.getText());
                                    insertAddressStmt.setString(3, cityField.getText());
                                    insertAddressStmt.setString(4, provinceField.getText());
                                    insertAddressStmt.setString(5, stateField.getText());

                                    insertAddressStmt.executeUpdate();
                                    ResultSet key = insertAddressStmt.getGeneratedKeys();

                                    if (key.next())
                                        addressId = key.getInt(1);
                                }

                                //Now we have the address, so we check if an office at that address already exists, otherwise we create a new office

                                String findOffice = "SELECT * FROM private_office  WHERE officeaddress = ?";

                                PreparedStatement findOfficeStmt = conn.prepareStatement(findOffice);
                                findOfficeStmt.setInt(1, addressId);

                                ResultSet rs2 = findOfficeStmt.executeQuery();

                                int officeId = 0;

                                //Either we already have the office and we save the its id, or we add a new one
                                if (rs2.next()) {

                                    officeId = rs.getInt("officeaddress");

                                } else {

                                    String addOffice = "INSERT INTO private_office (officeaddress) VALUES(?)";

                                    PreparedStatement insertOfficeStmt = conn.prepareStatement(addOffice, Statement.RETURN_GENERATED_KEYS);

                                    insertOfficeStmt.setInt(1, addressId);

                                    insertOfficeStmt.executeUpdate();
                                    ResultSet key = insertOfficeStmt.getGeneratedKeys();

                                    if (key.next())
                                        officeId = key.getInt(1);
                                }


                                String insertTimetableID = "INSERT INTO TIMETABLE (officeid, doctorid, dayoftheweek, beginningtime, endtime) " +
                                        "VALUES (?, ?, ?, ?, ?)";

                                PreparedStatement insertTimetableStmt = conn.prepareStatement(insertTimetableID);

                                insertTimetableStmt.setInt(1, officeId);
                                insertTimetableStmt.setInt(2, Integer.parseInt(doctorIdField.getText()));
                                insertTimetableStmt.setString(3, dayField.getText());
                                insertTimetableStmt.setTime(4, t1);
                                insertTimetableStmt.setTime(5, t2);

                                int res2 = insertTimetableStmt.executeUpdate();

                                //Confirm that timetable record has been added successfully
                                if (res2 > 0) {
                                    JOptionPane.showMessageDialog(container, "Timetable inserted successfully");
                                }

                                //Repaint the table

                                AppFrame.frame.getContentPane().setVisible(false);
                                AppFrame.frame.setContentPane(new TimetablePanel());
                                AppFrame.frame.getContentPane().setVisible(true);

                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Doctor ID must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;

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
            int timetableId = allTimetablesIdList.get(index);

            int result = JOptionPane.showConfirmDialog(container, "Are you sure you want to permanently delete the selected timetable?", "Warning", JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                String deleteTimetable = "DELETE FROM timetable WHERE timetableid = ?";

                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement s = conn.prepareStatement(deleteTimetable);
                    s.setInt(1, timetableId);

                    int res = s.executeUpdate();

                    //Confirm that timetable record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Timetable deleted successfully.");
                    }

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
        tab.setModel(new CustomTableModel(dataToBeInserted, timetableColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(90);
        columnModel.getColumn(2).setPreferredWidth(90);
        columnModel.getColumn(3).setPreferredWidth(140);
        columnModel.getColumn(4).setPreferredWidth(20);
        columnModel.getColumn(5).setPreferredWidth(50);
        columnModel.getColumn(6).setPreferredWidth(20);
        columnModel.getColumn(7).setPreferredWidth(5);
        columnModel.getColumn(8).setPreferredWidth(20);
        columnModel.getColumn(9).setPreferredWidth(35);
        columnModel.getColumn(10).setPreferredWidth(30);
    }

    private boolean checkTimetableAlreadyExists(int id, String day, String begin, String end) {

        boolean alreadyExists = false;

        String query = "SELECT FROM timetable t INNER JOIN doctor d ON t.doctorid = d.doctorid " +
                "INNER JOIN private_office o ON t.officeid = o.officeid " +
                "INNER JOIN address a ON o.officeaddress = a.addressid " +
                "WHERE d.doctorid = ? AND t.day = ? AND t.beginningtime = ? AND t.endtime = ?";

        Connection conn;

        long ms1 = 0, ms2 = 0;
        try {
            ms1 = simpleDateFormat.parse(begin).getTime();
            ms2 = simpleDateFormat.parse(end).getTime();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        Time t1 = new Time(ms1);
        Time t2 = new Time(ms2);

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement s = conn.prepareStatement(query);

            s.setInt(1, id);
            s.setString(2, day);
            s.setTime(3, t1);
            s.setTime(4, t2);

            int res = s.executeUpdate();

            if (res > 0)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alreadyExists;
    }
}
