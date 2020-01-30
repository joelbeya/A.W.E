package com.example.eventappwear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView idTxt, student_idTxt, gps_latTxt, gps_longTxt, student_messageTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        idTxt = (TextView) findViewById(R.id.idTxt);
        student_idTxt= (TextView) findViewById(R.id.student_idTxt);
        gps_latTxt = (TextView) findViewById(R.id.gps_latTxt);
        gps_longTxt = (TextView) findViewById(R.id.gps_longTxt);
        student_messageTxt = (TextView) findViewById(R.id.student_messageTxt);

        //GET INTENT
        Intent i=this.getIntent();

        //RECEIVE DATA
        String id=i.getExtras().getString("ID_KEY");
        String student_id=i.getExtras().getString("STUDENT_ID_KEY");
        String gps_lat=i.getExtras().getString("GPS_LAT_KEY");
        String gps_long=i.getExtras().getString("GPS_LONG_KEY");
        String student_message=i.getExtras().getString("STUDENT_MESSAGE_KEY");

        //BIND DATA
        idTxt.setText(id);
        student_idTxt.setText(student_id);
        gps_latTxt.setText(gps_lat);
        gps_longTxt.setText(gps_long);
        student_messageTxt.setText(student_message);
    }
}
