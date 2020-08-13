package com.example.chatapp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.chatapp.R;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCalling extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static final String TAG = "dfghjk";
    private static String API_KEY = "46683642";
    private static String SESSION_ID = "1_MX40NjY4MzY0Mn5-MTU4NzA3MjYyODcyMn5IdXZpczVCbXl6RWpocTc5bENQQ0l2dE9-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjY4MzY0MiZzaWc9ZDA3NjlhMzNkY2IwNTdlOTZjYjc2YTU4MGE4ODg0MDg0MzUzMTM3NzpzZXNzaW9uX2lkPTFfTVg0ME5qWTRNelkwTW41LU1UVTROekEzTWpZeU9EY3lNbjVJZFhacGN6VkNiWGw2Uldwb2NUYzViRU5RUTBsMmRFOS1mZyZjcmVhdGVfdGltZT0xNTg3MDcyNjc4Jm5vbmNlPTAuNTQ0Njg2NDY5MzU3MTU2MyZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTg5NjY0Njc3JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = VideoCalling.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private ImageView endCall;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private FrameLayout user2Container,currUserContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_calling);

        endCall = findViewById(R.id.end_call);
        user2Container = findViewById(R.id.user2_container);
        currUserContainer = findViewById(R.id.user1_container);


        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, VideoCalling.this);
    }
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private  void requestpermission(){
        String[]  perms = {Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perms)){
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(VideoCalling.this);
            mSession.connect(TOKEN);
        }else{
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {
        Log.i(TAG, "onConnected: ");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoCalling.this);

        currUserContainer.addView(mPublisher.getView());
        if(mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView)((GLSurfaceView) mPublisher.getView())).setZOrderOnTop(true);

        }
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(TAG, "onDisconnected: ");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(TAG, "onStreamReceived: ");
        if(mSubscriber==null){
            mSubscriber = new Subscriber.Builder(this,stream).build();
            mSession.subscribe(mSubscriber);
            user2Container.addView(mSubscriber.getView());
        }


    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(TAG, "onStreamDropped: ");
        if(mSubscriber!=null){
            mSubscriber=null;
            currUserContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(TAG, "onError: ");
    }
}
