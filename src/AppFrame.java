import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppFrame {
    static JFrame frame;

    public static void main(String[] args) {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem about, exit;
        ImageIcon menuImage, aboutImage, exitImage;
        JButton enterButton;

        // Create the menuBar
        menuBar = new JMenuBar();

        //Create the menu
        menu = new JMenu("Menu");
        menuImage = new ImageIcon(new ImageIcon("menu.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        menu.setIcon(menuImage);
        menuBar.add(menu);

        // Create the about subMenu and the exit subMenu
        about = new JMenuItem("About");
        aboutImage = new ImageIcon(
                new ImageIcon("about.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        about = new JMenuItem("About", aboutImage);
        String aboutString = "Developers: Yuri Scalzo & Elena Roncolino.";
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, aboutString);
            }
        });

        exit = new JMenuItem("Exit");
        exitImage = new ImageIcon(new ImageIcon("exit.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        exit = new JMenuItem("Exit", exitImage);
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menu.add(about);
        menu.add(exit);

        //Create the frame and add the menu bar
        frame = new JFrame("Hospital Application");
        frame.setJMenuBar(menuBar);

        //Set the background
        JLabel hospitalBackground = new JLabel(new ImageIcon("HospAppBackground.jpg"));
        hospitalBackground.setSize(1200, 600);

        //Create enter button
        enterButton = new JButton("Enter");
        enterButton.addActionListener(new enterListener());
        enterButton.setSize(100, 50);
        enterButton.setBounds(550, 500, 100, 50);

        JLayeredPane pane = new JLayeredPane();
        pane.add(hospitalBackground, new Integer(1));
        pane.add(enterButton, new Integer(2));

        frame.add(pane);

        //display a table
        //PostgresConnection.PostgresConnection();
        //frame.add(PostgresConnection.result);   /////////to be corrected. it is just a test

        //Set defaults
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 800));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    private static class enterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().setVisible(false);
            frame.setContentPane(new SearchPanel());
            frame.getContentPane().setVisible(true);
        }
    }

}
