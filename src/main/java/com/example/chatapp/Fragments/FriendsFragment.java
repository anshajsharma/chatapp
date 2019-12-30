package com.example.chatapp.Fragments;


import android.content.Context;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";
    private RecyclerView mFriendList;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference , mfriendRef;
    private FirebaseRecyclerAdapter adapter;
    private View mMainView;
    private Context ctx;
    private List<String> FriendList ;
    private RecyclerView.Adapter mAdapter;
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        //variable initialisation
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.keepSynced(true); ctx = container.getContext();
        mfriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");



      //Retreiving Friends in Friendlist array of string----------------------------------------------------------\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        if(current_user != null)
            mfriendRef.child(current_user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                        Log.i("asad", String.valueOf(map));
                       // FriendList.clear();
                        assert map != null;
                        FriendList = new ArrayList(map.keySet());
                        //Log.i(TAG, "onDataChange2: "+ FriendList.toString());

                        //Recycler view initialisation done here....
                        mFriendList =  mMainView.findViewById(R.id.friend_list);
                        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
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

  //      Log.i(TAG, "onDataChange22: "+ FriendList.toString());



//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Users")
//                .limitToLast(5000);
//

//        FirebaseRecyclerOptions<Frien> options =
//                new FirebaseRecyclerOptions.Builder<Users>()
//                        .setQuery(query, Users.class)
//                        .build();
//
//        adapter = new FirebaseRecyclerAdapter<Friendlist, FriendsFragment.FriendsViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull final Users user) {
//                // Bind the Users object to the messageViewHolder
//                // ...
//                String user2 = user.getUser_id();
//                mfriendRef.child(current_user.getUid()).child(user2).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//                            holder.setDetails(user.getName(), user.getStatus(), user.getImage(), ctx);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                final String user_id = getRef(position).getKey();
//
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i(TAG, "onClick23: " + user_id + " " + current_user.getUid());
//                        if (!(user_id.equals(current_user.getUid()))) {
//
//                            Intent intent = new Intent(getActivity(), User2ProfileActivity.class);
//                            intent.Fra("user_id2", user_id);
//                            startActivity(intent);
//
//                            //  Log.i(TAG, "onClick23: " + user_id );
//                        } else {
//
//                            Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                            //  intent.putExtra("user_id2", user_id);
//                            startActivity(intent);
//                        }
//
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public FriendsFragment.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                // Create a new instance of the ViewHolder, in this case we are using a custom
//                // layout called R.layout.single_user_layout for each item
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.single_user_layout, parent, false);
//
//                return new FriendsFragment.FriendsViewHolder(view);
//            }
//
//        };
//
//
//        mFriendList.setAdapter(adapter);
//        adapter.startListening();
        return mMainView;

    }

    @Override
    public void onStart() {
        super.onStart();




    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

public static class FriendsViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public FriendsViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

    }

    public void setDetails(String name , String status , String image , Context ctx)
    {
        TextView userNameView = (TextView) mView.findViewById(R.id.single_display_name);
        userNameView.setText(name);

        TextView userStatusView = (TextView) mView.findViewById(R.id.single_user_status);
        userStatusView.setText(status);

        CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.single_user_profile_pic);
        Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.avtar).into(userImageView);


    }



}


}
