import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HospitalPanel extends JPanel {
    JLabel searchLabel, stringLabel;
    JComboBox columnsList;
    JTextField textField;
    JTable tab;
    JButton findButton, insertButton, deleteButton, updateButton, goBackButton;

    public HospitalPanel ()  {

        //Create border
        setBorder(BorderFactory.createEmptyBorder(5,10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Hospital");
        tb.setTitleFont(new Font ("Verdana",Font.PLAIN, 30));
        tb.setTitleColor(Color.DARK_GRAY);
        setBorder(BorderFactory.createCompoundBorder(emptyBorder, tb));

        //Create the container panel
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        //Criteria (search by) Panel
        JPanel criteria = new JPanel();
        criteria.setLayout(new BoxLayout(criteria, BoxLayout.X_AXIS));

        searchLabel = new JLabel("Search by: ");
        searchLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        String[] hospitalColumns = {"ID", "Name", "Street", "Postal code", "City", "Province", "State"};
        columnsList = new JComboBox(hospitalColumns);

        criteria.add(searchLabel);
        criteria.add(Box.createRigidArea(new Dimension(30,0)));
        criteria.add(columnsList);

        container.add(criteria);

        //Parameter Panel
        JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout(new BoxLayout(parameterPanel, BoxLayout.X_AXIS));

        stringLabel = new JLabel("Enter string: ");
        stringLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        textField = new JTextField();

        parameterPanel.add(stringLabel);
        parameterPanel.add(Box.createRigidArea(new Dimension(20,0)));
        parameterPanel.add(textField);

        container.add(Box.createRigidArea(new Dimension(0,50)));
        container.add(parameterPanel);

        //Main Part Panel (the row)
        JPanel mainRow = new JPanel();
        mainRow.setLayout(new BoxLayout(mainRow, BoxLayout.X_AXIS));

        //Table
        JPanel tablePanel = new JPanel();

        //we need to read data in order to fill in the table
        Object[][] mydata = getHospitalsData();
        tab = new JTable(mydata,hospitalColumns);
        tab.setModel(new CustomTableModel(mydata, hospitalColumns));

        tab.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());

        JScrollPane pane = new JScrollPane(tab);
        tablePanel.add(pane);

        mainRow.add(tablePanel);

        //Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        findButton = new JButton("Find");
        findButton.addActionListener(new findListener());
        updateButton = new JButton("Update");
        updateButton.addActionListener(new updateListener());
        insertButton = new JButton("Insert");
        insertButton.addActionListener(new insertListener());
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new deleteListener());
        goBackButton = new JButton("Go back");
        goBackButton.addActionListener(new goBackListener());

        buttonPanel.add(findButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(insertButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(goBackButton);

        mainRow.add(Box.createRigidArea(new Dimension(50,0)));
        mainRow.add(buttonPanel);

        //AT the end
        container.add(Box.createRigidArea(new Dimension(0,100)));
        container.add(mainRow);

        add(container);
    }


    public Object[][] getHospitalsData(){

        ArrayList<Object[]> data = new ArrayList();

        String query="SELECT * FROM hospital INNER JOIN address ON hospital.hospitaladdress = address.addressid";
        Connection conn = null;

        try {
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);

            while(rs.next()) {
                Object[] row = {rs.getInt("hospitalid"), rs.getString("hospitalname"),rs.getString("street"), rs.getString("postalcode"), rs.getString("city"), rs.getString("province"), rs.getString("state") };
                data.add(row);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        Object[][] dataReturn = new Object[data.size()][7];

        for(int i =0; i < data.size(); i++) {

            dataReturn[i][0] = data.get(i)[0];
            dataReturn[i][1] = data.get(i)[1];
            dataReturn[i][2] = data.get(i)[2];

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
