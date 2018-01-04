import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExtractDataPanel extends JPanel{
    static JComboBox tablesCombo;
    JLabel selectLabel, selectColumnsLabel;
    JButton backButton;

    public ExtractDataPanel(){

        //Create button to go back to previous pane
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
            AppFrame.frame.setContentPane(new SearchPanel());
            AppFrame.frame.getContentPane().setVisible(true);
        }
    }
}
