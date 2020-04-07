package com.example.chatapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.chatapp.RegisterAndLogin.LoginActivity;
import com.example.chatapp.User2RelatedActivities.User1FriendList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import com.onesignal.OneSignal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class UsersHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "UsersHomePage";
    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private SectionPagerAdapter mSectionPagerAdapter;
    DatabaseReference databaseReference;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    NavigationView navigationView;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_page);

        mAuth = FirebaseAuth.getInstance();

        //setting up fragments and  pagers in tabLayout
        mViewPager = findViewById(R.id.tabPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());  //SeactionPagerAdapter java class is used
                                                                  // Open that for deteils
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTablayout = findViewById(R.id.tab_layout);
        mTablayout.setupWithViewPager(mViewPager);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


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
                        Log.d(TAG, "token" + " " + token);


                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(UsersHomePage.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        if(currentUser!=null) {

            // OneSignal Initialization
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();

//            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//                @Override
//                public void idsAvailable(String userId, String registrationId) {
//                    databaseReference.child("notification_keys").child(currentUser.getUid()).child("notification_id").setValue(userId);
//                }
//            });
//
//            new SendNotification("Message 1","heading 1","fbd7962b-1075-4dda-995b-4b9d13440708");
//
//            OneSignal.clearOneSignalNotifications();



            //toolbar and drawer setup took place
            setUpToolBar();
            navigationView = findViewById(R.id.clickable_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    if (menuItem.getItemId() == R.id.db) {
                        // Toast.makeText(UsersHomePage.this, "Account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UsersHomePage.this, SettingsActivity.class);
                        startActivity(intent);
                    } else if (menuItem.getItemId() == R.id.fr) {
                        Intent intent = new Intent(UsersHomePage.this, User1FriendList.class);
                        startActivity(intent);
                    } else if (menuItem.getItemId() == R.id.no) {

                    } else if (menuItem.getItemId() == R.id.feed) {
                        Intent intent = new Intent(UsersHomePage.this, NewsFeed.class);
                        startActivity(intent);
                    } else if (menuItem.getItemId() == R.id.ui) {
                        Intent intent = new Intent(UsersHomePage.this, UICheckActivity.class);
                        startActivity(intent);
                    }
                    else if (menuItem.getItemId() == R.id.ff){
                        Intent intent = new Intent(UsersHomePage.this, FindFriend.class);
                        startActivity(intent);

                    }else if (menuItem.getItemId() == R.id.st) {
                        Intent intent = new Intent(UsersHomePage.this, SettingsActivity.class);
                        startActivity(intent);
                    } else if (menuItem.getItemId() == R.id.lo) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .child("device_token").setValue("NULL").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseAuth.getInstance().signOut();
                            }
                        });



                        OneSignal.setSubscription(false);
                        sendToStart();
                    }

                    return false;
                }
            });



            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null)
            {
                databaseReference.child("Users").child(currentUser.getUid()).keepSynced(true);
                databaseReference.child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                            };
                            Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                            // Log.i("asd", String.valueOf(map));
                            List FriendList = new ArrayList(map.keySet());
                            Log.i(TAG, "onDataChange: " + FriendList.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }else{
            sendToStart();
        }

    }


    public void setUpToolBar()
    {
        mDrawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            Toast.makeText(this, item+"clicked", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onOptionsItemSelected: " + item.toString()+"clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        Log.i(TAG, "onOptionsItemSelected: " + id +" clicked");
        return false;
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();

        if(curr_user==null)
        {
            sendToStart();

        }else{
            OneSignal.clearOneSignalNotifications();
            databaseReference.child("Users").child(curr_user.getUid()).child("online").setValue("true");
        }
    }
    @Override
    public void onStop() {
        super.onStop();

    }

    private void sendToStart() {
        Intent startIntent = new Intent(this, LoginActivity.class);
        startActivity(startIntent);
        finish();;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            OneSignal.clearOneSignalNotifications();
        }


    }

    //Menu got connected to appbar......................................................................\\

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu1) {
//         super.onCreateOptionsMenu(menu1);
//
//        getMenuInflater().inflate(R.menu.menu, menu1);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//         super.onOptionsItemSelected(item);
//
//         if(item.getItemId() == R.id.log_out){
//             FirebaseAuth.getInstance().signOut();
//             sendToStart();
//         }
//         if(item.getItemId() == R.id.account_settings)
//         {
//             Intent intent = new Intent(UsersHomePage.this,SettingsActivity.class);
//             startActivity(intent);
//
//         }
//        if(item.getItemId() == R.id.all_users)
//        {
//            Intent intent = new Intent(UsersHomePage.this,FindFriend.class);
//            startActivity(intent);
//        }
//
//        return true;
//    }
    //....................................................................................................\\




    // Return Current  Date and Time Stamp in sdf1 FORMAT  ------------->>>>
    public String currentDateAndTime() {

        Long tsLong = System.currentTimeMillis() / 1000;
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());   // 12-03-2019 15:16:17
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.getDefault());              //  03:12 PM
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM YYYY", Locale.getDefault());              //  03:12 am (whatsApp uses this)
        java.util.Date currenTimeZone = new java.util.Date((long) tsLong * 1000);
        String currentDate = DateFormat.getDateInstance().format(new Date());                             //Jun 27, 2017 1:17:32 PM
        String s = sdf.format(currenTimeZone), s0 = sdf2.format(currenTimeZone);
        Log.i(TAG, "currentDateAndTime: " + s + " " + s0 + " " + currentDate+"303   " + sdf3.format(currenTimeZone));
        return  currentDate + " " +sdf3.format(currenTimeZone);
    }
}
