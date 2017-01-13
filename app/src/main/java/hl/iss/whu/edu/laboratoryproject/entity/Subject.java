package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/10/29.
 */

public class Subject {
    private String name;
    private String number;
    private String duration;
    private String imgURL;
    private String teacher;

    public Subject(String name, String number, String duration, String imgURL) {
        this.name = name;
        this.number = number;
        this.duration = duration;
        this.imgURL = imgURL;
    }

    public Subject(String name, String number, String duration, String imgURL, String teacher) {
        this.name = name;
        this.number = number;
        this.duration = duration;
        this.imgURL = imgURL;
        this.teacher = teacher;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
