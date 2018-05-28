package com.example.nabih.locationapp;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nabih.locationapp.databinding.ActivityLocationBinding;
import com.example.nabih.locationapp.util.LocationProvider;

public class LocationActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 401;
    private static final String TAG = LocationActivity.class.getSimpleName();
    private ActivityLocationBinding databinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding= DataBindingUtil.setContentView(this,R.layout.activity_location);
        if(checkPermissions()) {
            subcribeLocationUpdates();
        }
        else {
            startLocationPermissionRequest();
        }
    }

    /**
     * this method will observes a location object
     */
    private void subcribeLocationUpdates() {
        LocationProvider.getInstance(this).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                databinding.latitude.setText("Lat :"+location.getLatitude());
                databinding.longitude.setText("Lon :"+location.getLongitude());

            }
        });
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                subcribeLocationUpdates();
            }
        }
    }
}
