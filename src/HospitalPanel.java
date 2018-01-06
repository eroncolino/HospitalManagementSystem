import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HospitalPanel extends JPanel {
    JLabel searchLabel, stringLabel;
    JComboBox columnsList;
    JTextField textField;
    String parameter;

    public HospitalPanel () {

        //Create border
        setBorder(BorderFactory.createEmptyBorder(5,10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Hospital");
        tb.setTitleFont(new Font ("Verdana",Font.PLAIN, 30));
        tb.setTitleColor(Color.DARK_GRAY);
        setBorder(BorderFactory.createCompoundBorder(emptyBorder, tb));

        searchLabel = new JLabel("Search by: ");
        searchLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        stringLabel = new JLabel("Enter string: ");
        stringLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        String[] hospitalColumns = {"ID", "Name", "Address"};
        columnsList = new JComboBox(hospitalColumns);

        textField = new JTextField();
        parameter = textField.getText();


    }
}
