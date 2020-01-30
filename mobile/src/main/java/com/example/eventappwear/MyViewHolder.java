package com.example.eventappwear;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView id, student_id, gps_lat, gps_long, student_message;
    ItemClickListener itemClickListener;

    public MyViewHolder(View itemView) {
        super(itemView);

        id = (TextView) itemView.findViewById(R.id.id);
        student_id = (TextView) itemView.findViewById(R.id.student_id);
        gps_lat = (TextView) itemView.findViewById(R.id.gps_lat);
        gps_long = (TextView) itemView.findViewById(R.id.gps_long);
        student_message = (TextView) itemView.findViewById(R.id.student_message);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
}
