import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HospitalPanel extends JPanel {

    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, hospitalColumns;

    public HospitalPanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Hospitals");
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
        boxColumns = new String[]{"Show all", "ID", "Name", "Street", "ZIP Code", "City", "Province", "State"};
        hospitalColumns = new String[]{"ID", "Name", "Street", "ZIP Code", "City", "Province", "State"};
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

        Object[][] myData = getAllHospitalsData();
        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };

        tab.setModel(new CustomTableModel(myData, hospitalColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(15);
        columnModel.getColumn(5).setPreferredWidth(15);
        columnModel.getColumn(6).setPreferredWidth(20);

        tab.setToolTipText("One click to select the row, two to open the wards panel relative.");

        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (e.getClickCount() == 2) {
                    int hospitalId = (int) tab.getModel().getValueAt(row, 0);
                    String hospitalName = (String) tab.getModel().getValueAt(row, 1);
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new WardPanel(hospitalId, hospitalName));
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

    //Get all the data from the hospital table
    private Object[][] getAllHospitalsData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital INNER JOIN address ON hospital.hospitaladdress = address.addressid";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = {rs.getInt("hospitalid"), rs.getString("hospitalname"), rs.getString("street"),
                        rs.getString("postalcode"), rs.getString("city"), rs.getString("province"),
                        rs.getString("state")};

                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][7];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
        }
        return dataReturn;
    }

    //Get all data when an integer is inserted as query string
    private Object[][] getHospitalDataFromInteger(int number) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM hospital INNER JOIN address ON hospital.hospitaladdress = address.addressid WHERE hospitalid = ?";
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
                    Object[] row = {rs.getInt("hospitalid"), rs.getString("hospitalname"), rs.getString("street"),
                            rs.getString("postalcode"), rs.getString("city"), rs.getString("province"),
                            rs.getString("state")};
                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][7];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            System.out.print(dataReturn[i][0] + "\n " + dataReturn[i][1] + " " + dataReturn[i][2] + "\n");
        }
        return dataReturn;
    }

    //Get all data when a string is inserted as query string
    private Object[][] getHospitalDataFromString(String column, String stringToBeMatched) {
        ArrayList<Object[]> data = new ArrayList();
        String findIdQuery = "SELECT * FROM hospital INNER JOIN address ON hospital.hospitaladdress = address.addressid WHERE UPPER(" + column + ") = UPPER(?)";
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
                    Object[] row = {rs.getInt("hospitalid"), rs.getString("hospitalname"), rs.getString("street"),
                            rs.getString("postalcode"), rs.getString("city"), rs.getString("province"),
                            rs.getString("state")};
                    data.add(row);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][7];

        for (int i = 0; i < data.size(); i++) {
            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];
            dataReturn[i][3] = data.get(i)[3];
            dataReturn[i][4] = data.get(i)[4];
            dataReturn[i][5] = data.get(i)[5];
            dataReturn[i][6] = data.get(i)[6];
            System.out.print(dataReturn[i][0] + "\n " + dataReturn[i][1] + " " + dataReturn[i][2] + "\n");
        }
        return dataReturn;
    }

    private class findListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedColumn = (String) columnsList.getSelectedItem();
            String stringToBeMatched = textField.getText();
            int hospitalIDCheck;
            Object[][] myData;
            Object[][] allData;

            if (stringToBeMatched.length() != 0) {

                if (selectedColumn == "ID") {
                    try {
                        hospitalIDCheck = Integer.parseInt(textField.getText());
                        myData = getHospitalDataFromInteger(hospitalIDCheck);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllHospitalsData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Hospital id must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Name") {
                    if (stringToBeMatched.length() < 60) {
                        myData = getHospitalDataFromString("hospital.hospitalname", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllHospitalsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Hospital name must be less than 60 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Street") {
                    if (stringToBeMatched.length() < 50) {
                        myData = getHospitalDataFromString("street", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllHospitalsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Street name must be less than 50 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "ZIP Code") {
                    if (stringToBeMatched.length() == 5) {
                        myData = getHospitalDataFromString("postalcode", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllHospitalsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "ZIP Code must be 5 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "City") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getHospitalDataFromString("city", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllHospitalsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "City name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "Province") {
                    if (stringToBeMatched.length() == 2) {
                        myData = getHospitalDataFromString("province", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllHospitalsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Province name must be 2 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (selectedColumn == "State") {
                    if (stringToBeMatched.length() < 30) {
                        myData = getHospitalDataFromString("state", stringToBeMatched);

                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllHospitalsData();
                            repaintTable(allData);
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "State name must be less than 30 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textField.setText("");
                }

                textField.setText("");
            } else {

                if (selectedColumn == "Show all") {
                    repaintTable(getAllHospitalsData());
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

            // First row: Hospital Id
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel id = new JLabel("Hospital ID");
            id.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField idField = new JTextField();
            firstRow.add(id);
            firstRow.add(Box.createRigidArea(new Dimension(60, 0)));
            firstRow.add(idField);

            addPanel.add(firstRow);

            // Second row: Hospital Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Hospital Name");
            name.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField nameField = new JTextField();
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
            JTextField streetField = new JTextField();
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
            JTextField postalCodeField = new JTextField();
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
            JTextField cityField = new JTextField();
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
            JTextField provinceField = new JTextField();
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
            JTextField stateField = new JTextField();
            seventhRow.add(state);
            seventhRow.add(Box.createRigidArea(new Dimension(115, 0)));
            seventhRow.add(stateField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);
            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            //add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Add hospital", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                //first we need to check if the address already exists, if not we have to add it
                //before we add the hospital

                boolean hospitalAlreadyExists = checkHospitalExists(Integer.parseInt(idField.getText()));

                if (hospitalAlreadyExists) {
                    JOptionPane.showMessageDialog(container, "This hospital ID already exists. Insert another hospital ID or modify the existing one.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String findAddress = "SELECT * FROM address WHERE UPPER(street) = UPPER(?) and UPPER(postalcode) = UPPER(?) and UPPER(city) = UPPER(?) and UPPER(province) = UPPER(?) and UPPER(state) = UPPER(?) ";
                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(findAddress);
                    stat.setString(1, streetField.getText());
                    stat.setString(2, postalCodeField.getText());
                    stat.setString(3, cityField.getText());
                    stat.setString(4, provinceField.getText());
                    stat.setString(5, stateField.getText());

                    ResultSet rs = stat.executeQuery();

                    int hospitalId = 0;

                    //Hospital checks
                    try {
                        hospitalId = Integer.parseInt(idField.getText());

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Hospital ID must be an integer.\n" +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nameField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "Hospital name field cannot be empty.\n" +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nameField.getText().length() > 60) {
                        JOptionPane.showMessageDialog(container, "Hospital name should be less than 60 characters.\n" +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    //Address checks
                    if (streetField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "Street field cannot be empty.\n" +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (streetField.getText().length() > 50) {
                        JOptionPane.showMessageDialog(container, "Street should be less than 50 characters. \n" +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (postalCodeField.getText().length() != 5) {
                        JOptionPane.showMessageDialog(container, "Postal code should have 5 characters.\n " +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (cityField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "City field cannot be empty.\n " +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (cityField.getText().length() > 30) {
                        JOptionPane.showMessageDialog(container, "City should be less than 30 characters.\n " +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (provinceField.getText().length() != 2) {
                        JOptionPane.showMessageDialog(container, "Province should be 2 characters.\n " +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (stateField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(container, "State field cannot be empty.\n " +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (stateField.getText().length() > 30) {
                        JOptionPane.showMessageDialog(container, "State should be less than 30 characters.\n " +
                                "No hospital will be added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int addressId = 0;

                    //Either we have the address or we should first add it
                    if (rs.next()) {
                        addressId = rs.getInt("addressid");
                    } else {

                        String addAddress = "INSERT INTO address (street, postalcode, city,province,state) VALUES(?,?,?,?,?)";

                        PreparedStatement addAddressStat = conn.prepareStatement(addAddress, Statement.RETURN_GENERATED_KEYS);


                        addAddressStat.setString(1, streetField.getText());
                        addAddressStat.setString(2, postalCodeField.getText());
                        addAddressStat.setString(3, cityField.getText());
                        addAddressStat.setString(4, provinceField.getText());
                        addAddressStat.setString(5, stateField.getText());

                        addAddressStat.executeUpdate();
                        ResultSet key = addAddressStat.getGeneratedKeys();

                        if (key.next())
                            addressId = key.getInt(1);
                    }

                    //Now we have the address, so we add the hospital

                    String addHospital = " INSERT INTO hospital (hospitalid, hospitalname, hospitaladdress) VALUES (?,?,?)";

                    PreparedStatement addHospitalStat = conn.prepareStatement(addHospital);

                    addHospitalStat.setInt(1, hospitalId);
                    addHospitalStat.setString(2, nameField.getText());
                    addHospitalStat.setInt(3, addressId);

                    int res = addHospitalStat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Hospital added successfully");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new HospitalPanel());
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
        tab.setModel(new CustomTableModel(dataToBeInserted, hospitalColumns));

        //Set columns width
        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(15);
        columnModel.getColumn(5).setPreferredWidth(15);
        columnModel.getColumn(6).setPreferredWidth(20);
    }

    private boolean checkHospitalExists (int id) {
        boolean hospitalExists = false;

        String query = "SELECT * FROM hospital WHERE hospitalid = ?";

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
