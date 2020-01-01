package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.chatapp.Fragments.FriendListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPostsLikesActivity extends AppCompatActivity {
    private static final String TAG = "ViewPostsLikesActivity";

    private DatabaseReference mRootRef,mPostRef,mPostLikesRef;
   private  RecyclerView LikeList;
    private String postId;
    List<String> likersList;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts_likes);
        postId = getIntent().getStringExtra("post_id");
        LikeList =findViewById(R.id.likes_list);
        mRootRef= FirebaseDatabase.getInstance().getReference();
        likersList = new ArrayList<>();
        mPostRef= mRootRef.child("posts");
        mPostLikesRef= mRootRef.child("post_likes");
        mPostLikesRef.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(likersList.size()!=0) likersList.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        likersList.add(dataSnapshot1.getKey());
                    }
                    Log.i(TAG, "onDataChange: "+ likersList.toString());

                    //Recycler view initialisation done here....
                    LikeList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    LikeList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mAdapter = new LikesAndSharesAdapter(likersList,getApplicationContext());
                    LikeList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
