package com.example.chatapp.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.ChatFragmentViewData;
import com.example.chatapp.HomeChatViewAdapter;
import com.example.chatapp.Messaging.ChatActivity;
import com.example.chatapp.NewsFeed;
import com.example.chatapp.R;
import com.example.chatapp.SettingsActivity;
import com.example.chatapp.User2RelatedActivities.User1FriendList;
import com.example.chatapp.User2RelatedActivities.User2ProfileActivity;
import com.example.chatapp.RegisterAndLogin.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";
    private RecyclerView mchat_list;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference,mRootRef;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;
    private Context ctx;
    private FloatingActionButton floatingActionButton;
    private List<String> UsersList;
    RecyclerView.Adapter mAdapter;
    private List<ChatFragmentViewData> ExtraDataList;
    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_chats
                , container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        // mUsersList.hasFixedSize(true);
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        //databaseReference.keepSynced(true);
        ctx = container.getContext();
        mchat_list =  mMainView.findViewById(R.id.chat_list);
        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mchat_list.setLayoutManager(mLayoutManager);
        UsersList = new ArrayList<>();
        ExtraDataList = new ArrayList<>();

        floatingActionButton = mMainView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, User1FriendList.class);
                startActivity(intent);
            }
        });

        if(current_user != null)
        {
            mRootRef.child("chat_ref").child(current_user.getUid()).orderByChild("last_message_timestamp").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(UsersList.size()>0) UsersList.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        UsersList.add(dataSnapshot1.getKey());
                    }


                    mAdapter = new HomeChatViewAdapter(UsersList,container.getContext());
                    mchat_list.setAdapter(mAdapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }






        // Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

            }
    @Override
    public void onStop() {
        super.onStop();

    }
    public String path(String s1,String s2)
    {
        String res;
        if(s1.compareTo(s2)>0) res= s1+"/"+s2;
        else res= s2+"/"+s1;
        return res;
    }

}
