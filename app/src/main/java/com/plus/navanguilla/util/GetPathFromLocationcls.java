package com.plus.navanguilla.util;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetPathFromLocationcls extends AsyncTask<String, Void, List<Routes>> {

    private TourPointListener listener;

    public GetPathFromLocationcls(TourPointListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Routes> doInBackground(String... urlString) {
        try {
            URL url = new URL(urlString[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }
            br.close();

            JSONObject jsonObject = new JSONObject(jsonData.toString());
            return new DirectionHelper().parse(jsonObject, null, null, false);
        } catch (Exception e) {
            Log.e("GetPathFromLocation", "Error", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Routes> result) {
        super.onPostExecute(result);
        if (listener != null && result != null) {
            listener.onTour(result);
        }
    }

}