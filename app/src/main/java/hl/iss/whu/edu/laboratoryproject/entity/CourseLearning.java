package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2017/2/26.
 */

public class CourseLearning {
    private Course course;
    private long duration;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
