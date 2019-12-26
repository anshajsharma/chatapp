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

import com.example.chatapp.RegisterAndLogin.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class UsersHomePage extends AppCompatActivity {
    private static final String TAG = "UsersHomePage";
    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private SectionPagerAdapter mSectionPagerAdapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_page);

        mAuth = FirebaseAuth.getInstance();

        //setting up fragments and  pagers in tabLayout
        mViewPager = findViewById(R.id.tabPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTablayout = findViewById(R.id.tab_layout);
        mTablayout.setupWithViewPager(mViewPager);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        databaseReference.child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                    Log.i("asd", String.valueOf(map));

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
                      //  Toast.makeText(UsersHomePage.this, msg, Toast.LENGTH_SHORT).show();
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

        }else{
            databaseReference.child("Users").child(currentUser.getUid()).child("online").setValue("true");
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);

        if(currentUser!=null)
        {
            databaseReference.child("Users").child(currentUser.getUid()).child("online").setValue(currentDateAndTime());
        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(this, LoginActivity.class);
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
             Intent intent = new Intent(UsersHomePage.this,SettingsActivity.class);
             startActivity(intent);

         }
        if(item.getItemId() == R.id.all_users)
        {
            Intent intent = new Intent(UsersHomePage.this,AllUsersActivity.class);
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

    // Return Current  Date and Time Stamp in sdf1 FORMAT  ------------->>>>
    public String currentDateAndTime() {

        Long tsLong = System.currentTimeMillis() / 1000;
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());   // 12-03-2019 15:16:17
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.getDefault());              //  03:12 PM
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.getDefault());              //  03:12 am (whatsApp uses this)
        java.util.Date currenTimeZone = new java.util.Date((long) tsLong * 1000);
        String currentDate = DateFormat.getDateInstance().format(new Date());                             //Jun 27, 2017 1:17:32 PM
        String s = sdf.format(currenTimeZone), s0 = sdf2.format(currenTimeZone);
        Log.i(TAG, "currentDateAndTime: " + s + " " + s0 + " " + currentDate);
        return  currentDate + " " +sdf2.format(currenTimeZone) ;
    }
}
