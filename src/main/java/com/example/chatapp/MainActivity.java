package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
        if(currentUser==null)
        {
            sendToStart();


        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(this,StartActivity.class);
        startActivity(startIntent);
        finish();;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
         super.onCreateOptionsMenu(menu1);

        getMenuInflater().inflate(R.menu.menu, menu1);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId() == R.id.log_out){
             FirebaseAuth.getInstance().signOut();
             sendToStart();
         }

        return true;
    }
}
