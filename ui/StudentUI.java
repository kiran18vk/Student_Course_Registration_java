package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
			new String[] { "ID", "Name", "Department", "Credits", "Semester", "Minor Stream" }, 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	public StudentUI(String name, String id) {
		this.name = name;
		this.id = id;
		setTitle("Student Portal - University Management");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel topPanel = new JPanel(new BorderLayout(0, 15));
		topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));

		JLabel wl = new JLabel("Welcome back, " + name);
		wl.setFont(new Font("Segoe UI", Font.BOLD, 24));
		topPanel.add(wl, BorderLayout.NORTH);

		String[] details = CourseService.getStudentDetails(id);
		String dept = (details != null && details.length > 1) ? details[1] : "N/A";
		String semStr = (details != null && details.length > 2) ? details[2] : "N/A";
		String emailStr = (details != null && details.length > 3) ? details[3] : "N/A";
		String minorStr = (details != null && details.length > 4 && details[4] != null) ? details[4] : "None";
		if (minorStr.trim().isEmpty()) minorStr = "None";
		
		JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 10, 5));
		detailsPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
			new EmptyBorder(10, 15, 10, 15)
		));
		detailsPanel.setBackground(new Color(245, 247, 250));
		
		JLabel idLabel = new JLabel("<html><b>Roll No:</b> " + id + "</html>");
		JLabel deptLabel = new JLabel("<html><b>Department:</b> " + dept + "</html>");
		JLabel semLabel = new JLabel("<html><b>Semester:</b> " + semStr + "</html>");
		JLabel emailLabel = new JLabel("<html><b>Mail ID:</b> " + emailStr + "</html>");
		JLabel minorLabel = new JLabel("<html><b>Minor Stream:</b> " + minorStr + "</html>");
		
		idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		semLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		minorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		
		detailsPanel.add(idLabel);
		detailsPanel.add(deptLabel);
		detailsPanel.add(semLabel);
		detailsPanel.add(emailLabel);
		detailsPanel.add(minorLabel);
		detailsPanel.add(new JLabel("")); // empty space

		
		topPanel.add(detailsPanel, BorderLayout.CENTER);

		add(topPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
		centerPanel.setBorder(new EmptyBorder(10, 30, 20, 30));
		
		JLabel tableTitle = new JLabel("My Enrolled Courses");
		tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		centerPanel.add(tableTitle, BorderLayout.NORTH);

		JTable table = new JTable(model);
		table.setRowHeight(30);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		JScrollPane scrollPane = new JScrollPane(table);
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		add(centerPanel, BorderLayout.CENTER);

		JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
		bp.setBorder(new EmptyBorder(10, 20, 10, 20));

		String[] bts = { "Register", "Select Minor", "My Courses", "Change Password", "Logout" };
		for (String b : bts) {
			JButton btn = new JButton(b);
			btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
			bp.add(btn);
			btn.addActionListener(e -> {
				if (b.equals("Register"))
					regC();
				else if (b.equals("Select Minor"))
					selectMinor();
				else if (b.equals("My Courses"))
					viewC();
				else if (b.equals("Change Password"))
					changePassword();
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
		CourseService.getRegistrations().getOrDefault(id, new ArrayList<>())
				.forEach(c -> model.addRow(new Object[] { c.getId(), c.getName(), c.getDepartment(),
						c.getCredits(), c.getSemester(), c.getMinorStream() }));
	}

	public void selectMinor() {
		String[] details = CourseService.getStudentDetails(id);
		if (details == null) return;
		String studentDept = details.length > 1 ? details[1] : "";
		String currentMinor = (details.length > 4 && details[4] != null) ? details[4] : "";
		
		if (!currentMinor.isEmpty() && !currentMinor.equals("None")) {
			JOptionPane.showMessageDialog(this, "You have already selected " + currentMinor + ".\nMinor Streams cannot be changed by students. Please contact an Administrator.", "Action Denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String[] options;
		switch(studentDept) {
			case "CSE":
				options = new String[]{"None", "Network Security", "Machine Intelligence", "Data Engineering"};
				break;
			case "AIML":
				options = new String[]{"None", "Cognitive Analytics", "Mobile application development", "Network Security"};
				break;
			case "IT":
			case "CSE-DS":
				options = new String[]{"None", "Network Security", "Machine Intelligence"};
				break;
			default:
				options = new String[]{"None"};
				break;
		}
		
		JComboBox<String> streamBox = new JComboBox<>(options);
		if (!currentMinor.isEmpty()) {
			streamBox.setSelectedItem(currentMinor);
		}
		
		JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
		panel.add(new JLabel("Select your Minor Stream:"));
		panel.add(streamBox);
		
		int result = JOptionPane.showConfirmDialog(this, panel, "Select Minor Stream", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String selected = (String) streamBox.getSelectedItem();
			if (selected.equals("None")) {
				selected = "";
			}
			String nameStr = details[0] != null ? details[0] : name;
			int sem = details.length > 2 ? Integer.parseInt(details[2]) : 1;
			String emailStr = details.length > 3 ? details[3] : "";
			
			if (CourseService.updateStudentDetails(id, nameStr, emailStr, studentDept, sem, selected)) {
				JOptionPane.showMessageDialog(this, "Minor Stream updated successfully!");
				new StudentUI(name, id);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Failed to update Minor Stream.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void regC() {
		JDialog dialog = new JDialog(this, "Student Course Registration", true);
		dialog.setSize(500, 450);
		dialog.setLayout(new GridBagLayout());
		dialog.setLocationRelativeTo(this);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Fetch student details from DB
		String[] details = CourseService.getStudentDetails(id);
		String studentDept = (details != null && details.length > 1) ? details[1] : "";
		int studentSem = (details != null && details.length > 2) ? Integer.parseInt(details[2]) : 1;
		String studentMinor = (details != null && details.length > 4 && details[4] != null) ? details[4] : "";

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
		semBox.setSelectedItem(studentSem);
		semBox.setEnabled(false);
		gbc.gridx = 1;
		dialog.add(semBox, gbc);

		// Department
		gbc.gridx = 0;
		gbc.gridy = 3;
		dialog.add(new JLabel("Department:"), gbc);
		String[] dpts = { "CSE", "AIML", "IT", "CSE-DS", "EEE", "MECH", "ECE" };
		JComboBox<String> dptBox = new JComboBox<>(dpts);
		if (!studentDept.isEmpty()) {
			dptBox.setSelectedItem(studentDept);
		}
		dptBox.setEnabled(false);
		gbc.gridx = 1;
		dialog.add(dptBox, gbc);

		// Select Course
		gbc.gridx = 0;
		gbc.gridy = 5;
		dialog.add(new JLabel("Select Course(s):"), gbc);

		// Filter courses by branch (dept) and semester
		List<Course> courses = CourseService.getCoursesByDeptAndSemester(studentDept, studentSem);
		List<String> blockedIds = CourseService.getBlockedCoursesForStudent(id);
		courses.removeIf(c -> blockedIds.contains(c.getId()));

		// Filter out courses that have a minor stream which does not match the student's minor stream
		courses.removeIf(c -> {
			String cm = c.getMinorStream();
			if (cm != null && !cm.trim().isEmpty()) {
				return !cm.trim().equalsIgnoreCase(studentMinor.trim());
			}
			return false;
		});

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

		if (courses.isEmpty()) {
			coursePanel.add(new JLabel("No " + studentDept + " courses available for Semester " + studentSem));
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
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		
		JButton okBtn = new JButton("Register Selection");
		
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
							.anyMatch(c -> c.getId().equals(selected.getId()))) {
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
						refT();
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

	public void changePassword() {
		JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
		JPasswordField currentPassField = new JPasswordField(15);
		JPasswordField newPassField = new JPasswordField(15);
		
		panel.add(new JLabel("Current Password:"));
		panel.add(currentPassField);
		panel.add(new JLabel("New Password:"));
		panel.add(newPassField);
		
		int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String currentPass = new String(currentPassField.getPassword());
			String newPass = new String(newPassField.getPassword());
			
			if (currentPass.isEmpty() || newPass.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (CourseService.verifyStudentLogin(id, currentPass)) {
				if (CourseService.updateStudentPassword(id, newPass)) {
					JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}