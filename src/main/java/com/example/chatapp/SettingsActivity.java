package com.example.chatapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.RegisterAndLogin.StatusChangeActivity;
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
import com.google.firebase.storage.OnProgressListener;
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
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                final Uri resultUri = result.getUri();

                imageCompressorAndUploader(resultUri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void imageCompressorAndUploader(final Uri resultUri) {

        //private Bitmap compressImageFile; ---------- initialise it on top of class or activity
        //final Uri resultUri = data.getData(); ----- initialise it in onActivityResult

        if(resultUri!=null) {


            // Time stampGenerator.... best way to get unique id....
            // Nothing can be better than this in uniqueness
            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));


            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            final String curr_uid = mCurrentUser.getUid();

            final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(curr_uid);

            final StorageReference filePath = mProfilePictures.child("profile_pictures").child(curr_uid).child("profile_picture.jpg");

            final UploadTask uploadTask = filePath.putFile(resultUri);
//              final UploadTask  uploadTask = filePath.putFile(resultUri);

            final ProgressDialog mProgress = new ProgressDialog(SettingsActivity.this);
            mProgress.setTitle("Uploading...");


            mProgress.setCancelable(true);

            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadTask.cancel();
                }
            });

            //  mProgress.setInverseBackgroundForced(setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY));
            mProgress.show();

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String profilePicUrl = uri.toString();
                                if (resultUri.getPath() != null) {
                                    File imageFile = new File(resultUri.getPath());
                                    try {
                                        compressImageFile = new Compressor(getApplicationContext())
                                                .setMaxHeight(200)
                                                .setMaxWidth(200)
                                                .setQuality(15)
                                                .compressToBitmap(imageFile);
                                    } catch (IOException e) {

                                        e.printStackTrace();
                                    }
                                    //    Toast.makeText(SettingsActivity.this, profilePicUrl, Toast.LENGTH_SHORT).show();
                                    Log.i("fuygih", "onSuccess: " + profilePicUrl);


                                    // Thumbnail==Profile Imahe Compression upload done here

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    compressImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] thumbData = baos.toByteArray();

                                    final StorageReference thumbFileRef = mProfilePictures.child("profile_pictures").child(curr_uid).child("thumb_nail.jpg");

                                    UploadTask uploadTask1 = thumbFileRef.putBytes(thumbData);
                                    uploadTask1.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        }
                                    })
                                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        thumbFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                mDatabaseRef.child("thumb_nail").setValue(uri.toString());
                                                            }
                                                        });

                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Thumb_image compression failed!", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                    mDatabaseRef.child("image").setValue(profilePicUrl);
                                    // mDatabaseRef.keepSynced(true);
                                }

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed!!", Toast.LENGTH_SHORT).show();

                    }
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    mProgress.setMessage("Uploaded: " + (int) progress + "%");
                    mProgress.setProgress((int) progress);
                }
            });
        }
    }

    public static class AdapterA extends RecyclerView.Adapter<AdapterA.ViewHolder> {
        @NonNull
        @Override
        public AdapterA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterA.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
