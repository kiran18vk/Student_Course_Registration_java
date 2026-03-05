package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginUI extends JFrame {
    private JTextField adminUserField;
    private JPasswordField adminPassField;
    private JTextField studentRollField;
    private JTextField studentNameField;
    private JPasswordField studentPassField;

    public LoginUI() {
        setTitle("Login");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 30, 0));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Student Login Panel (Left)
        JPanel studentPanel = createLoginPanel("Student Login", false);
        add(studentPanel);

        // Admin Login Panel (Right)
        JPanel adminPanel = createLoginPanel("Admin Login", true);
        add(adminPanel);

        setVisible(true);
    }

    private JPanel createLoginPanel(String title, boolean isAdmin) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(240, 240, 240));
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header (Centered Title)
        JLabel headerLabel = new JLabel(title, JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(50, 50, 50));
        headerLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        wrapper.add(headerLabel, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        if (isAdmin) {
            addFormField(form, "Username:", adminUserField = new JTextField(15), gbc, 0);
            addFormField(form, "Password:", adminPassField = new JPasswordField(15), gbc, 1);

            JButton loginBtn = createCenteredButton("Login");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(40, 10, 10, 10);
            form.add(loginBtn, gbc);

            loginBtn.addActionListener(e -> handleAdminLogin());
        } else {
            addFormField(form, "Roll No:", studentRollField = new JTextField(15), gbc, 0);
            addFormField(form, "Name:", studentNameField = new JTextField(15), gbc, 1);
            addFormField(form, "Password:", studentPassField = new JPasswordField(15), gbc, 2);

            JButton loginBtn = createCenteredButton("Login");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(40, 10, 10, 10);
            form.add(loginBtn, gbc);

            loginBtn.addActionListener(e -> handleStudentLogin());
        }

        wrapper.add(form, BorderLayout.CENTER);
        return wrapper;
    }

    private void addFormField(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(field, gbc);
    }

    private JButton createCenteredButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(150, 40));
        // Simple light gray gradient look is default for system look and feel
        return btn;
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
        String name = studentNameField.getText();
        String pass = new String(studentPassField.getPassword());

        if (roll.isEmpty() || name.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            new StudentUI(name, roll);
            dispose();
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
