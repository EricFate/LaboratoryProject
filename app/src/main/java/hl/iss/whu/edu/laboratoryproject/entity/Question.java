package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/12/8.
 */

public class Question
{
    private int id;
    private String content;
    private String answer;
    private String answerer;


    public Question(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public Question(int id, String content, String answer, String answerer) {
        this.id = id;
        this.content = content;
        this.answer = answer;
        this.answerer = answerer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
