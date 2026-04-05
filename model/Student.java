package model;

public class Student {
    private String rollNo;
    private String name;
    private String email;
    private String password;
    private String department;
    private int semester;
    private String minorStream;

    public Student(String rollNo, String name, String email, String password, String department, int semester, String minorStream) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.semester = semester;
        this.minorStream = minorStream;
    }

    // Getters and Setters
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getMinorStream() { return minorStream; }
    public void setMinorStream(String minorStream) { this.minorStream = minorStream; }
}
