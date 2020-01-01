package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class Comments_handeling_Activity extends AppCompatActivity {

    private String post_id;
    private List<Comments>  commentsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_handeling_);

        // Hide keyeboard  when activity opens...............
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        commentsList = new ArrayList<>();
        post_id = getIntent().getStringExtra("post_id");
    }
}
