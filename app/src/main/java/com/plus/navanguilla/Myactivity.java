package com.plus.navanguilla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Myactivity extends AppCompatActivity {

    String getload;
    Handler handler2;
    String returnshift;
    String somebits;
    String option;
    String key;
    String locationnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity);
        handler2 = new Handler(Looper.getMainLooper());

        final LinearLayout layout = findViewById(R.id.scnf);



        try {
            doGetRequest("https://xcape.ai/navigation/loadactivities.php");




        } catch (IOException e) {
            e.printStackTrace();
        }







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
                button.setText(option);

                // Add an OnClickListener to handle button clicks
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle button click here
                       String  tag = (String) view.getTag();
                        // You can use the tag (index) to identify which button was clicked.

                        Intent intent = new Intent(getApplicationContext(), Loaditems.class);
                        intent.putExtra("list",tag);
                        startActivity(intent);

                    }
                });


                // Setting button height
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonParams.height = 80;  // adjust this value to your liking
                button.setTextSize(25);  // adjust this value to your liking
                int padding = 20;  // adjust this value to your liking
                button.setPadding(padding, padding, padding, padding);

                // Aligning text to the left and adding an image
                button.setGravity(Gravity.START);  // This aligns the text to the left
                int drawableLeft;
               // Log.i("side",tag);
                if(key.equals("1")) {
                     drawableLeft = R.drawable.beach;  // Replace with your drawable resource ID

                }else if(key.equals("2")){
                    drawableLeft = R.drawable.pineat;
                }else{
                    drawableLeft = R.drawable.pinmaps;
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
                        modifiednow = "18.177562900181787/-63.139738157244835";
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
        button.setTextSize(25);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
        button.setGravity(Gravity.START);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.maptour;  // Replace with your drawable resource ID

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

    private void needhelp(){

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.10);  // 30% of screen width

        Button button = new Button(this);
        button.setTag("1");
        button.setText("Need Help?");

        // Add an OnClickListener to handle button clicks
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                String  tag = (String) view.getTag();
                // You can use the tag (index) to identify which button was clicked.

                Intent intent = new Intent(getApplicationContext(), Loadmaps.class);
                intent.putExtra("list",tag);
                startActivity(intent);

            }
        });


        // Setting button height
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.height = 80;  // adjust this value to your liking
        button.setTextSize(25);  // adjust this value to your liking
        int padding = 20;  // adjust this value to your liking
        button.setPadding(padding, padding, padding, padding);

        // Aligning text to the left and adding an image
        button.setGravity(Gravity.START);  // This aligns the text to the left
        int drawableLeft;
        // Log.i("side",tag);

        drawableLeft = R.drawable.help;  // Replace with your drawable resource ID

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
                                islandtour();
                                needhelp();
                            }
                        });


                    }//end if




                });

    }
}