import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPanel extends JPanel{
    static JComboBox tablesCombo;
    JButton hospitalButton, doctorButton, nurseButton, patientButton, prescriptionButton, timetableButton, treatmentButton,
            medicineButton, admissionButton, addressButton;

    public DashboardPanel() {

        //Create border
        setBorder(BorderFactory.createEmptyBorder(5,10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 20, 20);
        TitledBorder tb = BorderFactory.createTitledBorder("Hospital Management System Dashboard");
        tb.setTitleFont(new Font ("Verdana", Font.PLAIN, 30));
        tb.setTitleColor(Color.DARK_GRAY);
        setBorder(BorderFactory.createCompoundBorder(emptyBorder, tb));

        //Set layout

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Create buttons

        hospitalButton = new JButton("Hospital");
        hospitalButton.setIcon(new ImageIcon("hospital.png"));
        hospitalButton.setHorizontalTextPosition(AbstractButton.CENTER);
        hospitalButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        hospitalButton.addActionListener(new hospitalButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 50, 10);
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 0;
        c.gridy = 0;
        add(hospitalButton, c);

        doctorButton = new JButton("Doctor");
        doctorButton.setIcon(new ImageIcon("doctor.png"));
        doctorButton.setHorizontalTextPosition(AbstractButton.CENTER);
        doctorButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        doctorButton.addActionListener(new doctorButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 1;
        c.gridy = 0;
        add(doctorButton, c);

        nurseButton = new JButton("Nurse");
        nurseButton.setIcon(new ImageIcon("nurse.png"));
        nurseButton.setHorizontalTextPosition(AbstractButton.CENTER);
        nurseButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        nurseButton.addActionListener(new nurseButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 2;
        c.gridy = 0;
        add(nurseButton, c);


        patientButton = new JButton("Patient");
        patientButton.setIcon(new ImageIcon("patient.png"));
        patientButton.setHorizontalTextPosition(AbstractButton.CENTER);
        patientButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        patientButton.addActionListener(new patientButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 3;
        c.gridy = 0;
        add(patientButton, c);

        prescriptionButton = new JButton("Prescription");
        prescriptionButton.setIcon(new ImageIcon("prescription.png"));
        prescriptionButton.setHorizontalTextPosition(AbstractButton.CENTER);
        prescriptionButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        prescriptionButton.addActionListener(new prescriptionButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 4;
        c.gridy = 0;
        add(prescriptionButton, c);


        timetableButton = new JButton("Timetable");
        timetableButton.setIcon(new ImageIcon("timetable.png"));
        timetableButton.setHorizontalTextPosition(AbstractButton.CENTER);
        timetableButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        timetableButton.addActionListener(new timetableButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 0;
        c.gridy = 1;
        add(timetableButton, c);


        treatmentButton = new JButton("Treatment");
        treatmentButton.setIcon(new ImageIcon("treatment.png"));
        treatmentButton.setHorizontalTextPosition(AbstractButton.CENTER);
        treatmentButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        treatmentButton.addActionListener(new treatmentButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 1;
        c.gridy = 1;
        add(treatmentButton, c);


        medicineButton = new JButton("Medicine");
        medicineButton.setIcon(new ImageIcon("medicine.png"));
        medicineButton.setHorizontalTextPosition(AbstractButton.CENTER);
        medicineButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        medicineButton.addActionListener(new medicineButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 2;
        c.gridy = 1;
        add(medicineButton, c);

        admissionButton = new JButton("Admission");
        admissionButton.setIcon(new ImageIcon("admission.png"));
        admissionButton.setHorizontalTextPosition(AbstractButton.CENTER);
        admissionButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        admissionButton.addActionListener(new admissionButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 3;
        c.gridy = 1;
        add(admissionButton, c);

        addressButton = new JButton("Address");
        addressButton.setIcon(new ImageIcon("address.png"));
        addressButton.setHorizontalTextPosition(AbstractButton.CENTER);
        addressButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        addressButton.addActionListener(new addressButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 4;
        c.gridy = 1;
        add(addressButton, c);

    }

    private class hospitalButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class doctorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class nurseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class patientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class prescriptionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class timetableButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class treatmentButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class medicineButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class admissionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class addressButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

        /*//Create button to go back to previous pane
        backButton = new JButton("Back");
        backButton.addActionListener(new backButtonListener());


        //Create the select label
        selectLabel = new JLabel("Select table: ");

        //Create the combo box from which one chooses the table name

         String[] tablesNames = {"Select a table", "Address", "Bedroom", "Doctor", "Hospital", "Hospital Admission",
                "Hospital Doctor", "Medicine", "Nurse", "Outpatient's Department", "Patient", "Private Doctor",
                "Private Office", "Prescription", "Timetable", "Treatment", "Ward"};

        tablesCombo = new JComboBox(tablesNames);



        //Creeate the empty table

        String[] emptyColumnHeadings = {"Column 1", "Column 2", "Column 3", "Column 4"};
        DefaultTableModel model = new DefaultTableModel(15, 4);
        model.setColumnIdentifiers(emptyColumnHeadings);
        JTable viewTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(viewTable);


        //Create the renderer which makes alternated colors

        viewTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table,
                        value, isSelected, hasFocus, row, column);
                Color lightGray = new Color(224, 224, 209);
                c.setBackground(row%2==0 ? Color.white : lightGray);
                return c;
            }
        });


        add(backButton);
        add(selectLabel);
        add(tablesCombo);
        add(scrollPane);
    }

    private class backButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
         //   AppFrame.frame.setContentPane(new SearchPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }*/
}
