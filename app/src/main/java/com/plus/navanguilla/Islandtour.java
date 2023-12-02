package com.plus.navanguilla;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.ahmadrosid.lib.drawroutemap.DrawMarker;
//import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
//import com.directions.route.Route;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.maps.android.SphericalUtil;
import com.plus.navanguilla.databinding.ActivityPickupBinding;
import com.plus.navanguilla.util.DirectionPointListener;
import com.plus.navanguilla.util.GetPathFromLocation;
import com.plus.navanguilla.util.GetPathFromLocationai;
import com.plus.navanguilla.util.GetPathFromLocationcls;
import com.plus.navanguilla.util.Routes;
import com.plus.navanguilla.util.Routes;
import com.plus.navanguilla.util.TourPointListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.AlertDialog;
import android.content.DialogInterface;

import static android.graphics.Color.RED;

public class Islandtour extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnPolylineClickListener,DirectionPointListener, TourPointListener {

    String dmylat;
    String dmylon ;
    String somebits;
    Handler handler2;
    private GoogleMap mMap;
    private ActivityPickupBinding binding;
    String responseBody;
    String responseCheck;
    String fname;
    String cunq;
    String thisorderid;
    String whataction;
    TextView distancetoplace;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    LinearLayout ll;
    LinearLayout lb;
    private List<Routes> routes = new ArrayList<>();
    private Marker infoMarker = null;

    private Marker mUserMarker;
    private Polyline mRoute;
    private Marker mStartMarker;
    private Marker drivermaker;
    String thephone;
    Button getback;
    Button prvieworders;
    Button startroutex;
    String itemid;
    String responseLocation;
    String locationnow;
    String theroute;
    PolylineOptions options;
    LatLng closestWaypoint = null;
    LatLng source;
    LatLng mysource;
    String closestWaypointStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityPickupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();
        handler2 = new Handler(Looper.getMainLooper());



        ll = (LinearLayout) findViewById(R.id.topbar);
        ll.setAlpha(0.5f);

        itemid = getIntent().getExtras().getString("itemid","defaultKey");


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }



        distancetoplace = (TextView)findViewById(R.id.distancetoplace);


        theroute = getIntent().getExtras().getString("theroute");
        // String theroute = getroute(cunq, thisorderid);

        getback = (Button)findViewById(R.id.dialcustomer);
        //startroutex = (Button)findViewById(R.id.startjourney);
        //prvieworders = (Button)findViewById(R.id.prvieworders);
















        getback.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {




                AlertDialog.Builder builder = new AlertDialog.Builder(Islandtour.this);
                builder.setTitle("Go Back");

                builder.setMessage(Html.fromHtml("<b>Return to list ?</b>"));

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String tag = (String) view.getTag();
                        Intent intent = new Intent(getApplicationContext(), Myactivity.class);
                        intent.putExtra("list",itemid);
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            }
        });




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mRoute = mMap.addPolyline(new PolylineOptions());


        boolean walkLine = true;
        //draw alternative routes if possible
        boolean alternatives = true;





        //String theroute = getroute(cunq, thisorderid, whataction);

        theroute = theroute.trim();
        String[] havles = theroute.split(Pattern.quote("~"));
        String mylocation;
        String mydestination;
        mylocation = havles[0];
        mydestination = havles[1];


        String[] latng = mylocation.split("/");
        String mylat = latng[0];
        String mylon = latng[1];


        String[] dlatng = mydestination.split("/");
        dmylat = dlatng[0];
        dmylon = dlatng[1];

        String sendroute = mylat +"," + mylon + ","+ dmylat + ","+dmylon;
