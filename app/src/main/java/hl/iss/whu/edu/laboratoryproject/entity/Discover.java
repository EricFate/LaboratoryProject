package hl.iss.whu.edu.laboratoryproject.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fate on 2016/11/27.
 */

public class Discover implements Serializable{
    private String title;
    private String detail;
    private ArrayList<Answer> answers;

    public Discover(String title, String detail, ArrayList<Answer> answers) {
        this.title = title;
        this.detail = detail;
        this.answers = answers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
