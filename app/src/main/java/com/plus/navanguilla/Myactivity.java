package com.plus.navanguilla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity);
        handler2 = new Handler(Looper.getMainLooper());

        final LinearLayout layout = findViewById(R.id.scnf);



        try {
            doGetRequest("https://axfull.com/nav/loadactivities.php");




        } catch (IOException e) {
            e.printStackTrace();
        }







    }


    public void initnav(String json) {

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = (int) (totalWidth * 0.30);  // 30% of screen width


        try {
            JSONObject jsonObject = new JSONObject(somebits);

            // Loop through the JSON object and create buttons
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);

                // Create a button with the index as a tag
                Button button = new Button(this);
                button.setTag(key);
                button.setText(value);

                // Add an OnClickListener to handle button clicks
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle button click here
                        String tag = (String) view.getTag();
                        // You can use the tag (index) to identify which button was clicked.

                        Intent intent = new Intent(getApplicationContext(), Loaditems.class);
                        intent.putExtra("list",tag);
                        startActivity(intent);

                    }
                });


                // Setting button height
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonParams.height = 80;  // adjust this value to your liking
                button.setLayoutParams(buttonParams);

                // Setting text size
                button.setTextSize(25);  // adjust this value to your liking

                // Setting padding
                int padding = 20;  // adjust this value to your liking
                button.setPadding(padding, padding, padding, padding);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(margin, 0, margin, 30);
                button.setLayoutParams(layoutParams);

                // Add the button to your layout (e.g., LinearLayout)
                LinearLayout linearLayout = findViewById(R.id.scnf); // Replace with your layout ID
                linearLayout.addView(button);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




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


                            }
                        });


                    }//end if




                });

    }
}