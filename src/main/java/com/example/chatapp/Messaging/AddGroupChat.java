package com.example.chatapp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.RegisterAndLogin.Users;
import com.example.chatapp.User2RelatedActivities.FriendListAdapter;
import com.example.chatapp.User2RelatedActivities.User1FriendList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddGroupChat extends AppCompatActivity {

    private String user;
    private RecyclerView recyclerView;
    private DatabaseReference mRootRef;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton createGroupChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_chat);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.addFriendToGroupList);
        final List<Users> friendList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new FriendlistAdapterGroupAdd(friendList, AddGroupChat.this);
        recyclerView.setAdapter(mAdapter);
        createGroupChat = findViewById(R.id.create_groupchat);


        createGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<Users> users = mAdapter.getItemId(2);
                int c=0;
                List<Users> groupMembers=new ArrayList<>();
                for(Users a:friendList){
                    if(a.isSelected()) groupMembers.add(a);
                }
                if(groupMembers.size()>1) {
                    Intent intent = new Intent(AddGroupChat.this, GroupChatInitialisation.class);
                    Bundle args = new Bundle();
//                args.putSerializable("ARRAYLIST",(Serializable)groupMembers);
                    intent.putExtra("userList", (Serializable) groupMembers);
                    startActivity(intent);


//                Intent intent = getIntent();
//                Bundle args = intent.getBundleExtra("BUNDLE");
//                ArrayList<Object> object = (ArrayList<Object>) args.getSerializable("ARRAYLIST");
                }else{
                    Toast.makeText(AddGroupChat.this, "Select atleast 2 members...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            user = FirebaseAuth.getInstance().getUid();
            mRootRef.child("Friends").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            mRootRef.child("Users").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                    if(dataSnapshot3.exists()){
                                        friendList.add(dataSnapshot3.getValue(Users.class));
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        mAdapter = new FriendlistAdapterGroupAdd(friendList, AddGroupChat.this);
                                        recyclerView.setAdapter(mAdapter);
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
