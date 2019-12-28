package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String user2;
    private DatabaseReference mRootRef;
    CircleImageView circleImageView;
    Toolbar toolbar;
    TextView mLastSeenView,user2name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user2 = getIntent().getStringExtra("user_id2");
        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
        mLastSeenView = findViewById(R.id.last_seen);
        user2name = findViewById(R.id.user2_name);
        circleImageView = findViewById(R.id.user2_profile_pic);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //toolbar initialisation----------------------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //--------------------------------------------------------------------

        mRootRef.child("Users").child(user2).keepSynced(true);
        mRootRef.child("Users").child(user2).child("online").keepSynced(false);
        mRootRef.child("Users").child(user2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String online = dataSnapshot.child("online").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();

                user2name.setText(name);

                Picasso.with(getApplicationContext())
                        .load(image)
                        .placeholder(R.drawable.avtar)
                        .into(circleImageView);

                if(online.equals("true")) {

                    mLastSeenView.setText("Online");

                } else {

//                    GetTimeAgo getTimeAgo = new GetTimeAgo();
//
//                    long lastTime = Long.parseLong(online);
//
//                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
//
//                    mLastSeenView.setText(lastSeenTime);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View app_bar = inflater.inflate(R.layout.app_bar_layout,null);


        GetTimeAgo getTimeAgo = new GetTimeAgo();
        String online="12";
        long lastTime = Long.parseLong(online);
        String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());

       // actionBar.setCustomView(app_bar);





    }

   //----------------------------------------------------Menu Creation START--------------------------------------------
        @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
         super.onCreateOptionsMenu(menu1);

        getMenuInflater().inflate(R.menu.chat_menu, menu1);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId() == R.id.view_profile){
             Toast.makeText(this, "Wohoo", Toast.LENGTH_SHORT).show();
         }
         if(item.getItemId() == R.id.clear_chat)
         {
             Toast.makeText(this, "Wohoo", Toast.LENGTH_SHORT).show();

         }

        return true;
    }
  //-----------------------------------------MENU END-------------------------------------------------------------------
}
