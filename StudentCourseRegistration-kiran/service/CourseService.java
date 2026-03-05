package service;

import model.Course;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class CourseService {

    // Add Course
    public static void addCourse(Course c) {
        String sql = "INSERT INTO courses (id, name, credits, department, semester) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getId());
            ps.setString(2, c.getName());
            ps.setInt(3, c.getCredits());
            ps.setString(4, c.getDepartment());
            ps.setInt(5, c.getSemester());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete Course
    public static void deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get All Courses
    public static ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("credits"),
                        rs.getString("department"),
                        rs.getInt("semester")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Register Courses
    public static boolean registerCourses(String studentId, String studentName, String studentDept,
            String studentSem, List<Course> selected) {
        String deleteSql = "DELETE FROM registrations WHERE student_id = ?";
        String insertSql = "INSERT INTO registrations (student_id, student_name, student_dept, student_sem, course_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement deletePs = con.prepareStatement(deleteSql);
                    PreparedStatement insertPs = con.prepareStatement(insertSql)) {

                deletePs.setString(1, studentId);
                deletePs.executeUpdate();

                for (Course c : selected) {
                    insertPs.setString(1, studentId);
                    insertPs.setString(2, studentName);
                    insertPs.setString(3, studentDept);
                    insertPs.setString(4, studentSem);
                    insertPs.setInt(5, c.getId());
                    insertPs.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get All Registrations
    public static Map<String, List<Course>> getRegistrations() {
        Map<String, List<Course>> registrations = new HashMap<>();
        String sql = "SELECT r.student_id, c.id, c.name, c.credits, c.department, c.semester " +
                "FROM registrations r " +
                "JOIN courses c ON r.course_id = c.id";
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String student = rs.getString("student_id");
                Course course = new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("credits"),
                        rs.getString("department"),
                        rs.getInt("semester"));
                registrations.computeIfAbsent(student, k -> new ArrayList<>()).add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registrations;
    }

    // Get Enrollments for a specific Course
    public static List<String[]> getEnrollmentsForCourse(int courseId) {
        List<String[]> enrollments = new ArrayList<>();
        String sql = "SELECT student_id, student_dept, student_name, student_sem FROM registrations WHERE course_id = ? AND student_id IS NOT NULL AND student_id <> ''";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(new String[] {
                            rs.getString("student_id"),
                            rs.getString("student_dept"),
                            rs.getString("student_name"),
                            rs.getString("student_sem")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enrollments;
    }
}
