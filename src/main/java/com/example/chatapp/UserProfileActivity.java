package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private DatabaseReference mDatabaseRef;
    private TextView display_name,friends,status;
    private ImageView profile_pic;
    private Button friendRequest;
    private String curr_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        curr_user_id = getIntent().getStringExtra("user_id");

        display_name = findViewById(R.id.profile_display_name);
        friends = findViewById(R.id.friends_count);
        status = findViewById(R.id.currr_status);
        friendRequest = findViewById(R.id.send_friend_request);
        profile_pic=findViewById(R.id.pro_pic);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(curr_user_id);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name,image,user_status;
                name = dataSnapshot.child("name").getValue().toString();
                image = dataSnapshot.child("image").getValue().toString();
                user_status = dataSnapshot.child("status").getValue().toString();
                Log.i(TAG, "onClick22 " + curr_user_id + image);

                display_name.setText(name);
                status.setText(user_status);
                Picasso.with(UserProfileActivity.this)
                        .load(image)
                        .placeholder(R.drawable.avtar)
                        .fit()
                        .into(profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
