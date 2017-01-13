package hl.iss.whu.edu.laboratoryproject.entity;

import org.jxmpp.jid.Jid;

/**
 * Created by fate on 2016/12/25.
 */

public class Request {
    public static final int TYPE_SUBSCRIBE = 0;
    public static final int TYPE_SUBSCRIBED = 1;
    public static final int TYPE_UNSUBSCRIBE = 2;

    private Jid from;
    private int type ;

    public Request(Jid from, int type) {
        this.from = from;
        this.type = type;
    }

    public Request() {
    }

    public Jid getFrom() {
        return from;
    }

    public void setFrom(Jid from) {
        this.from = from;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
