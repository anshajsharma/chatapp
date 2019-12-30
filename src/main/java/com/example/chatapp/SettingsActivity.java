package com.example.chatapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

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
    private Bitmap compressImageFile;
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

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String curr_uid = mCurrentUser.getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(curr_uid);
        mDatabaseRef.keepSynced(true);
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        // Changing profile picture using crop image api...
        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                     // Crop image activity api uses....
                      CropImage.activity()
                         .setAspectRatio(1,1)
                         .setGuidelines(CropImageView.Guidelines.ON)
                         .start(SettingsActivity.this);
             }}
             });

      //Status change using chane status button
        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                String user_curr_status;
                user_curr_status = user_status.getText().toString();
                Intent intent = new Intent(SettingsActivity.this, StatusChangeActivity.class);
                intent.putExtra("user_prev_status", user_curr_status);
                startActivity(intent);
            }
            }
        });

        // Showing all data of current user after retrieving from database
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                final String image= dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_nail").getValue().toString();

                display_name.setText(name);
                user_status.setText(status);
                mDatabaseRef.keepSynced(true);
               // Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avtar).into(circleImageView);

                if(!image.equals("default")) {

                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.avtar).into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avtar).into(circleImageView);

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Whole work of profile changing done here.. database... imageshow... links... all handled
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                final Uri resultUri2 = data.getData();


                // Time stampGenerator.... best way to get unique id....
                // Nothing can be better than this in uniqueness
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                //mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String curr_uid = mCurrentUser.getUid();

                final StorageReference filePath = mProfilePictures.child("profile_pictures").child(curr_uid).child("profile_picture.jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                       if(task.isSuccessful()){

                           filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   String profilePicUrl = uri.toString();
                                   File imageFile = new File(resultUri.getPath());
                                   try {
                                       compressImageFile = new Compressor(SettingsActivity.this)
                                               .setMaxHeight(200)
                                               .setMaxWidth(200)
                                               .setQuality(15)
                                               .compressToBitmap(imageFile);
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                                   //    Toast.makeText(SettingsActivity.this, profilePicUrl, Toast.LENGTH_SHORT).show();
                                   Log.i(TAG, "onSuccess: "+ profilePicUrl);


                              // Thumbnail==Profile Imahe Compression upload done here

                                   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                   compressImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                   byte[] thumbData = baos.toByteArray();

                                   final StorageReference thumbFileRef = mProfilePictures.child("profile_pictures").child(curr_uid).child("thumb_nail.jpg");

                                   UploadTask uploadTask = thumbFileRef.putBytes(thumbData);
                                   uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                       @Override
                                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                       }
                                   })
                                     .addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(SettingsActivity.this, "Thumb_image compression failed!", Toast.LENGTH_SHORT).show();
                                         }
                                     });




                                   mDatabaseRef.child("image").setValue(profilePicUrl);
                                   mDatabaseRef.keepSynced(true);
                               }
                           });
                           Toast.makeText(SettingsActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                       }
                       else{
                           Toast.makeText(SettingsActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();

                       }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
