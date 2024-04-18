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
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Myactivity extends AppCompatActivity {

    TextView setcountry;
    String getload;
    Handler handler2;
    String returnshift;
    String somebits;
    String option;
    String key;
    String locationnow;
    String responseLocation;
    String cid;
    String thiscountry;
    private  FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);


        setContentView(R.layout.activity_myactivity);
        handler2 = new Handler(Looper.getMainLooper());

        final LinearLayout layout = findViewById(R.id.scnf);
        setcountry = (TextView)findViewById(R.id.tv_title);



        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

        SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();


        justhelper.setBrightness(this, 75); // Sets brightness to 75%

        cid = shared.getString("cid", "");
        thiscountry = shared.getString("country", "");
        int j = shared.getInt("key", 0);


        Log.i("tagg url",cid + " / " + thiscountry);
        setcountry.setText("Choose your destination -" + thiscountry);

        try {
            doGetRequest("https://xcape.ai/navigation/loadactivities.php");




        } catch (IOException e) {
            e.printStackTrace();
        }







    }

    private void logactivity( String item, String activity, String page ){

        Log.i("action url",item + "/" + activity + "/" + page);

        //Bundle bundle = new Bundle();
        //bundle.putString(FirebaseAnalytics.Param.ITEM_ID, item);
        //bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, activity);
        //bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, page);
        //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


    }


    private void checkAndRequestPermissions(String tag) {
        if (!isLocationPermissionGranted() || !isGpsEnabled()) {
            // If the location permission has not been granted, redirect to the disclosure page.
            Intent activity = new Intent(getApplicationContext(), Nopermission.class);
            startActivity(activity);

        }else {

            if(tag.equals("9")) { //entertainment
                Intent intent = new Intent(getApplicationContext(), Loadevents.class);
                intent.putExtra("list", tag);
                startActivity(intent);

            }else if(tag.equals("8")) {
                Intent intent = new Intent(getApplicationContext(), Loaddiscounts.class);
                intent.putExtra("list", tag);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getApplicationContext(), Loaditems.class);
                intent.putExtra("list", tag);
                startActivity(intent);
            }
        }

    }



    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }



    public void initnav(String json) {

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width


        try {
            JSONObject jsonObject = new JSONObject(somebits);

            // Loop through the JSON object and create buttons
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                 key = keys.next();
                 option = jsonObject.getString(key);


                // Create a button with the index as a tag
                Button button = new Button(this);
                button.setTag(key);
                button.setTag(R.id.tag_first, option);
                button.setText(option);

                // Add an OnClickListener to handle button clicks
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle button click here
                       String  tag = (String) view.getTag();
                        String thisplace = (String) button.getTag(R.id.tag_first);
                        // You can use the tag (index) to identify which button was clicked.
                        logactivity(tag,thisplace, "andriod");
                        checkAndRequestPermissions(tag);


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
               button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
                int drawableLeft;

               // Log.i("side",tag);
                if(key.equals("1")) {
                     drawableLeft = R.drawable.beach;  // Replace with your drawable resource ID

                }else if(key.equals("2")){
                    drawableLeft = R.drawable.pineat;
                }else if(key.equals("5")){
                    drawableLeft = R.drawable.luggage;
                }else if(key.equals("6")){
                    drawableLeft = R.drawable.villa;

                }else if(key.equals("7")){
                    drawableLeft = R.drawable.petrol;

                }else if(key.equals("10")){
                    drawableLeft = R.drawable.retail;

                }else{
                    drawableLeft = R.drawable.mmpin;
            }

                button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
                button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background));

