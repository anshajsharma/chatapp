package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Messaging.ChatActivity;
import com.example.chatapp.RegisterAndLogin.Users;
import com.example.chatapp.User2RelatedActivities.User2FriendListAdapter;
import com.example.chatapp.User2RelatedActivities.User2ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriend extends AppCompatActivity {
    private static final String TAG = "FindFriend";

    private RecyclerView mUsersList;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private ImageView search;
    private EditText name;
    private List<String> mUsers;
    private RecyclerView.Adapter mAdapter;
    Toolbar toolbar;
    //private Object messageViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        mUsersList = findViewById(R.id.users_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        name = findViewById(R.id.name);
        search = findViewById(R.id.search);
        mUsersList.setHasFixedSize(true);
        mUsers = new ArrayList<>();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new User2FriendListAdapter(mUsers,FindFriend.this);
        mUsersList.setAdapter(mAdapter);
        readUsers();
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string= s.toString().trim();
                if(s=="") readUsers();
                else searchFriend(string);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void searchFriend(String s)
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("name")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        if(dataSnapshot1.exists())
                        {
                            if(!dataSnapshot1.getKey().equals(FirebaseAuth.getInstance().getUid()))
                                mUsers.add(dataSnapshot1.getKey());
                        }

                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readUsers()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(name.getText().toString().equals(""))
                {
                    mUsers.clear();
                    if(dataSnapshot.exists())
                    {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            if(dataSnapshot1.exists())
                            {
                                if(!dataSnapshot1.getKey().equals(FirebaseAuth.getInstance().getUid()))
                                    mUsers.add(dataSnapshot1.getKey());
                            }

                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
               // Toast.makeText(FindFriend.this, mUsers.size(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private Context getContext() {
        return FindFriend.this;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }




}
