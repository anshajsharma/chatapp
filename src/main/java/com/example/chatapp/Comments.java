package com.example.chatapp;

public class Comments {
    String post_id,user_id,pro_image,comment_body,time_of_comment,likes_count,user_name;

    public Comments() {
    }

    public Comments(String post_id, String user_id, String pro_image, String comment_body, String time_of_comment, String likes_count, String user_name) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.pro_image = pro_image;
        this.comment_body = comment_body;
        this.time_of_comment = time_of_comment;
        this.likes_count = likes_count;
        this.user_name = user_name;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPro_image() {
        return pro_image;
    }

    public void setPro_image(String pro_image) {
        this.pro_image = pro_image;
    }

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    public String getTime_of_comment() {
        return time_of_comment;
    }

    public void setTime_of_comment(String time_of_comment) {
        this.time_of_comment = time_of_comment;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
