package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NewsFeed extends AppCompatActivity {
    private static final String TAG = "NewsFeed";

    private FloatingActionButton floatingActionButton;
    List<Posts> reLatedPosts;
    DatabaseReference mRooRef;
    RecyclerView postsList;
    RecyclerView.Adapter mAdapter;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        floatingActionButton = findViewById(R.id.add_post);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crop image activity api uses....
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(NewsFeed.this);
            }
        });
        mRooRef = FirebaseDatabase.getInstance().getReference();
        reLatedPosts = new ArrayList<>();
        ctx=getApplicationContext();

        final String User1 = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Log.i(TAG, "onCreate: "+User1);


        mRooRef.child("Friends").child(User1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot3) {


                if (dataSnapshot3.exists()) {
                    Log.i(TAG, "onCreate: "+dataSnapshot3.getValue());

                mRooRef.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (reLatedPosts.size() != 0) reLatedPosts.clear();
                            Log.i(TAG, "onCreate: "+dataSnapshot.getValue());
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Posts tPost = dataSnapshot1.getValue(Posts.class);
                                    assert tPost != null;
                                    { String poster_id = tPost.getUser_id();
                                        assert poster_id != null;
                                     if (dataSnapshot3.hasChild(poster_id)) reLatedPosts.add(tPost);
                                    else if (poster_id.equals(User1)) reLatedPosts.add(tPost);}
                                }

                                if (reLatedPosts.size() > 0) {
                                    //  Log.i(TAG, "onDataChange: "+ reLatedPosts.toString());
                                    postsList = findViewById(R.id.postList);
                                    postsList.setLayoutManager(new LinearLayoutManager(ctx));
                                    mAdapter = new PostAdapter(reLatedPosts, ctx);
                                    postsList.setAdapter(mAdapter);
                                    //  mAdapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(NewsFeed.this, "Nothing to show!!", Toast.LENGTH_SHORT).show();
                                }


                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
//                Log.i("asdfg1","i am here");
//                Log.i("asdfg1",resultUri.toString());
//
                Intent intent = new Intent(NewsFeed.this,NewPost.class);
                intent.putExtra("image",resultUri.toString());
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
