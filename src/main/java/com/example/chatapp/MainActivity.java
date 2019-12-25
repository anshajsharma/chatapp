package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private SectionPagerAdapter mSectionPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mViewPager = findViewById(R.id.tabPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTablayout = findViewById(R.id.tab_layout);
        mTablayout.setupWithViewPager(mViewPager);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, "asdfgh" + msg);
                      //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

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
        Intent startIntent = new Intent(this,LoginActivity.class);
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
         if(item.getItemId() == R.id.account_settings)
         {
             Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
             startActivity(intent);

         }
        if(item.getItemId() == R.id.all_users)
        {
            Intent intent = new Intent(MainActivity.this,AllUsersActivity.class);
            startActivity(intent);
        }

        return true;
    }
    public static class MyFirebaseMessagingService extends FirebaseMessagingService {

        @Override
        public void onNewToken(String s) {
            super.onNewToken(s);
            Log.e("newToken", s);
            getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
        }

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
        }

        public static String getToken(Context context) {
            return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
        }
    }
}
