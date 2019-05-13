package com.example.assessment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.PrivateKey;

import static android.content.ContentValues.TAG;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 5;

    //Assign names to table and attributes.

    private static final String TIMETABLE_TABLE_NAME = "timetable";
    private static final String STUDENT_ID = "studentID";
    private static final String CLASS_NAME = "className";
    private static final String CLASS_LOCATION = "classRoom";
    private static final String CLASS_COLOUR = "classColour";
    private static final String CELL_START_TIME = "startTime";
    private static final String CELL_CLASS_DURATION = "cellDuration";
    private static final String CELL_DAY = "day";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TIMETABLE_TABLE_NAME + "(" +
                STUDENT_ID + " INTEGER, " +
                CLASS_NAME + " TEXT, " +
                CLASS_LOCATION + " TEXT, " +
                CLASS_COLOUR + " TEXT, " +
                CELL_START_TIME + " INTEGER, " +
                CELL_CLASS_DURATION + " INTEGER, " +
                CELL_DAY + " INTEGER " +")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertCell (int studentID, String className, String classRoom, String classColour, int startTime, int duration, int day){
        //Init Database objects
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Add Cell info to contentValues
        contentValues.put(STUDENT_ID, studentID);
        contentValues.put(CLASS_NAME, className);
        contentValues.put(CLASS_LOCATION, classRoom);
        contentValues.put(CLASS_COLOUR, classColour);
        contentValues.put(CELL_START_TIME, startTime);
        contentValues.put(CELL_CLASS_DURATION, duration);
        contentValues.put(CELL_DAY, day);
        //save contentValues to class table
        db.insert(TIMETABLE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cell queryCellData(int studentID, int recordsToSkip) {
        String query = "Select * FROM " + TIMETABLE_TABLE_NAME + " WHERE " + STUDENT_ID + " = " + "'" + studentID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Cell cell = new Cell();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            for (int i = 0; i < recordsToSkip; i++)
                cursor.moveToNext();

            cell.setStudentID(Integer.parseInt(cursor.getString(0)));
            cell.setClassName(cursor.getString(1));
            cell.setClassRoom(cursor.getString(2));
            cell.setClassColour(cursor.getString(3));
            cell.setStartTime(Integer.parseInt(cursor.getString(4)));
            cell.setDuration(Integer.parseInt(cursor.getString(5)));
            cell.setDay(Integer.parseInt(cursor.getString(6)));

            cursor.close();
        } else {
            cell = null;
        }
        db.close();
        return cell;
    }

    public long getTableLength(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TIMETABLE_TABLE_NAME);
    }

}

