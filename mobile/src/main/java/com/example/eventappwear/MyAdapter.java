package com.example.eventappwear;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    List<Message> messages;

    public MyAdapter(Context c, List<Message> messages) {
        this.c = c;
        this.messages = messages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Message message= messages.get(position);

        final int idX = message.getId();
        final int student_idX = message.getStudent_id();
        final double gps_latX = message.getGps_lat();
        final double gps_longX = message.getGps_long();
        final String student_messageX = message.getStudent_message();


        //BIND
        holder.id.setText(String.valueOf(idX));
//        holder.student_id.setText(String.valueOf(student_idX));
//        holder.gps_lat.setText(String.valueOf(gps_latX));
//        holder.gps_long.setText(String.valueOf(gps_longX));
        holder.student_message.setText(student_messageX);

        holder.setItemClickListener(pos -> openDetailActivity(idX,student_idX,gps_latX,gps_longX,student_messageX));

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    ////open activity
    private void openDetailActivity(int id, int student_id, double gps_lat, double gps_long, String student_messageX)
    {
        Intent i=new Intent(c,DetailActivity.class);
        i.putExtra("ID_KEY", "" + id);
        i.putExtra("STUDENT_ID_KEY", "" + student_id);
        i.putExtra("GPS_LAT_KEY", "" + gps_lat);
        i.putExtra("GPS_LONG_KEY", "" + gps_long);
        i.putExtra("STUDENT_MESSAGE_KEY", "" + student_messageX);


        c.startActivity(i);

    }

}
