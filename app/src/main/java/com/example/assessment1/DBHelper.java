package com.example.assessment1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.security.PrivateKey;
import static android.content.ContentValues.TAG;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 1;

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

    public void insertCell (int studentID, String className, String classRoom, String classColour, int startTime, int duration, int day){
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
    }

    public Cell[] queryCellData(int studentID) {
        String query = "Select * FROM " + TIMETABLE_TABLE_NAME + " WHERE " + STUDENT_ID + " = " + "'" + studentID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int rowCount = cursor.getCount();
        Cell[] cell = new Cell[rowCount];

        //Loop through all cell objects and instantiate
        for (int i = 0; i < cell.length; i++)
            cell[i] = new Cell();

        //Move cursor to first item
        if (cursor.moveToFirst()){
            cursor.moveToFirst();

            //Loop through all items found in query and add to their own object in array
            for (int i = 0; i < cell.length; i++) {
                cell[i].setStudentID(Integer.parseInt(cursor.getString(0)));
                cell[i].setClassName(cursor.getString(1));
                cell[i].setClassRoom(cursor.getString(2));
                cell[i].setClassColour(cursor.getString(3));
                cell[i].setStartTime(Integer.parseInt(cursor.getString(4)));
                cell[i].setDuration(Integer.parseInt(cursor.getString(5)));
                cell[i].setDay(Integer.parseInt(cursor.getString(6)));
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            cell = null;
        }
        db.close();
        return cell;
    }

    public void updateCellRecord(int studentID, String className, String classRoom, int startTime, int duration, int day){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CLASS_LOCATION, classRoom);
        contentValues.put(CELL_START_TIME, startTime);
        contentValues.put(CELL_CLASS_DURATION, duration);
        contentValues.put(CELL_DAY, day);

        db.update(TIMETABLE_TABLE_NAME, contentValues, STUDENT_ID+" = '" + studentID + "'" + " AND " + CLASS_NAME + " = " + "'" + className + "'", null);

    }

    public int[] queryStudentIDs(){
        String query = "SELECT DISTINCT " + STUDENT_ID + " FROM " + TIMETABLE_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "queryStudentIDs: " + cursor.getCount());

        int[] allStudentIDs = new int[cursor.getCount()];

        if (cursor.moveToFirst())
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            allStudentIDs[i] = cursor.getInt(0);
            cursor.moveToNext();
        }

        cursor.close();

        return allStudentIDs;
    }

    public String[] queryStringAttributeForStudent(String searchAttribute, int studentID){
        String query = "SELECT DISTINCT " + searchAttribute + " FROM " + TIMETABLE_TABLE_NAME + " WHERE " + STUDENT_ID + " = " + "'" + studentID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String[] searchReturn = new String[cursor.getCount()];

        if (cursor.moveToFirst())
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            searchReturn[i] = cursor.getString(0);
            cursor.moveToNext();
        }

        cursor.close();

        return searchReturn;
    }

    public String[] queryCellDetails(String searchAttribute, int studentID, String className){
        String query = "SELECT " + searchAttribute + " FROM " + TIMETABLE_TABLE_NAME
                + " WHERE " + STUDENT_ID + " = " + "'" + studentID + "'"
                + " AND " + CLASS_NAME + " = " + "'" + className + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String[] searchReturn = new String[cursor.getCount()];

        if (cursor.moveToFirst())
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            if (searchAttribute.equals("classRoom")){
                searchReturn[i] = cursor.getString(0);
            }else{
                searchReturn[i] = Integer.toString(cursor.getInt(0));
            }

            cursor.moveToNext();
        }

        cursor.close();

        return searchReturn;
    }

    public long getTableLength() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TIMETABLE_TABLE_NAME);
        db.close();
        return count;
    }


    public int getTableLengthForStudentID(int studentID){
        String query = "Select * FROM " + TIMETABLE_TABLE_NAME + " WHERE " + STUDENT_ID + " = " + "'" + studentID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int rowCount = cursor.getCount();
        return rowCount;
    }


}

