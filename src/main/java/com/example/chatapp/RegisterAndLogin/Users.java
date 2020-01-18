package com.example.chatapp.RegisterAndLogin;

public class Users {

    private String name;
    private String image;
    private  String status;
    private String thumb_nail;
    private String online;
    public String user_id;
    private String friend_list_visibility;
    private String online_visisbility;
    private String email_id;
    private String post_count;
    private String last_synced_location;
    private String background_image;
   // public  int friend_count;


    public Users(String name, String image, String status, String thumb_nail, String online, String user_id, String friend_list_visibility, String online_visisbility, String email_id, String post_count, String last_synced_location, String background_image) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_nail = thumb_nail;
        this.online = online;
        this.user_id = user_id;
        this.friend_list_visibility = friend_list_visibility;
        this.online_visisbility = online_visisbility;
        this.email_id = email_id;
        this.post_count = post_count;
        this.last_synced_location = last_synced_location;
        this.background_image = background_image;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public Users(String name, String image, String status, String thumb_nail, String online, String user_id, String friend_list_visibility, String online_visisbility, String email_id, String post_counts, String last_synced_location) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_nail = thumb_nail;
        this.online = online;
        this.user_id = user_id;
        this.friend_list_visibility = friend_list_visibility;
        this.online_visisbility = online_visisbility;
        this.email_id = email_id;
        this.post_count = post_counts;
        this.last_synced_location = last_synced_location;
    }


    public String getLast_synced_location() {
        return last_synced_location;
    }

    public void setLast_synced_location(String last_synced_location) {
        this.last_synced_location = last_synced_location;
    }

    public Users(String name, String image, String status, String thumb_nail, String online, String user_id, String friend_list_visibility, String online_visisbility, String email_id) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_nail = thumb_nail;
        this.online = online;
        this.user_id = user_id;
        this.friend_list_visibility = friend_list_visibility;
        this.online_visisbility = online_visisbility;
        this.email_id = email_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

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
