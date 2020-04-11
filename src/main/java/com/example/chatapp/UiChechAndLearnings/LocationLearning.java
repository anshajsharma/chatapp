package com.example.chatapp.UiChechAndLearnings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;

public class LocationLearning extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationLearning";
    private TextView textView,link;
    private Button button;
    private static final int REQUEST_CODE = 1657;
    private GoogleApiClient googleApiClient;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_learning);
        textView=findViewById(R.id.asdfg);
        button = findViewById(R.id.asdfgl);
        link = findViewById(R.id.link);

        googleApiClient = new GoogleApiClient.Builder(LocationLearning.this)
                .addConnectionCallbacks(LocationLearning.this)
                .addOnConnectionFailedListener(LocationLearning.this)
                .addApi(LocationServices.API).build();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(googleApiClient != null){
                    googleApiClient.connect();
                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "onConnected: " + "We are connected to user location");
        showTheUserLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + "Connection is suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionSuspended: " + "Connection Failed");
        if(connectionResult.hasResolution()){
            try {
                connectionResult.startResolutionForResult(LocationLearning.this,REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                Log.d(TAG,  e.getStackTrace().toString());
            }
        }else{
            Toast.makeText(this, "Goole Play Serve Not Workin", Toast.LENGTH_LONG).show();
//            finish();
        }
    }

    // Custom methods.....
    private void showTheUserLocation() {
        int permissinCheck = ContextCompat.checkSelfPermission(LocationLearning.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(permissinCheck == PackageManager.PERMISSION_GRANTED){
            FusedLocationProviderApi fusedLocationProviderApi =
                    LocationServices.FusedLocationApi;
            location = fusedLocationProviderApi.getLastLocation(googleApiClient);

            if (location != null){
                final double lat = location.getLatitude();
                final double lng = location.getLongitude();
                String location = lat+ ", " + lng;
                String LocationLink = "https://www.google.com/maps?q="+location;

                String Link = "<a href=\""+ LocationLink +"\">Go to Location</a>";

                Log.d(TAG, "showTheUserLocation: " + Link);


                // Takes me to uRl of link
                link.setText(Link);
//                link.setMovementMethod(LinkMovementMethod.getInstance());
                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + "Anshaj\'sChatApp" + ")";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                        startActivity(intent);
                    }
                });

                textView.setText(lat+ ", " + lng);
            }else{
                textView.setText("This App is not able to access location");
            }

        }else{
            textView.setText("This App is Not Allowed to Access Location");
            ActivityCompat.requestPermissions(LocationLearning.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},185);

        }
    }
}
