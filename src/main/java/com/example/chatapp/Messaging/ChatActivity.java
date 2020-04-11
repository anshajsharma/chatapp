package com.example.chatapp.Messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.SettingsActivity;
import com.example.chatapp.UiChechAndLearnings.LocationLearning;
import com.example.chatapp.User2RelatedActivities.User2ProfileActivity;
import com.example.chatapp.UsersHomePage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class ChatActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener {
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
    private ImageButton sendMessage,attach;
    private Context ctx;
    private LinearLayout back;
    SwipeRefreshLayout swipeRefreshLayout;
    private final int SENT_MESSAGE=0,RECEIVED_MESSAGE=1;
    private String res,fileType="",myUrl="";
    private Uri fileUri,pickedImage;
    private Bitmap compressImageFile;
    private static final int REQUEST_CODE = 1657;
    private GoogleApiClient googleApiClient;
    private Location location;
    private String newMessageId;

    EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user2 = getIntent().getStringExtra("user_id2");
        ActionBar actionBar = getSupportActionBar();
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        ctx = ChatActivity.this;

        mLastSeenView = findViewById(R.id.last_seen);
        user2name = findViewById(R.id.user2_name);

        circleImageView = findViewById(R.id.user2_profile_pic);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        message = findViewById(R.id.typed_message);
        mChatList = findViewById(R.id.message_list);
        back = findViewById(R.id.back_button);
        mMessageRef = mRootRef.child("Messages");
        mChatref = mRootRef.child("chat_ref");
        attach = findViewById(R.id.attach);
        messageList = new ArrayList<>();
         res=path(user2,user1.getUid());


        // Hide keyeboard  when activity opens...............
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // all type of images , pdfs etc can be sent in this way.........
        attachFilesAndSend();


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
                mMessageRef.child(path(user1.getUid(),user2)).orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(messageList.size() != 0) messageList.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                SingleMessage singleMessage = dataSnapshot1.getValue(SingleMessage.class);
                                assert singleMessage != null;
                                if(!singleMessage.getAvailability().equals(user2))
                                messageList.add(singleMessage);
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
                String type = "text";
                String delivery_status = "waiting";
                final String sender = user1.getUid();
                final String receiver = user2;

                //   SingleMessage current_message = new SingleMessage(message_body,type,delivery_status,sender,receiver,timestamp);

                final Map<String, Object> messages = new HashMap<>();
                messages.put("message_body", meassage_body);
                messages.put("type", type);
                messages.put("sender", sender);
                messages.put("receiver", receiver);
                messages.put("availability","both");
                newMessageId = mMessageRef.child("dfghjk").push().getKey();
                messages.put("messageID",newMessageId);
                messages.put("delivery_status", delivery_status);
                messages.put("timestamp", ServerValue.TIMESTAMP);
                final String newMessageBody;
                if(meassage_body.length()>=30)  newMessageBody = meassage_body.substring(0,25)+"...";
                else newMessageBody = meassage_body;


                if (!meassage_body.equals("")) {

                        mMessageRef.child(res).child(String.valueOf(newMessageId)).setValue(messages)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            DatabaseReference mNotRef = FirebaseDatabase.getInstance().getReference().child("notifications");
                                            mNotRef.child(user2);
                                            String notifId = mNotRef.push().getKey();
                                            Map<String,String> newNotif = new HashMap<>();
                                            newNotif.put("Type","Message");
                                            newNotif.put("receiver",user2);
                                            newNotif.put("notificationId",notifId);
                                            newNotif.put("sender",user1.getUid());
                                            mNotRef.child(user2).child(notifId).setValue(newNotif);



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
                    String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, getApplicationContext());
                    mLastSeenView.setText(lastSeenTime);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void attachFilesAndSend() {
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] options = new CharSequence[]{
                        "Images",
                        "PDF Files",
                        "Ms Word Files",
                        "Send Current Location"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select File Type");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i==0)
                        {
                           fileType = "image";
                           Intent intent = new Intent();
                           intent.setAction(Intent.ACTION_GET_CONTENT);
                           intent.setType("image/*");
                           startActivityForResult(Intent.createChooser(intent,"Select Image"),438);
                        }
                        if(i==1)
                        {
                           fileType = "pdf";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(Intent.createChooser(intent,"Select PDF file"),438);

                        }
                        if(i==2)
                        {
                           fileType = "docx";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
//                            intent.setType("file/*");                      //Select any type of file...
                            startActivityForResult(Intent.createChooser(intent,"Select MsWord file"),438);
                        }
                        if(i==3)
                        {
                            googleApiClient = new GoogleApiClient.Builder(ChatActivity.this)
                                    .addConnectionCallbacks(ChatActivity.this)
                                    .addOnConnectionFailedListener(ChatActivity.this)
                                    .addApi(LocationServices.API).build();
                            if(googleApiClient != null){
                                googleApiClient.connect();
                            }
                        }
                    }
                });
                builder.show();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;
        if(requestCode==438 && resultCode==RESULT_OK && data.getData()!=null)
        {
           fileUri = data.getData();
           if(fileType.equals("image"))
           {

               imageCompressorAndUploader(data.getData());


           }else if (fileType.equals("pdf")){

                sendPDF(data.getData());

           }else if (fileType.equals("docx")){

           }
        }
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            googleApiClient.connect();
        }
    }

    private void sendPDF(final Uri resultUri) {
        //private Bitmap compressImageFile; ---------- initialise it on top of class or activity
        //final Uri resultUri = result.getUri(); ----- initialise it in onActivityResult

        //    String filename = f.substring(f.lastIndexOf('/')+1 , f.lastIndexOf('.'));

        if (resultUri != null) {

            if (resultUri.getPath() != null) {


                final String rand = FirebaseDatabase.getInstance().getReference().child("asdf").push().getKey();
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + rand;

                String f =  resultUri.getPath();
                final String filename = f.substring(f.lastIndexOf('/')+1 , f.lastIndexOf('.'));



//                Log.i(TAG, "sendPDF12: " + resultUri.getPath() + " --- " + filename + " --- " + filename+ rand + ".pdf" );

                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("chat_docs").child(user1.getUid() + "--TO--" + user2).child(filename + ".pdf");
                final StorageReference filePath2 = FirebaseStorage.getInstance().getReference().child("chat_docs").child(user1.getUid() + "--TO--" + user2).child(filename+ " " + rand + ".pdf");
                FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String curr_uid = mCurrentUser.getUid();








                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL

//  ------------------------------WHEN FILE IS FOUND-------------------------------------
                        final DatabaseReference mDatabaseRef = mMessageRef.child(path(user1.getUid(), user2));

                        final UploadTask uploadTask = filePath2.putFile(resultUri);

                        final ProgressDialog mProgress = new ProgressDialog(ChatActivity.this);
                        mProgress.setTitle("Uploading...");


                        mProgress.setCancelable(false);

                        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadTask.cancel();
                            }
                        });

                        //  mProgress.setInverseBackgroundForced(setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY));
                        mProgress.show();

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        })
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    mProgress.dismiss();

                                                    final String meassage_body = message.getText().toString().trim();
                                                    String type = "pdf";
                                                    String delivery_status = "waiting";
                                                    final String sender = FirebaseAuth.getInstance().getUid();
                                                    final String receiver = user2;
                                                    //   SingleMessage current_message = new SingleMessage(message_body,type,delivery_status,sender,receiver,timestamp);
                                                    final Map<String, Object> messages = new HashMap<>();
                                                    messages.put("message_body", meassage_body);
                                                    messages.put("type", type);
                                                    messages.put("sender", sender);
                                                    messages.put("receiver", receiver);
                                                    messages.put("availability","both");
                                                    newMessageId = mMessageRef.child("dfghjk").push().getKey();
                                                    messages.put("messageID",newMessageId);
                                                    messages.put("delivery_status", delivery_status);
                                                    messages.put("timestamp", ServerValue.TIMESTAMP);
                                                    messages.put("file_url",uri.toString());
                                                    messages.put("downloaded","NO");
                                                    messages.put("file_name",filename+ " " + rand + ".pdf");

                                                    if (true) {

                                                        mMessageRef.child(res).child(String.valueOf(newMessageId)).setValue(messages)
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

                                        }
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getApplicationContext(), "Error in sending file!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                mProgress.setMessage("Uploaded: " + (int) progress + "%");
                                mProgress.setProgress((int) progress);

                            }
                        });


                        //  ------------------------------WHEN FILE IS FOUND-------------------------------------

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // File not found
                        //  ------------------------------WHEN FILE IS NOT FOUND-------------------------------------

                        final DatabaseReference mDatabaseRef = mMessageRef.child(path(user1.getUid(), user2));

                        final UploadTask uploadTask = filePath.putFile(resultUri);

                        final ProgressDialog mProgress = new ProgressDialog(ChatActivity.this);
                        mProgress.setTitle("Uploading...");


                        mProgress.setCancelable(false);

                        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadTask.cancel();
                            }
                        });

                        //  mProgress.setInverseBackgroundForced(setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY));
                        mProgress.show();

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        })
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    mProgress.dismiss();

                                                    final String meassage_body = message.getText().toString().trim();
                                                    String type = "pdf";
                                                    String delivery_status = "waiting";
                                                    final String sender = FirebaseAuth.getInstance().getUid();
                                                    final String receiver = user2;
                                                    //   SingleMessage current_message = new SingleMessage(message_body,type,delivery_status,sender,receiver,timestamp);
                                                    final Map<String, Object> messages = new HashMap<>();
                                                    messages.put("message_body", meassage_body);
                                                    messages.put("type", type);
                                                    messages.put("sender", sender);
                                                    messages.put("receiver", receiver);
                                                    newMessageId = mMessageRef.child("dfghjk").push().getKey();
                                                    messages.put("messageID",newMessageId);
                                                    messages.put("availability","both");
                                                    messages.put("delivery_status", delivery_status);
                                                    messages.put("timestamp", ServerValue.TIMESTAMP);
                                                    messages.put("file_url",uri.toString());
                                                    messages.put("downloaded","NO");
                                                    messages.put("file_name",filename + ".pdf");

                                                    if (true) {

                                                        mMessageRef.child(res).child(String.valueOf(newMessageId)).setValue(messages)
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

                                        }
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getApplicationContext(), "Error in sending file!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                mProgress.setMessage("Uploaded: " + (int) progress + "%");
                                mProgress.setProgress((int) progress);

                            }
                        });




                        //  ------------------------------WHEN FILE IS NOT FOUND-------------------------------------
                    }
                });






            }
        }
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

             mRootRef.child("Messages").child(path(user1.getUid(),user2)).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(dataSnapshot.exists()){
                         for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                             final SingleMessage message = dataSnapshot1.getValue(SingleMessage.class);
                             mRootRef.child("Messages").child(path(user1.getUid(),user2)).child(String.valueOf(message.getMessageID()))
                                     .addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                             if(dataSnapshot.exists())
                                             {

                                                 if(dataSnapshot.child("availability").getValue(String.class).equals(user1))
                                                 {
                                                     mRootRef.child("Messages").child(path(user1.getUid(),user2)).child(String.valueOf(message.getMessageID()))
                                                             .removeValue();
                                                 }
                                                 else{
                                                     mRootRef.child("Messages").child(path(user1.getUid(),user2)).child(String.valueOf(message.getMessageID()))
                                                             .child("availability").setValue(user2);
                                                 }

                                             }


                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     });
                         }
                         Toast.makeText(ctx, "Chat Cleared!", Toast.LENGTH_SHORT).show();

                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

         }
         if(item.getItemId() == R.id.audio_call){
             mRootRef.child("Users").child(user2).addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(dataSnapshot.hasChild("phoneNumber")){
                         String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                         callUser(phoneNumber);
                     }else{
                         Toast.makeText(ctx, "Phone number not provided", Toast.LENGTH_SHORT).show();
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
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
    private void imageCompressorAndUploader(final Uri resultUri) {

        //private Bitmap compressImageFile; ---------- initialise it on top of class or activity
        //final Uri resultUri = result.getUri(); ----- initialise it in onActivityResult

    //    String filename = f.substring(f.lastIndexOf('/')+1 , f.lastIndexOf('.'));

        if (resultUri != null) {


            // Time stampGenerator.... best way to get unique id....
            // Nothing can be better than this in uniqueness
            String rand = FirebaseDatabase.getInstance().getReference().child("asdf").push().getKey();
            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + rand;

            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("chat_images").child(user1.getUid() + "--TO--" + user2).child(timeStamp + ".jpg");

            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            final String curr_uid = mCurrentUser.getUid();

            final DatabaseReference mDatabaseRef = mMessageRef.child(path(user1.getUid(), user2));


            if (resultUri.getPath() != null) {

                File imageFile = new File(getRealPathFromURI_API19(ChatActivity.this,resultUri));
                Log.i(TAG, "imageCompressorAndUploader12: " + getRealPathFromURI_API19(ChatActivity.this,resultUri));
                try {
                    compressImageFile = new Compressor(getApplicationContext())
                            .setMaxHeight(300)
                            .setMaxWidth(300)
                            .setQuality(25)
                            .compressToBitmap(imageFile);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumbData = baos.toByteArray();

                final UploadTask uploadTask = filePath.putBytes(thumbData);
//              final UploadTask  uploadTask = filePath.putFile(resultUri);

                final ProgressDialog mProgress = new ProgressDialog(ChatActivity.this);
                mProgress.setTitle("Uploading...");


                mProgress.setCancelable(false);

                mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadTask.cancel();
                    }
                });

                //  mProgress.setInverseBackgroundForced(setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY));
                mProgress.show();

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mProgress.dismiss();

                                            final String meassage_body = message.getText().toString().trim();
                                            String type = "image";
                                            String delivery_status = "waiting";
                                            final String sender = FirebaseAuth.getInstance().getUid();
                                            final String receiver = user2;
                                            //   SingleMessage current_message = new SingleMessage(message_body,type,delivery_status,sender,receiver,timestamp);
                                            final Map<String, Object> messages = new HashMap<>();
                                            messages.put("message_body", meassage_body);
                                            messages.put("type", type);
                                            messages.put("sender", sender);
                                            messages.put("receiver", receiver);
                                            messages.put("availability","both");
                                            newMessageId = mMessageRef.child("dfghjk").push().getKey();
                                            messages.put("messageID",newMessageId);
                                            messages.put("delivery_status", delivery_status);
                                            messages.put("timestamp", ServerValue.TIMESTAMP);
                                            messages.put("image_url",uri.toString());
                                            messages.put("downloaded","NO");

                                            if (true) {

                                                mMessageRef.child(res).child(String.valueOf(newMessageId)).setValue(messages)
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

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error in sending file!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        mProgress.setMessage("Uploaded: " + (int) progress + "%");
                        mProgress.setProgress((int) progress);

                    }
                });;

                } catch (IOException e) {
                    Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    //     Log.i(TAG, "imageCompressorAndUploader12: "+ "cisdgbihujdksffhsdnjlcvj" + "5641+652+625");

                    e.printStackTrace();
                }



                // mDatabaseRef.child("image").setValue(profilePicUrl);
