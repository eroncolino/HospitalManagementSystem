import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExtractDataPanel extends JPanel{

    public ExtractDataPanel(){

        JLabel selectLabel = new JLabel("Select table: ");

        String[] tablesNames = {"Select a table", "Address", "Bedroom", "Doctor", "Hospital", "Hospital Admission",
                "Hospital Doctor", "Medicine", "Nurse", "Outpatient's Department", "Patient", "Private Doctor",
                "Private Office", "Prescription", "Timetable", "Treatment", "Ward"};

        JComboBox tablesCombo = new JComboBox(tablesNames);

        String[] emptyColumnHeadings = {"Column 1", "Column 2", "Column 3", "Column 4"};
        DefaultTableModel model = new DefaultTableModel(15, 4);
        model.setColumnIdentifiers(emptyColumnHeadings);
        JTable viewTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(viewTable);

        add(selectLabel);
        add(tablesCombo);
        add(scrollPane);
    }

}
