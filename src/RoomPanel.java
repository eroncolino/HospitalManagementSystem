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

public class RoomPanel extends JPanel {
    private JLabel searchLabel, stringLabel;
    private JComboBox columnsList;
    private JTextField textField;
    private JTable tab;
    private JButton findButton, insertButton, deleteButton, updateButton, goBackButton;
    private JPanel container;
    private String[] boxColumns, roomColumns;
    private int hospitalId, wardId;
    private String hospitalName, wardName;

    public RoomPanel(int hId, int wId, String hName, String wName) {
        hospitalId = hId;
        wardId = wId;
        hospitalName = hName;
        wardName = wName;

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder(wardName + "'s Rooms (Hospital ID: " + hospitalId + ")");
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
        boxColumns = new String[]{"Show all", "Bedroom Number"};
        roomColumns = new String[]{"#", "Bedroom number", "Total no. of beds", "No. of available beds"};
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

        Object[][] myData = getAllRoomsData();
        tab = new JTable() {
            public void changeSelection(int rowIndex, int columnIndex,
                                        boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, true, false);
            }
        };

        tab.setModel(new CustomTableModel(myData, roomColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        pane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(pane);
        mainRow.add(Box.createRigidArea(new Dimension(50, 0)));
        mainRow.add(tablePanel);

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(300);
        columnModel.getColumn(3).setPreferredWidth(300);

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

    //Get all the data from the ward table of the selected hospital
    private Object[][] getAllRoomsData() {

        ArrayList<Object[]> data = new ArrayList();
        String roomQuery = "SELECT * FROM bedroom WHERE hospitalid = ? AND wardid = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement s = conn.prepareStatement(roomQuery);
            s.setInt(1, hospitalId);
            s.setInt(2, wardId);
            ResultSet rs = s.executeQuery();

            int count = 0;

            while (rs.next()) {
                count++;

                Object[] row = {count, rs.getInt("bedroomnumber"), rs.getInt("totalnoofbeds"), rs.getInt("noavailablebeds")};

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
    private Object[][] getRoomDataFromInteger(String column, int number) {
        ArrayList<Object[]> data = new ArrayList();
        String roomQuery = "SELECT * FROM bedroom WHERE hospitalid = ? AND wardid = ? AND " + column + " = ?";
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            PreparedStatement s = conn.prepareStatement(roomQuery);
            s.setInt(1, hospitalId);
            s.setInt(2, wardId);
            s.setInt(3, number);
            ResultSet rs = s.executeQuery();

            int count = 0;

            while (rs.next()) {

                count++;

                Object[] row = {count, rs.getInt("bedroomnumber"), rs.getInt("totalnoofbeds"), rs.getInt("noavailablebeds")};

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

    private class findListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedColumn = (String) columnsList.getSelectedItem();
            String stringToBeMatched = textField.getText();
            int check;
            Object[][] myData;
            Object[][] allData;

            if (stringToBeMatched.length() != 0) {

                if (selectedColumn == "Bedroom Number") {
                    try {
                        check = Integer.parseInt(textField.getText());
                        myData = getRoomDataFromInteger("bedroomnumber",check);

                        //If matches to the given string have been found, they are shown in the table. Otherwise all the data from the table are shown again
                        if (myData.length != 0)
                            repaintTable(myData);

                        else {
                            allData = getAllRoomsData();
                            repaintTable(allData);
                        }

                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Room number must be an integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                textField.setText("");
            } else {

                if (selectedColumn == "Show all") {
                    repaintTable(getAllRoomsData());
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

            // First row: Room number
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel roomNumber = new JLabel("Room number");
            roomNumber.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField roomNumberField = new JTextField(tab.getModel().getValueAt(index, 1).toString());
            firstRow.add(roomNumber);
            firstRow.add(Box.createRigidArea(new Dimension(110, 0)));
            firstRow.add(roomNumberField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(firstRow);

            // Second row: Ward Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel bedNumber = new JLabel("Total number of beds");
            bedNumber.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField bedNumberField = new JTextField(tab.getModel().getValueAt(index, 2).toString());
            secondRow.add(bedNumber);
            secondRow.add(Box.createRigidArea(new Dimension(48, 0)));
            secondRow.add(bedNumberField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container,
                    addPanel, "Update room", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                Connection conn;

                try {
                    Integer.parseInt(roomNumberField.getText());

                }catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Room number must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Integer.parseInt(bedNumberField.getText());

                }catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "The total number of beds must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String findWardId = "SELECT * FROM bedroom WHERE wardid = ? AND hospitalid = ? AND bedroomnumber = ?";

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement stmt1 = conn.prepareStatement(findWardId);
                    stmt1.setInt(1, wardId);
                    stmt1.setInt(2, hospitalId);
                    stmt1.setInt(3, Integer.parseInt(roomNumberField.getText()));

                    ResultSet rs1 = stmt1.executeQuery();

                    if (rs1.next() && !tab.getModel().getValueAt(index, 1).toString().equals(roomNumber.getText().toString())) {
                        JOptionPane.showMessageDialog(container, "This room number already exists.\n" +
                                "Room will not be updated.", "Room number error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                if (Integer.parseInt(bedNumberField.getText()) < Integer.parseInt(tab.getModel().getValueAt(index, 3).toString())) {
                    JOptionPane.showMessageDialog(container, "Problem with the number of beds. You have to free the room\n" +
                            "before making the total number of beds smaller.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;

                }
                String updateRoom = "UPDATE bedroom SET bedroomnumber = ?, totalnoofbeds = ?, noavailablebeds = ? WHERE bedroomnumber = ? AND wardid = ? AND hospitalid = ?";
                Connection con;
                try {
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = con.prepareStatement(updateRoom);

                    int oldAvailable = (int) tab.getModel().getValueAt(index, 3);
                    int oldTotal = (int) tab.getModel().getValueAt(index, 2);
                    int newTotal = Integer.parseInt(bedNumberField.getText());
                    int newAvailable = oldAvailable + newTotal - oldTotal;

                    stat.setInt(1, Integer.parseInt(roomNumberField.getText()));
                    stat.setInt(2, Integer.parseInt(bedNumberField.getText()));
                    stat.setInt(3, newAvailable);
                    stat.setInt(4, Integer.parseInt(tab.getModel().getValueAt(index, 1).toString()));
                    stat.setInt(5, wardId);
                    stat.setInt(6, hospitalId);

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Room updated successfully.");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new RoomPanel(hospitalId, wardId, hospitalName, wardName));
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
            int index = tab.getSelectedRow();

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
            addPanel.add(Box.createRigidArea(new Dimension(500, 50)));

            // First row: Room number
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel roomNumber = new JLabel("Room number");
            roomNumber.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField roomNumberField = new JTextField();
            firstRow.add(roomNumber);
            firstRow.add(Box.createRigidArea(new Dimension(110, 0)));
            firstRow.add(roomNumberField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(firstRow);

            // Second row: Ward Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel bedNumber = new JLabel("Total number of beds");
            bedNumber.setFont(new Font("Verdana", Font.PLAIN, 18));
            JTextField bedNumberField = new JTextField();
            secondRow.add(bedNumber);
            secondRow.add(Box.createRigidArea(new Dimension(48, 0)));
            secondRow.add(bedNumberField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container,
                    addPanel, "Insert room", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if (result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                Connection conn;

                try {
                    Integer.parseInt(roomNumberField.getText());

                }catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "Room number must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Integer.parseInt(bedNumberField.getText());

                }catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(container, "The total number of beds must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String findWardId = "SELECT * FROM bedroom WHERE wardid = ? AND hospitalid = ? AND bedroomnumber = ?";

                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
                    PreparedStatement stmt1 = conn.prepareStatement(findWardId);
                    stmt1.setInt(1, wardId);
                    stmt1.setInt(2, hospitalId);
                    stmt1.setInt(3, Integer.parseInt(roomNumberField.getText()));

                    ResultSet rs1 = stmt1.executeQuery();

                    if (rs1.next()) {
                        JOptionPane.showMessageDialog(container, "This room number already exists.\n" +
                                "Room will not be inserted.", "Room number error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                String updateRoom = "INSERT INTO bedroom (bedroomnumber, totalnoofbeds, noavailablebeds, wardid, hospitalid) VALUES (?,?,?,?,?)";
                Connection con;
                try {
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = con.prepareStatement(updateRoom);

                    stat.setInt(1, Integer.parseInt(roomNumberField.getText()));
                    stat.setInt(2, Integer.parseInt(bedNumberField.getText()));
                    stat.setInt(3, Integer.parseInt(bedNumberField.getText()));
                    stat.setInt(4, wardId);
                    stat.setInt(5, hospitalId);

                    int res = stat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0) {
                        JOptionPane.showMessageDialog(container, "Room inserted successfully.");
                    }

                    //Repaint the table
                    AppFrame.frame.getContentPane().setVisible(false);
                    AppFrame.frame.setContentPane(new RoomPanel(hospitalId, wardId, hospitalName, wardName));
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
        tab.setModel(new CustomTableModel(dataToBeInserted, roomColumns));

        TableColumnModel columnModel = tab.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(300);
        columnModel.getColumn(3).setPreferredWidth(300);
    }
}
