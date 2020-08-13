package com.example.chatapp.User2RelatedActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.GoSocial.PostAdapter;
import com.example.chatapp.GoSocial.Posts;
import com.example.chatapp.R;
import com.example.chatapp.UsersHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

import de.hdodenhof.circleimageview.CircleImageView;

public class User2ProfileActivity extends AppCompatActivity {
    private static final String TAG = "User2ProfileActivity";
    private DatabaseReference mDatabaseRef, mFriendRequestRef, mFriendsRef;
    private TextView display_name, friends, status,post_count;
    private CircleImageView profile_pic;
    private ProgressDialog mProgressDialog;
    private Button friendRequest, cancelFriendRequest;
    private String user2;
    private FirebaseUser user1;
    DatabaseReference up;
    private int curr_state;
    private RecyclerView recView;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference mRootRef;
    List<Posts> PostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        user2 = getIntent().getStringExtra("user_id2");

        Log.i(TAG, "onClick22 " + user2);
        //int a = getCurrent_state();
        display_name = findViewById(R.id.profile_display_name);
        friends = findViewById(R.id.friends_count);
        status = findViewById(R.id.currr_status);
        friendRequest = findViewById(R.id.send_friend_request);
        cancelFriendRequest = findViewById(R.id.decline_friend_request);
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        profile_pic = findViewById(R.id.pro_pic);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user2);
        mFriendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        mFriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        post_count = findViewById(R.id.post_count);

        cancelFriendRequest.setVisibility(View.GONE);
        PostList = new ArrayList<>();


        recView = findViewById(R.id.recView);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("post_ref").child(user2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange1234: " + 1);
                if(dataSnapshot.exists()){
                    post_count.setText("POSTS: "+dataSnapshot.getChildrenCount());
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Log.i(TAG, "onDataChange1234: " + 2);
                        String postId= dataSnapshot1.getValue(String.class);
                        Log.i(TAG, "onDataChange1234: " + "  POSTID  " + postId );
                        mRootRef.child("posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                if(dataSnapshot3.exists()){
                                    Log.i(TAG, "onDataChange1234: " + 3);
                                    Posts post = dataSnapshot3.getValue(Posts.class);
                                    PostList.add(post);
                                    Log.i(TAG, "onDataChange1234: "+post.getPost_id());
                                    LinearLayoutManager mLayoutManager;
                                    mLayoutManager = new LinearLayoutManager(User2ProfileActivity.this);
                                    mLayoutManager.setReverseLayout(true);
                                    mLayoutManager.setStackFromEnd(true);
                                    // And set it to RecyclerView
                                    recView.setLayoutManager(mLayoutManager);
                                    mAdapter = new PostAdapter(PostList,User2ProfileActivity.this );
                                    recView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
                    post_count.setText("POST:"+0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        //Progress dialog initialisation
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        ///User2FriendView
        ((TextView)findViewById(R.id.friends_count)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User2ProfileActivity.this , User2FriendView.class );
                intent.putExtra("user2" , user2);
                startActivity(intent);
            }
        });


        // Determining current situation between user1 and user2
        mFriendRequestRef.child(user1.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user2)) {
                    String type = dataSnapshot.child(user2).getValue().toString();

                    Log.i(TAG, "onDataChange: " + type + " " + user1.getUid());
                    Log.i(TAG, "onDataChange: " + type + " " + user2);
                    switch (type) {
                        case "sent":
                            friendRequest.setText("Cancel Friend Request");
                            cancelFriendRequest.setVisibility(View.GONE);
                            curr_state = 1;
                            break;
                        case "received":
                            friendRequest.setText("Accept Friend Request");
                            cancelFriendRequest.setVisibility(View.VISIBLE);
                            curr_state = 2;
                            break;
                        case "Not Friend Now":
                        case "Friend request cancelled":
                            friendRequest.setText("Send Friend Request");
                            cancelFriendRequest.setVisibility(View.GONE);
                            curr_state = 4;
                            break;
                        default:
                            friendRequest.setText("Unfriend");
                            cancelFriendRequest.setVisibility(View.GONE);
                            curr_state = 3;
                            break;
                    }

                }

                //All data loaded so we dismissed dialog box
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

         getTotalAndMutualFriendsCount(user1.getUid(),user2);
        //Displaying User2 profile
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name, image, user_status;
                name = dataSnapshot.child("name").getValue().toString();
                image = dataSnapshot.child("image").getValue().toString();
                user_status = dataSnapshot.child("status").getValue().toString();
                Log.i(TAG, "onClick22 " + user2 + image);

                display_name.setText(name);
                status.setText(user_status);
                Picasso.with(User2ProfileActivity.this)
                        .load(image)
                        .placeholder(R.drawable.avtar)
                        .fit()
                        .into(profile_pic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Send Friend Request , Accept Friend Request , Cancel Friend Request , Unfriend .... Button Click tasks
        friendRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // user1 sent friend request to user2
                if (curr_state == 0 || curr_state == 4) {

                    mFriendRequestRef.child(user1.getUid()).child(user2)
                            .setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mFriendRequestRef.child(user2).child(user1.getUid())
                                                .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(User2ProfileActivity.this, "Friend request sent...", Toast.LENGTH_SHORT).show();
                                                friendRequest.setText("Cancel Friend Request");

                                                DatabaseReference mNotRef = FirebaseDatabase.getInstance().getReference().child("notifications");
                                                mNotRef.child(user2);
                                                String notifId = mNotRef.push().getKey();
                                                Map<String,String> newNotif = new HashMap<>();
                                                newNotif.put("Type","Request");
                                                newNotif.put("receiver",user2);
                                                newNotif.put("notificationId",notifId);
                                                newNotif.put("sender",user1.getUid());
                                                mNotRef.child(user2).child(notifId).setValue(newNotif);

                                                curr_state = 1;
                                            }
                                        });
                                    } else {
                                        Toast.makeText(User2ProfileActivity.this, "Failed in sending friend request..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }

                // user1 cancelled friend request sent to user2
                else if (curr_state == 1) {

                    mFriendRequestRef.child(user1.getUid()).child(user2).setValue("Friend request cancelled")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {



                                        mFriendRequestRef.child(user2).child(user1.getUid()).setValue("Friend request cancelled")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(User2ProfileActivity.this, "Friend request cancelled...", Toast.LENGTH_SHORT).show();
                                                        friendRequest.setText("Send Friend Request");
                                                        curr_state = 0;
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(User2ProfileActivity.this, "Failed in cancelling friend request..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                // User1 has choice of Accepting and Declining the friend Request of user 2 :::: user1 accepted
                else if (curr_state == 2) {

                    mFriendRequestRef.child(user1.getUid()).child(user2)
                            .setValue("friends")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mFriendRequestRef.child(user2).child(user1.getUid())
                                                .setValue("friends").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(User2ProfileActivity.this, "Friend request accepted...", Toast.LENGTH_SHORT).show();
                                                friendRequest.setText("Unfriend");
                                                mFriendsRef.child(user1.getUid()).child(user2).setValue("friends");
                                                mFriendsRef.child(user2).child(user1.getUid()).setValue("friends").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        DatabaseReference mNotRef = FirebaseDatabase.getInstance().getReference().child("notifications");
                                                        mNotRef.child(user2);
                                                        String notifId = mNotRef.push().getKey();
                                                        Map<String,String> newNotif = new HashMap<>();
                                                        newNotif.put("Type","Request Accepted");
                                                        newNotif.put("receiver",user2);
                                                        newNotif.put("notificationId",notifId);
                                                        newNotif.put("sender",user1.getUid());
                                                        mNotRef.child(user2).child(notifId).setValue(newNotif);
                                                    }
                                                });
                                                cancelFriendRequest.setVisibility(View.GONE);
                                                curr_state = 3;
                                            }
                                        });
                                    } else {
                                        Toast.makeText(User2ProfileActivity.this, "Error in accepting friend request..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                // user1 removed user2 from friend list
                else if (curr_state == 3) {

                    mFriendRequestRef.child(user1.getUid()).child(user2).setValue("Not Friend Now")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mFriendRequestRef.child(user2).child(user1.getUid()).setValue("Not Friend Now")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(User2ProfileActivity.this, "Done...", Toast.LENGTH_SHORT).show();
                                                        friendRequest.setText("Send Friend Request");
                                                        mFriendsRef.child(user1.getUid()).child(user2).removeValue();
                                                        mFriendsRef.child(user2).child(user1.getUid()).removeValue();
                                                        curr_state = 0;
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(User2ProfileActivity.this, "Some error occurred ..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
        //----------------------------------------------------------------------------

        // User1 has choice of Accepting and Declining the friend Request of user 2 :::: user1 declined
        cancelFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFriendRequestRef.child(user1.getUid()).child(user2).setValue("Not Friend Now")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    mFriendRequestRef.child(user2).child(user1.getUid()).setValue("Not Friend Now")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(User2ProfileActivity.this, "Done...", Toast.LENGTH_SHORT).show();
                                                    friendRequest.setText("Send Friend Request");
                                                    mFriendsRef.child(user1.getUid()).child(user2).removeValue();
                                                    mFriendsRef.child(user2).child(user1.getUid()).removeValue();
                                                    curr_state = 0;

                                                    cancelFriendRequest.setVisibility(View.GONE);
                                                }
                                            });

                                } else {
                                    Toast.makeText(User2ProfileActivity.this, "Some error occurred ..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //-----------------------------------------------------------------------------------------------------------------\\


    }

    @Override
    public void onBackPressed() {

        if(isTaskRoot()){
            //// This is last activity
            startActivity(new Intent(this, UsersHomePage.class));
            finish();
        }else{
            super.onBackPressed();
        }


    }

    // Return Current  Date and Time Stamp in sdf1 FORMAT  ------------->>>>
    public String currentDateAndTime() {

        Long tsLong = System.currentTimeMillis() / 1000;
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());   // 12-03-2019 15:16:17
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.getDefault());              //  03:12 am (whatsApp uses this)
        java.util.Date currenTimeZone = new java.util.Date((long) tsLong * 1000);
        String currentDate = DateFormat.getDateInstance().format(new Date());                             //Jun 27, 2017 1:17:32 PM
        String s = sdf.format(currenTimeZone), s0 = sdf2.format(currenTimeZone);
        Log.i(TAG, "currentDateAndTime: " + s + " " + s0 + " " + currentDate);
        return sdf.format(currenTimeZone);
    }

     ArrayList<String> user1Friendlist , user2Friendlist;

    public void getTotalAndMutualFriendsCount(String user_1 , String user_2)
    {
        DatabaseReference user1FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(user_1);
        final DatabaseReference user2FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(user_2);

         String user1 = user_1 ;
       final  String user2 = user_2 ;



            user1FriendsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );

                        final long user1FriendsCount ,user2FriendsCount;
                        // FriendList.clear();
                        assert map != null;
                        user1Friendlist = new ArrayList(map.keySet());
                        user1FriendsCount = user1Friendlist.size();

                        user2FriendsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()) {
                                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                    };
                                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                                    assert map != null;
                                    user2Friendlist = new ArrayList(map.keySet());

                                    long user2FriendsCount =   user2Friendlist.size();


                                    user1Friendlist.retainAll(user2Friendlist);



                                      int mutual = user1Friendlist.size();
                                      String s =String.valueOf(user2FriendsCount)+" Friends | "+String.valueOf(mutual) + " Mutual Friends";

                                    ((TextView)findViewById(R.id.friends_count)).setText(s ); ;




                                }
                                else{
                                    String s="0 Friends | 0 Mutual Friends";
                                    ((TextView)findViewById(R.id.friends_count)).setText(s );

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }else{

                        user2FriendsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()) {
                                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                    };
                                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                                    assert map != null;
                                    user2Friendlist = new ArrayList(map.keySet());

                                    long user2FriendsCount =   user2Friendlist.size();
                                    String s=String.valueOf(user2FriendsCount) + " Friends | " + "0 Mutual Friends";

                                    ((TextView)findViewById(R.id.friends_count)).setText( s);

                                }
                                else{
                                    String s ="0 Friends | 0 Mutual Friends";
                                    ((TextView)findViewById(R.id.friends_count)).setText(s );
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

       // findViewById(R.id.) "0 Friends | " + "0 Mutual Friends" ;
    }

//    public int getCurrent_state()
//    {
//        final int[] curr_state = new int[1];
//
//        // Determining current situation between user1 and user2
//       // DatabaseReference myFriendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
//        mFriendRequestRef.child(user1.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(user2)){
//                    String type = dataSnapshot.child(user2).getValue().toString();
//                    if(type.equals("sent")){
//                        curr_state[0] =1;
//                    }
//                    else if(type.equals("received")) {
//                        curr_state[0] = 2;
//                    }
//                    else if(type.equals("friends")) {
//
//                        curr_state[0] =3;
//                    }
//                    else{
//
//                        curr_state[0] =0;
//                    }
//
//                }
//                //All data loaded so we dismissed dialog box
//                mProgressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//        Log.i(TAG, "getCurrent_state: " + curr_state[0] );
//        return curr_state[0];
//    };

}
