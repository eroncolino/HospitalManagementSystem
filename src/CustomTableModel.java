
import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel{

    public CustomTableModel(Object[][] data, String[] colName) {
        super(data, colName);
    }
    @Override
         public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
