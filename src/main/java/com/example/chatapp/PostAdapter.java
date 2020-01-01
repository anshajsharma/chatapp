package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Messaging.GetTimeAgo;
import com.example.chatapp.User2RelatedActivities.User2ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Posts> relatedPosts;
    private Context ctx;
    private DatabaseReference mRootRef;
    private FirebaseUser currUser;

    public PostAdapter(List<Posts> relatedPosts, Context ctx) {
        this.relatedPosts = relatedPosts;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_post_view, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Every position describes a different message
        final Posts post = relatedPosts.get(position);

        //What to set in single holder
        holder.setDetailAndOnclickFns(post);
    }


    @Override
    public int getItemCount() {
        return relatedPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

     public void setDetailAndOnclickFns(final Posts post){

         //Variable Initialisation........................................................
         final TextView name,time,likes,comments,shares,description;
         final ImageView like,postImage,postMenu;
         CircleImageView posterImage;
         final DatabaseReference mLikeRef,mCommentRef,mShareRef,mRootRef;
         final String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

         final String curr_user = FirebaseAuth.getInstance().getUid();
         CardView like_card = mView.findViewById(R.id.like_card);
         CardView commentCard = mView.findViewById(R.id.comment_card);
         mRootRef= FirebaseDatabase.getInstance().getReference();
         mLikeRef = mRootRef.child("post_likes");
         mCommentRef= mRootRef.child("post_comment");
         likes = mView.findViewById(R.id.like_count);
         comments =mView.findViewById(R.id.comment_count);
         shares = mView.findViewById(R.id.share_count);
         name = mView.findViewById(R.id.posted_by_name);

         posterImage = mView.findViewById(R.id.posted_by_image);
         like =mView.findViewById(R.id.like_image);
         time = mView.findViewById(R.id.time_of_post);
         description = mView.findViewById(R.id.description);
         postImage = mView.findViewById(R.id.posted_image);
         postMenu =mView.findViewById(R.id.post_menu);
         final String totSahre,totLikes,totComments;
         LinearLayout open_user_profile =mView.findViewById(R.id.open_user_profile);
         //.............................................................................................


         // Likes handeling done here....................................................................

         mLikeRef.child(post.getPost_id()).child(curr_user).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                     like.setImageResource(R.drawable.liked);
                 }else{
                     like.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

         like_card.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mLikeRef.child(post.getPost_id()).child(curr_user).addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         if(!dataSnapshot.exists())
                         {
                             mRootRef.child("posts").child(post.getPost_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     if(dataSnapshot.exists()){
                                         mLikeRef.child(post.getPost_id()).child(curr_user).setValue(timeStamp);
                                         like.setImageResource(R.drawable.liked);
                                        long temp = Long.parseLong(Objects.requireNonNull(dataSnapshot.child("likes_count").getValue(String.class)));
                                        temp++;
                                         mRootRef.child("posts").child(post.getPost_id()).child("likes_count").setValue(String.valueOf(String.valueOf(temp)));
                                     }
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                 }
                             });
                         }else{
                             mRootRef.child("posts").child(post.getPost_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     if(dataSnapshot.exists()){
                                         mLikeRef.child(post.getPost_id()).child(curr_user).removeValue();
                                         like.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                         long temp = Long.parseLong(Objects.requireNonNull(dataSnapshot.child("likes_count").getValue(String.class)));
                                         temp--;
                                         String totLikes;

                                         mRootRef.child("posts").child(post.getPost_id()).child("likes_count").setValue(String.valueOf(String.valueOf(temp)));

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
         });
      //-------------------------------------LIKES HANDELING DONE-------------------------------------------------------------



         //Comments handeling Activity Opening.........................................................

         commentCard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(ctx,Comments_handeling_Activity.class);
                 intent.putExtra("post_id", post.getPost_id());
                 ctx.startActivity(intent);
             }
         });
         //.............................................................................................







         //Posted image haldeling.....................................
         Picasso.with(ctx)
                 .load(post.getPosted_image())
                 .into(postImage);
         postImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(ctx,Show_Clicked_Image.class);
                 intent.putExtra("url",post.getPosted_image());
                 ctx.startActivity(intent);
             }
         });
         //............................................................................................


        //What if you click on poster name , profile pic or time_of_post regions..........................................
         open_user_profile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!post.getUser_id().equals(FirebaseAuth.getInstance().getUid()))
                 {
                     Intent intent = new Intent(ctx, User2ProfileActivity.class);
                     intent.putExtra("user_id2",post.getUser_id());
                     ctx.startActivity(intent);
                 }
                 else{
                     Intent intent = new Intent(ctx,SettingsActivity.class);
                     ctx.startActivity(intent);
                 }
             }
         });
         //............................................................................................


         //Just putting data obtained.................................................................
         Picasso.with(ctx)
                 .load(post.getUser_profile_image())
                 .placeholder(R.drawable.avtar)
                 .into(posterImage);
         name.setText(post.getPoster_name());
         description.setText(post.getDescription());
         mRootRef.child("posts").child(post.getPost_id()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()) {


                     String totSahre, totLikes, totComments;
                     long temp=0;
                      temp = Long.parseLong(Objects.requireNonNull(dataSnapshot.child("shares_count").getValue(String.class)));
                     if (temp >= 1000000) {
                         totSahre = String.valueOf(temp / 1000000) + "M" + " Shares";
                     } else if (temp >= 1000) {
                         totSahre = String.valueOf(temp / 1000) + " Shares";
                     } else {
                         totSahre = String.valueOf(temp) + " Shares";
                     }
                     temp = Long.parseLong(Objects.requireNonNull(dataSnapshot.child("likes_count").getValue(String.class)));
                     if (temp >= 1000000) {
                         totLikes = String.valueOf(temp / 1000000) + "M" + " Likes";
                     } else if (temp >= 1000) {
                         totLikes = String.valueOf(temp / 1000) + "K" + " Likes";
                     } else {
                         totLikes = String.valueOf(temp) + " Likes";
                     }
                     temp = Long.parseLong(Objects.requireNonNull(dataSnapshot.child("comments_count").getValue(String.class)));
                     if (temp >= 1000000) {
                         totComments = String.valueOf(temp / 1000000) + "M" + " Comments";
                     } else if (temp >= 1000) {
                         totComments = String.valueOf(temp / 1000) + "K" + " Comments";
                     } else {
                         totComments = String.valueOf(temp) + " Comments";
                     }
                     shares.setText(totSahre);
                     likes.setText(totLikes);
                     comments.setText(totComments);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

         //.................................................................................................

         // Time of post managed by this...........................................................
         GetTimeAgo getTimeAgo = new GetTimeAgo();
         String postTimestamp = post.getTimestamp();
         long lastTime = Long.parseLong(postTimestamp);
         String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, ctx);
         time.setText(lastSeenTime);
         //.....................................................................................

         //Menu handeling done here.....................................................
         postMenu.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Creating the instance of PopupMenu
                 PopupMenu popup = new PopupMenu(ctx, postMenu);
                 //Inflating the Popup using xml file
                 popup.getMenuInflater()
                         .inflate(R.menu.post_menu, popup.getMenu());

                 //registering popup with OnMenuItemClickListener
                 popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     public boolean onMenuItemClick(MenuItem item) {

                       if (item.getItemId() ==  R.id.view_profile ) {
                           if(!post.getUser_id().equals(FirebaseAuth.getInstance().getUid()))
                           {
                               Intent intent = new Intent(ctx,User2ProfileActivity.class);
                               intent.putExtra("user_id2",post.getUser_id());
                               ctx.startActivity(intent);
                           }
                           else{
                               Intent intent = new Intent(ctx,SettingsActivity.class);
                               ctx.startActivity(intent);
                           }
                       }
                       if(item.getItemId() == R.id.dislay_all_likes){
                           Intent intent = new Intent(ctx, ViewPostsLikesActivity.class);
                           intent.putExtra("post_id",post.getPost_id());
                           ctx.startActivity(intent);
                       }

                         return true;
                     }
                 });

                 popup.show(); //showing popup menu
             }
         }); //closing the setOnClickListener method
         //.........................................................................................................







        }




    }

};

