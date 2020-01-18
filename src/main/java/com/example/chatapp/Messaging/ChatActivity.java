package com.example.chatapp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.User2RelatedActivities.User2ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private Toolbar mToolbar;
    private String user2;
    private DatabaseReference mRootRef,mMessageRef,mChatref;
    private CircleImageView circleImageView;
    private Toolbar toolbar;
    private FirebaseUser user1;
    private TextView mLastSeenView,user2name;
    private RecyclerView mChatList;
    private List<SingleMessage> messageList,sender;
    private RecyclerView.Adapter mAdapter;
    private ImageButton sendMessage;
    private Context ctx;
    private ImageView back;
    SwipeRefreshLayout swipeRefreshLayout;
    private final int SENT_MESSAGE=0,RECEIVED_MESSAGE=1;
    private String res;

    EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user2 = getIntent().getStringExtra("user_id2");
        ActionBar actionBar = getSupportActionBar();
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        ctx = ChatActivity.this;
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
        mLastSeenView = findViewById(R.id.last_seen);
//        swipeRefreshLayout = findViewById(R.id.swipe_view);
//        swipeRefreshLayout.setEnabled(false);
        user2name = findViewById(R.id.user2_name);
        circleImageView = findViewById(R.id.user2_profile_pic);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        message = findViewById(R.id.typed_message);
        mChatList = findViewById(R.id.message_list);
        back = findViewById(R.id.back_button);
        mMessageRef = mRootRef.child("Messages");
        mChatref = mRootRef.child("chat_ref");
        messageList = new ArrayList<>();
         res=path(user2,user1.getUid());

        // Hide keyeboard  when activity opens...............
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




        //toolbar initialisation----------------------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, User2ProfileActivity.class);
                intent.putExtra("user_id2",user2);
                startActivity(intent);
            }
        });
        //--------------------------------------------------------------------

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        mChatref.push().child("kya").setValue("haal");
//        Log.i(TAG, "onCreate: " + mChatref.child(user2).push().getKey().trim() ) ;


        ////////-----RecyclerView Workings -----------------------------------------------------------------
        ///This Method is finely working and updation ui but we can't set different ui for different users in this case......

        if(user2 != null)
        {
                mMessageRef.child(path(user1.getUid(),user2)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(messageList.size() != 0) messageList.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                SingleMessage singleMessage = dataSnapshot1.getValue(SingleMessage.class);
                                assert singleMessage != null;
                                if(!singleMessage.getAvaibility().equals(user2))
                                messageList.add(singleMessage);
                            }
                            if(messageList.size()>0)
                            {
                                SingleMessage sm = messageList.get(messageList.size()-1);
                                String res = sm.getMessage_body();
                                if(sm.getMessage_body().length() >=30 ) res= res.substring(0,28) + "...";
                                mRootRef.child("chat_ref").child(user1.getUid()).child(user2).child("last_message").setValue(res);
                                mRootRef.child("chat_ref").child(user1.getUid()).child(user2).child("last_message_timestamp").setValue(sm.getTimestamp());
                            }else{

                            }



//------------------    Recycler view initialisation done here....
                            mChatList = findViewById(R.id.message_list);
                            mChatList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mAdapter = new MessageLIstAdapter(messageList,ChatActivity.this,user2);
                            mChatList.setAdapter(mAdapter);
                            Objects.requireNonNull(mChatList.getLayoutManager()).scrollToPosition(messageList.size()-1);
                            //  mAdapter.notifyDataSetChanged();

                            mChatList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                @Override
                                public void onLayoutChange(View v,
                                                           int left, int top, int right, int bottom,
                                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                    if (bottom < oldBottom) {
                                        mChatList.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(messageList.size()>0)
                                                {
                                                    mChatList.smoothScrollToPosition(
                                                            Objects.requireNonNull(messageList.size() - 1));
                                                }

                                            }
                                        }, 0);
                                    }
                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        }


        // This is just an alternate way of doing what we did above


        //--------------Working in  messages internal-----------------------------------------

        sendMessage = findViewById(R.id.send_message_btn);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String meassage_body = message.getText().toString().trim();
                ;
                String type = "text";
                String delivery_status = "waiting";
                final String sender = user1.getUid();
                final String receiver = user2;
                final long timestamp = System.currentTimeMillis();
                //   SingleMessage current_message = new SingleMessage(message_body,type,delivery_status,sender,receiver,timestamp);
                final Map<String, Object> messages = new HashMap<>();
                messages.put("message_body", meassage_body);
                messages.put("type", type);
                messages.put("sender", sender);
                messages.put("receiver", receiver);
                messages.put("avaibility","both");
                messages.put("delivery_status", delivery_status);
                messages.put("timestamp", timestamp);
                final String newMessageBody;
                if(meassage_body.length()>=30)  newMessageBody = meassage_body.substring(0,25)+"...";
                else newMessageBody = meassage_body;


                if (!meassage_body.equals("")) {

                        mMessageRef.child(res).child(String.valueOf(timestamp)).setValue(messages)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                        }
                                    }
                                });




                    message.setText("");
                    message.setHint("Enter message here...");

                }
            }
        });
        //////---------------------------------------------INTERNAL WORKING DONE------------------------------------------------------\\\\\\\\\\\\\\\

        mRootRef.child("Users").child(user2).keepSynced(true);
        mRootRef.child("Users").child(user2).child("online").keepSynced(false);
        mRootRef.child("Users").child(user2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String online = Objects.requireNonNull(dataSnapshot.child("online").getValue()).toString();
                String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String temp = Objects.requireNonNull(dataSnapshot.child("thumb_nail").getValue()).toString();

                user2name.setText(name);

                if(!temp.equals("default")) image=temp;



                Picasso.with(getApplicationContext())
                        .load(image)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.avtar)
                        .into(circleImageView);

                if (online.equals("true")) {

                    mLastSeenView.setText("Online");

                } else {

//                    GetTimeAgo getTimeAgo = new GetTimeAgo();
//
//                    long lastTime = Long.parseLong(online);
//
//                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
//
//                    mLastSeenView.setText(lastSeenTime);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View app_bar = inflater.inflate(R.layout.app_bar_layout, null);


        mRootRef.child("Users").child(user2 + "/" + "online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue().toString();

                if (s.equals("true")) {
                    mLastSeenView.setText("Online");
                } else {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String online = s;
                    long lastTime = Long.parseLong(online);
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
                    mLastSeenView.setText(lastSeenTime);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

   //----------------------------------------------------Menu Creation START--------------------------------------------
        @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
         super.onCreateOptionsMenu(menu1);

        getMenuInflater().inflate(R.menu.chat_menu, menu1);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId() == R.id.view_profile){
             Toast.makeText(this, "Wohoo", Toast.LENGTH_SHORT).show();
         }
         if(item.getItemId() == R.id.clear_chat)
         {




         }

        return true;
    }
  //-----------------------------------------MENU END-------------------------------------------------------------------
    public String path(String s1,String s2)
    {
        String res;
        if(s1.compareTo(s2)>0) res= s1+"/"+s2;
        else res= s2+"/"+s1;
        return res;
    }
}
