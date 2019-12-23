package com.example.chatapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText mDisplayName;
    EditText mEmailID;
    EditText mPassword;
    Button mRegister;
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
                    mProgressDialog.setTitle("Regestring User");
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

                    mDatabaseRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mProgressDialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
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
