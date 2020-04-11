package com.example.chatapp.GoSocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
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
    ImageView back;
    private RecyclerView.Adapter mAdapter;
    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts_likes);
        postId = getIntent().getStringExtra("post_id");
        LikeList =findViewById(R.id.likes_list);
        back=findViewById(R.id.back_button);
        mRootRef= FirebaseDatabase.getInstance().getReference();
        likersList = new ArrayList<>();
        Title = findViewById(R.id.poster_detail);

        mPostRef= mRootRef.child("posts");
        mPostLikesRef= mRootRef.child("post_likes");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRootRef.child("posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("poster_name").getValue(String.class);
                title = title + "'s Post Likes";
                Title.setText(title);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    if(likersList.size()==0){
                        Toast.makeText(ViewPostsLikesActivity.this, "Nothing to show!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

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