/* WAY POINTS TO GET TIME ALL
        try {
            sendforroute("https://xcape.ai/navigation/fetchroutedetails.php?location="+sendroute);

        } catch (IOException e) {
            e.printStackTrace();
        }

*/


        Double mydoublelat = 0.0;
        try {
            mydoublelat = Double.parseDouble(mylat);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        Double mydoublelon = 0.0;
        try {
            mydoublelon = Double.parseDouble(mylon);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }


        Double myddoublelat = 0.0;
        try {
            myddoublelat = Double.parseDouble(dmylat);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        Double myddoublelon = 0.0;
        try {
            myddoublelon = Double.parseDouble(dmylon);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }


        String gosource = mydoublelat +","+mydoublelon;
        String godest = myddoublelat +","+myddoublelon;

        String getlocation = readFile().trim();
        String[] lochalves = getlocation.split(Pattern.quote(","));
        String mylatnow = lochalves[0];
        String mylonnow = lochalves[1];
        double myLatNowDouble = Double.parseDouble(mylatnow);
        double myLonNowDouble = Double.parseDouble(mylonnow);
        mysource  = new LatLng(myLatNowDouble, myLonNowDouble);

        try {
            goloadmap("https://xcape.ai/navigation/markwaypoints.php");

        } catch (IOException e) {
            e.printStackTrace();
        }

         source  = new LatLng(mydoublelat, mydoublelon);
        LatLng destination  = new LatLng(myddoublelat, myddoublelon);
        String waypointStr = getwaypoints();
        //waypoints = gosource + "|" + waypoints.trim() + "" + godest;


        String[] waypointArray = waypointStr.split("\\|");
        List<LatLng> waypoints = new ArrayList<>();
        for (String waypoint : waypointArray) {
            String[] latLong = waypoint.split(",");
            double latitude = Double.parseDouble(latLong[0]);
            double longitude = Double.parseDouble(latLong[1]);
            waypoints.add(new LatLng(latitude, longitude));
        }



        double minDistance = Double.MAX_VALUE;
        for (LatLng waypoint : waypoints) {
            double distance = SphericalUtil.computeDistanceBetween(mysource, waypoint);
            if (distance < minDistance) {
                closestWaypoint = waypoint;
                minDistance = distance;
            }
        }


        options = new PolylineOptions();
        String API_KEY = getResources().getString(R.string.google_maps_key);
        String url = " https://maps.googleapis.com/maps/api/directions/json?origin="+gosource+"&destination="+godest+"&sensor=false&alternatives=false&units=imperial&key="+API_KEY+"&waypoints="+waypointStr +"";
        Log.i("myurl",url);
        new GetPathFromLocationai(this).execute(url);

        if (closestWaypoint != null) {
            double lat = closestWaypoint.latitude;
            double lng = closestWaypoint.longitude;
             closestWaypointStr = lat + "," + lng;

            // Now closestWaypointStr is a string in the format "lat,long"
            // You can use closestWaypointStr in your URL for the API request
        }

        String urlcls = " https://maps.googleapis.com/maps/api/directions/json?origin="+getlocation+"&destination="+closestWaypointStr+"&sensor=false&alternatives=false&units=imperial&key="+API_KEY;
        Log.i("myurl",urlcls);
        new GetPathFromLocationcls(this).execute(urlcls);



/*
        new GetPathFromLocation(source, waypoints, destination, alternatives, walkLine, API_KEY, new DirectionPointListener() {
            @Override
            public void onPath(List<Routes> allRoutes) {
                routes = allRoutes;
                drawRoutes();
                drawDuration(0);
            }
        }).execute();
*/

        mMap.setOnPolylineClickListener(this);

        //mMap.setOnPolylineClickListener(this);
        // Add a marker in Sydney and move the camera
        LatLng anguilla = new LatLng(myLatNowDouble, myLonNowDouble);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setTrafficEnabled(true);
        drivermaker = mMap.addMarker(new MarkerOptions()
                .position(anguilla)
                .title("My Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker))
        );

        // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(anguilla, 16.0f);
        //mMap.animateCamera(cameraUpdate);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(anguilla));
        // mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );

        LatLng location = new LatLng(myddoublelat, myddoublelon);
        Marker marker =  mMap.addMarker(new MarkerOptions().position(location).title("Destination"));
        marker.showInfoWindow();
/*
        drivermaker = mMap.addMarker(new MarkerOptions().position(anguilla).title("My Location")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_directions_car_24)));


 */
        //mMap.addMarker(new MarkerOptions().position(source).title("SOURCE"));
        //mMap.addMarker(new MarkerOptions().position(destination).title("DEST"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(anguilla));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );
        //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mydoublelat,mydoublelon), 16.0f), 4000, null);
    }


    @Override
    public void onPath(List<Routes> routes) {
        for (Routes route : routes) {
            PolylineOptions options = new PolylineOptions();
            options.addAll(route.drivingRoute);
            options.width(10);
            options.color(Color.RED);
            mMap.addPolyline(options);
        }
/*
        // Create and add polyline to the closest waypoint
        if (closestWaypoint != null) {
            PolylineOptions closestWaypointOptions = new PolylineOptions();
            closestWaypointOptions.add(mysource);
            closestWaypointOptions.add(closestWaypoint);
            closestWaypointOptions.width(10);
            closestWaypointOptions.color(Color.BLUE); // Different color for this polyline

            mMap.addPolyline(closestWaypointOptions);
        }

 */
    }

    @Override
    public void onTour(List<Routes> routes) {
        for (Routes route : routes) {
            PolylineOptions options = new PolylineOptions();
            options.addAll(route.drivingRoute);
            options.width(10);
            options.color(Color.BLUE);
            mMap.addPolyline(options);
        }

    }


    /*
    //a dotted pattern for the walk line
    final List<PatternItem> pattern = Arrays.asList(new Dot(), new Gap(20));
    // color for different routes
    final int routeColors[] = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

    private void drawRoutes() {
        Routes route = null;
        int color = routeColors[0];
        //iterate over all routes
        for (int i = 0; i < routes.size(); i++) {
            route = routes.get(i);
            color = routeColors[i >= routes.size() ? 0 : i];
            //draw the driving route
            options = new PolylineOptions();
            options.addAll(route.drivingRoute);
            options.width(10);
            options.color(color);
            options.clickable(true);

           // options.add(new LatLng(18.25696954161798, -62.996735501521876)); // example point
            //options.add(new LatLng(18.252026375083627, -63.03074350126321)); // example point
            //add the route to the map
            Polyline drivingRoute = mMap.addPolyline(options);
            //add tag to the route to be accessible
            drivingRoute.setTag(route.route_id);
        }
        //here we draw the dotted walk line once
        if (route != null && route.destWalk != null) {
            //the dotted line between source->near driving route
            PolylineOptions destWalk = new PolylineOptions()
                    .addAll(route.destWalk)
                    .width(10)
                    .color(color)
                    .pattern(pattern);
            //the dotted line between dest->last driving route
            PolylineOptions srcWalk = new PolylineOptions()
                    .addAll(route.sourceWalk)
                    .width(10)
                    .color(color)
                    .pattern(pattern);
            //add both routes to the map
            mMap.addPolyline(destWalk);
            mMap.addPolyline(srcWalk);
        }
    }
*/


    void goloadmap(String url) throws IOException {
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


                                loadpointers(somebits);

                            }
                        });


                    }//end if




                });

    }



    public void loadpointers(String json) {

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extracting data from JSON object
                String placeId = jsonObject.getString("placeid");

                double dlat = jsonObject.getDouble("dlat");
                double dlon = jsonObject.getDouble("dlon");

                String cord = dlat + "/"+dlon;


                    LatLng location = new LatLng(dlat, dlon);
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(placeId)
                            .icon(createCustomMarker(placeId + "\n", Color.BLUE, Color.WHITE)));
                    marker.setTag(cord);




            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    private BitmapDescriptor createCustomMarker(String text, int bgColor, int textColor) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(15); // Text size
        textPaint.setColor(textColor); // Text color

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(bgColor); // Background color

        // Calculate the width and height of the text
        float baseline = -textPaint.ascent(); // ascent() is negative
        int width = (int) (textPaint.measureText(text) + 20f); // Add some padding
        int height = (int) (baseline + textPaint.descent() + 20f);

        // Define pointer size
        int pointerWidth = 20;
        int pointerHeight = 10;

        // Increase height to accommodate the pointer
        height += pointerHeight;

        // Create a bitmap and draw background and text on it
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawRect(0, 0, width, height - pointerHeight, backgroundPaint); // Draw background
        canvas.drawText(text, 10, baseline + 10, textPaint); // Draw text

        // Draw the pointer
        Path path = new Path();
        path.moveTo((width - pointerWidth) / 2, height - pointerHeight); // Left point
        path.lineTo(width / 2, height); // Bottom point
        path.lineTo((width + pointerWidth) / 2, height - pointerHeight); // Right point
        path.close();

        canvas.drawPath(path, backgroundPaint); // Draw the pointer with background paint

        return BitmapDescriptorFactory.fromBitmap(image);
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



    public String getwaypoints() {

        String thisdevice = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String location = readFile().trim();
        //String modifiednow = locationnow.replace(',', '/');
        String url = "https://xcape.ai/navigation/getwaypoints.php?location="+location;
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

    private void drawDuration(int route_id) {
        //select route by id from multiple routes
        Routes route = null;
        for (Routes r : this.routes) {
            if (r.route_id == route_id) {
                route = r;
                break;
            }
        }
        if (route == null) return;

        /*get route duration*/
        //text value ex '8 mins'
        String text_duration = route.text_duration;
        //value in seconds ex '469'
        double duration = route.duration;

        /*get route distance*/
        //text value ex '12 km'
        String text_distance = route.text_distance;
        //value in meter ex '12000'
        double distance = route.distance;
        //select the middle point on the marker
        LatLng middlePoint = route.drivingRoute.get(route.drivingRoute.size() / 2);
        //draw window info to show the distance and duration
        if (infoMarker != null) infoMarker.remove();


        //distancetoplace.setText(text_duration + " - " + text_distance);

        /*
        infoMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(middlePoint)
                        .title(text_duration)
                        .snippet(text_distance)
        );

        infoMarker.showInfoWindow();
           */
    }


    void sendforroute(String url) throws IOException {
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

                                distancetoplace.setText(somebits);


                            }
                        });


                    }//end if




                });

    }

    @Override
    public void onPolylineClick(Polyline route) {
        //set the clicked route at the top
        route.setZIndex(route.getZIndex() + 1);
        //do something with the selected route..
        drawDuration((int) route.getTag());
    }







    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        // This registers messageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("my-location"));
    }




    // Handling the received Intents for the "my-integer" event
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String mylat = intent.getStringExtra("mylat"); // -1 is going to be used as the default value
            String mylon = intent.getStringExtra("mylon");
            String mybearing = intent.getStringExtra("mybearing");

            System.out.println("degres  lat :" + mylat + " long : " +  mylon);
            Double mydoublelat = 0.0;
            try {
                mydoublelat = Double.parseDouble(mylat);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            Double mydoublelon = 0.0;
            try {
                mydoublelon = Double.parseDouble(mylon);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            float thebearing = 0;
            try {
                thebearing = Float.parseFloat(mybearing);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }


            CameraPosition position = CameraPosition.builder()
                    .bearing(thebearing)
                    .target(new LatLng(mydoublelat, mydoublelon))
                    .zoom(mMap.getCameraPosition().zoom)
                    .tilt(mMap.getCameraPosition().tilt)
                    .build();

            //mMap.clear();
            drivermaker.remove();
            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(mydoublelat, mydoublelon));
            mp.title("my position");
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));
            drivermaker = mMap.addMarker(mp);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));


            String nextroute = mydoublelat + "," + mydoublelon + "," + dmylat + "," + dmylon ;

            try {
                sendforroute("https://xcape.ai/navigation/fetchroutedetails.php?location="+nextroute);

            } catch (IOException e) {
                e.printStackTrace();
            }

/*
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mydoublelat, mydoublelon), 16));
*/

        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
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

    /*
    @Override
    protected void onStart() {
        super.onStart();

        // Store our shared preference- NOT IN USE - TO GET
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.commit();

    }


     */








    @Override
    public void onBackPressed() {

    }


    public String getroute(String drvierid, String theorder, String action){

        String thisdevice = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String url = "https://axcess.ai/barapp/driver_route.php?&action=" + action + "&driverid="+drvierid + "&orderid=" + theorder;
        Log.i("action url",url);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("what","this" )

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
            responseBody =  response.body().string();
            Log.i("respBody:main",responseBody);
            Log.i("MSG",resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;


    }





}