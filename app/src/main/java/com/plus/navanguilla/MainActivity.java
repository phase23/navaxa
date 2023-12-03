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
import android.view.View;
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

public class MainActivity extends AppCompatActivity   {
    Button finish;
    String somebits;



    LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();


            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        String globaldevice = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        createLocationFile();
        createWayFile();

        try {

            senddevice("https://xcape.ai/navigation/deviceload.php?deviceid="+globaldevice);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(getApplicationContext(), Myservice.class);
        getApplicationContext().startService(i);

        finish = (Button)findViewById(R.id.button);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Myactivity.class);
                startActivity(intent);

            }
        });





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