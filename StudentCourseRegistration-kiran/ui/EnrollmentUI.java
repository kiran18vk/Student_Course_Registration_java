package ui;

import javax.swing.*;
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
        setTitle("View Enrollments");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("VIEW ENROLLMENTS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> dispose());
        btnPanel.add(backBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginUI();
            dispose();
        });
        btnPanel.add(logoutBtn);

        headerPanel.add(btnPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Filter and Table Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Filter Sub-panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Select Course:"));

        List<Course> courses = CourseService.getCourses();
        courseBox = new JComboBox<>(courses.toArray(new Course[0]));
        courseBox.setPreferredSize(new Dimension(300, 30));
        courseBox.addActionListener(e -> updateTable());
        filterPanel.add(courseBox);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Student ID", "Department", "Student Name", "Sem" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(30);

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
