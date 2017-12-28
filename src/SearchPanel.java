import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {

    public SearchPanel() {

        setLayout(new BorderLayout());

        JLabel header = new JLabel("Hospitals Management System");      ////// to be improved in font and size
        JButton extractButton = new JButton("Extract Data");
        JButton insertButton = new JButton("Insert Data");
        JButton updateButton = new JButton("Update Data");
        JButton deleteButton = new JButton("Delete Data");

        //Create left panel
        JPanel leftPanel = new JPanel();
        //leftPanel.setBackground(Color.green);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
       // leftPanel.setBackground(Color.green);
        JLabel chooseLabel = new JLabel("Choose an option: ");
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Set button sizes and alignment
        extractButton.setMaximumSize(new Dimension(200, 30));
        extractButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        insertButton.setMaximumSize(new Dimension(200, 30));
        insertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.setMaximumSize(new Dimension(200, 30));
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setMaximumSize(new Dimension(200, 30));
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add buttons to the left panel and create rigid areas
        leftPanel.add(Box.createRigidArea(new Dimension(80, 0)));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 150)));
        leftPanel.add(chooseLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(extractButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(insertButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(updateButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(deleteButton);

        //Create center panel
        JPanel centerPanel = new JPanel();
        centerPanel.add(header);

        //Add panels to the search panel
        setLayout(new GridLayout(1, 2));
        add(leftPanel);
        add(centerPanel);

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
