package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import model.Course;
import service.CourseService;

public class AdminUI extends JFrame {
    public JTable table = new JTable();

    public AdminUI() {
        setTitle("Admin Portal - University Management");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Header Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("ADMINISTRATOR DASHBOARD");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title, BorderLayout.WEST);
        
        add(header, BorderLayout.NORTH);

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] bts = { "Add Course", "Edit Course", "Delete Course", "Add Student", "View Students", "Registrations", "Clear Registrations", "Refresh", "Logout" };
        for (String b : bts) {
            JButton btn = new JButton(b);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 40));
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
            btn.addActionListener(e -> {
                if (b.equals("Add Course"))
                    addC();
                else if (b.equals("Edit Course"))
                    editC();
                else if (b.equals("Delete Course"))
                    delC();
                else if (b.equals("Add Student"))
                    addS();
                else if (b.equals("View Students"))
                    viewS();
                else if (b.equals("Registrations"))
                    showR();
                else if (b.equals("Clear Registrations"))
                    clearR();
                else if (b.equals("Refresh"))
                    refT();
                else {
                    new LoginUI();
                    dispose();
                }
            });
        }
        add(sidebar, BorderLayout.WEST);

        // Main Content
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane, BorderLayout.CENTER);
        
        add(content, BorderLayout.CENTER);

        refT();
        setVisible(true);
    }

    public void refT() {
        List<Course> cs = CourseService.getCourses();
        String[][] d = new String[cs.size()][6];
        for (int i = 0; i < cs.size(); i++) {
            Course c = cs.get(i);
            d[i] = new String[] {
                    String.valueOf(c.getId()),
                    c.getName(),
                    c.getDepartment(),
                    String.valueOf(c.getCredits()),
                    String.valueOf(c.getSemester()),
                    c.getMinorStream()
            };
        }
        table.setModel(new javax.swing.table.DefaultTableModel(d,
                new String[] { "ID", "Name", "Department", "Credits", "Semester", "Minor Stream" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        JPanel deptPanel = new JPanel(new GridLayout(0, 2));
        List<JCheckBox> deptCheckBoxes = new ArrayList<>();
        for (String d : depts) {
            JCheckBox cb = new JCheckBox(d);
            deptCheckBoxes.add(cb);
            deptPanel.add(cb);
        }
        
        JTextField creditsField = new JTextField("3", 20);
        Integer[] sems = { 1, 2, 3, 4, 5, 6, 7, 8 };
        JComboBox<Integer> semBox = new JComboBox<>(sems);
        String[] minorStreams = { "None", "Network Security", "Machine Intelligence", "Data Engineering", "Cognitive Analytics", "Mobile application development" };
        JComboBox<String> minorField = new JComboBox<>(minorStreams);

        // UI Layout matching screenshot style
        addFormField(dialog, "Course ID", idField, gbc, 0);
        addFormField(dialog, "Course Name", nameField, gbc, 1);
        gbc.gridy = 2; gbc.gridx = 0; dialog.add(new JLabel("Departments"), gbc);
        gbc.gridx = 1; dialog.add(new JScrollPane(deptPanel), gbc);
        addFormField(dialog, "Credits", creditsField, gbc, 3);
        addFormField(dialog, "Semester", semBox, gbc, 4);
        addFormField(dialog, "Minor Stream", minorField, gbc, 5);

        JButton saveBtn = new JButton("Save Course");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                int credits = Integer.parseInt(creditsField.getText());
                
                List<String> selectedDepts = new ArrayList<>();
                for (JCheckBox cb : deptCheckBoxes) {
                    if (cb.isSelected()) selectedDepts.add(cb.getText());
                }
                String dept = String.join(",", selectedDepts);
                int sem = (Integer) semBox.getSelectedItem();
                String minor = (String) minorField.getSelectedItem();
                if (minor.equals("None")) minor = "";

                if (CourseService.addCourse(new Course(id, name, credits, dept, sem, minor))) {
                    refT();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add course. Course ID might already exist!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid Input. Please check all fields.");
            }
        });

        dialog.setVisible(true);
    }

    public void editC() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to edit");
            return;
        }

        String id = table.getValueAt(r, 0).toString();
        String name = table.getValueAt(r, 1).toString();
        String dept = table.getValueAt(r, 2).toString();
        int credits = Integer.parseInt(table.getValueAt(r, 3).toString());
        int sem = Integer.parseInt(table.getValueAt(r, 4).toString());
        String minor = table.getValueAt(r, 5) != null ? table.getValueAt(r, 5).toString() : "";

        JDialog dialog = new JDialog(this, "Edit Course", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(id, 20);
        idField.setEditable(false);
        JTextField nameField = new JTextField(name, 20);
        String[] depts = { "CSE", "AIML", "IT", "CSE-DS", "EEE", "MECH", "ECE" };
        JPanel deptPanel = new JPanel(new GridLayout(0, 2));
        List<JCheckBox> deptCheckBoxes = new ArrayList<>();
        String[] currentDepts = dept.split(",");
        for (String d : depts) {
            JCheckBox cb = new JCheckBox(d);
            for (String cd : currentDepts) {
                if (cd.trim().equals(d)) cb.setSelected(true);
            }
            deptCheckBoxes.add(cb);
            deptPanel.add(cb);
        }

        JTextField creditsField = new JTextField(String.valueOf(credits), 20);
        Integer[] sems = { 1, 2, 3, 4, 5, 6, 7, 8 };
        JComboBox<Integer> semBox = new JComboBox<>(sems);
        semBox.setSelectedItem(sem);
        String[] minorStreams = { "None", "Network Security", "Machine Intelligence", "Data Engineering", "Cognitive Analytics", "Mobile application development" };
        JComboBox<String> minorField = new JComboBox<>(minorStreams);
        minorField.setSelectedItem(minor.isEmpty() ? "None" : minor);

        addFormField(dialog, "Course ID", idField, gbc, 0);
        addFormField(dialog, "Course Name", nameField, gbc, 1);
        gbc.gridy = 2; gbc.gridx = 0; dialog.add(new JLabel("Departments"), gbc);
        gbc.gridx = 1; dialog.add(new JScrollPane(deptPanel), gbc);
        addFormField(dialog, "Credits", creditsField, gbc, 3);
        addFormField(dialog, "Semester", semBox, gbc, 4);
        addFormField(dialog, "Minor Stream", minorField, gbc, 5);

        JButton saveBtn = new JButton("Update Changes");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                String uName = nameField.getText();
                int uCredits = Integer.parseInt(creditsField.getText());
                List<String> selectedDepts = new ArrayList<>();
                for (JCheckBox cb : deptCheckBoxes) {
                    if (cb.isSelected()) selectedDepts.add(cb.getText());
                }
                String uDept = String.join(",", selectedDepts);
                int uSem = (Integer) semBox.getSelectedItem();
                String uMinor = (String) minorField.getSelectedItem();
                if (uMinor.equals("None")) uMinor = "";

                if (uName.isEmpty() || uDept.isEmpty())
                    throw new Exception("Name and at least one Department are required");

                if (CourseService.updateCourse(new Course(id, uName, uCredits, uDept, uSem, uMinor))) {
                    refT();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid Input. Please check all fields.");
            }
        });

        dialog.setVisible(true);
    }

    public void addS() {
        JDialog dialog = new JDialog(this, "Add New Student", true);
        dialog.setSize(400, 500);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField rollField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        String[] depts = { "CSE", "AIML", "IT", "CSE-DS", "EEE", "MECH", "ECE" };
        JComboBox<String> deptBox = new JComboBox<>(depts);
        Integer[] sems = { 1, 2, 3, 4, 5, 6, 7, 8 };
        JComboBox<Integer> semBox = new JComboBox<>(sems);
        String[] minorStreams = { "None", "Network Security", "Machine Intelligence", "Data Engineering", "Cognitive Analytics", "Mobile application development" };
        JComboBox<String> minorField = new JComboBox<>(minorStreams);
        JTextField passField = new JTextField(20);
        passField.setEditable(true);

        addFormField(dialog, "Roll No", rollField, gbc, 0);
        addFormField(dialog, "Name", nameField, gbc, 1);
        addFormField(dialog, "Email", emailField, gbc, 2);
        addFormField(dialog, "Department", deptBox, gbc, 3);
        addFormField(dialog, "Semester", semBox, gbc, 4);
        addFormField(dialog, "Minor Stream", minorField, gbc, 5);
        addFormField(dialog, "Generated Password", passField, gbc, 6);

        JButton genBtn = new JButton("Generate Password");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        dialog.add(genBtn, gbc);

        genBtn.addActionListener(e -> {
            passField.setText(generatePassword());
        });

        JButton saveBtn = new JButton("Save Student Account");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                String rRoll = rollField.getText();
                String rName = nameField.getText();
                String rEmail = emailField.getText();
                String rDept = (String) deptBox.getSelectedItem();
                int rSem = (Integer) semBox.getSelectedItem();
                String rPass = passField.getText();
                String rMinor = (String) minorField.getSelectedItem();
                if (rMinor.equals("None")) rMinor = "";

                if (rRoll.isEmpty() || rName.isEmpty() || rEmail.isEmpty() || rPass.isEmpty())
                    throw new Exception("All fields are required");

                if (!rEmail.contains("@"))
                    throw new Exception("Invalid email format");

                model.Student s = new model.Student(rRoll, rName, rEmail, rPass, rDept, rSem, rMinor);
                if (CourseService.addStudent(s)) {
                    boolean emailSent = service.EmailService.sendCredentials(rEmail, rRoll, rPass);
                    String msg = "Student Added Successfully!\nPassword: " + rPass;
                    if (emailSent) {
                        msg += "\n\nAn email with these credentials has been sent to the student.";
                    } else {
                        msg += "\n\nWarning: Failed to send email with credentials.";
                    }
                    JOptionPane.showMessageDialog(dialog, msg);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add student. Roll No might already exist.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder sb = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
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
            CourseService.deleteCourse(table.getValueAt(r, 0).toString());
            refT();
        } else
            JOptionPane.showMessageDialog(this, "Select a row");
    }

    public void showR() {
        new EnrollmentUI();
    }

    private void clearR() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear ALL student registrations?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (CourseService.clearAllRegistrations()) {
                JOptionPane.showMessageDialog(this, "All registrations cleared successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to clear registrations.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void viewS() {
        JDialog dialog = new JDialog(this, "Registered Students", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        List<model.Student> students = CourseService.getAllStudents();
        String[][] data = new String[students.size()][6];
        for (int i = 0; i < students.size(); i++) {
            model.Student s = students.get(i);
            data[i] = new String[] { s.getRollNo(), s.getName(), s.getEmail(), s.getDepartment(), String.valueOf(s.getSemester()), s.getMinorStream() };
        }

        String[] columns = { "Roll No", "Name", "Email", "Department", "Semester", "Minor Stream" };
        JTable studentTable = new JTable(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        dialog.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel p = new JPanel();
        
        JButton editBtn = new JButton("Edit Student");
        editBtn.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                String roll = (String) studentTable.getValueAt(row, 0);
                String name = (String) studentTable.getValueAt(row, 1);
                String email = (String) studentTable.getValueAt(row, 2);
                String dept = (String) studentTable.getValueAt(row, 3);
                int sem = Integer.parseInt((String) studentTable.getValueAt(row, 4));
                String minor = studentTable.getValueAt(row, 5) != null ? (String) studentTable.getValueAt(row, 5) : "";

                JDialog editDialog = new JDialog(dialog, "Edit Student - " + roll, true);
                editDialog.setSize(400, 450);
                editDialog.setLayout(new GridBagLayout());
                editDialog.setLocationRelativeTo(dialog);
                
                GridBagConstraints eGbc = new GridBagConstraints();
                eGbc.insets = new Insets(10, 10, 10, 10);
                eGbc.fill = GridBagConstraints.HORIZONTAL;
                
                JTextField eNameField = new JTextField(name, 20);
                JTextField eEmailField = new JTextField(email, 20);
                String[] eDepts = { "CSE", "AIML", "IT", "CSE-DS", "EEE", "MECH", "ECE" };
                JComboBox<String> eDeptBox = new JComboBox<>(eDepts);
                eDeptBox.setSelectedItem(dept);
                
                Integer[] eSems = { 1, 2, 3, 4, 5, 6, 7, 8 };
                JComboBox<Integer> eSemBox = new JComboBox<>(eSems);
                eSemBox.setSelectedItem(sem);
                String[] eMinorStreams = { "None", "Network Security", "Machine Intelligence", "Data Engineering", "Cognitive Analytics", "Mobile application development" };
                JComboBox<String> eMinorField = new JComboBox<>(eMinorStreams);
                eMinorField.setSelectedItem(minor.isEmpty() ? "None" : minor);
                
                addFormField(editDialog, "Name", eNameField, eGbc, 0);
                addFormField(editDialog, "Email", eEmailField, eGbc, 1);
                addFormField(editDialog, "Department", eDeptBox, eGbc, 2);
                addFormField(editDialog, "Semester", eSemBox, eGbc, 3);
                addFormField(editDialog, "Minor Stream", eMinorField, eGbc, 4);
                
                JButton saveBtn = new JButton("Update Details");
                eGbc.gridx = 0; eGbc.gridy = 5; eGbc.gridwidth = 2;
                editDialog.add(saveBtn, eGbc);
                
                saveBtn.addActionListener(ev -> {
                    String uName = eNameField.getText().trim();
                    String uEmail = eEmailField.getText().trim();
                    String uDept = (String) eDeptBox.getSelectedItem();
                    int uSem = (Integer) eSemBox.getSelectedItem();
                    String uMinor = (String) eMinorField.getSelectedItem();
                    if (uMinor.equals("None")) uMinor = "";
                    
                    if (uName.isEmpty() || uEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(editDialog, "Name and Email cannot be empty.");
                        return;
                    }
                    if (!uEmail.contains("@")) {
                        JOptionPane.showMessageDialog(editDialog, "Invalid email format!");
                        return;
                    }
                    
                    if (CourseService.updateStudentDetails(roll, uName, uEmail, uDept, uSem, uMinor)) {
                        JOptionPane.showMessageDialog(editDialog, "Student Updated Successfully!");
                        studentTable.setValueAt(uName, row, 1);
                        studentTable.setValueAt(uEmail, row, 2);
                        studentTable.setValueAt(uDept, row, 3);
                        studentTable.setValueAt(String.valueOf(uSem), row, 4);
                        studentTable.setValueAt(uMinor, row, 5);
                        editDialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(editDialog, "Failed to update student.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                
                editDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a student to edit.");
            }
        });
        p.add(editBtn);

        JButton resetBtn = new JButton("Reset Password");
        resetBtn.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                String roll = (String) studentTable.getValueAt(row, 0);
                String name = (String) studentTable.getValueAt(row, 1);
                String email = (String) studentTable.getValueAt(row, 2);
                
                JDialog resetDialog = new JDialog(dialog, "Reset Password - " + name, true);
                resetDialog.setSize(350, 200);
                resetDialog.setLayout(new GridBagLayout());
                resetDialog.setLocationRelativeTo(dialog);
                
                GridBagConstraints rGbc = new GridBagConstraints();
                rGbc.insets = new Insets(10, 10, 10, 10);
                rGbc.fill = GridBagConstraints.HORIZONTAL;
                
                JTextField passField = new JTextField(15);
                JButton genBtn = new JButton("Generate");
                JButton saveBtn = new JButton("Save");
                
                rGbc.gridx = 0; rGbc.gridy = 0;
                resetDialog.add(new JLabel("New Password:"), rGbc);
                rGbc.gridx = 1;
                resetDialog.add(passField, rGbc);
                
                rGbc.gridx = 0; rGbc.gridy = 1;
                resetDialog.add(genBtn, rGbc);
                rGbc.gridx = 1;
                resetDialog.add(saveBtn, rGbc);
                
                genBtn.addActionListener(e2 -> passField.setText(generatePassword()));
                
                saveBtn.addActionListener(e2 -> {
                    String newPass = passField.getText().trim();
                    if (!newPass.isEmpty()) {
                        if (CourseService.updateStudentPassword(roll, newPass)) {
                            boolean emailSent = service.EmailService.sendCredentials(email, roll, newPass);
                            String msg = "Password updated successfully!";
                            if (emailSent) {
                                msg += "\n\nAn email with the new password has been sent to the student.";
                            }
                            JOptionPane.showMessageDialog(resetDialog, msg);
                            resetDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(resetDialog, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(resetDialog, "Password cannot be empty.");
                    }
                });
                
                resetDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a student from the table.");
            }
        });
        p.add(resetBtn);

        JButton blockBtn = new JButton("Manage Restrictions");
        blockBtn.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                String roll = (String) studentTable.getValueAt(row, 0);
                String name = (String) studentTable.getValueAt(row, 1);
                showBlockDialog(dialog, roll, name);
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a student.");
            }
        });
        p.add(blockBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        p.add(closeBtn);
        dialog.add(p, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showBlockDialog(JDialog parent, String studentId, String studentName) {
        JDialog bDialog = new JDialog(parent, "Restrictions - " + studentName, true);
        bDialog.setSize(500, 400);
        bDialog.setLocationRelativeTo(parent);
        bDialog.setLayout(new BorderLayout());

        List<Course> allCourses = CourseService.getCourses();
        List<String> blockedIds = CourseService.getBlockedCoursesForStudent(studentId);

        String[][] data = new String[allCourses.size()][4];
        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            data[i] = new String[] { 
                String.valueOf(c.getId()), 
                c.getName(), 
                c.getDepartment(), 
                blockedIds.contains(c.getId()) ? "BLOCKED" : "Allowed" 
            };
        }

        JTable bTable = new JTable(new javax.swing.table.DefaultTableModel(data, 
            new String[] { "ID", "Name", "Department", "Status" }) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        bDialog.add(new JScrollPane(bTable), BorderLayout.CENTER);

        JPanel p = new JPanel();
        JButton blockBtn = new JButton("Block Selected");
        JButton unblockBtn = new JButton("Allow Selected");

        blockBtn.addActionListener(e -> {
            int r = bTable.getSelectedRow();
            if (r != -1) {
                String cId = bTable.getValueAt(r, 0).toString();
                if (CourseService.blockStudentFromCourse(studentId, cId)) {
                    JOptionPane.showMessageDialog(bDialog, "Course Blocked!");
                    bDialog.dispose();
                    showBlockDialog(parent, studentId, studentName);
                }
            }
        });

        unblockBtn.addActionListener(e -> {
            int r = bTable.getSelectedRow();
            if (r != -1) {
                String cId = bTable.getValueAt(r, 0).toString();
                if (CourseService.unblockStudentFromCourse(studentId, cId)) {
                    JOptionPane.showMessageDialog(bDialog, "Course Allowed!");
                    bDialog.dispose();
                    showBlockDialog(parent, studentId, studentName);
                }
            }
        });

        p.add(blockBtn); p.add(unblockBtn);
        bDialog.add(p, BorderLayout.SOUTH);
        bDialog.setVisible(true);
    }
}