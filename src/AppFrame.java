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

        // Create the menuBar
        menuBar = new JMenuBar();

        //Create the menu
        menu = new JMenu("Menu");
        menuImage = new ImageIcon(new ImageIcon("menu.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        menu.setIcon(menuImage);
        menuBar.add(menu);

        // Create the about subMenu and the exit subMenu
        aboutImage = new ImageIcon(
                new ImageIcon("about.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        about = new JMenuItem("About", aboutImage);
        String aboutString = "Developers: Yuri Scalzo & Elena Roncolino.\nImages for the dashboard and the buttons have been taken from https://it.123rf.com/.";
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, aboutString);
            }
        });

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

        frame.add(new DashboardPanel());

        //Set defaults
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1350, 1000));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }
}
