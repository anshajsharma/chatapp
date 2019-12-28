package com.example.chatapp.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.chatapp.ChatActivity;
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
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";
    private RecyclerView mchat_list;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;
    private Context ctx;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_chats
                , container, false);
        mchat_list =  mMainView.findViewById(R.id.chat_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        // mUsersList.hasFixedSize(true);
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        //databaseReference.keepSynced(true);
        ctx = container.getContext();
        mchat_list.setLayoutManager(new LinearLayoutManager(ctx));

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

        adapter = new FirebaseRecyclerAdapter<Users, ChatsFragment.FriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsFragment.FriendsViewHolder holder, int position, @NonNull Users user) {
                // Bind the Users object to the userViewHolder(holder)
                // ...



                holder.setDetails(user.getName(), user.getStatus(), user.getImage(), ctx , user.getOnline());
                databaseReference.keepSynced(true);



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

                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (!user_id.equals(current_user.getUid())) {
             //Choice dialog box on clicking user2 profile for long time
                            CharSequence options[] = new CharSequence[] {"Open Profile" , "Send Message" };
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Select Option");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    // If events for item clicked
                                    if(i==0){
                                        Intent intent = new Intent(ctx,UserProfileActivity.class);
                                        intent.putExtra("user_id2",user_id);
                                        startActivity(intent);
                                    }
                                    if(i==1){
                                        Intent intent = new Intent(ctx, ChatActivity.class);
                                        intent.putExtra("user_id2",user_id);
                                        startActivity(intent);
                                    }
                                }
                            });
                            builder.show();
           //If you press your profile for long time you will go to settings
                        } else {

                            Intent intent = new Intent(getActivity(), SettingsActivity.class);
                            //  intent.putExtra("user_id2", user_id);
                            startActivity(intent);

                        }




                        return false;
                    }
                });
            }

            @NonNull
            @Override
            public ChatsFragment.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.single_user_layout for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_user_layout, parent, false);

                return new ChatsFragment.FriendsViewHolder(view);
            }

        };


        mchat_list.setAdapter(adapter);
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
            TextView userNameView = (TextView) mView.findViewById(R.id.single_display_name);
            userNameView.setText(name);

            TextView userStatusView = (TextView) mView.findViewById(R.id.single_user_status);
            userStatusView.setText(status);

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.single_user_profile_pic);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.avtar).into(userImageView);

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.online_check_image);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }
        }
}

    }
