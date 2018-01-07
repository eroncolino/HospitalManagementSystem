import javax.swing.*;

import javax.swing.border.Border;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

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

    public HospitalPanel() {

        // Create border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Hospital");
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
        String[] hospitalColumns = { "ID", "Name", "Street", "Postal code", "City", "Province", "State" };
        columnsList = new JComboBox(hospitalColumns);
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

        Object[][] mydata = getHospitalsData();
        tab = new JTable(mydata, hospitalColumns);
        tab.setModel(new CustomTableModel(mydata, hospitalColumns));
        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        JScrollPane pane = new JScrollPane(tab);
        tablePanel.add(pane);
        mainRow.add(tablePanel);

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
        buttonPanel.add(Box.createRigidArea(new Dimension(250, 0)));
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
    }

    public Object[][] getHospitalsData() {

        ArrayList<Object[]> data = new ArrayList();
        String query = "SELECT * FROM hospital INNER JOIN address ON hospital.hospitaladdress = address.addressid";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Object[] row = { rs.getInt("hospitalid"), rs.getString("hospitalname"), rs.getString("street"),
                        rs.getString("postalcode"), rs.getString("city"), rs.getString("province"),
                        rs.getString("state") };

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
            System.out.print(dataReturn[i][0] + " " + dataReturn[i][1] + " " + dataReturn[i][2]);
        }
        return dataReturn;
    }

    private class findListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    private class updateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Currently selected: " + tab.getSelectedRow());
        }
    }

    private class insertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Container
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

            // First row: Hospital Id
            JPanel firstRow = new JPanel();
            firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));

            JLabel id = new JLabel("Hospital Code");
            JTextField idField = new JTextField();
            firstRow.add(id);
            firstRow.add(Box.createRigidArea(new Dimension(20, 0)));
            firstRow.add(idField);

            addPanel.add(firstRow);

            // Second row: Hospital Name
            JPanel secondRow = new JPanel();
            secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));

            JLabel name = new JLabel("Hospital Name");
            JTextField nameField = new JTextField();
            secondRow.add(name);
            secondRow.add(Box.createRigidArea(new Dimension(15, 0)));
            secondRow.add(nameField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(secondRow);

            // Third row: Street
            JPanel thirdRow = new JPanel();
            thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));

            JLabel street = new JLabel("Street");
            JTextField streetField = new JTextField();
            thirdRow.add(street);
            thirdRow.add(Box.createRigidArea(new Dimension(63, 0)));
            thirdRow.add(streetField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(thirdRow);

            // Fourth row: PostalCode
            JPanel fourthRow = new JPanel();
            fourthRow.setLayout(new BoxLayout(fourthRow, BoxLayout.X_AXIS));

            JLabel postalcode = new JLabel("Postal code");
            JTextField postalcodeField = new JTextField();
            fourthRow.add(postalcode);
            fourthRow.add(Box.createRigidArea(new Dimension(30, 0)));
            fourthRow.add(postalcodeField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fourthRow);

            // Fifth row: City
            JPanel fifthRow = new JPanel();
            fifthRow.setLayout(new BoxLayout(fifthRow, BoxLayout.X_AXIS));

            JLabel city = new JLabel("City");
            JTextField cityField = new JTextField();
            fifthRow.add(city);
            fifthRow.add(Box.createRigidArea(new Dimension(75, 0)));
            fifthRow.add(cityField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(fifthRow);

            // Sixth row: Province
            JPanel sixthRow = new JPanel();
            sixthRow.setLayout(new BoxLayout(sixthRow, BoxLayout.X_AXIS));

            JLabel province = new JLabel("Province");
            JTextField provinceField = new JTextField();
            sixthRow.add(province);
            sixthRow.add(Box.createRigidArea(new Dimension(45, 0)));
            sixthRow.add(provinceField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(sixthRow);

            // Seventh row: Province
            JPanel seventhRow = new JPanel();
            seventhRow.setLayout(new BoxLayout(seventhRow, BoxLayout.X_AXIS));

            JLabel state = new JLabel("State");
            JTextField stateField = new JTextField();
            seventhRow.add(state);
            seventhRow.add(Box.createRigidArea(new Dimension(67, 0)));
            seventhRow.add(stateField);

            addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addPanel.add(seventhRow);
            addPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            // add all to JOptionPane
            int result = JOptionPane.showConfirmDialog(container, // use your JFrame here
                    addPanel, "Add a hospital", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

            //now we check the result

            if(result == JOptionPane.YES_OPTION) {
                //it is a yes so we want to add it
                //first we need to check if the address already exists, if not we have to add it
                //before we add the hospital

                String findAddress = "SELECT * FROM address WHERE UPPER(street) = UPPER(?) and UPPER(postalcode) = UPPER(?) and UPPER(city) = UPPER(?) and UPPER(province) = UPPER(?) and UPPER(state) = UPPER(?) ";
                Connection conn;
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");

                    PreparedStatement stat = conn.prepareStatement(findAddress);
                    stat.setString(1, streetField.getText());
                    stat.setString(2, postalcodeField.getText());
                    stat.setString(3, cityField.getText());
                    stat.setString(4, provinceField.getText());
                    stat.setString(5, stateField.getText());

                    ResultSet rs = stat.executeQuery();

                    int addressId = 0;

                    //Either we have it or we should first add it
                    if(rs.next()) {
                        addressId = rs.getInt("addressid");
                    } else {

                        String addAddress = "INSERT INTO address (street, postalcode, city,province,state) VALUES(?,?,?,?,?)";

                        PreparedStatement addAddressStat = conn.prepareStatement(addAddress, Statement.RETURN_GENERATED_KEYS);


                        //Checks

                        if(streetField.getText().length() > 50) {
                            JOptionPane.showMessageDialog(container, "Street should be less than 50 characters");
                            return;
                        }

                        //...

                        addAddressStat.setString(1, streetField.getText());
                        addAddressStat.setString(2, postalcodeField.getText());
                        addAddressStat.setString(3, cityField.getText());
                        addAddressStat.setString(4, provinceField.getText());
                        addAddressStat.setString(5, stateField.getText());

                        addAddressStat.executeUpdate();
                        ResultSet key = addAddressStat.getGeneratedKeys();

                        if (key.next())
                            addressId = key.getInt(1);

                    }

                    //Now we have the address, so we add the hospital

                    int hospitalId = 0;
                    //Hospital checks
                    try {

                        hospitalId = Integer.parseInt(idField.getText());

                    } catch(NumberFormatException n) {
                        JOptionPane.showMessageDialog(container, "Hospital id must be an integer");
                        return;
                    }

                    if(nameField.getText().length() > 60) {
                        JOptionPane.showMessageDialog(container, "Hospital Name should be less than 60 characters");
                        return;
                    }

                    //We add it
                    String addHospital = " INSERT INTO hospital (hospitalid, hospitalname, hospitaladdress) VALUES (?,?,?)";

                    PreparedStatement addHospitalStat = conn.prepareStatement(addHospital);

                    addHospitalStat.setInt(1, hospitalId);
                    addHospitalStat.setString(2, nameField.getText());
                    addHospitalStat.setInt(3, addressId);

                    int res = addHospitalStat.executeUpdate();

                    //Confirm that hospital record has been added successfully
                    if (res > 0){
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

    private class deleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
}