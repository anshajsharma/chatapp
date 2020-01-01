package com.example.chatapp.User2RelatedActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.chatapp.Fragments.FriendListAdapter;
import com.example.chatapp.R;
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
    private DatabaseReference databaseReference , mfriendRef;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;
    private Context ctx;
    private List<String> FriendList ;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user1_friend_list);

        //variable initialisation
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.keepSynced(true); ctx = getApplicationContext();
        mfriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");

        //Retreiving Friends in Friendlist array of string----------------------------------------------------------\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        if(current_user != null)
            mfriendRef.child(current_user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                        //Log.i("asad", String.valueOf(map));
                        // FriendList.clear();
                        assert map != null;
                        FriendList = new ArrayList(map.keySet());
                       // Log.i(TAG, "onDataChange2: "+ FriendList.toString());

                        //Recycler view initialisation done here....
                        mFriendList =  findViewById(R.id.friend_list);
                        mFriendList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mFriendList.setLayoutManager(new LinearLayoutManager(ctx));
                        mAdapter = new FriendListAdapter(FriendList,ctx);
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
