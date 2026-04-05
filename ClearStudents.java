import util.DBConnection;
import java.sql.Connection;
import java.sql.Statement;

public class ClearStudents {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement()) {
            
            stmt.executeUpdate("DELETE FROM registrations");
            stmt.executeUpdate("DELETE FROM blocked_registrations");
            int deleted = stmt.executeUpdate("DELETE FROM students");
            System.out.println("Successfully deleted " + deleted + " students and all associated registrations.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
