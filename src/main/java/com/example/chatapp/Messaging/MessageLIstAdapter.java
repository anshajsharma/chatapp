package com.example.chatapp.Messaging;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageLIstAdapter extends RecyclerView.Adapter<MessageLIstAdapter.ViewHolder> {
    private List<SingleMessage> chats;
    private Context ctx;
    private DatabaseReference mMessageRef, mChatRooomRef;
    private FirebaseUser currUser;
    private int MESSAGE_RECEIVED=0,MESSAGE_SENT=1;
    private String user2;


    public MessageLIstAdapter(List<SingleMessage> chats, Context ctx ,String User2) {
        this.chats = chats;
        this.ctx = ctx;
        this.user2 = User2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_messages_layout, parent, false);
        }
        else{
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sent_messages_layout, parent, false);
        }

        return new MessageLIstAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //Every position describes a different message
        final SingleMessage message = chats.get(position);

        //What to set in single holder
        holder.setDetailAndOnclickFns(message);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user1 = currUser.getUid();
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                long now = System.currentTimeMillis();
                final TextView tv = holder.mView.findViewById(R.id.message_body);

                long temp  = now - message.getTimestamp();
                 final int SECOND_MILLIS = 1000;
                 final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
                 final int HOUR_MILLIS = 60 * MINUTE_MILLIS;

     //           Log.i("asd", "onLongClick: " + ctx);
                 if(temp<HOUR_MILLIS && message.getSender().equals(currUser.getUid()) && !message.getAvaibility().equals("none"))
                 {
                     AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                     builder1.setMessage("Delete message??");
                     builder1.setCancelable(true);

                     builder1.setPositiveButton(
                             "DELETE FOR ME",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                             mRootRef.child("Messages").child(path(user1,user2)).child(String.valueOf(message.getTimestamp()))
                                     .addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                             if(dataSnapshot.exists() && dataSnapshot.hasChild("avaibility"))
                                             {
                                                 if(dataSnapshot.child("avaibility").getValue(String.class).equals(user1))
                                                 {
                                                     mRootRef.child("Messages").child(path(user1,user2)).child(String.valueOf(message.getTimestamp()))
                                                             .removeValue();
                                                 }
                                                 else{
                                                     mRootRef.child("Messages").child(path(user1,user2)).child(String.valueOf(message.getTimestamp()))
                                                             .child("avaibility").setValue(user2);
                                                 }

                                             }


                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     });

                                 }
                             });

                     builder1.setNegativeButton(
                             "DELETE FOR EVERYONE",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     mRootRef.child("Messages").child(path(user1,user2)).child(String.valueOf(message.getTimestamp()))
                                             .addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                                                     if(dataSnapshot.exists() && dataSnapshot.hasChild("message_body"))
//                                                     {
//                                                         if(dataSnapshot.child("avaibility").getValue(String.class).equals(user1))
//                                                         {
//                                                             mRootRef.child("Messages").child(path(user1,user2)).child(String.valueOf(message.getTimestamp()))
//                                                                     .child("message_body").setValue("@-> This message was deleted");
//                                                         }
//                                                         else{
//                                                             mRootRef.child("Messages").child(path(user1,user2)).child(String.valueOf(message.getTimestamp()))
//                                                                     .child("avaibility").setValue("none");
//                                                         }
//
//                                                     }


                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                                 }
                                             });



                                 }
                             });

                     AlertDialog alert11 = builder1.create();
                     alert11.show();



                 }
                 else
                 {
                     AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                     builder1.setMessage("Delete message??");
                     builder1.setCancelable(true);

                     builder1.setPositiveButton(
                             "DELETE FOR ME",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     // dialog.cancel();


                                 }
                             });


                     builder1.setNegativeButton(
                             "Cancel",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {

                                     dialog.dismiss();


                                 }
                             });

                     AlertDialog alert11 = builder1.create();
                     alert11.show();



                 }



                return false;
            }
        });



    }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        @SuppressLint("ResourceAsColor")
        public void setDetailAndOnclickFns(SingleMessage message) {

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            final String user1 = currUser.getUid();
            final TextView tv = mView.findViewById(R.id.message_body);
            final ImageView imageView = mView.findViewById(R.id.image);
            DatabaseReference mRootRef=FirebaseDatabase.getInstance().getReference();
            tv.setText(message.getMessage_body());
            //Log.i("fghbjn", "imageCompressorAndUploader12: " + message.getType());
            if(message.getType().equals("image"))
            {
               // Log.i("fghbjn", "imageCompressorAndUploader12: " + "gvhidfhujmdfhg");
                imageView.setVisibility(View.VISIBLE);
                if(message.getMessage_body().equals("")) tv.setVisibility(View.GONE);
                Picasso.with(ctx)
                       .load(message.getImage_url())
                       .placeholder(R.drawable.image_loading)
                       .into(imageView);
            }
            if(message.getType().equals("pdf"))
            {
                imageView.setVisibility(View.VISIBLE);
                if(message.getMessage_body().equals("")) tv.setVisibility(View.GONE);
                Picasso.with(ctx)
                        .load(message.getFile_url())
                        .placeholder(R.drawable.document_icon)
                        .into(imageView);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currUser.getUid().equals(chats.get(position).getSender())){
            return MESSAGE_SENT;
        }
        else{
            return MESSAGE_RECEIVED;
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
