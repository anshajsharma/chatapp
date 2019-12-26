package com.example.chatapp;

public class Users {

    public String name;
    public String image;
    public  String status;
    public String thumb_image;
    public  String online;
    public String user_id;
   // public  int friend_count;

    public Users(String name, String image, String status, String thumb_image, String online, String user_id) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.online = online;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Users(String name, String image, String status, String thumb_image, String online) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.online = online;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public Users() {
    }

    public Users(String name, String image, String status) {
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
