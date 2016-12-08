package hl.iss.whu.edu.laboratoryproject.entity;

import java.io.Serializable;

/**
 * Created by fate on 2016/11/18.
 */

public class Chatter implements Serializable {
    private String message;
    private String time;
    private String imageURL;
    private String name;
    private int state;
    private String signiture;
    private String jid;

    public Chatter(String name, String message, String imageURL, String time) {
        this.name = name;
        this.message = message;
        this.imageURL = imageURL;
        this.time = time;
    }

    public Chatter(int state, String name, String imageURL, String signiture, String jid) {
        this.imageURL = imageURL;
        this.name = name;
        this.state = state;
        this.signiture = signiture;
        this.jid = jid;
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

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }
}
