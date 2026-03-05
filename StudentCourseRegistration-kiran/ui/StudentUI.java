package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import service.CourseService;

public class StudentUI extends JFrame {
        public String name, id;
        public DefaultTableModel model = new DefaultTableModel(
                        new String[] { "ID", "Name", "Department", "Credits", "Semester" }, 0);

        public StudentUI(String name, String id) {
                this.name = name;
                this.id = id;
                setTitle("Student Panel");
                setSize(700, 400);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);

                JLabel wl = new JLabel("Welcome, " + name, 0);
                wl.setFont(new Font("Arial", 1, 20));
                add(wl, BorderLayout.NORTH);
                add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

                JPanel bp = new JPanel();
                String[] bts = { "Register", "My Courses", "Logout" };
                for (String b : bts) {
                        JButton btn = new JButton(b);
                        bp.add(btn);
                        btn.addActionListener(e -> {
                                if (b.equals("Register"))
                                        regC();
                                else if (b.equals("My Courses"))
                                        viewC();
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
                model.setRowCount(0);
                CourseService.getCourses()
                                .forEach(c -> model.addRow(new Object[] { c.getId(), c.getName(), c.getDepartment(),
                                                c.getCredits(), c.getSemester() }));
        }

        public void regC() {
                JDialog dialog = new JDialog(this, "Student Course Registration", true);
                dialog.setSize(500, 450);
                dialog.setLayout(new GridBagLayout());
                dialog.setLocationRelativeTo(this);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.anchor = GridBagConstraints.WEST;

                // Student Name
                gbc.gridx = 0;
                gbc.gridy = 0;
                dialog.add(new JLabel("Student Name:"), gbc);
                JTextField nameField = new JTextField(name, 20);
                nameField.setEditable(false);
                gbc.gridx = 1;
                dialog.add(nameField, gbc);

                // Student ID
                gbc.gridx = 0;
                gbc.gridy = 1;
                dialog.add(new JLabel("Student ID:"), gbc);
                JTextField idField = new JTextField(id, 20);
                idField.setEditable(false);
                gbc.gridx = 1;
                dialog.add(idField, gbc);

                // Semester
                gbc.gridx = 0;
                gbc.gridy = 2;
                dialog.add(new JLabel("Semester (1-8):"), gbc);
                Integer[] semesters = { 1, 2, 3, 4, 5, 6, 7, 8 };
                JComboBox<Integer> semBox = new JComboBox<>(semesters);
                gbc.gridx = 1;
                dialog.add(semBox, gbc);

                // Department
                gbc.gridx = 0;
                gbc.gridy = 3;
                dialog.add(new JLabel("Department:"), gbc);
                String[] dpts = { "CSE", "AIML", "IT", "CSE-DS", "EEE", "MECH", "ECE" };
                JComboBox<String> dptBox = new JComboBox<>(dpts);
                gbc.gridx = 1;
                dialog.add(dptBox, gbc);

                // Select Course
                gbc.gridx = 0;
                gbc.gridy = 5;
                dialog.add(new JLabel("Select Course(s):"), gbc);

                List<Course> courses = CourseService.getCourses();
                JPanel coursePanel = new JPanel();
                coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
                List<JCheckBox> checkBoxes = new ArrayList<>();

                JTextField creditsField = new JTextField("0", 20);
                creditsField.setEditable(false);

                java.awt.event.ActionListener checkListener = e -> {
                        int totalCredits = 0;
                        for (int i = 0; i < checkBoxes.size(); i++) {
                                if (checkBoxes.get(i).isSelected()) {
                                        totalCredits += courses.get(i).getCredits();
                                }
                        }
                        creditsField.setText(String.valueOf(totalCredits));
                };

                for (Course c : courses) {
                        JCheckBox cb = new JCheckBox(c.toString());
                        cb.addActionListener(checkListener);
                        checkBoxes.add(cb);
                        coursePanel.add(cb);
                }

                JScrollPane scrollPane = new JScrollPane(coursePanel);
                scrollPane.setPreferredSize(new Dimension(250, 100));
                gbc.gridx = 1;
                gbc.gridy = 5;
                dialog.add(scrollPane, gbc);

                // Credits
                gbc.gridx = 0;
                gbc.gridy = 6;
                dialog.add(new JLabel("Total Credits:"), gbc);
                gbc.gridx = 1;
                dialog.add(creditsField, gbc);

                // Registration Date
                gbc.gridx = 0;
                gbc.gridy = 7;
                dialog.add(new JLabel("Registration Date:"), gbc);
                JTextField dateField = new JTextField(LocalDate.now().toString(), 20);
                dateField.setEditable(false);
                gbc.gridx = 1;
                dialog.add(dateField, gbc);

                // Buttons
                JPanel btnPanel = new JPanel();
                JButton okBtn = new JButton("OK");
                JButton cancelBtn = new JButton("Cancel");
                btnPanel.add(okBtn);
                btnPanel.add(cancelBtn);

                gbc.gridx = 0;
                gbc.gridy = 8;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                dialog.add(btnPanel, gbc);

                okBtn.addActionListener(e -> {
                        List<Course> selectedCourses = new ArrayList<>();
                        for (int i = 0; i < checkBoxes.size(); i++) {
                                if (checkBoxes.get(i).isSelected()) {
                                        selectedCourses.add(courses.get(i));
                                }
                        }

                        if (!selectedCourses.isEmpty()) {
                                List<Course> existingRegistrations = CourseService.getRegistrations()
                                                .getOrDefault(id, new ArrayList<>());

                                List<Course> toAdd = new ArrayList<>();
                                StringBuilder message = new StringBuilder();

                                for (Course selected : selectedCourses) {
                                        if (existingRegistrations.stream()
                                                        .anyMatch(c -> c.getId() == selected.getId())) {
                                                message.append("Already registered for: ").append(selected.getName())
                                                                .append("\n");
                                        } else {
                                                toAdd.add(selected);
                                        }
                                }

                                if (!toAdd.isEmpty()) {
                                        existingRegistrations.addAll(toAdd);
                                        boolean success = CourseService.registerCourses(id, name,
                                                        (String) dptBox.getSelectedItem(),
                                                        String.valueOf(semBox.getSelectedItem()),
                                                        existingRegistrations);
                                        if (success) {
                                                message.append("Successfully registered for ").append(toAdd.size())
                                                                .append(" course(s)!");
                                                JOptionPane.showMessageDialog(dialog, message.toString());
                                                dialog.dispose();
                                        } else {
                                                JOptionPane.showMessageDialog(dialog,
                                                                "Failed to register courses. Please try again.",
                                                                "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                } else if (message.length() > 0) {
                                        JOptionPane.showMessageDialog(dialog, message.toString());
                                }
                        } else {
                                JOptionPane.showMessageDialog(dialog, "Please select at least one course.");
                        }
                });

                cancelBtn.addActionListener(e -> dialog.dispose());

                dialog.setVisible(true);
        }

        public void viewC() {
                StringBuilder sb = new StringBuilder();
                CourseService.getRegistrations().getOrDefault(id, new ArrayList<>())
                                .forEach(c -> sb.append(c.getName()).append(" (").append(c.getDepartment())
                                                .append(", Sem: ").append(c.getSemester()).append(")\n"));
                JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No courses", "My Courses", 1);
        }
}