package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Course;
import service.CourseService;

public class AdminUI extends JFrame {
    public JTable table = new JTable();

    public AdminUI() {
        setTitle("Admin Panel - Course Management");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Admin Panel", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bp = new JPanel();
        String[] bts = { "Add Course", "Delete Course", "Registrations", "Refresh", "Logout" };
        for (String b : bts) {
            JButton btn = new JButton(b);
            bp.add(btn);
            btn.addActionListener(e -> {
                if (b.equals("Add Course"))
                    addC();
                else if (b.equals("Delete Course"))
                    delC();
                else if (b.equals("Registrations"))
                    showR();
                else if (b.equals("Refresh"))
                    refT();
                else {
                    new LoginUI();
                    dispose();
                }
            });
        }
        add(bp, BorderLayout.SOUTH);
        refT();
        setVisible(true);
    }

    public void refT() {
        List<Course> cs = CourseService.getCourses();
        String[][] d = new String[cs.size()][5];
        for (int i = 0; i < cs.size(); i++) {
            Course c = cs.get(i);
            d[i] = new String[] {
                    String.valueOf(c.getId()),
                    c.getName(),
                    c.getDepartment(),
                    String.valueOf(c.getCredits()),
                    String.valueOf(c.getSemester())
            };
        }
        table.setModel(new javax.swing.table.DefaultTableModel(d,
                new String[] { "ID", "Name", "Department", "Credits", "Semester" }));
    }

    public void addC() {
        JDialog dialog = new JDialog(this, "Add New Course", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and Fields
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        String[] depts = { "CSE", "AIML", "IT", "CSE-DS", "EEE", "MECH", "ECE" };
        JComboBox<String> deptBox = new JComboBox<>(depts);
        JTextField creditsField = new JTextField("3", 20);
        Integer[] sems = { 1, 2, 3, 4, 5, 6, 7, 8 };
        JComboBox<Integer> semBox = new JComboBox<>(sems);

        // UI Layout matching screenshot style
        addFormField(dialog, "Course ID", idField, gbc, 0);
        addFormField(dialog, "Course Name", nameField, gbc, 1);
        addFormField(dialog, "Department", deptBox, gbc, 2);
        addFormField(dialog, "Credits", creditsField, gbc, 3);
        addFormField(dialog, "Semester", semBox, gbc, 4);

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int credits = Integer.parseInt(creditsField.getText());
                String dept = (String) deptBox.getSelectedItem();
                int sem = (Integer) semBox.getSelectedItem();

                if (name.isEmpty())
                    throw new Exception();

                CourseService.addCourse(new Course(id, name, credits, dept, sem));
                refT();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid Input. Please check all fields.");
            }
        });

        dialog.setVisible(true);
    }

    private void addFormField(JDialog dialog, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridwidth = 1;
        gbc.gridy = row;
        gbc.gridx = 0;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }

    public void delC() {
        int r = table.getSelectedRow();
        if (r != -1) {
            CourseService.deleteCourse(Integer.parseInt(table.getValueAt(r, 0).toString()));
            refT();
        } else
            JOptionPane.showMessageDialog(this, "Select a row");
    }

    public void showR() {
        new EnrollmentUI();
    }
}