package hl.iss.whu.edu.laboratoryproject.entity;

import java.util.ArrayList;

/**
 * Created by fate on 2016/12/8.
 */

public class Group {
    private String name;
    private ArrayList<Chatter> presence;
    private ArrayList<Chatter> absence;

    public Group(String name, ArrayList<Chatter> presence, ArrayList<Chatter> absence) {
        this.name = name;
        this.presence = presence;
        this.absence = absence;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Chatter> getPresence() {
        return presence;
    }

    public void setPresence(ArrayList<Chatter> presence) {
        this.presence = presence;
    }

    public ArrayList<Chatter> getAbsence() {
        return absence;
    }

    public void setAbsence(ArrayList<Chatter> absence) {
        this.absence = absence;
    }
}
