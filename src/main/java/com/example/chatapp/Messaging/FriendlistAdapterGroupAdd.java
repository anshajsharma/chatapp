package com.example.chatapp.Messaging;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.RegisterAndLogin.Users;
import com.example.chatapp.User2RelatedActivities.FriendListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class FriendlistAdapterGroupAdd extends RecyclerView.Adapter< FriendlistAdapterGroupAdd.ViewHolder>{
    private List<Users> mFriendList ;
    DatabaseReference mDataRef,mFriendRef;
    FirebaseUser currUser;
    Context ctx;
    int count=0;
    int c=0;

    public FriendlistAdapterGroupAdd(List<Users> mFriendList, Context ctx) {
        this.mFriendList = mFriendList;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public FriendlistAdapterGroupAdd.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_friend_layout,parent,false);
        return new FriendlistAdapterGroupAdd.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendlistAdapterGroupAdd.ViewHolder holder, int position) {
            final Users users = mFriendList.get(position);
        TextView tv = holder.mView.findViewById(R.id.single_display_name);
        TextView tv2 = holder.mView.findViewById(R.id.single_user_status);
        final RelativeLayout layout = holder.mView.findViewById(R.id.foreground_changer);
        FloatingActionButton fab = holder.mView.findViewById(R.id.floatingActionButton12);
        fab.setVisibility(View.GONE);
        users.setSelected(false);
        tv.setText(users.getName());
        tv2.setText(users.getStatus());
        CircleImageView circleImageView = holder.mView.findViewById(R.id.single_user_profile_pic);
        Picasso.with(ctx)
                .load(users.getImage())
                .placeholder(R.drawable.avtar)
                .into(circleImageView);
                holder.mView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + "clicked " + c + " "  + users.isSelected() );
                {
                    Log.i("TAG", "onClickssds: " + users.isSelected());
                    if(users.isSelected()){
                        final int color = 0x16FFFFFF;
                        final Drawable drawable = new ColorDrawable(color);
                        layout.setForeground(drawable);
                        users.setSelected(false);
                    }else{
                        final int color = 0x3B61A354;
                        final Drawable drawable = new ColorDrawable(color);
                        layout.setForeground(drawable);
                        users.setSelected(true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public List<Users> getCheckedItems(){
        return mFriendList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
    }
}
