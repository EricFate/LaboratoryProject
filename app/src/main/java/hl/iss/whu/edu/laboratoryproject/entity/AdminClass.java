package hl.iss.whu.edu.laboratoryproject.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mrwen on 2017/2/23.
 */

public class AdminClass {
    private int id;
    private int sNumber;
    private int csNumber;
    private String region;
    private String school;
    private String grade;
    private String imageURL;
    private ArrayList<Student> students;
    private ArrayList<CollegeStudent> collegeStudents;
    private ArrayList<Course> course;
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getCsNumber() {
        return csNumber;
    }

    public void setCsNumber(int csNumber) {
        this.csNumber = csNumber;
    }

    public ArrayList<Course> getCourse() {
        return course;
    }

    public void setCourse(ArrayList<Course> course) {
        this.course = course;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<CollegeStudent> getCollegeStudents() {
        return collegeStudents;
    }

    public void setCollegeStudents(ArrayList<CollegeStudent> collegeStudents) {
        this.collegeStudents = collegeStudents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getsNumber() {
        return sNumber;
    }

    public void setsNumber(int sNumber) {
        this.sNumber = sNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
