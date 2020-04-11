package com.example.chatapp.GoSocial;

public class Posts {

    String user_id;
    String poster_name;
    String posted_image;
    String thumb_nail;
    String description;
    String last_comment;
    String post_id;
    String user_profile_image;
    long likes_count,shares_count,comments_count,timestamp;

    public Posts(String user_id, String poster_name, String posted_image, String thumb_nail, long timestamp,
                 String description, long likes_count, long comments_count, long shares_count, String last_comment,
                 String post_id, String user_profile_image) {
        this.user_id = user_id;
        this.poster_name = poster_name;
        this.posted_image = posted_image;
        this.thumb_nail = thumb_nail;
        this.timestamp = timestamp;
        this.description = description;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.shares_count = shares_count;
        this.last_comment = last_comment;
        this.post_id = post_id;
        this.user_profile_image = user_profile_image;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }


    public Posts(String user_id, String poster_name, String posted_image, String thumb_nail, long timestamp,
                 String description, long likes_count,
                 long comments_count, long shares_count, String last_comment, String user_profile_image) {
        this.user_id = user_id;
        this.poster_name = poster_name;
        this.posted_image = posted_image;
        this.thumb_nail = thumb_nail;
        this.timestamp = timestamp;
        this.description = description;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.shares_count = shares_count;
        this.last_comment = last_comment;
        this.user_profile_image = user_profile_image;
    }



    public Posts(String user_id, String poster_name, String posted_image,
                 String thumb_nail, long timestamp, String description, long likes_count,
                 long comments_count, long shares_count, String last_comment) {
        this.user_id = user_id;
        this.poster_name = poster_name;
        this.posted_image = posted_image;
        this.thumb_nail = thumb_nail;
        this.timestamp = timestamp;
        this.description = description;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.shares_count = shares_count;
        this.last_comment = last_comment;
    }

    public Posts() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPoster_name() {
        return poster_name;
    }

    public void setPoster_name(String poster_name) {
        this.poster_name = poster_name;
    }

    public String getPosted_image() {
        return posted_image;
    }

    public void setPosted_image(String posted_image) {
        this.posted_image = posted_image;
    }

    public String getThumb_nail() {
        return thumb_nail;
    }

    public void setThumb_nail(String thumb_nail) {
        this.thumb_nail = thumb_nail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(long likes_count) {
        this.likes_count = likes_count;
    }

    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }

    public long getShares_count() {
        return shares_count;
    }

    public void setShares_count(long shares_count) {
        this.shares_count = shares_count;
    }

    public String getLast_comment() {
        return last_comment;
    }

    public void setLast_comment(String last_comment) {
        this.last_comment = last_comment;
    }
}
