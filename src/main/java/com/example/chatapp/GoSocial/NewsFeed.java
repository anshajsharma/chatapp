package com.example.chatapp.GoSocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.R;
import com.example.chatapp.UsersHomePage;
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


        // Log.i(TAG, "onCreate: "+User1);
        mRooRef.child("Friends").child(User1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot3) {
                if (dataSnapshot3.exists()) {


                    mRooRef.child("posts").orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            if(dataSnapshot.exists())
                            {
                                Posts tPost = dataSnapshot.getValue(Posts.class);
                                assert tPost != null;
                                {
                                    LinearLayoutManager mLayoutManager;
                                    mLayoutManager = new LinearLayoutManager(NewsFeed.this);
                                    mLayoutManager.setReverseLayout(true);
                                    mLayoutManager.setStackFromEnd(true);

                                    // And set it to RecyclerView
                                    postsList = findViewById(R.id.postList);
                                    postsList.setLayoutManager(mLayoutManager);
                                    mAdapter = new PostAdapter(reLatedPosts, ctx);
                                    postsList.setAdapter(mAdapter);
                                    //  mAdapter.notifyDataSetChanged();
                                    String poster_id = tPost.getUser_id();
                                    //    Log.i(TAG, "onDataChange: " + "12453" + " " + poster_id + " " + dataSnapshot.getValue());
                                    if (poster_id != null) {
                                        if (dataSnapshot3.hasChild(poster_id)) reLatedPosts.add(tPost);
                                        else if (poster_id.equals(User1)) reLatedPosts.add(tPost);
                                        mAdapter.notifyItemInserted(0);
                                    }

                                }


                            }


                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                            Posts tPost = dataSnapshot.getValue(Posts.class);
                            assert tPost != null;
                            {
                                String poster_id = tPost.getUser_id();
                       //         Log.i(TAG, "onDataChange: " + "123" + " " + poster_id + " " + tPost.toString());
                                assert poster_id != null;
                                if (reLatedPosts.contains(tPost)) {
                                int a = reLatedPosts.lastIndexOf(tPost);
                                reLatedPosts.remove(tPost);
                                mAdapter.notifyItemRemoved(a);}
                            }
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            mAdapter.notifyDataSetChanged();
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
//                Log.i("asdfg1","i am here");
//                Log.i("asdfg1",resultUri.toString());
//
                Intent intent = new Intent(NewsFeed.this, NewPost.class);
                intent.putExtra("image", resultUri.toString());
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, UsersHomePage.class));
        finish();
    }


}
