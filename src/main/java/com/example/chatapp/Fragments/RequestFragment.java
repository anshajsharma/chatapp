package com.example.chatapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.SettingsActivity;
import com.example.chatapp.UserProfileActivity;
import com.example.chatapp.Users;
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
public class RequestFragment extends Fragment {

    private static final String TAG = "RequestFragment";
    private RecyclerView mFriendShip_status_list;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;
    private Context ctx;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_request
                , container, false);
        mFriendShip_status_list =  mMainView.findViewById(R.id.mFriendShip_status_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        // mUsersList.hasFixedSize(true);
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        //databaseReference.keepSynced(true);
        ctx = container.getContext();
        mFriendShip_status_list.setLayoutManager(new LinearLayoutManager(ctx));

        // Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .limitToLast(5000);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Users, RequestFragment.FriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestFragment.FriendsViewHolder holder, int position, @NonNull Users user) {
                // Bind the Users object to the userViewHolder
                // ...

                String user1 = current_user.getUid();



                holder.setDetails(user.getName(), user.getStatus(), user.getImage(), ctx , user.getOnline());




                final String user_id = getRef(position).getKey();



                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick23: " + user_id + " " + current_user.getUid());
                        if (!user_id.equals(current_user.getUid())) {

                            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                            intent.putExtra("user_id2", user_id);
                            startActivity(intent);

                            //  Log.i(TAG, "onClick23: " + user_id );
                        } else {

                            Intent intent = new Intent(getActivity(), SettingsActivity.class);
                            //  intent.putExtra("user_id2", user_id);
                            startActivity(intent);
                        }

                    }
                });
            }

            @NonNull
            @Override
            public RequestFragment.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.single_user_layout for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.curr_user_friends, parent, false);

                return new  RequestFragment.FriendsViewHolder(view);
            }

        };


        mFriendShip_status_list.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(String name , String status , String image , Context ctx , String online_status)
        {
            TextView userNameView = (TextView) mView.findViewById(R.id.user2_name);
            userNameView.setText(name);

            TextView userStatusView = (TextView) mView.findViewById(R.id.mutual_friends_count);
            userStatusView.setText(status);

            CircleImageView userImageView =  mView.findViewById(R.id.user2_profile_pic);
            Picasso.with(ctx).load(image).placeholder(R.drawable.avtar).into(userImageView);

            ImageView userOnlineView =  mView.findViewById(R.id.online_check_image);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }
        }

        public void setUserOnline(String online_status) {



        }

    }

}
