package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.ArrayList;

/**
 * Created by fate on 2016/12/2.
 */

public class SubjectDetail {
    private String name;
    private String description;
    private Teacher teacher;
    private String knowledge;


    private ArrayList<Rank> ranks;
    private float avgRank;

    public SubjectDetail(String name, String description, Teacher teacher, ArrayList<Rank> ranks, float avgRank) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.ranks = ranks;
        this.avgRank = avgRank;
    }
    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
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

    public float getAvgRank() {
        return avgRank;
    }

    public void setAvgRank(float avgRank) {
        this.avgRank = avgRank;
    }
}
