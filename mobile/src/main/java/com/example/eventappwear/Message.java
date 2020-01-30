package com.example.eventappwear;

public class Message {

    public int id;
    public int student_id;
    public double gps_lat;
    public double gps_long;
    public String student_message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public double getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(double gps_lat) {
        this.gps_lat = gps_lat;
    }

    public double getGps_long() {
        return gps_long;
    }

    public void setGps_long(double gps_long) {
        this.gps_long = gps_long;
    }

    public String getStudent_message() {
        return student_message;
    }

    public void setStudent_message(String student_message) {
        this.student_message = student_message;
    }
}
