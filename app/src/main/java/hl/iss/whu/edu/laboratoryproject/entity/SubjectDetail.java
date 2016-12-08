package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.ArrayList;

/**
 * Created by fate on 2016/12/2.
 */

public class SubjectDetail {
    private String name;
    private String description;
    private Teacher teacher;
    private ArrayList<Rank> ranks;
    private double avgRank;

    public SubjectDetail(String name, String description, Teacher teacher, ArrayList<Rank> ranks, double avgRank) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.ranks = ranks;
        this.avgRank = avgRank;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ArrayList<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(ArrayList<Rank> ranks) {
        this.ranks = ranks;
    }

    public double getAvgRank() {
        return avgRank;
    }

    public void setAvgRank(double avgRank) {
        this.avgRank = avgRank;
    }
}
