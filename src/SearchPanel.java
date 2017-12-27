import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {

    public SearchPanel() {

        setLayout(new BorderLayout());
        Dimension buttonSize = new Dimension(100, 100);

        JLabel header = new JLabel("Hospitals Management System");      ////// to be improved in font and size
        JButton extractButton = new JButton("Extract Data");
        JButton insertButton = new JButton("Insert Data");
        JButton updateButton = new JButton("Update Data");
        JButton deleteButton = new JButton("Delete Data");

        //Create left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.green);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        //Set sizes of buttons
        extractButton.setSize(buttonSize);
        insertButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        deleteButton.setSize(buttonSize);

        //Add buttons to the left panel and create rigid areas
       // leftPanel.add(Box.createRigidArea(new Dimension(50, 0)));
       // leftPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        leftPanel.add(extractButton);
        //leftPanel.add(Box.createRigidArea(new Dimension(0, 80)));
        leftPanel.add(insertButton);
        //leftPanel.add(Box.createRigidArea(new Dimension(0, 80)));
        leftPanel.add(updateButton);
        //leftPanel.add(Box.createRigidArea(new Dimension(0, 80)));
        leftPanel.add(deleteButton);

        //Create center panel
        JPanel centerPanel = new JPanel();
        centerPanel.add(header);

        //Add panels to the search panel
        add(leftPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.EAST);

        //Listeners
        extractButton.addActionListener(new extractListener());
        insertButton.addActionListener(new insertListener());
        updateButton.addActionListener(new updateListener());
        deleteButton.addActionListener(new deleteListener());

    }

    private class extractListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new ExtractDataPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class insertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class updateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class deleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
