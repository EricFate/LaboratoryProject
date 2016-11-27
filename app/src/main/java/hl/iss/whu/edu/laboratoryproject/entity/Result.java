package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/11/17.
 */

public class Result {
    private int code;
    private String message;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
