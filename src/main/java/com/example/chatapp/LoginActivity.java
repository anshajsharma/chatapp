package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText mEmailId;
    EditText mPassword;
    Button mSignIn;
    private FirebaseAuth mAuth;

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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            mProgressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Incorrect EmailID or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
