package model;

import java.io.Serializable;

public class Course implements Serializable {

    public String id;
    public String name;
    public int credits;
    public String department;
    public int semester;
    public String minorStream;

    // Main constructor
    public Course(String id, String name, int credits, String department, int semester, String minorStream) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.department = department;
        this.semester = semester;
        this.minorStream = minorStream;
    }

    // Overloaded constructor (default credits = 3)
    public Course(String id, String name, int credits) {
        this(id, name, credits, "General", 1, "");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getDepartment() {
        return department;
    }

    public int getSemester() {
        return semester;
    }

    public String getMinorStream() {
        return minorStream;
    }

    @Override
    public String toString() {
        String msStr = (minorStream != null && !minorStream.trim().isEmpty()) ? ", Minor: " + minorStream : "";
        return id + " - " + name + " (" + department + ", Sem: " + semester + msStr + ", " + credits + " credits)";
    }
}