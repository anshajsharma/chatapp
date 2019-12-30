package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageLIstAdapter extends RecyclerView.Adapter<MessageLIstAdapter.ViewHolder> {
    private List<SingleMessage> chats;
    private Context ctx;
    private DatabaseReference mMessageRef, mChatRooomRef;
    private FirebaseUser currUser;
    private int MESSAGE_RECEIVED=0,MESSAGE_SENT=1;

    public MessageLIstAdapter(List<SingleMessage> chats, Context ctx) {
        this.chats = chats;
        this.ctx = ctx;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Every position describes a different message
        final SingleMessage message = chats.get(position);

        //What to set in single holder
        holder.setDetailAndOnclickFns(message);


//        mMessageRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        mFriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user1 = currUser.getUid();

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

        public void setDetailAndOnclickFns(SingleMessage message) {

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            final String user1 = currUser.getUid();
            TextView tv = mView.findViewById(R.id.message_body);

            tv.setText(message.getMessage_body());
//            CardView cv = mView.findViewById(R.id.cardView);
//            LinearLayout linearLayout = mView.findViewById(R.id.linearLayout);


//            if (message.getSender().equals(user1)) {
//
//              //  linearLayout.setLayoutDirection(mView.LAYOUT_DIRECTION_LTR);
//                //tv.setGravity(0);
//
//            } else {
//                cv.setBackgroundColor(123);
//            }
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
}
