🎓 Student Course Registration System

📌 Overview

The Student Course Registration System is a Java-based application that allows students to register for courses, view available courses, and manage enrollments. It also provides administrative functionalities for managing courses and student data.

This project uses:

Java (Core + Swing for UI)

MySQL Database

JDBC for database connectivity

🚀 Features

👨‍🎓 Student Module
Student login
View available courses
Register for courses
View enrolled courses
🛠️ Admin Module
Add / remove courses
Manage student data
View registrations
📧 Additional Features
Email notifications (via Java Mail API)
Database connectivity using JDBC
🗂️ Project Structure
StudentCourseRegistration-kiran/
│
├── model/              # Data models (Student, Course)
├── service/            # Business logic (CourseService, EmailService)
├── ui/                 # User Interfaces (LoginUI, AdminUI, StudentUI)
├── util/               # Database connection utility
├── lib/                # External libraries (MySQL connector, Mail API)
├── bin/                # Compiled class files
│
├── setup.sql           # Database setup script
├── migrate.sql         # Migration script

⚙️ Technologies Used
Java (JDK 8 or above)
Swing (GUI)
MySQL
JDBC
JavaMail API
🧰 Setup Instructions
1️⃣ Clone or Extract Project
unzip StudentCourseRegistration.zip
cd StudentCourseRegistration-kiran
2️⃣ Setup Database
Open MySQL
Run the SQL scripts:
source setup.sql;
source migrate.sql;
3️⃣ Configure Database Connection

Edit the file:

util/DBConnection.java

Update:

String url = "jdbc:mysql://localhost:3306/your_database";
String user = "root";
String password = "your_password";
4️⃣ Add Libraries

Ensure these JAR files are included:

mysql-connector-j
javax.mail
activation

(Already present in /lib folder)

5️⃣ Compile & Run
Option 1: Using Command Line
javac -cp ".;lib/*" ui/LoginUI.java
java -cp ".;lib/*" ui.LoginUI
Option 2: Use run.bat

Simply double-click:

run.bat
🧪 Testing

Test files included:

TestStudent.java
TestDB.java
TestPrintStudents.java

Run them individually to verify functionality.

🔐 Default Credentials (if applicable)

(Update this if you implemented login credentials)

Admin:
Username: admin
Password: admin123
📈 Future Enhancements
Web-based version (Spring Boot / React)
Mobile app integration
Advanced analytics dashboard
Role-based authentication (JWT)
Cloud deployment
🤝 Contribution

Feel free to fork and improve this project:

Add new features
Improve UI/UX
Optimize database queries
📄 License

This project is for educational purposes.
