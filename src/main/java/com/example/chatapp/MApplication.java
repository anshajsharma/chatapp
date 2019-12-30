package com.example.chatapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();

        Long tsLong = System.currentTimeMillis();
        if(curr_user!=null)
        {
            DatabaseReference userLastOnlineRef = FirebaseDatabase.getInstance().getReference("Users/"+curr_user.getUid()+"/online");
            userLastOnlineRef.onDisconnect().setValue(tsLong.toString());

        }




    }
}