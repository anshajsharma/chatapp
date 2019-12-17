package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusChangeActivity extends AppCompatActivity {

    private EditText status;
    private Button changeStatus;
    private DatabaseReference mDatabase;
    private FirebaseUser curr_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_change);
        status = findViewById(R.id.new_status);
        changeStatus = findViewById(R.id.change_status);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        curr_user = FirebaseAuth.getInstance().getCurrentUser();
        String prev_status = getIntent().getStringExtra("user_prev_status");

        status.setText(prev_status);

        final String userId = curr_user.getUid();

        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStatus = status.getText().toString();
                if(!newStatus.equals("")){
                    mDatabase.child("Users").child(userId).child("status").setValue(newStatus)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(StatusChangeActivity.this,SettingsActivity.class);
                                    startActivity(intent);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StatusChangeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(StatusChangeActivity.this, "Enter something in status!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
