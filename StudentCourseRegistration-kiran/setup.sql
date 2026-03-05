CREATE DATABASE IF NOT EXISTS university;
USE university;

CREATE TABLE IF NOT EXISTS courses (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    credits INT,
    department VARCHAR(50),
    semester INT
);

CREATE TABLE IF NOT EXISTS registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50),
    student_name VARCHAR(255),
    student_dept VARCHAR(50),
    student_sem VARCHAR(20),
    course_id INT,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
