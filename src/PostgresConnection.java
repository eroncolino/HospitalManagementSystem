import javax.swing.*;
import java.sql.*;

//////commentsssssss
public class PostgresConnection {
    static JLabel result;

    public static void PostgresConnection() {
        result = new JLabel();

        try {
            Class.forName("org.postgresql.Driver");
            Connection con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hospital", "postgres", "elena");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from doctor");

            while (resultSet.next()) {
                result.setText(resultSet.getInt(1) + " " + resultSet.getString(2) + " " +
                        resultSet.getString(3) + " " + resultSet.getString(4) + " " + resultSet.getBoolean(5));
            }
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

