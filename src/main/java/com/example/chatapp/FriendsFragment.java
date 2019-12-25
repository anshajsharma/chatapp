package com.example.chatapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";
    private RecyclerView mFriendList;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);
//        mFriendList = (RecyclerView) mMainView.findViewById(R.id.friend_list);
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
//        // mUsersList.hasFixedSize(true);
//        current_user = FirebaseAuth.getInstance().getCurrentUser();
//        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
//
//
//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Users")
//                .limitToLast(50);
//
//        FirebaseRecyclerOptions<Users> options =
//                new FirebaseRecyclerOptions.Builder<Users>()
//                        .setQuery(query, Users.class)
//                        .build();
//
//        adapter = new FirebaseRecyclerAdapter<Users, AllUsersActivity.userViewHolder>(options) {
//            @Override
//            public AllUsersActivity.userViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                // Create a new instance of the ViewHolder, in this case we are using a custom
//                // layout called R.layout.message for each item
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.single_user_layout, parent, false);
//
//                return new AllUsersActivity.userViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(AllUsersActivity.userViewHolder holder, int position, Users user  ) {
//                // Bind the Users object to the userViewHolder
//                // ...
//                holder.setDetails(user.getName(),user.getStatus(),user.getImage(),getApplicationContext());
//                final String user_id = getRef(position).getKey();
//
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i(TAG, "onClick23: " + user_id +" " + current_user.getUid());
//                        if(!user_id.equals(current_user.getUid())){
//
//                            Intent intent = new Intent(AllUsersActivity.this , UserProfileActivity.class);
//                            intent.putExtra("user_id",user_id);
//                            //  Log.i(TAG, "onClick23: " + user_id );
//                            startActivity(intent);
//                        }
//                        else{
//
//                            Intent intent = new Intent(AllUsersActivity.this , SettingsActivity.class);
//
//                            startActivity(intent);
//                        }
//
//                    }
//                });
//            }
//        };
//
//        mUsersList.setAdapter(adapter);
//
//
//
//
//


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//    public class userViewHolder extends RecyclerView.ViewHolder{
//
//        View mView;
//
//        public userViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mView=itemView;
//        }
//        public void setDetails(String name , String status, String imageUrl , Context ctx){
//            TextView tv = mView.findViewById(R.id.single_display_name);
//            TextView tv2 = mView.findViewById(R.id.single_user_status);
//            tv.setText(name);
//            tv2.setText(status);
//            Uri resultUri = Uri.parse(imageUrl);
//            CircleImageView circleImageView = mView.findViewById(R.id.single_user_profile_pic);
//            Log.i(TAG, "setDetails: "+ name +" "+  imageUrl);
//            Picasso.with(FriendsFragment.this)
//                    .load(imageUrl)
//                    .placeholder(R.drawable.avtar)
//                    .into(circleImageView);
//            //Context ka problem hai....
//
//        }

//        public View getmView() {
//            return mView;
//        }
//    }


}
