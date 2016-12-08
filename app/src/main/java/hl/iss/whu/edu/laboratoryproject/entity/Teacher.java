package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/12/2.
 */
public class Teacher {
    private  String name;
    private String imageURL;

    public Teacher(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
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
