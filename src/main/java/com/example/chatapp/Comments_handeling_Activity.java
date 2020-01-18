package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.RegisterAndLogin.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comments_handeling_Activity extends AppCompatActivity {
    private static final String TAG = "Comments_handeling_Acti";

    private String post_id;
    private List<Comments>  commentsList;
    ImageView back;
    RecyclerView commentListrecyclerView;
    ImageButton sendComment;
    EditText commentBody;
    DatabaseReference mRootRef;
    RecyclerView.Adapter mAdapter;
    String curr_user;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_handeling_);

        // Hide keyeboard  when activity opens...............
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //------------------------------------------------------------

        commentsList = new ArrayList<>();
        post_id = getIntent().getStringExtra("post_id");
        back = findViewById(R.id.back_button);
        ctx = getApplicationContext();
        commentListrecyclerView = findViewById(R.id.comment_list);
        sendComment = findViewById(R.id.send_comment);
        commentBody = findViewById(R.id.comment_body);
        curr_user = FirebaseAuth.getInstance().getUid();
        commentsList = new ArrayList<>();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        commentListrecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mAdapter = new CommentsAdapter(commentsList, ctx,post_id);
        commentListrecyclerView.setAdapter(mAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /// Displaying comments workings..............................................
        mRootRef.child("posts_comments").child(post_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    commentsList.add(dataSnapshot.getValue(Comments.class));
                    mAdapter.notifyItemInserted(commentsList.size()-1);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(commentsList.contains(dataSnapshot.getValue(Comments.class))){
                        int index = commentsList.indexOf(dataSnapshot.getValue(Comments.class));
                        commentsList.remove(dataSnapshot.getValue(Comments.class));
                        mAdapter.notifyItemRemoved(index);
                    }

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Setting heading.....................
        final TextView heading = findViewById(R.id.user2_name);
        mRootRef.child("posts").child(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("poster_name")) {
                    Posts post = dataSnapshot.getValue(Posts.class);
                    String name = post.getPoster_name();
                    heading.setText(name+"'s Post Comments");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        ///Internal Workings of commenting.................................................................
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s= commentBody.getText().toString().trim();
                if(!s.equals("")){
                    final String timestamp = String.valueOf(System.currentTimeMillis());
                    mRootRef.child("Users").child(curr_user).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users user = dataSnapshot.getValue(Users.class);
                            String image = user.getImage();
                          final String commentId = mRootRef.child("Users").child(curr_user).child("image1").push().getKey();
                            if(!user.getThumb_nail().equals("default")) image = user.getThumb_nail();
                            Map<String,String> commentMap = new HashMap<>();
                            commentMap.put("post_id",post_id);
                            commentMap.put("user_id",curr_user);
                            commentMap.put("pro_image",image);
                            commentMap.put("comment_body",commentBody.getText().toString().trim());
                            commentMap.put("time_of_comment",timestamp);
                            commentMap.put("likes_count","0");
                            commentMap.put("user_name",user.getName());
                            commentMap.put("type","text");
                            commentMap.put("comment_id",commentId);
                            mRootRef.child("posts_comments").child(post_id).child(commentId).setValue(commentMap);
                            mRootRef.child("comments_ref").child(post_id).child(curr_user).child(commentId).setValue(timestamp);
                            commentBody.setText("");
                            commentBody.setHint("Enter your comment here...");

                            //  Adding comment count by 1

                            mRootRef.child("posts").child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists() && dataSnapshot.hasChild("comments_count")){

                                        String comments_count  = dataSnapshot.child("comments_count").getValue(String.class);
                                        long temp = Long.parseLong(comments_count);
                                        temp++;

                                        mRootRef.child("posts").child(post_id).child("comments_count").setValue(String.valueOf(temp));
                                        mRootRef.child("posts").child(post_id).child("last_comment").setValue(commentId);


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        ///........................................INTERNAL WORK DONE............................................................\\\



    }
}
