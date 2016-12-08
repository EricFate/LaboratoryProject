package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/12/2.
 */

public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String number;
    private String realname;
    private String classname;
    private String region;
    private String school;
    private String phone;
    private String signiture;
    private String imageURL;



    public User(String username, String password, String nickname, String signiture) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.signiture = signiture;
    }

    public User(String imageURL, String username, String password, String nickname, String email, String number, String realname, String classname, String region, String school, String phone, String signiture) {
        this.imageURL = imageURL;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.number = number;
        this.realname = realname;
        this.classname = classname;
        this.region = region;
        this.school = school;
        this.phone = phone;
        this.signiture = signiture;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSigniture() {
        return signiture;
    }

    public void setSigniture(String signiture) {
        this.signiture = signiture;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
