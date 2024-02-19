package com.plus.navanguilla;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.plus.navanguilla.util.DirectionPointListener;
import com.plus.navanguilla.util.TourPointListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.os.Handler;


public class MainActivity extends AppCompatActivity   {
    Button finish;
    String somebits;



    LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private final int SPLASH_DISPLAY_LENGTH = 3000; // Splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Hide the status bar.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the navigation bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_main);



        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();


            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
/*
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }




*/
        checkAndRequestPermissions();

        String globaldevice = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        createLocationFile();
        createWayFile();

        try {

            senddevice("https://xcape.ai/navigation/deviceload.php?deviceid="+globaldevice);

        } catch (IOException e) {
            e.printStackTrace();
        }

       // Intent i = new Intent(getApplicationContext(), Myservice.class);
        //getApplicationContext().startService(i);






    }

    void senddevice(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.i("mydevice",url);
        OkHttpClient client = new OkHttpClient();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        Log.i("ddevice","errot"); // Error

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {


                        somebits = response.body().string();
                        Log.i("ddevice",somebits);


                    }//end if




                });

    }



    private void checkAndRequestPermissions() {
        if (!isLocationPermissionGranted() || !isGpsEnabled()) {
            // If the location permission has not been granted, redirect to the disclosure page.
            Intent activity = new Intent(getApplicationContext(), Disclosure.class);
            startActivity(activity);
        }else if (!NetworkUtil.isInternetAvailable(getApplicationContext()) ) {
            Intent activity = new Intent(getApplicationContext(), Nointernet.class);
            startActivity(activity);

        }else{

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Create an Intent that will start the main activity.
                    Intent i = new Intent(getApplicationContext(), Myservice.class);
                    getApplicationContext().startService(i);

                    Intent activity = new Intent(getApplicationContext(), Myactivity.class);
                    startActivity(activity);
                }
            }, SPLASH_DISPLAY_LENGTH);






        }
    }


    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }



    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                startLocationService();
            } else {
                // Permission was denied
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationService() {
        // Start your service that requires location here
        Intent serviceIntent = new Intent(this, Myservice.class);
        startService(serviceIntent);
    }




    public void createLocationFile() {



        String fileName = "navi.txt";
        String content = "18.18568295254444, -63.134536600353854";

        File file = new File(getFilesDir(), fileName);

        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(content.getBytes());
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
         } else {
            // File already exists, handle accordingly
        }


    }



    public void createWayFile() {



        String fileName = "wayfile.txt";
        String content = "18.192583092946766,-63.0976363138236|18.195838616814342,-63.08673349580098|18.200522576706323,-63.07608053256025|18.200991044243878,-63.07634663720534|18.201147317948575,-63.0754322631268";

        File file = new File(getFilesDir(), fileName);

        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(content.getBytes());
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
        } else {
            // File already exists, handle accordingly
        }


    }




}