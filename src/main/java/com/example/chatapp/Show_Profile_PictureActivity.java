package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Show_Profile_PictureActivity extends AppCompatActivity {

    String user2;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__profile__picture);
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        user2= getIntent().getStringExtra("user_id2");
        final ImageView imageView = findViewById(R.id.profile_picture);

        mRef.child(user2).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s;
                if(dataSnapshot.exists()){
                    s=dataSnapshot.getValue().toString();
                    Picasso.with(Show_Profile_PictureActivity.this)
                            .load(s)
                            .placeholder(R.drawable.avtar)
                            .into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
