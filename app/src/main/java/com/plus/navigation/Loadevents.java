package com.plus.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class Loadevents extends AppCompatActivity {
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
    String cid;
    String thiscountry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Hide the status bar.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the navigation bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

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
                Intent intent = new Intent(Loadevents.this, Myactivity.class);
                startActivity(intent);

            }
        });


        itemid = "";
        Log.i("side",itemid);

        try {

            SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = shared.edit();

            cid = shared.getString("cid", "");
            thiscountry = shared.getString("country", "");

            String getlocation = readFile();
            doLoadlist("https://xcape.ai/navigation/loadevents.php?location="+getlocation + "&cid="+cid);

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

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Loadevents.this);
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


                    drawableLeft = R.drawable.music;

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


                button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
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



    private void callofficebutton(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("1");
        button.setText("Call Office");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                Uri number = Uri.parse("tel:4760608");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);

            }
        });


        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(14);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
        button.setGravity(Gravity.START);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.calloffice;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 30);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }


    private void callpolicebutton(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("1");
        button.setText("Call Police");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                Uri number = Uri.parse("tel:4760608");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });


        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(14);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
        button.setGravity(Gravity.START);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.police;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 30);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }





    private void beachbutton(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("1");
        button.setText("Beach Map");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                Intent intent = new Intent(getApplicationContext(), Loadmaps.class);
                intent.putExtra("itemid",itemid);
                intent.putExtra("list",tag);
                startActivity(intent);

            }
        });


        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(14);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
        button.setGravity(Gravity.START);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.beachmap;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 30);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }


    private void restaurantbutton(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("2");
        button.setText("Restaurant Map");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                Intent intent = new Intent(getApplicationContext(), Loadmaps.class);
                intent.putExtra("itemid",itemid);
                intent.putExtra("list",tag);
                startActivity(intent);

            }
        });



        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(14);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
        button.setGravity(Gravity.START);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.eatmap;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 30);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }


    private void interestbutton(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("2");
        button.setText("Interest Map");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                Intent intent = new Intent(getApplicationContext(), Loadmaps.class);
                intent.putExtra("itemid",itemid);
                intent.putExtra("list",tag);
                startActivity(intent);

            }
        });



        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(14);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
        button.setGravity(Gravity.START);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.eatmap;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 30);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

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



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}