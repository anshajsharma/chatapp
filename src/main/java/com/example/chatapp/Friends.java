package com.example.chatapp;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Friends {
    String user2id;

    public Friends() {
    }

    public Friends(String user2id) {
        this.user2id = user2id;
    }

    public String getUser2id() {
        return user2id;
    }

    public void setUser2id(String user2id) {
        this.user2id = user2id;
    }
}
