package com.example.progull.Utils;

public class Friends {
    private String profileImageUrl,username,status;

    public Friends(String profileImageUrl, String username, String status) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.status = status;
    }

    public Friends() {
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
