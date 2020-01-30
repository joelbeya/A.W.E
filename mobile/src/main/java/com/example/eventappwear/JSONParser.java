package com.example.eventappwear;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

class JSONParser extends AsyncTask<Void,Void,Boolean> {

    Context c;
    String jsonData;
    RecyclerView rv;

    ProgressDialog pd;
    ArrayList<Message> messages = new ArrayList<>();

    public JSONParser(Context c, String jsonData, RecyclerView rv) {
        this.c = c;
        this.jsonData = jsonData;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return parse();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        pd.dismiss();

        if(isParsed)
        {
            //BIND
            rv.setAdapter(new MyAdapter(c,messages));

        }else
        {
            Toast.makeText(c, "Unable To Parse,Check Your Log output", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean parse()
    {
        try
        {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo;

            messages.clear();
            Message message;

            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);

                int id = jo.getInt("id");
                int student_id = jo.getInt("student_id");
                int gps_lat = jo.getInt("gps_lat");
                int gps_long = jo.getInt("gps_long");
                String student_message = jo.optString("student_message");

                message = new Message();

                message.setId(id);
                message.setStudent_id(student_id);
                message.setGps_lat(gps_lat);
                message.setGps_long(gps_long);
                message.setStudent_message(student_message);

                messages.add(message);
            }

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}