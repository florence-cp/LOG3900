package com.example.polydraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import retrofit2.http.HTTP;

import static androidx.constraintlayout.widget.Constraints.TAG;

//source: https://medium.com/@lewisjkl/android-httpurlconnection-with-asynctask-tutorial-7ce5bf0245cd

public class HttpPost extends AsyncTask<String, Void, String> {
    JSONObject postData;
    public String status;
    public String token;
    public JSONObject player;
    public HttpPost(Map<String, String> postData) {
        if (postData != null) {
            this.postData = new JSONObject(postData);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println(status);
        System.out.println(player);
        System.out.println(token);

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");

            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            final int statusCode = urlConnection.getResponseCode();

            if(statusCode == 200||statusCode == 201){
                System.out.println(statusCode + ": Successful request");
                status = "success";
                if (statusCode < 299) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    String data = response.toString();
                    Gson gson = new Gson();
                    JSONObject reader = new JSONObject(data);
                    player  = reader.getJSONObject("player");
                    token = player.get("token").toString();

                }
            }

            else{
                System.out.println(statusCode + ": Something went wrong...");
                status = "failed";
            }


        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return status;
    }

}
