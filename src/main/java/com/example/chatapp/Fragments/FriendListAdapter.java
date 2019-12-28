package com.example.chatapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatapp.ChatActivity;
import com.example.chatapp.R;
import com.example.chatapp.SettingsActivity;
import com.example.chatapp.Show_Profile_PictureActivity;
import com.example.chatapp.UserProfileActivity;
import com.example.chatapp.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
                .inflate(R.layout.single_user_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        //Every position describes a different friend
        final String user2=mFriendList.get(position);

        //What to set in single holder
        holder.setDetailAndOnclickFns(user2);

        mDataRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mFriendRef=FirebaseDatabase.getInstance().getReference().child("Friends");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user1 = currUser.getUid();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mFriendList.get(position)).equals(currUser.getUid())) {

                    Intent intent = new Intent(ctx, UserProfileActivity.class);
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
                if (!(mFriendList.get(position)).equals(currUser.getUid())) {
                    //Choice dialog box on clicking user2 profile for long time
                    CharSequence options[] = new CharSequence[] {"Open Profile" , "Send Message" , "Unfriend"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Select Option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            // If events for item clicked
                            if(i==0){
                                Intent intent = new Intent(ctx, UserProfileActivity.class);
                                intent.putExtra("user_id2",user2);
                                ctx.startActivity(intent);
                            }
                            if(i==1){
                                Intent intent = new Intent(ctx, ChatActivity.class);
                                intent.putExtra("user_id2",user2);
                                ctx.startActivity(intent);
                            }
                            if(i==2){
                                mDataRef= FirebaseDatabase.getInstance().getReference();
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

    @Override
    public int getItemCount() {
        //return  0;
        return mFriendList.size();
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
                        CircleImageView circleImageView = mView.findViewById(R.id.single_user_profile_pic);
                        Picasso.with(ctx)
                                .load(sec_user.getImage())
                                .placeholder(R.drawable.avtar)
                                .into(circleImageView);

                        ImageView userOnlineView =  mView.findViewById(R.id.online_check_image);
                        CardView cv = mView.findViewById(R.id.cv);

                        if(sec_user.getOnline().equals("true")){

                            userOnlineView.setVisibility(View.VISIBLE);
                            cv.setVisibility(View.VISIBLE);

                        } else {

                            userOnlineView.setVisibility(View.INVISIBLE);
                            cv.setVisibility(View.INVISIBLE);
                        }

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

