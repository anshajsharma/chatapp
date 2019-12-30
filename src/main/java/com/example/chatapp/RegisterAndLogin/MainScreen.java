package com.example.chatapp.RegisterAndLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.chatapp.R;
import com.example.chatapp.UsersHomePage;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class MainScreen extends AppCompatActivity {
    ImageView a,s,d,f,g,h,j,k,l;
    private static int TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


         d= findViewById(R.id.heart);    //1
         f= findViewById(R.id.welcome);  //2
         g= findViewById(R.id.chat_app); //3
         a= findViewById(R.id.letus);    //4
         s= findViewById(R.id.chat);    //5
        d.setTranslationZ(-1500f);
        f.setTranslationY(1500f);
        g.setTranslationY(1500f);
        a.setTranslationY(-1500f);
        s.setTranslationX(-1500f);



        d.animate().translationZBy(1500f).setDuration(200);
        f.animate().translationYBy(-1500f).setDuration(500);
        g.animate().translationYBy(-1500f).setDuration(850);
        a.animate().translationYBy(1500f).setDuration(1000);
        s.animate().translationXBy(1500f).setDuration(1100);





        //final View myLayout = findViewById(R.id.acti);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainScreen.this, UsersHomePage.class);
                startActivity(i);
                finish();
            }
        }, 1600);


    }
}
