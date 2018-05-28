package com.example.nabih.locationapp.util;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Uzair Qureshi on 5/28/2018.
 * Description:this is centralised class for getting a cuurent location
 */
public class LocationProvider extends LiveData<Location>  {
    private final static String TAG = "LocationProvider";
    private static LocationProvider instance;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationProvider(Context context) {
        buildFusedApiClient(context);
    }

    public static LocationProvider getInstance(Context context) {
        if (instance == null) {

            instance = new LocationProvider(context);
        }
        return instance;
    }

    /**
     * this method will build a fused location api client
     *
     * @param context
     */
    private synchronized void buildFusedApiClient(Context context) {
        Log.d(TAG, "Build fused api client");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActive() {
        super.onActive();
        //LocationRequest locationRequest = LocationRequest.create();
        Looper looper = Looper.myLooper();
        mFusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, looper);

    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }

    }

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return mFusedLocationProviderClient;

    }

    /**
     * this will return location request object
     * @return
     */
    private LocationRequest getLocationRequest() {
        return new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10 * 1000);

    }


    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult.getLastLocation()!=null) {
                Location lastLocation = locationResult.getLastLocation();
                setValue(lastLocation);
            }

        }
    };


}
