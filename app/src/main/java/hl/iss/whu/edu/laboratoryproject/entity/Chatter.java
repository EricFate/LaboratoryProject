package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/11/18.
 */

public class Chatter {
    private String name;
    private String message;
    private String imageURL;


    public Chatter(String name, String message, String imageURL) {
        this.name = name;
        this.message = message;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
