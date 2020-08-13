package com.example.chatapp.User2RelatedActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.chatapp.Messaging.AddGroupChat;
import com.example.chatapp.R;
import com.example.chatapp.RegisterAndLogin.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User1FriendList extends AppCompatActivity {
    private static final String TAG = "User1FriendList";
    private RecyclerView mFriendList;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference , mfriendRef, mRootRef ;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;
    private Context ctx;
    private String user;
    private List<String> FriendList ;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user1_friend_list);

        //variable initialisation
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.keepSynced(true); ctx = getApplicationContext();
        mfriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        mFriendList = findViewById(R.id.friend_list);

        //Retreiving Friends in Friendlist array of string----------------------------------------------------------\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            user = FirebaseAuth.getInstance().getUid();
            mRootRef.child("Friends").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final List<Users> friendList=new ArrayList<>();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Log.i(TAG, "onDataChange1234: " + dataSnapshot1.getKey() );
                            mRootRef.child("Users").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                    if(dataSnapshot3.exists()){

                                        Users users = dataSnapshot3.getValue(Users.class);

                                        friendList.add(users);

                                        mFriendList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        mFriendList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        mAdapter = new FriendListAdapter(friendList, User1FriendList.this);
                                        mFriendList.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }




                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }
}