//                mDatabaseRef.keepSynced(true);
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("NewApi")
    public  String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: " + "We are connected to user location");
        showTheUserLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + "Connection is suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionSuspended: " + "Connection Failed");
        if(connectionResult.hasResolution()){
            try {
                connectionResult.startResolutionForResult(ChatActivity.this,REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                Log.d(TAG,  e.getStackTrace().toString());
            }
        }else{
            Toast.makeText(this, "Goole Play Serve Not Workin", Toast.LENGTH_LONG).show();
//            finish();
        }
    }
    // Custom methods.....
    private void showTheUserLocation() {
        int permissinCheck = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(permissinCheck == PackageManager.PERMISSION_GRANTED){
            FusedLocationProviderApi fusedLocationProviderApi =
                    LocationServices.FusedLocationApi;
            location = fusedLocationProviderApi.getLastLocation(googleApiClient);

            if (location != null) {
                final double lat = location.getLatitude();
                final double lng = location.getLongitude();
                String location = lat+ ", " + lng;
//                String LocationLink = "https://www.google.com/maps?q="+location;

                mRootRef.child("Users").child(user2).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String user2Name = dataSnapshot.child("name").getValue(String.class);

                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + user2Name + "\'s Shared Location" + ")";
                            final Map<String, Object> messages = new HashMap<>();
                            messages.put("message_body", "Open Location");
                            messages.put("type", "Location");
                            messages.put("sender", FirebaseAuth.getInstance().getUid());
                            messages.put("receiver", user2);
                            messages.put("availability","both");
                            newMessageId = mMessageRef.child("dfghjk").push().getKey();
                            messages.put("messageID",newMessageId);
                            messages.put("delivery_status", "waiting");
                            messages.put("timestamp", ServerValue.TIMESTAMP);
                            messages.put("location_URL",geoUri);
                            mMessageRef.child(res).child(String.valueOf(newMessageId)).setValue(messages)
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




//                String Link = "<a href=\""+ LocationLink +"\">Go to Location</a>";

//                Log.d(TAG, "showTheUserLocation: " + Link);



            }else{
                Toast.makeText(ctx, "This App is not able to access location", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(ctx, "This App is Not Allowed to Access Location", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(ChatActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},185);

        }
    }
    private void callUser(String phoneNumber){

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber ));
                    ctx.startActivity(intent);
    }
}
