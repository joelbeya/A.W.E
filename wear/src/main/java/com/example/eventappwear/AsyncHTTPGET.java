package com.example.eventappwear;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class AsyncHTTPGET extends AsyncTask<String, Integer, String> {

    private WearableActivity myActivity;
    ArrayList<Message> messages = new ArrayList<>();

    public AsyncHTTPGET(WearableActivity mainActivity) {
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
            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream
            publishProgress(2);
            result = readStream(in); // Read stream


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        publishProgress(4);
        return result; // returns the result
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        ProgressBar pb = (ProgressBar) myActivity.findViewById(R.id.progressBar);
        pb.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        TextView text = (TextView) myActivity.findViewById(R.id.text);
        text.setText(s); // Updates the textview
        ProgressBar pb = (ProgressBar) myActivity.findViewById(R.id.progressBar);
        pb.setProgress(5);
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);

        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line+"n");
        }
        is.close();
        return sb.toString();
    }

    private class MyAdapter implements ListAdapter {

        public MyAdapter(WearableActivity myActivity, ArrayList<Message> messages) {
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}