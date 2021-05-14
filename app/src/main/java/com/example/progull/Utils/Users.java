package com.example.progull.Utils;

public class Users {

   private String username,profileImg,clas,phonenum;

    public Users() {
    }

    public Users(String clas, String profileImg, String username, String phonenum) {
        this.clas = clas;
        this.profileImg = profileImg;
        this.username = username;
        this.phonenum = phonenum;


    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
