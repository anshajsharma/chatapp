package com.example.chatapp.User2RelatedActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Show_Profile_PictureActivity extends AppCompatActivity {

    String user2;
    TextView tv;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__profile__picture);
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        user2= getIntent().getStringExtra("user_id2");
        final ImageView imageView = findViewById(R.id.profile_picture);
        final ImageView back = findViewById(R.id.back);
        tv =findViewById(R.id.name);


        mRef.child(user2).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s;
                if(dataSnapshot.exists()){
                    s=dataSnapshot.getValue().toString();
                    Picasso.with(Show_Profile_PictureActivity.this)
                            .load(s)
                            .placeholder(R.drawable.avtar)
                            .fit()
                            .into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child(user2).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s;
                if(dataSnapshot.exists()){
                    s=dataSnapshot.getValue().toString();
                    tv.setText(s);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
