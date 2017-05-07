package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.ArrayList;

/**
 * Created by fate on 2016/12/10.
 */

public class Major {
    private String title;
    private ArrayList<Course> courses;

    public Major(String title, ArrayList<Course> courses) {
        this.title = title;
        this.courses = courses;
    }

    public Major() {
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
