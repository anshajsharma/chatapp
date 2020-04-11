package com.example.chatapp.RegisterAndLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.UsersHomePage;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText mEmailId;
    EditText mPassword;
    CardView mSignIn;
    private FirebaseAuth mAuth;
    TextView mButtom;
   // Button signIn;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailId=findViewById(R.id.login_email_id);
        mPassword = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
        mSignIn = findViewById(R.id.sign_in);
        mProgressDialog = new ProgressDialog(this);

        mButtom=findViewById(R.id.new_account);
      //  signIn = findViewById(R.id.sign_in);
        mButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(reg_intent);
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailId=mEmailId.getText().toString();
                String password = mPassword.getText().toString();
                if( !emailId.equals("") && !password.equals("") )
                {
                    mProgressDialog.setTitle("Logging In");
                    mProgressDialog.setMessage("Please Wait while we check your credentials !!");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    loginUser(emailId,password);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error in Sign In !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginUser(String emailId, String password) {
        mAuth.signInWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mProgressDialog.dismiss();

                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            if( deviceToken != null){
                                FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                        .child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(LoginActivity.this, UsersHomePage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }else{
                                Intent intent = new Intent(LoginActivity.this, UsersHomePage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else{
                            mProgressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Incorrect EmailID or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
