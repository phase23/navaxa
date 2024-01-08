package com.plus.navanguilla;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Myservice extends Service {
    private LocationManager locationManager;
    private LocationListener locationListener;
    Context mContext;
    String somebits;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;


        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager=(LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                2000,
                1, locationListenerGPS);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
              //  Toast.makeText(Myservice.this, "Location changed: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Toast.makeText(this, "Location listener is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS is disabled", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "GPS provider does not exist on this device", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Your start command handling
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            Float bearing = location.getBearing();
            String msg= latitude + ","+ longitude;
            //Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            Log.d("Changed", ": " + msg);

            createAndWriteToFile(msg);




            //sendlocation(latitude , longitude);
            sendnewlocationtomaps( latitude, longitude, bearing);
            //Toast.makeText(getApplicationContext(), "Location changed", Toast.LENGTH_LONG).show();

            String thisdevice = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            try {
                updatedevicelocation("https://xcape.ai/navigation/updatedevicelocation.php?lat="+latitude + "&long="+longitude + "&device="+ thisdevice);

            } catch (IOException e) {
                e.printStackTrace();
            }



           /*
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://axcessdrivers-default-rtdb.firebaseio.com/");

            DatabaseReference newdriver = database.getReference(thisdevice);
            newdriver.child("latitude").setValue(latitude);
            newdriver.child("longitude").setValue(longitude);

           */

        }


        void updatedevicelocation(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Log.i("ddevice",url);
            OkHttpClient client = new OkHttpClient();
            client.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(final Call call, IOException e) {
                            Log.i("ddevice","errot"); // Error


                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {


                            somebits = response.body().string();



                        }//end if




                    });

        }



        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }





    };


    private void sendnewlocationtomaps(Double lat, Double lon, Float Bearing){
        Intent intent = new Intent("my-location");
        // Adding some data
        String mylatconvert = String.valueOf(lat);
        String mylongconvert = String.valueOf(lon);
        String mybearingconvert = String.valueOf(Bearing);

        intent.putExtra("mylat", mylatconvert);
        intent.putExtra("mylon", mylongconvert);
        intent.putExtra("mybearing", mybearingconvert);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


    public void createAndWriteToFile(String data) {
        String fileName = "navi.txt";
        // Uncomment the line below to display a toast message with the content
        // Toast.makeText(getApplicationContext(), "ss: " + data, Toast.LENGTH_LONG).show();
        System.out.println("ss: " + data); // This is equivalent to println in Kotlin

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            // File written successfully
        } catch (IOException e) {
            e.printStackTrace();
            // Error writing file
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
