package com.example.chatapp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.RegisterAndLogin.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class VideoCall extends AppCompatActivity {

    private ImageView profile,accept,reject;
    private String user2,user1,type;
    private TextView callerName,calling;
    private DatabaseReference mRootRef;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        profile = findViewById(R.id.pro_pic);
        accept = findViewById(R.id.accept_call);
        reject = findViewById(R.id.reject_call);
        callerName = findViewById(R.id.callerName);
        calling = findViewById(R.id.calling);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        user2 = getIntent().getStringExtra("user_id2");
        type = getIntent().getStringExtra("type");
        user1 = FirebaseAuth.getInstance().getUid();
        calling.setVisibility(View.GONE);
        reject.setVisibility(View.GONE);
        mediaPlayer = MediaPlayer.create(this,R.raw.ring);
        if(type.equals("coming")){
            reject.setVisibility(View.VISIBLE);
            accept.setVisibility(View.VISIBLE);
        }else{
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.GONE);
        }

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("coming")){
                    mRootRef.child("currentVideoCall").child(user1).child("status").setValue("rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mRootRef.child("currentVideoCall").child(user1).removeValue();
                            finish();
                        }
                    });
                }else{
                    mRootRef.child("currentVideoCall").child(user2).removeValue();
                    mediaPlayer.stop();
                }


            }
        });

        mRootRef.child("currentVideoCall").child(user2).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue(String.class).equals("connected")){
                        Intent intent = new Intent(VideoCall.this,VideoCalling.class);
                        intent.putExtra("user_id2",user2);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        mRootRef.child("videoCallHistory").child(user2).setValue("connected");




        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!type.equals("coming")){
                    accept.setVisibility(View.VISIBLE);

                    final HashMap<String,Object> map = new HashMap<>();
                    map.put("callerId",user1);
                    map.put("receiverId",user2);
                    map.put("startTime", ServerValue.TIMESTAMP);
                    map.put("status","calling");
                    mRootRef.child("currentVideoCall").child(user2).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!type.equals("coming")){
                            if(!dataSnapshot.exists()){
                                accept.setVisibility(View.GONE);
                                reject.setVisibility(View.VISIBLE);
                                calling.setVisibility(View.VISIBLE);
                                mRootRef.child("currentVideoCall").child(user2).setValue(map);
                                calling.setText("calling...");
                                mediaPlayer.start();


                            }else{
                                reject.setVisibility(View.GONE);
                                accept.setVisibility(View.VISIBLE);
                                calling.setVisibility(View.VISIBLE);
                                calling.setText("Phone engaged...");
                                Toast.makeText(VideoCall.this, "Busy ... On another call..", Toast.LENGTH_SHORT).show();
                            }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    mRootRef.child("currentVideoCall").child(user1).child("status").setValue("connected");
                    Intent intent = new Intent(VideoCall.this,VideoCalling.class);
                    intent.putExtra("user_id2",user2);
                    intent.putExtra("type",type);
                    startActivity(intent);

                }

            }
        });



//        mRootRef.child()



        mRootRef.child("Users").child(user2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Users users = dataSnapshot.getValue(Users.class);
                    Picasso.with(VideoCall.this)
                            .load(users.getImage())
                            .placeholder(R.drawable.avtar)
                            .into(profile);
                    callerName.setText(users.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mRootRef.child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    Users users = dataSnapshot.getValue(Users.class);
//                    Picasso.with(VideoCall.this)
//                            .load(users.getImage())
//                            .placeholder(R.drawable.avtar)
//                            .into(profile);
//                    callerName.setText(users.getName());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });






    }
}
