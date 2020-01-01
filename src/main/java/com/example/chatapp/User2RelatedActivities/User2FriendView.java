package com.example.chatapp.User2RelatedActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class User2FriendView extends AppCompatActivity {

    private static final String TAG = "User2FriendView";

    String user2;
    RecyclerView user2Friendlist;

    private FirebaseUser current_user;
    private DatabaseReference databaseReference , mfriendRef;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;
    private Context ctx;
    private List<String> user2FriendList ;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2_friend_view);

        user2 = getIntent().getStringExtra("user2");


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        ctx = getApplicationContext();
        mfriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");



        //Retreiving Friends in Friendlist array of string----------------------------------------------------------\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        if(current_user != null)
            mfriendRef.child(user2).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                        Log.i("asad", String.valueOf(map));
                        // FriendList.clear();
                        assert map != null;
                        user2FriendList = new ArrayList(map.keySet());
                       // user2FriendList.add(FirebaseAuth.getInstance().getUid());
                        Log.i(TAG, "onDataChange2: "+ user2FriendList.toString());
                  if(user2FriendList.contains(FirebaseAuth.getInstance().getUid()))      user2FriendList.remove(FirebaseAuth.getInstance().getUid());
                        //Recycler view initialisation done here....
                        user2Friendlist = findViewById(R.id.user2_friendlist);
                        user2Friendlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        user2Friendlist.setLayoutManager(new LinearLayoutManager(ctx));
                        mAdapter = new User2FriendListAdapter(user2FriendList,ctx);
                        user2Friendlist.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(ctx, "No Friend !!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }
}
