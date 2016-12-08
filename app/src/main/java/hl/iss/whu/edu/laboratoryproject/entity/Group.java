package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.ArrayList;

/**
 * Created by fate on 2016/12/8.
 */

public class Group {
    private String name;
    private ArrayList<Chatter> chatters;

    public Group(String name, ArrayList<Chatter> chatters) {
        this.name = name;
        this.chatters = chatters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Chatter> getContacts() {
        return chatters;
    }

    public void setChatters(ArrayList<Chatter> chatters) {
        this.chatters = chatters;
    }
}
