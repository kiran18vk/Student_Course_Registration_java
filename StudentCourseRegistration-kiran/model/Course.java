package model;

import java.io.Serializable;

public class Course implements Serializable {

    public int id;
    public String name;
    public int credits;
    public String department;
    public int semester;

    // Main constructor
    public Course(int id, String name, int credits, String department, int semester) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.department = department;
        this.semester = semester;
    }

    // Overloaded constructor (default credits = 3)
    public Course(int id, String name, int credits) {
        this(id, name, credits, "General", 1);
    }

    public int getId() {
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

    @Override
    public String toString() {
        return id + " - " + name + " (" + department + ", Sem: " + semester + ", " + credits + " credits)";
    }
}