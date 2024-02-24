package com.plus.navanguilla;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Loaddiscounts extends AppCompatActivity {
    String getload;
    Handler handler2;
    String returnshift;
    String someitems;
    Button goback;
    String locationnow;
    String itemid;
    String thistag;
    TextView loading;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaddiscounts);

        justhelper.setBrightness(this, 75); // Sets brightness to 75%
        setContentView(R.layout.activity_loadevents);
        handler2 = new Handler(Looper.getMainLooper());

        final LinearLayout layout = findViewById(R.id.scnf);
        goback = (Button)findViewById(R.id.backmain);
        loading = (TextView) findViewById(R.id.loadingtext);
        progressBar = findViewById(R.id.spin_kit);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loaddiscounts.this, Myactivity.class);
                startActivity(intent);

            }
        });


        itemid = "";
        Log.i("side",itemid);

        try {
            String getlocation = readFile();
            doLoadlist("https://xcape.ai/navigation/loaddiscounts.php?location="+getlocation);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readFile() {
        String fileName = "navi.txt";
        StringBuilder stringBuilder = new StringBuilder();

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            fis = openFileInput(fileName);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }

            locationnow = stringBuilder.toString();
            // Use the file contents as needed
            // Uncomment the line below to display a toast message with the content
            // Toast.makeText(getApplicationContext(), "Serlat: " + locationnow, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Error reading file
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return  locationnow;
    }

    void doLoadlist(String url) throws IOException {
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


                        someitems = response.body().string();
                        Log.i("ddevice",someitems);

                        handler2.post(new Runnable() {
                            @Override
                            public void run() {





                                othernav(someitems);
                                loading.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });


                    }//end if




                });

    }

    private void checkAndRequestPermissions() {
        if (!isLocationPermissionGranted() || !isGpsEnabled()) {
            // If the location permission has not been granted, redirect to the disclosure page.
            Intent activity = new Intent(getApplicationContext(), Nopermission.class);
            startActivity(activity);

        }else {

            gettheroutes(thistag);


        }

    }


    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    public void othernav(String json) {
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        Log.i("jss",json);

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extracting data from JSON object
                String placeId = jsonObject.getString("placeid");
                String whichday = jsonObject.getString("whichday").trim();

                String showtime = jsonObject.getString("showtime").trim();
                String venue = jsonObject.getString("venue").trim();
                String artist = jsonObject.getString("artist").trim();

                double distance = jsonObject.getDouble("distance");
                String formattedDistance = String.format("%.2f", distance);



                // Creating button text
                String buttonText = whichday + "\n" + venue + "\n" + artist + "\n" + showtime +"\n" + formattedDistance + " Miles";
                // Create a button
                Log.i("button",buttonText);
                Button button = new Button(this);
                button.setTag(placeId);  // Set placeId as tag
                button.setText(buttonText);

                // Add an OnClickListener to handle button clicks
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Loaddiscounts.this);
                        dialog.setCancelable(false);
                        dialog.setTitle("Start Navigation");
                        dialog.setMessage("Are you sure you want start this route?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                thistag = (String) button.getTag();
                                //gettheroutes(thistag);
                                checkAndRequestPermissions();

                                dialog.dismiss();
                            }
                        })
                                .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Action for "Cancel".
                                        dialog.dismiss();
                                    }
                                });

                        final AlertDialog alert = dialog.create();
                        alert.show();


                    }
                });

                // Setting button height and other properties
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonParams.height = 80;  // adjust this value to your liking
                button.setTextSize(14);  // adjust this value to your liking
                int padding = 20;  // adjust this value to your liking
                button.setPadding(padding, padding, padding, padding);

// Aligning text to the left and adding an image
                button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                Log.i("ddevice",itemid); // Error
                int drawableLeft;


                drawableLeft = R.drawable.discount;
                int drawableRight = R.drawable.chevron;

                switch(whichday){
                    default:
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_ent));
                        break;
                    case "MONDAY":
                    case "WEDNESDAY":
                    case "FRIDAY":
                    case "SUNDAY ":
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background));
                        break;



                }


                button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, drawableRight, 0);

                button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image


// Setting margins
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(margin, 0, margin, 30);
                button.setLayoutParams(layoutParams);

// Add the button to your layout
                LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
                linearLayout.addView(button);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

    @Override
    public void onBackPressed() {

    }

    public void  gettheroutes(String placeid){





        try {

            System.out.println("https://xcape.ai/navigation/getroute.php?&id=" + placeid );
            returnroute("https://xcape.ai/navigation/getroute.php?&id=" + placeid );

        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    void returnroute(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error

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





                        String resulting = response.body().string().trim();
                        String modifiednow = locationnow.replace(',', '/');
                        String routenow = modifiednow+"~"+resulting;
                        Log.d("routex", ": " + routenow);


                        Intent activity = new Intent(getApplicationContext(), Pickup.class);
                        activity.putExtra("itemid",itemid);
                        activity.putExtra("theroute",routenow);
                        activity.putExtra("placeid",thistag);
                        activity.putExtra("preclass","1");

                        startActivity(activity);



                    }//end void

                });
    }






}
