package com.example.chatapp.Fragments;

public class ChatFragmentViewData {
    String timestamp , last_message ;
    Long unread_message_count;

    public ChatFragmentViewData() {
    }

    public ChatFragmentViewData(String timestamp, String last_message, Long unread_message_count) {
        this.timestamp = timestamp;
        this.last_message = last_message;
        this.unread_message_count = unread_message_count;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public Long getUnread_message_count() {
        return unread_message_count;
    }

    public void setUnread_message_count(Long unread_message_count) {
        this.unread_message_count = unread_message_count;
    }
}
