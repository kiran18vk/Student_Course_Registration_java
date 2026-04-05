package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginUI extends JFrame {
    private JTextField adminUserField;
    private JPasswordField adminPassField;
    private JTextField studentRollField;
    private JPasswordField studentPassField;

    public LoginUI() {
        setTitle("University Portal - Login");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 0, 0)); // No gap for clean split

        // Student Login Panel (Left)
        JPanel studentPanel = createLoginPanel("Student Portal", false);
        add(studentPanel);

        // Admin Login Panel (Right)
        JPanel adminPanel = createLoginPanel("Admin Access", true);
        add(adminPanel);

        setVisible(true);
    }

    private JPanel createLoginPanel(String title, boolean isAdmin) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        
        if (isAdmin) {
            wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(100, 50, 60, 50)
            ));
        } else {
            wrapper.setBorder(new EmptyBorder(100, 50, 60, 50));
        }

        // Header
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(headerLabel);
        wrapper.add(Box.createVerticalStrut(40));

        // Form Container
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(350, 400));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (isAdmin) {
            addFormField(form, "Username", adminUserField = new JTextField(15));
            addFormField(form, "Password", adminPassField = new JPasswordField(15));
            wrapper.add(form);

            wrapper.add(Box.createVerticalStrut(20));
            JButton loginBtn = new JButton("Login as Admin");
            styleButton(loginBtn);
            wrapper.add(loginBtn);

            loginBtn.addActionListener(e -> handleAdminLogin());
        } else {
            addFormField(form, "Roll Number", studentRollField = new JTextField(15));
            addFormField(form, "Password", studentPassField = new JPasswordField(15));
            wrapper.add(form);

            wrapper.add(Box.createVerticalStrut(20));
            JButton loginBtn = new JButton("Student Login");
            styleButton(loginBtn);
            wrapper.add(loginBtn);

            loginBtn.addActionListener(e -> handleStudentLogin());

            wrapper.add(Box.createVerticalStrut(15));
            JButton forgotBtn = new JButton("Reset Password");
            forgotBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            forgotBtn.setContentAreaFilled(false);
            forgotBtn.setBorderPainted(false);
            forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            forgotBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            wrapper.add(forgotBtn);

            forgotBtn.addActionListener(e -> {
                String roll = JOptionPane.showInputDialog(this, "Enter your Roll Number:", "Password Recovery", JOptionPane.QUESTION_MESSAGE);
                if (roll != null && !roll.trim().isEmpty()) {
                    model.Student student = service.CourseService.getStudentByRollNo(roll.trim());
                    if (student != null) {
                        boolean sent = service.EmailService.sendPasswordReset(student.getEmail(), student.getRollNo(), student.getPassword());
                        if (sent) {
                            JOptionPane.showMessageDialog(this, "Your password has been sent to your registered email ID.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to send email. Please contact IT.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No student found with that Roll Number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        wrapper.add(Box.createVerticalGlue());
        return wrapper;
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(350, 45));
        btn.setPreferredSize(new Dimension(350, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addFormField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(6));

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
    }

    private void handleAdminLogin() {
        String user = adminUserField.getText();
        String pass = new String(adminPassField.getPassword());
        if (user.equals("admin") && pass.equals("admin123")) {
            new AdminUI();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Admin Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleStudentLogin() {
        String roll = studentRollField.getText();
        String pass = new String(studentPassField.getPassword());

        if (roll.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (service.CourseService.verifyStudentLogin(roll, pass)) {
                String[] details = service.CourseService.getStudentDetails(roll);
                String name = (details != null && details.length > 0) ? details[0] : "Student";
                new StudentUI(name, roll);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Student Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        new LoginUI();
    }
}
