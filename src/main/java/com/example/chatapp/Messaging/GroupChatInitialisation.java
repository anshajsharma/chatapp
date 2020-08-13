package com.example.chatapp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.RegisterAndLogin.Users;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatInitialisation extends AppCompatActivity {

    private EditText groupName,groupdescription,FirstMessage;
    private CircleImageView groupIcon;
    private Button create;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference mRootRef;
    private String currUser;
    private String availableTo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_initialisation);
                Bundle args = getIntent().getBundleExtra("BUNDLE");
//                ArrayList<Users> groupMembers = (ArrayList<Users>) args.getSerializable("ARRAYLIST");
          List<Users> groupMembers = new ArrayList<Users>();
               groupMembers   = (ArrayList<Users>) getIntent().getSerializableExtra("userList");
                groupName = findViewById(R.id.groupName);
                groupdescription = findViewById(R.id.groupDescription);
                mRootRef = FirebaseDatabase.getInstance().getReference();
                groupIcon = findViewById(R.id.group_icon);
                create = findViewById(R.id.create);
                recyclerView = findViewById(R.id.groupChatMembers);
                FirstMessage = findViewById(R.id.firstMessage);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter = new FriendlistAdapterGroupAdd(groupMembers, GroupChatInitialisation.this);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                currUser = FirebaseAuth.getInstance().getUid();
        final Map<String,String> userDetails = new HashMap<>();
        userDetails.put(currUser,"Admin"); availableTo+=currUser;
        for (Users users:groupMembers){

                userDetails.put(users.getUser_id(),"Not Admin");
                availableTo+=users.getUser_id();

        }

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String groupname = groupName.getText().toString().trim();
                        final String groupId = mRootRef.child("sdfg").push().getKey();
                        final String firstMessage = FirstMessage.getText().toString().trim();
                        HashMap<String,Object> groupDetails = new HashMap<>();
                        groupDetails.put("createdAt", ServerValue.TIMESTAMP);
                        groupDetails.put("createdBy", currUser);
                        groupDetails.put("groupId",groupId);
                        groupDetails.put("groupIcon","default");
                        groupDetails.put("groupName",groupname);
                        groupDetails.put("groupDescription",groupdescription.getText().toString().trim());
                        groupDetails.put("usersDetails",userDetails);

                        if(!groupname.equals("") && !firstMessage.equals("")){
                            mRootRef.child("Groups").child(groupId).setValue(groupDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    final Map<String, Object> messages = new HashMap<>();
                                    messages.put("message_body", firstMessage);
                                    messages.put("type", "text");
                                    messages.put("sender", currUser);
                                    messages.put("availability","All");
                                    String newMessageId = mRootRef.child("dfghjk").push().getKey();
                                    messages.put("messageID",newMessageId);
                                    messages.put("delivery_status", "pending");
                                    messages.put("timestamp", ServerValue.TIMESTAMP);
                                    // using for-each loop for iteration over Map.entrySet()

                                    mRootRef.child("GroupMessages").child(groupId).child(newMessageId).setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            int temp=0;
                                            for (String userId:userDetails.keySet()){
                                                final Map<String, Object> lastmessages = new HashMap<>();
                                                lastmessages.put("last_message",firstMessage);
                                                lastmessages.put("last_message_timestamp",ServerValue.TIMESTAMP);
                                                lastmessages.put("type","Group");
                                                temp++;
                                                final int finalTemp = temp;
                                                mRootRef.child("chat_ref").child(userId).child(groupId).setValue(lastmessages).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        if(finalTemp==userDetails.size()){
                                                            Toast.makeText(GroupChatInitialisation.this, "Created!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GroupChatInitialisation.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(GroupChatInitialisation.this, "Groupname & First Message reqiured..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}
