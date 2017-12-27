import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {

    public SearchPanel() {

        JLabel header = new JLabel("Hospitals Management System");      ////// to be improoved in font and size
        JButton extractButton = new JButton("Extract Data");
        JButton insertButton = new JButton("Insert Data");
        JButton updateButton = new JButton("Update Data");
        JButton deleteButton = new JButton("Delete Data");

        extractButton.addActionListener(new extractListener());
        insertButton.addActionListener(new insertListener());
        updateButton.addActionListener(new updateListener());
        deleteButton.addActionListener(new deleteListener());

        add(header);
        add(extractButton);
        add(insertButton);
        add(updateButton);
        add(deleteButton);

    }

    private class extractListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

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
