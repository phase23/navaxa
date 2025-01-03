package com.plus.navanguilla;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class Loaditems extends AppCompatActivity {
    String getload;
    Handler handler2;
    String returnshift;
    String someitems;
    Button goback;
    String locationnow;
    String itemid;
    String placeId;
    String thistag;
    TextView loading;
    ProgressBar progressBar;
    String cid;
    String thiscountry;
    String isinterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Hide the status bar.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the navigation bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        justhelper.setBrightness(this, 75); // Sets brightness to 75%

        setContentView(R.layout.activity_loaditems);
        handler2 = new Handler(Looper.getMainLooper());


        final LinearLayout layout = findViewById(R.id.scnf);
        goback = (Button)findViewById(R.id.backmain);
        loading = (TextView) findViewById(R.id.loadingtext);

        SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();

        cid = shared.getString("cid", "");
        thiscountry = shared.getString("country", "");


        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loaditems.this, Myactivity.class);
                startActivity(intent);

            }
        });


        itemid = getIntent().getExtras().getString("list","defaultKey");
        Log.i("side",itemid);

        if(itemid.equals("2") || itemid.equals("1") || itemid.equals("3") || itemid.equals("7") || itemid.equals("5") || itemid.equals("4") || itemid.equals("10") || itemid.equals("11")   ) {
            loadlist("distance");
            loading.setText("Sorting by distance .. loading");
        }else{
            loadlist("venue");
            loading.setText("Sorting A to Z .. loading");
        }


        if(  itemid.equals("2")  ) {
            restaurantbutton();

        }

    }

    @Override
    public void onBackPressed() {

    }

    public void loadlist(String sortorder){
        loading.setVisibility(View.VISIBLE);
        progressBar = findViewById(R.id.spin_kit);
        LinearLayout linearLayout = findViewById(R.id.scnf);

        /*
        for (int i = 1; i <= 950; i++) {
            View viewToRemove = linearLayout.findViewWithTag(String.valueOf(i));
            if (viewToRemove != null) {
                linearLayout.removeView(viewToRemove);
            }
        }
         */
        // Remove all dynamically added container views
        for (int i = 0; i <= 950; i++) {
            View containerToRemove = linearLayout.findViewWithTag("container_" + i); // Use consistent tagging
            if (containerToRemove != null) {
                linearLayout.removeView(containerToRemove);
            }
        }




        try {
            String getlocation = readFile();
            doLoadlist("https://xcape.ai/navigation/loadlist.php?id="+itemid + "&location="+getlocation + "&sortorder=" +sortorder + "&cid=" +cid);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end list

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
                        Log.i("ddevice","errot " + e); // Error

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
                                if(itemid.equals("1")) {
                                    //fbeachbutton();


                                }else if(itemid.equals("4")){
                                    //callofficebutton();
                                  //  callpolicebutton();
                                }




                                othernav(someitems);
                                loading.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });


                    }//end if




                });

    }


    public String toSentenceCase(String inputString) {
        if (inputString == null || inputString.isEmpty()) {
            return inputString;
        }

        return inputString.substring(0, 1).toUpperCase() + inputString.substring(1).toLowerCase();
    }




    /*
    public void othernav(String json) {
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width



        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extracting data from JSON object
                placeId = jsonObject.getString("placeid");
                String whichSite = jsonObject.getString("whichsite").trim();
                String phone = jsonObject.getString("phone").trim();
                double distance = jsonObject.getDouble("distance");
                String thisplace = jsonObject.getString("thisplace");
                 isinterest = jsonObject.getString("isinterest");
                String isAdvertiser = jsonObject.optString("isAdvertiser", "0"); // Default to "0" if not present
                String openingTimes = jsonObject.optString("openingTimes", "N/A");
                String about = jsonObject.optString("slug", "No details available");
                String imageUrl = jsonObject.optString("imageUrl", "");


                String formattedDistance = String.format("%.2f", distance);
                String buttonText;
                String onwhichSite = toSentenceCase(whichSite);
                Log.i("wsite",whichSite);

                if(whichSite.equals("Monday")){
                    buttonText = whichSite + " ";
                } else {
                    // Creating button text
                    buttonText = onwhichSite + "\n" + formattedDistance + " Miles";
                }
                // Create a button
                Button button = new Button(this);
                button.setTag(placeId);  // Set placeId as tag
                button.setTag(R.id.tag_first, phone);
                button.setTag(R.id.tag_second, thisplace);
                button.setTag(R.id.tag_next, isinterest);
                button.setText(buttonText);
                //button.setTransformationMethod(null);
                //button.setAllCaps(false);
                // Add an OnClickListener to handle button clicks
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String thistagnext = (String) button.getTag(R.id.tag_next);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Loaditems.this);
                        dialog.setCancelable(false);
                            String thisaction;
                            String thiscancel;
                        if(itemid.equals("2") || thistagnext.equals("1")){
                            String msgtop;
                            String msgbtm;
                            if(thistagnext.equals("1")){
                                msgtop = "Book Activity or start navigation";
                                msgbtm = "Book Activity";
                            }else{
                                 msgtop = "Reserve a table or start navigation";
                                msgbtm = "Reserve table";
                            }
                            String thisplace = (String) button.getTag(R.id.tag_second);
                            dialog.setTitle(thisplace);
                            dialog.setMessage(msgtop);
                            thisaction = "Start";
                            thiscancel = "Cancel";

                            dialog.setNeutralButton(msgbtm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Code to execute on "Info"
                                    String thisphone = (String) button.getTag(R.id.tag_first);
                                    //Toast.makeText(getApplicationContext(), "phone " + thisphone, Toast.LENGTH_SHORT).show();

                                    Uri number = Uri.parse("tel:" +thisphone);
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                    startActivity(callIntent);

                                }
                            });




                        }else {
                            dialog.setTitle("Start Navigation");
                            dialog.setMessage("Are you sure you want start this route?");
                            thisaction = "Yes";
                            thiscancel = "No";

                        }


                        dialog.setPositiveButton(thisaction, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                thistag = (String) button.getTag();
                                gettheroutes(thistag);


                                dialog.dismiss();
                            }
                        })
                                .setNegativeButton(thiscancel, new DialogInterface.OnClickListener() {
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
                button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
// Aligning text to the left and adding an image
                button.setGravity(Gravity.START);  // This aligns the text to the left
                Log.i("ddevice",itemid); // Error
                int drawableLeft;
                if (itemid.equals("1")){
                    drawableLeft = R.drawable.beach;  // Replace with your drawable resource ID
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_beach));

                }else if(itemid.equals("2")){

                    drawableLeft = R.drawable.pineat;
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));

                }else if(itemid.equals("3")){

                    if(isinterest.equals("1")) {
                        drawableLeft = R.drawable.outdoor;
                    }else{
                        drawableLeft = R.drawable.mmpin;
                    }




                }else if(itemid.equals("4")){
                    drawableLeft = R.drawable.hospitalabr;
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));

                }else if(itemid.equals("5")){
                    drawableLeft = R.drawable.luggage;
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));


                }else if(itemid.equals("6")){
                    drawableLeft = R.drawable.villa;
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));



                }else if(itemid.equals("10")){
                    drawableLeft = R.drawable.retail;
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));

                }else if(itemid.equals("11")){
                    drawableLeft = R.drawable.crental;
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));



                }else if(itemid.equals("7")){
                    drawableLeft = R.drawable.petroloutline;
                    button.setTextColor(Color.BLACK);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));


                }else{


                    drawableLeft = R.drawable.pineat;

                }
                int drawableRight = R.drawable.chevron;


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


     */


    public void othernav(String json) {
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 10% of screen width

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extracting data from JSON object
                placeId = jsonObject.getString("placeid");
                String whichSite = jsonObject.getString("whichsite").trim();
                String phone = jsonObject.getString("phone").trim();
                double distance = jsonObject.getDouble("distance");
                String thisplace = jsonObject.getString("thisplace");
                String drivingtime = jsonObject.getString("drivingtime");
                isinterest = jsonObject.getString("isinterest");
                String isAdvertiser = jsonObject.optString("isAdvertiser", "0");
                String openingTimes = jsonObject.optString("openingTimes", "N/A");
                String openingTimesOthertxt = jsonObject.optString("openingTimesOther", "");
                String about = jsonObject.optString("slug", "No details available");
                String imageUrl = jsonObject.optString("imageUrl", "");

                String formattedDistance = String.format("%.2f", distance);
               // String buttonText = whichSite + "\n" + formattedDistance + " Miles";
                String buttonText = drivingtime;
                // Create a container layout for each item
                LinearLayout container = new LinearLayout(this);
                container.setOrientation(LinearLayout.VERTICAL);
                container.setTag("container_" + i);
                container.setPadding(20, 20, 20, 20);
                container.setBackground(ContextCompat.getDrawable(this,
                        isAdvertiser.equals("1") ? R.drawable.advertiser_background : R.drawable.default_background));
                container.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                // Add ImageView for advertiser
                if (isAdvertiser.equals("1")) {
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 300)); // Adjust height as needed
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    if (!imageUrl.isEmpty()) {
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.placeholder) // Placeholder image
                                .error(R.drawable.placeholder) // Error image
                                .into(imageView);
                    } else {
                        imageView.setImageResource(R.drawable.placeholder); // Default placeholder
                    }

                    container.addView(imageView);
                }

                // Add TextView for place details
                TextView detailsText = new TextView(this);
                detailsText.setText(thisplace + "\n" + formattedDistance + " Miles");
                detailsText.setTextSize(16);
                detailsText.setTextColor(Color.BLACK);
                container.addView(detailsText);

                // Add advertiser-specific details
                if (isAdvertiser.equals("1")) {
                    TextView openingTimesText = new TextView(this);
                    openingTimesText.setText(  openingTimes);
                    openingTimesText.setTextSize(14);
                    openingTimesText.setTextColor(Color.GREEN);
                    container.addView(openingTimesText);

                    TextView openingTimesTextOther = new TextView(this);
                    openingTimesTextOther.setText("" + openingTimesOthertxt);
                    openingTimesTextOther.setTextSize(14);
                    openingTimesTextOther.setTextColor(Color.GREEN);
                    container.addView(openingTimesTextOther);

                    TextView aboutText = new TextView(this);
                    aboutText.setText("About: " + about);
                    aboutText.setTextSize(14);
                    aboutText.setTextColor(Color.DKGRAY);
                    container.addView(aboutText);
                }

                // Add Button for actions
                Button button = new Button(this);
                button.setTag(placeId);  // Set placeId as tag
                button.setTag(R.id.tag_first, phone);
                button.setTag(R.id.tag_second, thisplace);
                button.setTag(R.id.tag_next, isinterest);
                button.setText(buttonText);
                button.setTextColor(Color.BLACK);

                // Add drawable for the button
                int drawableLeft;
                if (itemid.equals("1")) {
                    drawableLeft = R.drawable.beach;
                    //button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_beach));
                } else if (itemid.equals("2")) {
                    drawableLeft = R.drawable.pineat;
                    //button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));
                } else if (itemid.equals("3")) {
                    drawableLeft = isinterest.equals("1") ? R.drawable.outdoor : R.drawable.mmpin;

                }else if(itemid.equals("4")){
                    drawableLeft = R.drawable.hospitalabr;
                    button.setTextColor(Color.BLACK);
                   // button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));

                }else if(itemid.equals("5")){
                    drawableLeft = R.drawable.luggage;
                    button.setTextColor(Color.BLACK);
                   // button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));


                }else if(itemid.equals("6")){
                    drawableLeft = R.drawable.villa;
                    button.setTextColor(Color.BLACK);
                   // button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));



                }else if(itemid.equals("10")){
                    drawableLeft = R.drawable.retail;
                    button.setTextColor(Color.BLACK);
                   // button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));

                }else if(itemid.equals("11")){
                    drawableLeft = R.drawable.crental;
                    button.setTextColor(Color.BLACK);
                    //button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_food));


                } else {
                    drawableLeft = R.drawable.pineat; // Default icon
                }

                // Add chevron drawable to the right of the button
                button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, R.drawable.chevron, 0);
                button.setCompoundDrawablePadding(10);

                // Button click listener
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String thistagnext = (String) button.getTag(R.id.tag_next);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Loaditems.this);
                        dialog.setCancelable(false);

                        String thisaction;
                        String thiscancel;

                        if (itemid.equals("2") || thistagnext.equals("1")) {
                            String msgtop;
                            String msgbtm;

                            if (thistagnext.equals("1")) {
                                msgtop = "Book Activity or start navigation";
                                msgbtm = "Book Activity";
                            } else {
                                msgtop = "Reserve a table or start navigation";
                                msgbtm = "Reserve table";
                            }

                            String thisplace = (String) button.getTag(R.id.tag_second);
                            dialog.setTitle(thisplace);
                            dialog.setMessage(msgtop);
                            thisaction = "Start";
                            thiscancel = "Cancel";

                            dialog.setNeutralButton(msgbtm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String thisphone = (String) button.getTag(R.id.tag_first);
                                    Uri number = Uri.parse("tel:" + thisphone);
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                    startActivity(callIntent);
                                }
                            });
                        } else {
                            dialog.setTitle("Start Navigation");
                            dialog.setMessage("Are you sure you want to start this route?");
                            thisaction = "Yes";
                            thiscancel = "No";
                        }

                        dialog.setPositiveButton(thisaction, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                thistag = (String) button.getTag();
                                gettheroutes(thistag);
                                dialog.dismiss();
                            }
                        });

                        dialog.setNegativeButton(thiscancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        final AlertDialog alert = dialog.create();
                        alert.show();
                    }
                });

                container.addView(button);

                // Set margins for the container
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(margin, 0, margin, 30);
                container.setLayoutParams(layoutParams);

                // Add the container to the main layout
                LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
                linearLayout.addView(container);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        button.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_res));



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
        button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);  // This aligns the text to the left
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
        button.setTag("99999");//so not removed
        button.setText("Initializing..");
        button.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_background_res));


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // This code will be executed after a delay of 2 seconds
                button.setText("Sort List A - Z");
            }
        }, 4000); // Delay in milliseconds (2000ms = 2s)

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();

                if(tag.equals("99999")){
                    button.setTag("99998");
                    button.setText("Sort by Distance");
                    loadlist("venue");
                    loading.setText("Sorting A to Z .. loading");
                }else if(tag.equals("99998")){
                    button.setTag("99999");
                    button.setText("Sort List A - Z");
                    loadlist("distance");
                    loading.setText("Sorting by distance .. loading");

                }


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

       // button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
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



    public void  gettheroutes(String thisplace){





        try {

            System.out.println("https://xcape.ai/navigation/getroute.php?&id=" + thisplace );
            returnroute("https://xcape.ai/navigation/getroute.php?&id=" + thisplace );

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
                        activity.putExtra("placeid",thistag);
                        activity.putExtra("theroute",routenow);
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