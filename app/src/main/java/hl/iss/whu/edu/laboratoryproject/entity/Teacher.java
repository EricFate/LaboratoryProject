package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/12/2.
 */
public class Teacher {
    private int id;


    private String realname;
    private String imageURL;

    public Teacher(String name, String imageURL) {
        this.realname = name;
        this.imageURL = imageURL;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
