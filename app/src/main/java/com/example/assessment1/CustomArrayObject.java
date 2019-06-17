package com.example.assessment1;

public class CustomArrayObject {


    private int id_;
    private String day;
    private String time;
    private String duration;
    private String room;

    public CustomArrayObject(int id_, String day, String prop2, String prop3, String prop4) {
        this.id_ = id_;
        this.day = day;
        this.time = prop2;
        this.duration = prop3;
        this.room = prop4;
    }


    public void setDay(String day) {
        this.day = day;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public int getId_() {
        return id_;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }

    public String getRoom() {
        return room;
    }
}

