package com.example.assessment1;

public class Cell {

    Cell() {
    }

    private int recordID;



    private int studentID;
    private String className;
    private String classRoom;
    private String classColour;
    private int startTime;
    private int duration;
    private int day;

    public int getRecordID() {
        return recordID;
    }

    void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getStudentID() {
        return studentID;
    }

    void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    String getClassName() {
        return className;
    }

    void setClassName(String className) {
        this.className = className;
    }

    String getClassRoom() {
        return classRoom;
    }

    void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    String getClassColour() {
        return classColour;
    }

    void setClassColour(String classColour) {
        this.classColour = classColour;
    }

    int getStartTime() {
        return startTime;
    }

    void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    int getDuration() {
        return duration;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }

    int getDay() {
        return day;
    }

    void setDay(int day) {
        this.day = day;
    }
}