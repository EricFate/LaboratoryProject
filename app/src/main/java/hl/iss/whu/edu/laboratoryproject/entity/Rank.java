package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.Date;

/**
 * Created by fate on 2016/12/2.
 */

public class Rank {
    private int id;
    private String content;
    private float rank;
    private Student ranker;
    private Date time;
    private Course course;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Rank(String content, float rank, Student ranker) {
        this.content = content;
        this.rank = rank;
        this.ranker = ranker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public Student getRanker() {
        return ranker;
    }

    public void setRanker(Student ranker) {
        this.ranker = ranker;
    }
}
