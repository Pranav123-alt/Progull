package com.example.progull.Utils;

public class Posts {


    private String datePost,postDesc,postImageUri,userProfileImageUri,username;


    public Posts(){

    }

     public Posts(String datePost,String postDesc,String postImageUri,String userProfileImageUri,String username) {
         this.datePost = datePost;
         this.postDesc = postDesc;
         this.postImageUri = postImageUri;
         this.userProfileImageUri = userProfileImageUri;
         this.username = username;
     }


    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImageUri() {
        return postImageUri;
    }

    public void setPostImageUri(String postImageUri) {
        this.postImageUri = postImageUri;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getUserProfileImageUri() {
        return userProfileImageUri;
    }

    public void setUserProfileImageUri(String userProfileImageUri) {
        this.userProfileImageUri = userProfileImageUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
