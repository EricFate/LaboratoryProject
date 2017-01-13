package hl.iss.whu.edu.laboratoryproject.entity;

import org.jxmpp.jid.BareJid;

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
    private BareJid jid;

    public Chatter(String name, String message, byte[] image, String time,BareJid jid) {
        this.name = name;
        this.message = message;
        this.image = image;
        this.time = time;
        this.jid = jid;
    }

    public Chatter(int state, String name, byte[] image, String signiture, BareJid jid) {
        this.image = image;
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

    public BareJid getJid() {
        return jid;
    }

    public void setJid(BareJid jid) {
        this.jid = jid;
    }
}
