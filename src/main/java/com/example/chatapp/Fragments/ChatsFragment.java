package com.example.chatapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.User2RelatedActivities.User1FriendList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


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
    private List<String> UsersList,typeList;
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
        typeList =  new ArrayList<>();
        ExtraDataList = new ArrayList<>();
        AdView adView = mMainView.findViewById(R.id.adView);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId();

        MobileAds.initialize(ctx, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Toast.makeText(ctx, "complete", Toast.LENGTH_SHORT).show();

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


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
//            mRootRef.child("chat_ref").child(current_user.getUid()).orderByChild("last_message_timestamp").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    if(dataSnapshot.exists()){
//                        UsersList.add(dataSnapshot.getKey());
//                    }
//                    if (dataSnapshot.hasChild("type")){
//                        typeList.add("Group");
//                    }else{
//                        typeList.add("Single user");
//                    }
//                    mAdapter = new HomeChatViewAdapter(UsersList,typeList, container.getContext());
//                    mchat_list.setAdapter(mAdapter);
//                    mAdapter.notifyItemInserted(typeList.size()-1);
//
//                }
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        if(dataSnapshot.exists()){
//                            String s1 = dataSnapshot.getKey();
//                            if(UsersList.contains(s1)){
//                                int pos = UsersList.indexOf(s1);
//                                mAdapter.notifyItemChanged(pos);
//                            }
//                        }
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.exists()){
//                        String s1 = dataSnapshot.getKey();
//                        if(UsersList.contains(s1)){
//                            int pos = UsersList.indexOf(s1);
//                            typeList.remove(pos);
//                            UsersList.remove(pos);
//                            mAdapter.notifyItemRemoved(pos);
//                        }
//                    }
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    mAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

            mRootRef.child("chat_ref").child(current_user.getUid()).orderByChild("last_message_timestamp").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(UsersList.size()>0) UsersList.clear(); if(typeList.size()>0) typeList.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        UsersList.add(dataSnapshot1.getKey());
                        if (dataSnapshot1.hasChild("type")){
                            typeList.add("Group");
                        }else{
                            typeList.add("Single user");
                        }
                    }
                    if(UsersList.size()>0){
                        mAdapter = new HomeChatViewAdapter(UsersList,typeList, container.getContext());
                        mchat_list.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }


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
