package service;

import model.Course;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class CourseService {

    // Add Course
    public static boolean addCourse(Course c) {
        String sql = "INSERT INTO courses (id, name, credits, department, semester, minor_stream) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getId());
            ps.setString(2, c.getName());
            ps.setInt(3, c.getCredits());
            ps.setString(4, c.getDepartment());
            ps.setInt(5, c.getSemester());
            ps.setString(6, c.getMinorStream());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update Course
    public static boolean updateCourse(Course c) {
        String sql = "UPDATE courses SET name = ?, credits = ?, department = ?, semester = ?, minor_stream = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getCredits());
            ps.setString(3, c.getDepartment());
            ps.setInt(4, c.getSemester());
            ps.setString(5, c.getMinorStream());
            ps.setString(6, c.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Course
    public static void deleteCourse(String id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
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
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("credits"),
                        rs.getString("department"),
                        rs.getInt("semester"),
                        rs.getString("minor_stream")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    // Register Courses (No changes needed for registrations table itself yet)

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
                    insertPs.setString(5, c.getId());
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
        String sql = "SELECT r.student_id, c.id, c.name, c.credits, c.department, c.semester, c.minor_stream " +
                "FROM registrations r " +
                "JOIN courses c ON r.course_id = c.id";
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String student = rs.getString("student_id");
                Course course = new Course(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("credits"),
                        rs.getString("department"),
                        rs.getInt("semester"),
                        rs.getString("minor_stream"));
                registrations.computeIfAbsent(student, k -> new ArrayList<>()).add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registrations;
    }

    // Get Enrollments for a specific Course
    public static List<String[]> getEnrollmentsForCourse(String courseId) {
        List<String[]> enrollments = new ArrayList<>();
        String sql = "SELECT student_id, student_dept, student_name, student_sem FROM registrations WHERE course_id = ? AND student_id IS NOT NULL AND student_id <> ''";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courseId);
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

    // Clear All Registrations
    public static boolean clearAllRegistrations() {
        String sql = "DELETE FROM registrations";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add Student
    public static boolean addStudent(model.Student s) {
        String sql = "INSERT INTO students (roll_no, name, email, password, department, semester, minor_stream) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPassword());
            ps.setString(5, s.getDepartment());
            ps.setInt(6, s.getSemester());
            ps.setString(7, s.getMinorStream());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update Student Details
    public static boolean updateStudentDetails(String rollNo, String name, String email, String department, int semester, String minorStream) {
        String sql = "UPDATE students SET name = ?, email = ?, department = ?, semester = ?, minor_stream = ? WHERE roll_no = ?";
        try (Connection con = util.DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, department);
            ps.setInt(4, semester);
            ps.setString(5, minorStream);
            ps.setString(6, rollNo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verify Student Login
    public static boolean verifyStudentLogin(String rollNo, String password) {
        String sql = "SELECT * FROM students WHERE roll_no = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get Student details
    public static String[] getStudentDetails(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[] {
                            rs.getString("name"),
                            rs.getString("department"),
                            String.valueOf(rs.getInt("semester")),
                            rs.getString("email"),
                            rs.getString("minor_stream")
                    };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get Student by Roll No
    public static model.Student getStudentByRollNo(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new model.Student(
                            rs.getString("roll_no"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("department"),
                            rs.getInt("semester"),
                            rs.getString("minor_stream"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get Courses by Dept and Semester
    public static ArrayList<Course> getCoursesByDeptAndSemester(String dept, int semester) {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE department LIKE ? AND semester = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + dept + "%");
            ps.setInt(2, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getInt("credits"),
                            rs.getString("department"),
                            rs.getInt("semester"),
                            rs.getString("minor_stream")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Delete a specific registration
    public static boolean deleteRegistration(String studentId, String courseId) {
        String sql = "DELETE FROM registrations WHERE student_id = ? AND course_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get All Students
    public static List<model.Student> getAllStudents() {
        List<model.Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new model.Student(
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("department"),
                        rs.getInt("semester"),
                        rs.getString("minor_stream")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    // Update Student Password
    public static boolean updateStudentPassword(String rollNo, String newPassword) {
        String sql = "UPDATE students SET password = ? WHERE roll_no = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, rollNo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Block Student from Course
    public static boolean blockStudentFromCourse(String studentId, String courseId) {
        String sql = "INSERT INTO blocked_registrations (student_id, course_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Unblock Student from Course
    public static boolean unblockStudentFromCourse(String studentId, String courseId) {
        String sql = "DELETE FROM blocked_registrations WHERE student_id = ? AND course_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get Blocked Courses for Student
    public static List<String> getBlockedCoursesForStudent(String studentId) {
        List<String> blocked = new ArrayList<>();
        String sql = "SELECT course_id FROM blocked_registrations WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    blocked.add(rs.getString("course_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blocked;
    }

}
