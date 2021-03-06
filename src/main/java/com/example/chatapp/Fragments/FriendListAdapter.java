package com.example.chatapp.Fragments;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatapp.Messaging.ChatActivity;
import com.example.chatapp.R;
import com.example.chatapp.SettingsActivity;
import com.example.chatapp.User2RelatedActivities.Show_Profile_PictureActivity;
import com.example.chatapp.User2RelatedActivities.User2ProfileActivity;
import com.example.chatapp.RegisterAndLogin.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter< FriendListAdapter.ViewHolder> {

    private List<String> mFriendList ;
    DatabaseReference mDataRef,mFriendRef;
    FirebaseUser currUser;
    Context ctx;
    public FriendListAdapter(List<String> friendList,Context c) {
       mFriendList = friendList;
       ctx = c;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_friend_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        CircleImageView imageView =holder.mView.findViewById(R.id.single_user_profile_pic);
        FloatingActionButton floatingActionButton = holder.mView.findViewById(R.id.floatingActionButton12);
        TextView name,status;
        name =holder.mView.findViewById(R.id.single_display_name);
        status =holder.mView.findViewById(R.id.single_user_status);

        if (position == 0) {
               floatingActionButton.setImageResource(R.drawable.ic_group_add_black_24dp);
               imageView.setVisibility(View.INVISIBLE);
               name.setText("Add New Group");
               status.setVisibility(View.INVISIBLE);
            name.animate().translationYBy(25f).setDuration(150);

        } else if (position == 1) {

            //All ok
            name.setText("Add Friends");
            imageView.setVisibility(View.INVISIBLE);
            status.setVisibility(View.INVISIBLE);
            name.animate().translationYBy(25f).setDuration(150);

        } else {

          floatingActionButton.setVisibility(View.INVISIBLE);

        //Every position describes a different friend
        final String user2 = mFriendList.get(position-2);

        //What to set in single holder
        holder.setDetailAndOnclickFns(user2);

        mDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mFriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user1 = currUser.getUid();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mFriendList.get(position-2)).equals(currUser.getUid())) {

                    Intent intent = new Intent(ctx, User2ProfileActivity.class);
                    intent.putExtra("user_id2", user2);
                    ctx.startActivity(intent);

                } else {

                    Intent intent = new Intent(ctx, SettingsActivity.class);
                    ctx.startActivity(intent);
                }

            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!(mFriendList.get(position-2)).equals(currUser.getUid())) {
                    //Choice dialog box on clicking user2 profile for long time
                    CharSequence options[] = new CharSequence[]{"Open Profile", "Send Message", "Unfriend"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Select Option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            // If events for item clicked
                            if (i == 0) {
                                Intent intent = new Intent(ctx, User2ProfileActivity.class);
                                intent.putExtra("user_id2", user2);
                                ctx.startActivity(intent);
                            }
                            if (i == 1) {
                                Intent intent = new Intent(ctx, ChatActivity.class);
                                intent.putExtra("user_id2", user2);
                                ctx.startActivity(intent);
                            }
                            if (i == 2) {
                                mDataRef = FirebaseDatabase.getInstance().getReference();
                                mDataRef.child("Friend_Requests").child(user1).child(user2).setValue("Not Friend Now");
                                mDataRef.child("Friend_Requests").child(user2).child(user1).setValue("Not Friend Now");
                                mDataRef.child("Friends").child(user2).child(user1).removeValue();
                                mDataRef.child("Friends").child(user1).child(user2).removeValue();

                            }
                        }
                    });
                    builder.show();
                    //If you press your profile for long time you will go to settings
                } else {

                    Intent intent = new Intent(ctx, SettingsActivity.class);
                    //  intent.putExtra("user_id2", user_id);
                    ctx.startActivity(intent);

                }


                return false;

            }
        });
    }

    }

    @Override
    public int getItemCount() {
        //return  0;
        return mFriendList.size()+2;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDetailAndOnclickFns(String s){

            mDataRef= FirebaseDatabase.getInstance().getReference().child("Users");
            mFriendRef=FirebaseDatabase.getInstance().getReference().child("Friends");
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            final String user1 = currUser.getUid();
            final String user2 = s;

            mDataRef.child(user2).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Users sec_user = dataSnapshot.getValue(Users.class);
                        TextView tv = mView.findViewById(R.id.single_display_name);
                        TextView tv2 = mView.findViewById(R.id.single_user_status);
                        tv.setText(sec_user.getName());
                        tv2.setText(sec_user.getStatus());

                        String image;
                        if(!sec_user.getThumb_nail().equals("default")) image = sec_user.getThumb_nail();
                        else image =sec_user.getImage();

                        CircleImageView circleImageView = mView.findViewById(R.id.single_user_profile_pic);
                        Picasso.with(ctx)
                                .load(image)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.avtar)
                                .into(circleImageView);




                        circleImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ctx, Show_Profile_PictureActivity.class);
                                intent.putExtra("user_id2",user2);
                                ctx.startActivity(intent);

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
}

