package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllUsersActivity extends AppCompatActivity {

    private RecyclerView mUsersList;

    private DatabaseReference databaseReference;
    FirebaseRecyclerAdapter adapter;
    //private Object userViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mUsersList = findViewById(R.id.users_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
       // mUsersList.hasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


    //loads data into recycler view onstart up


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .limitToLast(50);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();

         adapter = new FirebaseRecyclerAdapter<Users, userViewHolder>(options) {
            @Override
            public userViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_user_layout, parent, false);

                return new userViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(userViewHolder holder, int position, Users user) {
                // Bind the Users object to the userViewHolder
                // ...
                holder.setDetails(user.getName(),user.getStatus());

            }
        };

        mUsersList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class userViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setDetails(String name , String status){
            TextView tv = mView.findViewById(R.id.single_display_name);
            TextView tv2 = mView.findViewById(R.id.single_user_status);
            tv.setText(name);
            tv2.setText(status);
        }
    }
}
