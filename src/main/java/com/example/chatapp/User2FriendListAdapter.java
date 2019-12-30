package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class User2FriendListAdapter extends RecyclerView.Adapter<User2FriendListAdapter.ViewHolder> {

    private List<String> mUser2FriendList;
    DatabaseReference mDataRef, mFriendRef;
    FirebaseUser currUser;
    Context ctx;

    public User2FriendListAdapter(List<String> mUser2FriendList, Context ctx) {
        this.mUser2FriendList = mUser2FriendList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public User2FriendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user2_single_friend_layout, parent, false);
        return new User2FriendListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User2FriendListAdapter.ViewHolder holder, final int position) {

        //Every position describes a different friend
        final String user2 = mUser2FriendList.get(position);

        //What to set in single holder
        holder.setDetailAndOnclickFns(user2);

        mDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mFriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user1 = currUser.getUid();

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!(mUser2FriendList.get(position)).equals(currUser.getUid())) {
//
//                    Intent intent = new Intent(ctx, User2ProfileActivity.class);
//                    intent.putExtra("user_id2", user2);
//                    ctx.startActivity(intent);
//
//                } else {
//
//                    Intent intent = new Intent(ctx, SettingsActivity.class);
//                    ctx.startActivity(intent);
//                }
//
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return mUser2FriendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetailAndOnclickFns(String s) {


            mDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
            mFriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            final String user1 = currUser.getUid();
            final String user2 = s;

            final CardView viewProfile = mView.findViewById(R.id.view_profile);
//            if(user1.equals(user2)) viewProfile.setVisibility(View.INVISIBLE);

            mDataRef.child(user2).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Users sec_user = dataSnapshot.getValue(Users.class);
                        TextView tv = mView.findViewById(R.id.single_display_name);
                       final DatabaseReference mFriendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
                        tv.setText(sec_user.getName());

                        CircleImageView circleImageView = mView.findViewById(R.id.single_user_profile_pic);
                        Picasso.with(ctx)
                                .load(sec_user.getImage())
                                .placeholder(R.drawable.avtar)
                                .into(circleImageView);

                        circleImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ctx, Show_Profile_PictureActivity.class);
                                intent.putExtra("user_id2", user2);
                                ctx.startActivity(intent);

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mFriendRef.child(user2).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        if(!dataSnapshot.hasChild(user1)){
                            final CardView cv = mView.findViewById(R.id.add_friend);
                            cv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                               final DatabaseReference mFriendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
                                mFriendRequestRef.child(user1).child(user2)
                                            .setValue("sent")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        mFriendRequestRef.child(user2).child(user1)
                                                                .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ctx, "Friend request sent...", Toast.LENGTH_SHORT).show();
                                                                cv.setVisibility(View.INVISIBLE);
                                                                viewProfile.animate().translationXBy(-210f).setDuration(1100);
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(ctx, "Failed in sending friend request..", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                        else{
                            final CardView cv = mView.findViewById(R.id.add_friend);
                            cv.setVisibility(View.INVISIBLE);
                            viewProfile.animate().translationXBy(-210f).setDuration(1100);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            TextView tv2 = mView.findViewById(R.id.mutual_friends_count);

            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mView.findViewById(R.id.mutual_friends_count).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ctx , User2FriendView.class );
                            intent.putExtra("user2" , user2);
                            ctx.startActivity(intent);
                        }
                    });
                }
            });

            viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user2.equals(user1)) {

                    Intent intent = new Intent(ctx, User2ProfileActivity.class);
                    intent.putExtra("user_id2", user2);
                    ctx.startActivity(intent);

                } else {

                    Intent intent = new Intent(ctx, SettingsActivity.class);
                    ctx.startActivity(intent);
                }

            }
        });









            /// ------------------------MUTUAL FRIENDS COUNT SETUP------------------------------------------------- \\\\\\\\\\\\\\\\\\\\\\


            DatabaseReference user1FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(user1);
            final DatabaseReference user2FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(user2);


            final ArrayList<String> user2Friendlist;

            user1FriendsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);

                        final long user1FriendsCount, user2FriendsCount;
                        // FriendList.clear();
                        assert map != null;
                        final ArrayList<String> user1Friendlist = new ArrayList(map.keySet());
                        user1FriendsCount = user1Friendlist.size();

                        user2FriendsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                    };
                                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                                    assert map != null;
                                    ArrayList<String> user2Friendlist = new ArrayList(map.keySet());

                                    long user2FriendsCount = user2Friendlist.size();


                                    user1Friendlist.retainAll(user2Friendlist);


                                    int mutual = user1Friendlist.size();
                                    String s = String.valueOf(user2FriendsCount) + " Friends | " + String.valueOf(mutual) + " Mutual Friends";
                                    TextView tv = (mView.findViewById(R.id.mutual_friends_count));
                                    tv.setText(s);


                                } else {
                                    String s = "0 Friends | 0 Mutual Friends";
                                    TextView tv = (mView.findViewById(R.id.mutual_friends_count));
                                    tv.setText(s);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    } else {

                        user2FriendsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                    };
                                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                                    assert map != null;
                                    ArrayList<String> user2Friendlist = new ArrayList(map.keySet());

                                    long user2FriendsCount = user2Friendlist.size();
                                    String s = String.valueOf(user2FriendsCount) + " Friends | " + "0 Mutual Friends";

                                    TextView tv = (mView.findViewById(R.id.mutual_friends_count));
                                    tv.setText(s);


                                } else {
                                    String s = "0 Friends | 0 Mutual Friends";
                                    TextView tv = (mView.findViewById(R.id.mutual_friends_count));
                                    tv.setText(s);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }
};

