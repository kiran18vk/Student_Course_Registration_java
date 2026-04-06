# Student Course Registration System

A robust, Java-based desktop application for managing student registrations, course enrollments, and automated communication. This system provides a seamless interface for both administrators and students, backed by a MySQL database and a purely Java-based email notification system.

## 🚀 Key Features

### For Administrators
*   **Student Management**: Add new students with detailed profiles (Name, Email, Roll Number).
*   **Automatic Credentials**: The system generates a password and automatically emails it to the student upon registration.
*   **Password Control**: Admins can reset student passwords, triggering an automated notification email.

### For Students
*   **Secure Login**: Role-based access for students and administrators.
*   **Course Selection**: Browse and enroll in available courses.
*   **Self-Service**: Update account passwords and recover forgotten credentials via email.

### System Features
*   **Mail System**: Integrated JavaMail API for SMTP-based notifications (Gmail).
*   **Database Persistence**: Uses JDBC for reliable data storage in a MySQL environment.
*   **Responsive UI**: Developed with Java Swing for a consistent desktop experience.

---

## 🛠️ Technology Stack

*   **Logic & UI**: Java (SE), Swing, AWT
*   **Database**: MySQL
*   **Communication**: JavaMail API (`javax.mail`)
*   **Connectivity**: JDBC (MySQL Connector/J)

---

## 📋 Prerequisites

Before running the application, ensure you have the following installed:
1.  **Java Development Kit (JDK)**: Version 8 or higher.
2.  **MySQL Server**: Running on `localhost:3306`.
3.  **Libraries**: The following `.jar` files must be in your `lib/lib/` directory:
    *   `mysql-connector-j-9.6.0.jar`
    *   `javax.mail-1.6.2.jar`
    *   `activation-1.1.1.jar`

---

## ⚙️ Setup Instructions

### 1. Database Configuration
Run the provided SQL scripts to set up your database environment:
```sql
-- Execute this in your MySQL terminal or Workbench
source setup.sql;
```

### 2. Email Configuration
Update the sender credentials in `service/EmailService.java` to use your own Gmail account and App Password:
```java
private static final String SENDER_EMAIL = "your-email@gmail.com";
private static final String SENDER_APP_PASSWORD = "your-app-password";
```

### 3. Connection Settings
Ensure `util/DBConnection.java` reflects your MySQL credentials:
```java
public static final String URL = "jdbc:mysql://localhost:3306/university";
public static final String USER = "root";
public static final String PASSWORD = "your_mysql_password";
```

---

## 🏃 How to Run

The easiest way to launch the application on Windows is by using the provided batch file:

1.  Open your terminal in the project directory.
2.  Run the command:
    ```bash
    run.bat
    ```
    *This script will automatically compile all Java files and launch the `LoginUI`.*

---

## 📁 Project Structure

*   `model/`: Data objects (Student, Course).
*   `service/`: Business logic (Course management, Email service).
*   `ui/`: Graphical User Interface components.
*   `util/`: Database connection and utility classes.
*   `lib/`: External dependencies.
*   `setup.sql`: Database schema initialization.
