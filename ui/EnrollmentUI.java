package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Course;
import service.CourseService;


public class EnrollmentUI extends JFrame {
    private JComboBox<Course> courseBox;
    private JTable table;
    private DefaultTableModel model;

    public EnrollmentUI() {
        setTitle("Enrollment Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(25, 30, 20, 30));

        JLabel titleLabel = new JLabel("ENROLLMENT RECORDS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));

        JButton deleteBtn = new JButton("Revoke Registration");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a registration to delete.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String studentId = (String) table.getValueAt(selectedRow, 0);
            Course selectedCourse = (Course) courseBox.getSelectedItem();

            if (selectedCourse != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete the registration for " + table.getValueAt(selectedRow, 2)
                                 + " from " + selectedCourse.getName() + "?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (CourseService.deleteRegistration(studentId, selectedCourse.getId())) {
                        JOptionPane.showMessageDialog(this, "Registration deleted successfully.");
                        updateTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete registration.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        backBtn.addActionListener(e -> dispose());
        logoutBtn.addActionListener(e -> {
            new LoginUI();
            dispose();
        });

        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);
        btnPanel.add(logoutBtn);

        headerPanel.add(btnPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Filter and Table Panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(10, 30, 20, 30));

        // Filter Sub-panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterLabel = new JLabel("Select Course:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterPanel.add(filterLabel);

        List<Course> courses = CourseService.getCourses();
        courseBox = new JComboBox<>(courses.toArray(new Course[0]));
        courseBox.setPreferredSize(new Dimension(300, 30));
        courseBox.addActionListener(e -> updateTable());
        filterPanel.add(courseBox);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Student ID", "Department", "Student Name", "Sem" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        if (!courses.isEmpty()) {
            updateTable();
        }

        setVisible(true);
    }

    private void updateTable() {
        model.setRowCount(0);
        Course selected = (Course) courseBox.getSelectedItem();
        if (selected != null) {
            List<String[]> enrollments = CourseService.getEnrollmentsForCourse(selected.getId());
            for (String[] row : enrollments) {
                model.addRow(row);
            }
        }
    }
}
