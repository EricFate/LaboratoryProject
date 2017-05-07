package hl.iss.whu.edu.laboratoryproject.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fate on 2016/11/27.
 */

public class Issue implements Serializable{
    private int id;
    private String title;
    private String content;

    private int answerNumber;
    private boolean anonymous;
    private Student user;
    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Student getUser() {
        return user;
    }

    public void setUser(Student student) {
        this.user = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
