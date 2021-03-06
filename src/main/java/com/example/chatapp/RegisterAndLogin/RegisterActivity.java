package com.example.chatapp.RegisterAndLogin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.UsersHomePage;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText mDisplayName;
    EditText mEmailID;
    EditText mPassword;
    CardView mRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDisplayName = findViewById(R.id.diplay_name);
        mEmailID = findViewById(R.id.EmailId);
        mAuth = FirebaseAuth.getInstance();
        mPassword = findViewById(R.id.Password);
        mRegister = findViewById(R.id.register);
        mProgressDialog = new ProgressDialog(this);




        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name =mDisplayName.getText().toString();
                String EmailID = mEmailID.getText().toString();
                String password = mPassword.getText().toString();
                if(!EmailID.equals("") && !password.equals("") && !display_name.equals(""))
                {
                    mProgressDialog.setTitle("Registering User");
                    mProgressDialog.setMessage("Please wait while we create your account !!");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    register_user(display_name,EmailID,password);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Recheck Details Entered!!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void register_user(final String DisplayName, String EmailID, String Password) {
        mAuth.createUserWithEmailAndPassword(EmailID,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = curr_user.getUid();

                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();

                    userMap.put("name",DisplayName);
                    userMap.put("status","Hey there I'm using Anshaj's Chat App!!");
                    userMap.put("image","default");
                    userMap.put("thumb_nail","default");
                    userMap.put("online", "true");
                    userMap.put("last_synced_location","Earth");
                    userMap.put("user_id",curr_user.getUid());
                    userMap.put("friend_list_visibility","visible");
                    userMap.put("online_visisbility","visible");
                    userMap.put("post_count","0");
                    userMap.put("email_id",curr_user.getEmail());
                    userMap.put("background_image","https://firebasestorage.googleapis.com/v0/b/chat-application-c97dd.appspot.com/o/bird-4706963_1280.jpg?alt=media&token=ca52ea77-ee2b-425c-8397-3fa7a6410972");

                    mDatabaseRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mProgressDialog.dismiss();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            assert deviceToken != null;
                            FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                    .child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(RegisterActivity.this, UsersHomePage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    });


                }
                else{
                    mProgressDialog.hide();
                    Toast.makeText(RegisterActivity.this, "Some Error Occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
