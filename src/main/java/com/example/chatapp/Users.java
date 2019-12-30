package com.example.chatapp;

public class Users {

    public String name;
    public String image;
    public  String status;
    public String thumb_nail;
    public  String online;
    public String user_id;
    public String friend_list_visibility;
    public String online_visisbility;
   // public  int friend_count;

    public Users(String name, String image, String status, String thumb_nail, String online, String user_id, String friend_list_visibility, String online_visisbility) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_nail = thumb_nail;
        this.online = online;
        this.user_id = user_id;
        this.friend_list_visibility = friend_list_visibility;
        this.online_visisbility = online_visisbility;
    }

    public String getFriend_list_visibility() {
        return friend_list_visibility;
    }

    public void setFriend_list_visibility(String friend_list_visibility) {
        this.friend_list_visibility = friend_list_visibility;
    }

    public String getOnline_visisbility() {
        return online_visisbility;
    }

    public void setOnline_visisbility(String online_visisbility) {
        this.online_visisbility = online_visisbility;
    }

    public Users(String name, String image, String status, String thunb_nail, String online, String user_id) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_nail = thunb_nail;
        this.online = online;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Users(String name, String image, String status, String thumb_nail, String online) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_nail = thumb_nail;
        this.online = online;
    }

    public String getThumb_nail() {
        return thumb_nail;
    }

    public void setThumb_nail(String thumb_nail) {
        this.thumb_nail = thumb_nail;
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
