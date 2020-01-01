package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.User2RelatedActivities.User2ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikesAndSharesAdapter extends RecyclerView.Adapter<LikesAndSharesAdapter.ViewHolder> {
    List<String > likeORshareList;
    Context ctx;


    public LikesAndSharesAdapter(List<String> likeORshareList, Context ctx) {
        this.likeORshareList = likeORshareList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.likes_and_shares_layout,parent,false);
        return new LikesAndSharesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!likeORshareList.get(position).equals(FirebaseAuth.getInstance().getUid()))
                {
                    Intent intent = new Intent(ctx, User2ProfileActivity.class);
                    intent.putExtra("user_id2",likeORshareList.get(position));
                    ctx.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(ctx,SettingsActivity.class);
                    ctx.startActivity(intent);
                }
            }
        });

        final CircleImageView proPic = holder.mView.findViewById(R.id.pro_pic);
        final TextView name = holder.mView.findViewById(R.id.name);

        FirebaseDatabase.getInstance().getReference().child("Users").child(likeORshareList.get(position))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String proUrl = dataSnapshot.child("image").getValue(String.class);
                            String name1 = dataSnapshot.child("name").getValue(String.class);

                            Picasso.with(ctx)
                                    .load(proUrl)
                                    .placeholder(R.drawable.avtar)
                                    .into(proPic);

                            name.setText(name1);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




    }

    @Override
    public int getItemCount() {
        return likeORshareList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

    }
}
