import java.sql.*;

public class DoctorInfo {
    static String info;

    public static String DoctorInformation(int doctorId){
        info = "";

        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            String hospitalDoctorQuery = "SELECT * FROM hospital_doctor d INNER JOIN ward w ON d.hospitalid = w.hospitalid AND d.wardid = w.wardid " +
                    "INNER JOIN hospital h ON w.hospitalid = h.hospitalid WHERE d.doctorid = " + doctorId;
            String privateDoctorQuery = "SELECT * FROM private_doctor d INNER JOIN private_office o ON o.officeid = d.officeid " +
                    "INNER JOIN address a ON d.officeid = a.addressid WHERE d.doctorid = " + doctorId;


            Statement s1 = conn.createStatement();
            Statement s2 = conn.createStatement();

            ResultSet rs1 = s1.executeQuery(hospitalDoctorQuery);
            ResultSet rs2 = s2.executeQuery(privateDoctorQuery);


            if (rs1.next()) {
               info = "This doctor works for the following hospital(s):\n";
               getHospitalDoctorData(rs1);
               info += "\n\n";
            }

           if (rs2.next()) {
               info += "This doctor works in the following private office(s):\n";
               getPrivateDoctorData(rs2);
               info += "\n\n";
           }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }

    private static void getHospitalDoctorData(ResultSet rs1) throws SQLException {
        String hospitalName, wardName;
        int hospitalId, wardId, departmentNumber;

        do {
               hospitalName = rs1.getString("hospitalname");
               hospitalId = rs1.getInt("hospitalid");
               wardName = rs1.getString("wardname");
               wardId = rs1.getInt("wardid");
               departmentNumber = rs1.getInt("departmentnumber");

               info += "\n---- " + hospitalName + " (ID: " + hospitalId + ") in the " + wardName + " ward (ID: " + wardId + "). ";

               if (departmentNumber != 0)
                   info += "His/Her department number is " + departmentNumber + ".";

        } while (rs1.next());
    }

    private static void getPrivateDoctorData(ResultSet rs2) throws SQLException {
        String street, postalCode, city, province, state;
        int officeId;

        do {
            street = rs2.getString("street");
            postalCode = rs2.getString("postalcode");
            city = rs2.getString("city");
            province = rs2.getString("province");
            state = rs2.getString("state");
            officeId = rs2.getInt("officeid");

            info += "\n---- Office no. " + officeId + ", " + street + ", " + postalCode + ", " + city + ", (" + province + "), " + state + ".";

        } while (rs2.next());
    }
}
