package com.example.chatapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Messaging.ChatActivity;
import com.example.chatapp.Messaging.Groups;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeChatViewAdapter extends RecyclerView.Adapter<HomeChatViewAdapter.ViewHolder> {
    private List<String> UsersList, typeList;

    Context ctx;

    public HomeChatViewAdapter(List<String> usersList, List<String> typeList, Context ctx) {
        UsersList = usersList;
        this.typeList = typeList;
        this.ctx = ctx;
    }

//    public HomeChatViewAdapter(List<String> usersList, Context ctx) {
//        UsersList = usersList;
//        this.ctx = ctx;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_user_layout, parent, false);

        return new HomeChatViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        final CardView unread_message_count =  holder.view.findViewById(R.id.numberCard);
        final TextView time_of_last_message = holder.view.findViewById(R.id.time);
        final TextView last_message = holder.view.findViewById(R.id.single_user_status);
        final TextView count = holder.view.findViewById(R.id.count);
        unread_message_count.setVisibility(View.INVISIBLE);
        final String user1= FirebaseAuth.getInstance().getUid();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final String user2= UsersList.get(i);
        holder.setUserDetail(user2);
        final String type = typeList.get(i);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ChatActivity.class);
                intent.putExtra("user_id2",UsersList.get(i));
                intent.putExtra("type",type);
                ctx.startActivity(intent);
            }
        });


        final CircleImageView circleImageView = holder.view.findViewById(R.id.single_user_profile_pic);
        final TextView tv = holder.view.findViewById(R.id.single_display_name);

        //Message details setting done here
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            if(type.equals("Group")){
                mRootRef.child("Groups").child(user2).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() ){
                            Groups groups = dataSnapshot.getValue(Groups.class);
                            tv.setText(groups.getGroupName());
                            Picasso.with(ctx)
                                    .load(groups.getGroupIcon())
                                    .placeholder(R.drawable.avtar)
                                    .into(circleImageView);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            mRootRef.child("chat_ref").child(user1).child(user2).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        if(dataSnapshot.hasChild("last_message_timestamp"))
                        {
                            long tsLong = dataSnapshot.child("last_message_timestamp").getValue(Long.class);
                            if(tsLong!=0){
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.getDefault());              //  03:12 PM
                                java.util.Date currenTimeZone = new java.util.Date((long) tsLong );
                                time_of_last_message.setText(sdf2.format(currenTimeZone));
                            }else{
                                time_of_last_message.setText("");
                            }
                        }
                        if(dataSnapshot.hasChild("last_message"))
                        {
                            last_message.setText(dataSnapshot.child("last_message").getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return UsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setUserDetail(String s) {
            final CircleImageView circleImageView = view.findViewById(R.id.single_user_profile_pic);
            final TextView tv = view.findViewById(R.id.single_display_name);
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mRootRef.child("Users").child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("thumb_nail"))
                    {
                        if(!dataSnapshot.child("thumb_nail").getValue(String.class).equals("default"))
                        {
                            Picasso.with(ctx)
                                    .load(dataSnapshot.child("thumb_nail").getValue(String.class))
                                    .placeholder(R.drawable.avtar)
                                    .into(circleImageView);
                        }
                        else{
                            Picasso.with(ctx)
                                    .load(dataSnapshot.child("image").getValue(String.class))
                                    .placeholder(R.drawable.avtar)
                                    .into(circleImageView);
                        }

                    }else{
                        if(dataSnapshot.exists())
                        {
                            Picasso.with(ctx)
                                    .load(dataSnapshot.child("image").getValue(String.class))
                                    .placeholder(R.drawable.avtar)
                                    .into(circleImageView);
                        }

                    }
                    if(dataSnapshot.exists()){
                        tv.setText(dataSnapshot.child("name").getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public String path(String s1,String s2)
    {
        String res;
        if(s1.compareTo(s2)>0) res= s1+"/"+s2;
        else res= s2+"/"+s1;
        return res;
    }
}
