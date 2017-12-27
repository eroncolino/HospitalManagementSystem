import javax.swing.*;

public class ExtractDataPanel extends JPanel{

    public ExtractDataPanel(){

        JLabel selectLabel = new JLabel("Select table: ");

        String[] tablesNames = {"Select a table", "Address", "Bedroom", "Doctor", "Hospital", "Hospital Admission",
                "Hospital Doctor", "Medicine", "Nurse", "Outpatient's Department", "Patient", "Private Doctor",
                "Private Office", "Prescription", "Timetable", "Treatment", "Ward"};

        JComboBox tablesCombo = new JComboBox(tablesNames);

        add(selectLabel);
        add(tablesCombo);

    }

}
