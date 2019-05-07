package com.example.assessment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timetableDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String STUDENT_TABLE_NAME = "student";
    private static final String STUDENT_COLUMN_ID = "_id";
    
    private static final String CLASS_TABLE_NAME = "class";
    private static final String CLASS_NAME = "className";
    private static final String CLASS_COLUMN_ID = "_id";
    private static final String CLASS_COLOUR = "colour";

    private static final String CELL_TABLE_NAME = "cells";
    private static final String CELL_COLUMN_ID = "_id";
    private static final String CELL_START_TIME = "startTime";
    private static final String CELL_DURATION = "duration";
    private static final String CELL_TUTOR = "tutor";
    private static final String CELL_DAY = "day";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STUDENT_TABLE_NAME + "(" +
                STUDENT_COLUMN_ID + " INTEGER PRIMARY KEY)"
        );
        db.execSQL("CREATE TABLE " + CLASS_TABLE_NAME +"(" +
                CLASS_COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                CLASS_NAME + "TEXT, " +
                CLASS_COLOUR + "TEXT" + ")"
        );

        db.execSQL("CREATE TABLE " + CELL_TABLE_NAME + "(" +
                CELL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CELL_DAY + "INTEGER, " +
                CELL_START_TIME + "INTEGER, " +
                CELL_DURATION + "INTEGER, " +
                CELL_TUTOR + "STRING" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CELL_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertStudent(int studentID){
        //Init Database objects
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Add student ID to contentValues
        contentValues.put(STUDENT_COLUMN_ID, studentID);
        //Save contentValues to table
        db.insert(STUDENT_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertClass(String className, String colour) {
        //Init Database objects
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Add Class info to contentValues
        contentValues.put(CLASS_NAME, className);
        contentValues.put(CLASS_COLOUR, colour);

        //save contentValues to class table
        db.insert(CLASS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertCell (int day, int duration, int startTime, String tutor){
        //Init Database objects
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Add Cell info to contentValues
        contentValues.put(CELL_START_TIME, startTime);
        contentValues.put(CELL_DURATION, duration);
        contentValues.put(CELL_TUTOR, tutor);
        contentValues.put(CELL_DAY, day);

        //save contentValues to class table
        db.insert(CELL_TABLE_NAME, null, contentValues);
        return true;
    }

}

