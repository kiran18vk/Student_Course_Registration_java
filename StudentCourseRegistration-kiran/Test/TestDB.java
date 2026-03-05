package Test;

import util.DBConnection;
import java.sql.Connection;

public class TestDB {

    public static void main(String[] args) {

        try {
            Connection con = DBConnection.getConnection();
            System.out.println("Database Connected Successfully!");

            // Check if tables exist and count courses
            java.sql.Statement stmt = con.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM courses");
            if (rs.next()) {
                System.out.println("Total courses in database: " + rs.getInt(1));
            }

        } catch (Exception e) {
            System.err.println("Database connection failed or tables missing!");
            e.printStackTrace();
        }
    }
}