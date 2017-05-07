package hl.iss.whu.edu.laboratoryproject.entity;


import java.io.Serializable;

/**
 * Created by fate on 2016/11/18.
 */

public class Chatter implements Serializable {
    private String message;
    private String time;
    private byte[] image;
    private String name;
    private int state;
    private String signiture;


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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSigniture() {
        return signiture;
    }

    public void setSigniture(String signiture) {
        this.signiture = signiture;
    }

}
