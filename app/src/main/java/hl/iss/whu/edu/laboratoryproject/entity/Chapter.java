package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.ArrayList;

/**
 * Created by fate on 2016/11/26.
 */

public class Chapter {
    private String title;
    private ArrayList<Lesson> lessons;

    public Chapter(String title, ArrayList<Lesson> lessons) {
        this.title = title;
        this.lessons = lessons;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public static class Lesson{
        private String title;
        private String URL;

        public Lesson(String title, String URL) {
            this.title = title;
            this.URL = URL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
