package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    private DatabaseReference mDatabaseRef;
    private FirebaseUser mCurrentUser;

    private CircleImageView circleImageView;
    private TextView display_name;
    private Button change_status;
    private ImageView imageView;
    private Button change_profile;
    private TextView user_status;
    final int PICK_IMAGE=1;
    private StorageReference mProfilePictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        circleImageView = findViewById(R.id.profile_picture);
        display_name = findViewById(R.id.display_name);
        user_status = findViewById(R.id.curr_status);
        change_status = findViewById(R.id.change_user_status);
        change_profile = findViewById(R.id.change_user_picture);





        mProfilePictures = FirebaseStorage.getInstance().getReference();

        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            // Crop image activity api uses....
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
            }
        });

        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_curr_status;
                user_curr_status=user_status.getText().toString();
                Intent intent = new Intent(SettingsActivity.this,StatusChangeActivity.class);
                intent.putExtra("user_prev_status",user_curr_status);
                startActivity(intent);
                finish();
            }
        });

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String curr_uid = mCurrentUser.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(curr_uid);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image= dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_nail").getValue().toString();

                display_name.setText(name);
                user_status.setText(status);
               // circleImageView.set



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final StorageReference filePath = mProfilePictures.child("profile_pictures").child(curr_uid).child("profile_picture.jpg");
        Log.i(TAG, "onActivityResult: "+filePath.toString());
        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Picasso image handler is used....
                    Picasso.get()
                        .load(uri)
                        .into(circleImageView);
            }
        })
          .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(SettingsActivity.this, "Failed to load current user's profile pic... Add a new one!", Toast.LENGTH_SHORT).show();
              }
          }) ;




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                // Time stampGenerator.... best way to get unique id....
                // Nothing can be better than this in uniqueness
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String curr_uid = mCurrentUser.getUid();

                final StorageReference filePath = mProfilePictures.child("profile_pictures").child(curr_uid).child("profile_picture.jpg");
                Log.i(TAG, "onActivityResult: "+filePath.toString());

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                       if(task.isSuccessful()){


                           Toast.makeText(SettingsActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                       }
                       else{
                           Toast.makeText(SettingsActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();

                       }
                    }
                });
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String profilePicUrl = uri.toString();
                        Toast.makeText(SettingsActivity.this, profilePicUrl, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onSuccess: "+ profilePicUrl);

                        mDatabaseRef.child("image").setValue(profilePicUrl);
                    }
                });

                //Picasso image handler is used....
                Picasso.get()
                        .load(resultUri)
                        .into(circleImageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

   // Generetes random string
    protected String randomStringGenerator() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
