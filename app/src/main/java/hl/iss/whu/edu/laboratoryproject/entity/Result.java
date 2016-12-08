package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/11/17.
 */

public class Result {
    private int code;
    private String message;
    private String username;
    private String password;
    private String nickname;
    private String imageURL;
    private String signiture;


    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, String username, String password, String nickname, String imageURL, String signiture) {
        this.code = code;
        this.message = message;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.imageURL = imageURL;
        this.signiture = signiture;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSigniture() {
        return signiture;
    }

    public void setSigniture(String signiture) {
        this.signiture = signiture;
    }
}
