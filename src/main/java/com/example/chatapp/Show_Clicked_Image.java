package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Show_Clicked_Image extends AppCompatActivity {
    ImageView imageView,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__clicked__image);
         imageView = findViewById(R.id.image_to_be_shown);
         back = findViewById(R.id.back);
        String imageUrl = getIntent().getStringExtra("url");

        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.image_loading)
                .into(imageView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
