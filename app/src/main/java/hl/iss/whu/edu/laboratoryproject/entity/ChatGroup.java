package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.List;

/**
 * Created by fate on 2017/3/5.
 */
public class ChatGroup {
    private int id;
    private String name;
    private String imageURL;
    private List<Notice> notices;

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
