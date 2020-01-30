package com.example.eventappwear;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class AsyncHTTPPost extends AsyncTask<String, Integer, String> {

    private AppCompatActivity myActivity;

    public AsyncHTTPPost(AppCompatActivity mainActivity) {
        myActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... strings) {
        publishProgress(1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = null;
        HttpURLConnection urlConnection = null;
        String result = null;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");


            JSONObject jsonParam = new JSONObject();

            jsonParam.put("student_id", "21511843");
            jsonParam.put("gps_lat", "34.001");
            jsonParam.put("gps_long", "3.235");
            jsonParam.put("student_message", "message777");

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();
//
//            urlConnection.connect();
            Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
            Log.i("MSG" , urlConnection.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
//        ProgressBar pb = (ProgressBar) myActivity.findViewById(R.id.progressBar);
//        pb.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
//        TextView text = (TextView) myActivity.findViewById(R.id.text);
//        text.setText(s); // Updates the textview
//        ProgressBar pb = (ProgressBar) myActivity.findViewById(R.id.progressBar);
//        pb.setProgress(5);
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}
