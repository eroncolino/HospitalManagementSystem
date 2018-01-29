import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

public class WardPanel extends JPanel{
    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, wardColumns;
    private int hospitalId;
    private String hospitalName;

    public WardPanel(int hId, String hName) {
        hospitalId = hId;
        hospitalName = hName;

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder(hName + "'s Wards (Hospital ID: " + hospitalId + ")");
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
        boxColumns = new String[]{"Show all", "ID", "Name"};
        wardColumns = new String[]{"Ward ID", "Ward Name", "No. of Rooms", "No. of Outpatient's Departments"};
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

        Object[][] myData = getAllWardsData();
        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };

        tab.setModel(new CustomTableModel(myData, wardColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        tab.setToolTipText("One click to select the row, two to open rooms and departments panel.");

        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (e.getClickCount() == 2) {
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new RoomDepartmentPanel());
                    AppFrame.frame.getContentPane().setVisible(true);
                }
            }
        });

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

    //Get all the data from the ward table of the selected hospital
    public Object[][] getAllWardsData() {

        ArrayList<Object[]> data = new ArrayList();
        String wardQuery = "SELECT * FROM ward WHERE hospitalid = " + hospitalId;
        Connection conn;
        int noOfRooms = 0, noOfDeparments = 0;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(wardQuery);

            while (rs.next()) {

                String countRoomsQuery = "SELECT count(*) FROM bedroom WHERE hospitalid = " + hospitalId + " AND wardid = " + rs.getInt("wardid");
                Statement s1 = conn.createStatement();
                ResultSet rs1 = s1.executeQuery(countRoomsQuery);
                if(rs1.next())
                     noOfRooms = rs1.getInt("count");

                String countDepartmentsQuery = "SELECT count(*) FROM outpatients_department WHERE hospitalid = " + hospitalId + " AND wardid = " + rs.getInt("wardid");
                Statement s2 = conn.createStatement();
                ResultSet rs2 = s2.executeQuery(countDepartmentsQuery);
                if(rs2.next())
                    noOfDeparments = rs2.getInt("count");

                Object[] row = {rs.getInt("wardid"), rs.getString("wardname"), noOfRooms, noOfDeparments};

                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][4];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    public Object[][] getWardDataFromInteger(String column, int number) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM ward WHERE hospitalid = " + hospitalId + " AND (" + column + ") = (?)";
        Connection conn;
        int noOfRooms = 0, noOfDeparments = 0;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setInt(1, number);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {

                    String countRoomsQuery = "SELECT count(*) FROM bedroom WHERE hospitalid = " + hospitalId + " AND wardid = " + rs.getInt("wardid");
                    Statement s1 = conn.createStatement();
                    ResultSet rs1 = s1.executeQuery(countRoomsQuery);
                    if(rs1.next())
                        noOfRooms = rs1.getInt("count");

                    String countDepartmentsQuery = "SELECT count(*) FROM outpatients_department WHERE hospitalid = " + hospitalId + " AND wardid = " + rs.getInt("wardid");
                    Statement s2 = conn.createStatement();
                    ResultSet rs2 = s2.executeQuery(countDepartmentsQuery);
                    if(rs2.next())
                        noOfDeparments = rs2.getInt("count");


                    Object[] row = {rs.getInt("wardid"), rs.getString("wardname"), noOfRooms, noOfDeparments};
                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][4];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
        }
        return dataReturn;
    }

    //Get all data when a string is inserted as query string
    public Object[][] getWardDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM ward WHERE hospitalid = " + hospitalId + " AND  (" + column + ") = (?)";
        Connection conn;
        int noOfRooms = 0, noOfDeparments = 0;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement stmt = conn.prepareStatement(findIdQuery);
            stmt.setString(1, stringToBeMatched);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                JOptionPane.showMessageDialog(container, "No match was found for the given string.");

            else {
                do {
                    String countRoomsQuery = "SELECT count(*) FROM bedroom WHERE hospitalid = " + hospitalId + " AND wardid = " + rs.getInt("wardid");
                    Statement s1 = conn.createStatement();
                    ResultSet rs1 = s1.executeQuery(countRoomsQuery);
                    if(rs1.next())
                        noOfRooms = rs1.getInt("count");

                    String countDepartmentsQuery = "SELECT count(*) FROM outpatients_department WHERE hospitalid = " + hospitalId + " AND wardid = " + rs.getInt("wardid");
                    Statement s2 = conn.createStatement();
                    ResultSet rs2 = s2.executeQuery(countDepartmentsQuery);
                    if(rs2.next())
                        noOfDeparments = rs2.getInt("count");


                    Object[] row = {rs.getInt("wardid"), rs.getString("wardname"), noOfRooms, noOfDeparments};
                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][4];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
        }
        return dataReturn;
    }

    private class findListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedColumn = (String) columnsList.getSelectedItem();
            String stringToBeMatched = textField.getText();
            int wardIDCheck;
            Object[][] myData;
            Object[][] allData;

            if (stringToBeMatched.length() != 0) {

                if (selectedColumn == "ID") {
                    try {
                        wardIDCheck = Integer.parseInt(textField.getText());
                        myData = getWardDataFromInteger("wardid", wardIDCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllWardsData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Ward id must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Name") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getWardDataFromString("wardname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllWardsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Ward name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                textField.setText("");
            } else {

                if (selectedColumn == "Show all") {
                    repaintTable(getAllWardsData());
                    textField.setText("");
                } else
                    JOptionPane.showMessageDialog(container, "Enter the string to be found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class updateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {/*
            int index;

            index = tab.getSelectedRow();

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Hospital Id
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel id = new JLabel("Hospital ID");
            id.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField idField = new JTextField(String.valueOf(tab.getModel().getValueAt(index, 0)));
            idField.setEditable(false);
            firstRow.add(id);
            firstRow.add(Box.createRigidArea(new Dimension(60, 0)));
            firstRow.add(idField);

            addPanel.add(firstRow);

            // Second row: Hospital Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Hospital Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField(tab.getModel().getValueAt(index, 1).toString());
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(30, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Street
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel street = new JLabel("Street");
            street.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField streetField = new JTextField(tab.getModel().getValueAt(index, 2).toString());
            thirdRow.add(street);
            thirdRow.add(Box.createRigidArea(new Dimension(107, 0)));
            thirdRow.add(streetField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: PostalCode
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel postalCode = new JLabel("Postal code");
            postalCode.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField postalCodeField = new JTextField(tab.getModel().getValueAt(index, 3).toString());
            fourthRow.add(postalCode);
            fourthRow.add(Box.createRigidArea(new Dimension(60, 0)));
            fourthRow.add(postalCodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: City
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel city = new JLabel("City");
            city.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField cityField = new JTextField(tab.getModel().getValueAt(index, 4).toString());
            fifthRow.add(city);
            fifthRow.add(Box.createRigidArea(new Dimension(125, 0)));
            fifthRow.add(cityField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Province
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel province = new JLabel("Province");
            province.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField provinceField = new JTextField(tab.getModel().getValueAt(index, 5).toString());
            sixthRow.add(province);
            sixthRow.add(Box.createRigidArea(new Dimension(85, 0)));
            sixthRow.add(provinceField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            // Seventh row: State
            JPanel seventhRow = new JPanel();
            seventhRow.setLayout(new BoxLayout(seventhRow, BoxLayout.X_AXIS));

            JLabel state = new JLabel("State");
            state.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField stateField = new JTextField(tab.getModel().getValueAt(index, 6).toString());
            seventhRow.add(state);
            seventhRow.add(Box.createRigidArea(new Dimension(115, 0)));
            seventhRow.add(stateField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);
            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Update hospital", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                //first we need to check if the address already exists, if not we have to add it
                //before we add the hospital

                //Hospital checks

                if (nameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Hospital name field cannot be empty. \n" +
                            "No hospital will be updated.");
                    return;
                }

                if (nameField.getText().length() > 60) {
                    JOptionPane.showMessageDialog(container, "Hospital name should be less than 60 characters. \n" +
                            "No hospital will be updated.");
                    return;
                }

                //Address checks

                if (streetField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "Street field cannot be empty.\n" +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (streetField.getText().length() > 50) {
                    JOptionPane.showMessageDialog(container, "Street should be less than 50 characters. \n" +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (postalCodeField.getText().length() != 5) {
                    JOptionPane.showMessageDialog(container, "Postal code should have 5 characters.\n " +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (cityField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "City field cannot be empty.\n " +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (cityField.getText().length() > 30 ) {
                    JOptionPane.showMessageDialog(container, "City field should be less than 30 characters.\n " +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (provinceField.getText().length() != 2) {
                    JOptionPane.showMessageDialog(container, "Province should be 2 characters.\n " +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (stateField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(container, "State field cannot be empty.\n " +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (stateField.getText().length() > 30) {
                    JOptionPane.showMessageDialog(container, "State field should be less than 30 characters.\n " +
                            "The hospital will not be updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String findAddress = "SELECT * FROM address WHERE UPPER(street) = UPPER(?) and UPPER(postalcode) = UPPER(?) and UPPER(city) = UPPER(?) and UPPER(province) = UPPER(?) and UPPER(state) = UPPER(?) ";
                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(findAddress);
                    stat.setString(1, tab.getModel().getValueAt(index, 2).toString());
                    stat.setString(2, tab.getModel().getValueAt(index, 3).toString());
                    stat.setString(3, tab.getModel().getValueAt(index, 4).toString());
                    stat.setString(4, tab.getModel().getValueAt(index, 5).toString());
                    stat.setString(5, tab.getModel().getValueAt(index, 6).toString());

                    ResultSet rs = stat.executeQuery();
ciao
                    int addressId = 0;

                    //Either we already have the address and we update it immediately
                    if (rs.next()) {
                        addressId = rs.getInt("addressid");
                        String updateAddress = "UPDATE address SET street = ?, postalcode = ?, city = ?, province = ?, state = ? WHERE addressid = " + addressId;

                        PreparedStatement updateAddressStmt = conn.prepareStatement(updateAddress);

                        updateAddressStmt.setString(1, streetField.getText());
                        updateAddressStmt.setString(2, postalCodeField.getText());
                        updateAddressStmt.setString(3, cityField.getText());
                        updateAddressStmt.setString(4, provinceField.getText());
                        updateAddressStmt.setString(5, provinceField.getText());

                        updateAddressStmt.executeUpdate();

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

                    //Now we have the address, so we add the hospital

                    String updateHospital = " UPDATE hospital SET hospitalname = ?, hospitaladdress = ? WHERE hospitalid = " + tab.getModel().getValueAt(index, 0);

                    PreparedStatement updateHospitalStat = conn.prepareStatement(updateHospital);

                    updateHospitalStat.setString(1, nameField.getText());
                    updateHospitalStat.setInt(2, addressId);

                    int res = updateHospitalStat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Hospital updated successfully.");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new HospitalPanel());
                    AppFrame.frame.getContentPane().setVisible(true);

                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }*/
        }
    }

    private class insertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Ward Id
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel id = new JLabel("Ward ID");
            id.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField idField = new JTextField();
            firstRow.add(id);
            firstRow.add(Box.createRigidArea(new Dimension(60, 0)));
            firstRow.add(idField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(firstRow);

            // Second row: Ward Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Ward Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField();
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(30, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            //Third Row: Hospital ID
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel hospId = new JLabel("Hospital ID");
            hospId.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField hospitalIdField = new JTextField(String.valueOf(hospitalId));
            hospitalIdField.setEditable(false);
            thirdRow.add(hospId);
            thirdRow.add(Box.createRigidArea(new Dimension(30, 0)));
            thirdRow.add(hospitalIdField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            //add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Add Ward", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                //first we need to check if the address already exists, if not we have to add it
                //before we add the hospital

                boolean wardAlreadyExists = checkWardExists(Integer.parseInt(idField.getText()));

                if (wardAlreadyExists) {
                    JOptionPane.showMessageDialog(container, "This ward ID already exists. Insert another ward ID or modify the existing one.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                    int wardId = 0;

                    //ward checks
                    try {
                        wardId = Integer.parseInt(idField.getText());

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Ward ID must be an integer.\n" +
                                "No ward will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (Integer.parseInt(idField.getText()) == 0) {
                        JOptionPane.showMessageDialog(container, "Ward Id field cannot be empty.\n" +
                            "No ward will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nameField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "Ward name field cannot be empty.\n" +
                                "No ward will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nameField.getText().length() > 30) {
                        JOptionPane.showMessageDialog(container, "Ward name should be less than 30 characters.\n" +
                                "No ward will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                String insertWard = "INSERT INTO ward (wardid, wardname, hospitalid) VALUES (?,?,?)";
                Connection con;
                try {
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = con.prepareStatement(insertWard);
                    stat.setInt(1, Integer.parseInt(idField.getText()));
                    stat.setString(2, nameField.getText());
                    stat.setInt(3, Integer.parseInt(hospitalIdField.getText()));

                    int res = stat.executeUpdate();

                    //Confirm that ward record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Ward added successfully.");
                    }

                    //Repaint the table

                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new WardPanel(hospitalId, hospitalName));
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
        tab.setModel(new CustomTableModel(dataToBeInserted, wardColumns));
    }

    private boolean checkWardExists (int id) {
        boolean hospitalExists = false;

        String query = "SELECT * FROM ward WHERE hospitalid = " + hospitalId + " AND wardid = ?";

        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement s = conn.prepareStatement(query);

            s.setInt(1, id);

            ResultSet res = s.executeQuery();

            if (res.next())
                hospitalExists = true;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospitalExists;
    }
}
