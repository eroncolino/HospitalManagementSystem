import javax.swing.*;
import java.awt.*;

public class RoomDepartmentPanel extends JPanel {

    public RoomDepartmentPanel(int hospitalId, int wardId, String hospitalName, String wardName){
        JTabbedPane tb = new JTabbedPane();
        tb.setFont(new Font("Verdana", Font.PLAIN, 18));
        tb.addTab("Rooms", new RoomPanel(hospitalId, wardId, hospitalName, wardName));
        tb.addTab("Outpatient's departments", new OutpatientsDepartmentPanel(hospitalId, wardId, hospitalName, wardName));
        add(tb);
    }
}
