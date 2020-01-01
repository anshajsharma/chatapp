package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.RegisterAndLogin.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class NewPost extends AppCompatActivity {
    private static final String TAG = "NewPost";

    ImageView imageView;
    String imageUri;
    CardView post,cancel;
    private DatabaseReference mDatabaseRef;
    Bitmap compressImageFile;
    private StorageReference mPostPicture;
    ProgressDialog mProgressDialog;
    EditText editText;
    Uri image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        post = findViewById(R.id.post_image);
        cancel = findViewById(R.id.cancel_post);
        mPostPicture= FirebaseStorage.getInstance().getReference();
        imageView=findViewById(R.id.image_to_be_posted);
        editText = findViewById(R.id.description);
        imageUri = getIntent().getStringExtra("image");
        Picasso.with(NewPost.this)
                .load(imageUri)
                .placeholder(R.drawable.avtar)
                .into(imageView);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = new ProgressDialog(getApplicationContext());
                mProgressDialog.setTitle("Uploading");
                mProgressDialog.setMessage("Please wait while we upload your data.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                   postImage(imageUri,mProgressDialog);
            }
        });

    }

    private void postImage(String imageUri, final ProgressDialog mProgressDialog) {

        // Time stampGenerator.... best way to get unique id....
        // Nothing can be better than this in uniqueness
        final String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        //mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String curr_uid = FirebaseAuth.getInstance().getUid();
        final Uri resultUri = Uri.parse(imageUri);
        final String description = editText.getText().toString();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        final StorageReference filePath = mPostPicture.child("posted_pictures").child(curr_uid).child(timeStamp);

        filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                          final  String profilePicUrl = uri.toString();


                       //
                            mDatabaseRef.child("Users").child(curr_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    String s=dataSnapshot.child("post_count").getValue(String.class);

                                    long count = Long.parseLong(s);
                                    count++;
                                    mDatabaseRef.child("Users").child(curr_uid).child("post_count").setValue(String.valueOf(count));

                                    Users user1= dataSnapshot.getValue(Users.class);

                                    String s1 = mDatabaseRef.child("posts").push().getKey();


                                    assert user1 != null;
                                    String  image = user1.getImage();
                                    if(!user1.getThumb_nail().equals("default")) image=user1.getThumb_nail();

                                    HashMap<String, String> PostMap = new HashMap<>();
                                    PostMap.put("user_id",user1.getUser_id());
                                    PostMap.put("poster_name", user1.getName());
                                    PostMap.put("posted_image", profilePicUrl);
                                    PostMap.put("thumb_nail", "image");
                                    PostMap.put("user_profile_image",user1.getImage());
                                    PostMap.put("timestamp", timeStamp);
                                    PostMap.put("description", description);
                                    PostMap.put("likes_count","0");
                                    PostMap.put("comments_count","0");
                                    PostMap.put("shares_count","0");
                                    PostMap.put("last_comment","default");
                                    PostMap.put("post_id",s1);

                                    assert s1 != null;
                                    mDatabaseRef.child("posts").child(s1).setValue(PostMap);
                                    mDatabaseRef.child("post_ref").child(user1.getUser_id()).child(timeStamp).setValue(s1);



                                 mProgressDialog.hide();



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                       //


                        }
                    });
                    Toast.makeText(NewPost.this, "Successful", Toast.LENGTH_SHORT).show();
                    finish();

                }
                else{
                    Toast.makeText(NewPost.this, "Failed!!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                final Uri resultUri2 = data.getData();

                Log.i("asdfg2",resultUri.toString());
                Log.i("asdfg2","i am here");
                Picasso.with(NewPost.this)
                        .load(resultUri)
                        .placeholder(R.drawable.avtar)
                        .into(imageView);


            }
        }
    }
}


