import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPanel extends JPanel{
    JButton hospitalButton, doctorButton, nurseButton, patientButton, prescriptionButton, timetableButton, treatmentButton,
            medicineButton, admissionButton, closeButton;

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
        hospitalButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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
        doctorButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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
        nurseButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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
        patientButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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
        prescriptionButton.setFont(new Font("Verdana", Font.PLAIN, 17));
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
        timetableButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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
        treatmentButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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
        medicineButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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
        admissionButton.setFont(new Font("Verdana", Font.PLAIN, 18));
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

        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        closeButton.setIcon(new ImageIcon("close.png"));
        closeButton.setHorizontalTextPosition(AbstractButton.CENTER);
        closeButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        closeButton.addActionListener(new exitButtonListener());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.ipady = 80;
        c.gridx = 4;
        c.gridy = 1;
        add(closeButton, c);
    }

    private class hospitalButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new HospitalPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class doctorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new DoctorPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class nurseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new NursePanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class patientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new PatientPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class prescriptionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new PrescriptionPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class timetableButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new TimetablePanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class treatmentButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new TreatmentPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class medicineButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new MedicinePanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class admissionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppFrame.frame.getContentPane().setVisible(false);
            AppFrame.frame.setContentPane(new AdmissionPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }

    private class exitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