// Setting margins
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(margin, 0, margin, 55);
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


    public String gethomeloc() {

        String thisdevice = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        //String modifiednow = locationnow.replace(',', '/');
        String url = "https://xcape.ai/navigation/gethomeloc.php?device="+thisdevice;
        Log.i("action url",url);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("loc","loc" )

                .build();
        Request request = new Request.Builder()
                .url(url)//your webservice url
                .post(requestBody)
                .build();
        try {
            //String responseBody;
            okhttp3.Response response = client.newCall(request).execute();
            // Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                Log.i("SUCC",""+response.message());
            }
            String resp = response.message();
            responseLocation =  response.body().string().trim();
            //responseLocation = location + "|" + responseLocation;
            Log.i("respBody:main",responseLocation);
            Log.i("MSG",resp);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return responseLocation;
    }



    private void gohome(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("1");
        button.setText("Return Home");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();



                AlertDialog.Builder dialog = new AlertDialog.Builder(Myactivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Let's get going");
                dialog.setMessage("Press yes to start your journey home");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {




                        String locationnow = readFile();
                        String gohome = gethomeloc();


                        locationnow = locationnow.replace(',', '/');
                         gohome = gohome.replace(',', '/');
                        String routenow = locationnow+"~"+gohome;
                        Log.i("MSG",routenow);

                        //startroute - Endroute
                        Log.i("route",routenow); // Error
                        Intent activity = new Intent(getApplicationContext(), Renturnhome.class);
                        //activity.putExtra("itemid",itemid);
                        activity.putExtra("theroute",routenow);
                        startActivity(activity);





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





        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(14);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
       button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.locationhome;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background));

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 55);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }






    private void changecountry(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("1");
        button.setText("Change Country");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();


                Intent activity = new Intent(getApplicationContext(), SelectCountry.class);
                activity.putExtra("change","dochange");
                startActivity(activity);



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
        button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.citizenship;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_beach));

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 55);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }




    private void contact(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("1");
        button.setText("Feedback");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();


                        Intent activity = new Intent(getApplicationContext(), Contactus.class);
                        startActivity(activity);



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
       button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.feedback;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background));

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 55);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }





    private void islandtour(){
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        /* Button  new start here */
        Button button = new Button(this);
        button.setTag("1");
        button.setText("Island Tour");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();



                AlertDialog.Builder dialog = new AlertDialog.Builder(Myactivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Let's get going");
                dialog.setMessage("Press yes to start your island tour");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {




                        String locationnow = readFile();

                        String modifiednow = locationnow.replace(',', '/');
                       // modifiednow = "18.177562900181787/-63.139738157244835";
                        //String routenow = modifiednow+"~18.2567503457155/-63.00025013253823";
                        String routenow = modifiednow+"~"+"18.18656230761481/-63.13412319134523";

                                            //startroute - Endroute
                        Log.i("route",routenow); // Error
                        Intent activity = new Intent(getApplicationContext(), Islandtour.class);
                        //activity.putExtra("itemid",itemid);
                        activity.putExtra("theroute",routenow);
                        startActivity(activity);


                        //gettheroutes(marker.getTag());


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


        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(14);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
       button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.maptour;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background));

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 55);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);

        /* Button New End Here */

    }


    private void discounts(){

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        Button button = new Button(this);
        button.setTag("1");
        button.setText("Discounts");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                checkAndRequestPermissions("8");
                /*
                Intent intent = new Intent(getApplicationContext(), Loaditems.class);
                intent.putExtra("list","4");
                startActivity(intent);
                    */
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
       button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.couponoff;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background));

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 55);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);


    }

    private void needhelp(){

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        Button button = new Button(this);
        button.setTag("1");
        button.setText("Emergencies");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                checkAndRequestPermissions("4");
                /*
                Intent intent = new Intent(getApplicationContext(), Loaditems.class);
                intent.putExtra("list","4");
                startActivity(intent);
                    */
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
       button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.help;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background));

// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, 0, margin, 55);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);


    }


    private void ent(){

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        Button button = new Button(this);
        button.setTag("1");
        button.setText("Entertainment");
        button.setTextColor(Color.WHITE);
       //button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                checkAndRequestPermissions("9");

                //Intent intent = new Intent(getApplicationContext(), Loadevents.class);
                //intent.putExtra("list","5");
               // startActivity(intent);

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
       button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.livemusic;  // Replace with your drawable resource ID

        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        button.setCompoundDrawablePadding(10); // Optional, if you want padding between text and image
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_ent_main));


        int heightInDp = 80; // Height in dp
        float scale = getResources().getDisplayMetrics().density;
        int heightInPx = (int) (heightInDp * scale + 0.5f);
// Setting margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, heightInPx);
        layoutParams.setMargins(margin, 0, margin, 55);
        button.setLayoutParams(layoutParams);

// Add the button to your layout
        LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
        linearLayout.addView(button);


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



    void doGetRequest(String url) throws IOException {
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


                        somebits = response.body().string();
                        Log.i("ddevice",somebits);

                        handler2.post(new Runnable() {
                            @Override
                            public void run() {

                            initnav(somebits);
                                ent();
                                islandtour();
                                discounts();
                                //gohome();
                                needhelp();
                                contact();
                                changecountry();
                            }
                        });


                    }//end if




                });

    }
}