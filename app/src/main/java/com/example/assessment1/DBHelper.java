package com.example.assessment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 1;
    public static final String STUDENT_TABLE_NAME = "student";
    public static final String STUDENT_COLUMN_ID = "_id";
    
    public static final String CLASS_TABLE_NAME = "class";
    public static final String CLASS_NAME = "className";
    public static final String CLASS_COLUMN_ID = "_id";
    public static final String CLASS_START_TIME = "startTime";
    public static final String CLASS_DURATION = "duration";
    public static final String CLASS_TUTOR = "tutor";
    public static final String CLASS_COLOUR = "colour";
    public static final String CLASS_DAY = "day";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STUDENT_TABLE_NAME + "(" +
                STUDENT_COLUMN_ID + " INTEGER PRIMARY KEY)"
        );
        db.execSQL("CREATE TABLE " + CLASS_TABLE_NAME +"(" +
                CLASS_COLUMN_ID + "INTEGER PRIMARY KEY,  " +
                CLASS_NAME + "TEXT, " +
                CLASS_START_TIME + "INTEGER, " +
                CLASS_DURATION + " INTEGER, " +
                CLASS_TUTOR + " TEXT, " +
                CLASS_COLOUR + "TEXT, " +
                CLASS_DAY + "INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertClass(String className, int startTime, int duration, String tutor, String colour, int day) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASS_NAME, className);
        contentValues.put(CLASS_START_TIME, startTime);
        contentValues.put(CLASS_DURATION, duration);
        contentValues.put(CLASS_TUTOR, tutor);
        contentValues.put(CLASS_COLOUR, colour);
        contentValues.put(CLASS_DAY, day);
        db.insert(CLASS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updatePerson(int id, String className, int startTime, int duration, String tutor, String colour, int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASS_NAME, className);
        contentValues.put(CLASS_START_TIME, startTime);
        contentValues.put(CLASS_DURATION, duration);
        contentValues.put(CLASS_TUTOR, tutor);
        contentValues.put(CLASS_COLOUR, colour);
        contentValues.put(CLASS_DAY, day);
        db.update(CLASS_TABLE_NAME, contentValues, CLASS_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Cursor getPerson(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + CLASS_TABLE_NAME + " WHERE " +
                CLASS_NAME + "=?", new String[] { className } );
        return res;
    }
    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + CLASS_TABLE_NAME, null );
        return res;
    }

    public Integer deleteClass(String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CLASS_TABLE_NAME,
                CLASS_NAME + " = ? ",
                new String[] { className });
    }

}

