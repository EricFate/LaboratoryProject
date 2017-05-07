package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2017/2/25.
 */

public class MessageRecord {
    private String fromUid;
    private String toUid;
    private int number;

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public MessageRecord(String fromUid, String toUid) {
        this.fromUid = fromUid;
        this.toUid = toUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public void increment(){
        number++;
    }

}
