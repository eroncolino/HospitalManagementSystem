import java.sql.*;

public class PostgresConnection {

    public static void PostgresConnection() {
        Connection con;
        Statement stmt;
        ResultSet rs;


        try {
            Class.forName("org.postgresql.Driver");
            con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            stmt = con.createStatement();
            rs = stmt.executeQuery("");

            if (DashboardPanel.tablesCombo.getSelectedItem().toString() == "Address"){

            }

//            ResultSet resultSet = statement.executeQuery("select * from doctor");
//
//            while (resultSet.next()) {
//                result.setText(resultSet.getInt(1) + " " + resultSet.getString(2) + " " +
//                        resultSet.getString(3) + " " + resultSet.getString(4) + " " + resultSet.getBoolean(5));
//            }
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

