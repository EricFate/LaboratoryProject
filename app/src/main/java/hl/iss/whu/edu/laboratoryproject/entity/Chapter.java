package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.ArrayList;

/**
 * Created by fate on 2016/11/26.
 */

public class Chapter {
   private int id;
   private String chapterName;
   private String lessonNumber;
   private String finishLessonNumber;
   private String knowledgePoint;
   private String description;
    private ArrayList<Lesson> lessons;

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getFinishLessonNumber() {
        return finishLessonNumber;
    }

    public void setFinishLessonNumber(String finishLessonNumber) {
        this.finishLessonNumber = finishLessonNumber;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
