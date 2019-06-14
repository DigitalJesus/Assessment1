package com.example.assessment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 2;

    //Assign names to table and attributes.

    private static final String TIMETABLE_TABLE_NAME = "timetable";
    private static final String CELL_ID = "_id";
    private static final String STUDENT_ID = "studentID";
    private static final String CLASS_NAME = "className";
    private static final String CLASS_LOCATION = "classRoom";
    private static final String CLASS_COLOUR = "classColour";
    private static final String CELL_START_TIME = "startTime";
    private static final String CELL_DURATION = "cellDuration";
    private static final String CELL_DAY = "day";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TIMETABLE_TABLE_NAME + "(" +
                CELL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_ID + " INTEGER, " +
                CLASS_NAME + " TEXT, " +
                CLASS_LOCATION + " TEXT, " +
                CLASS_COLOUR + " TEXT, " +
                CELL_START_TIME + " INTEGER, " +
                CELL_DURATION + " INTEGER, " +
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
        contentValues.put(CELL_DURATION, duration);
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
                cell[i].setStudentID(Integer.parseInt(cursor.getString(1)));
                cell[i].setClassName(cursor.getString(2));
                cell[i].setClassRoom(cursor.getString(3));
                cell[i].setClassColour(cursor.getString(4));
                cell[i].setStartTime(Integer.parseInt(cursor.getString(5)));
                cell[i].setDuration(Integer.parseInt(cursor.getString(6)));
                cell[i].setDay(Integer.parseInt(cursor.getString(7)));
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            cell = null;
        }
        db.close();
        return cell;
    }

    public void updateRow(int studentID, String className, int day, String classRoom, int startTime, int duration, int position){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery(STUDENT_ID+" = '" + studentID + "'" + " AND " + CLASS_NAME + " = " + "'" + className + "'", null);

        Log.d(TAG, "number of records: " + cursor.getCount());

        for (int i = 0; i < position; i++) {
            cursor.moveToNext();
        }
        int modifiedID = cursor.getInt(0);

        if (startTime >= 0)
            contentValues.put(CELL_START_TIME, startTime);
        if (duration >= 0)
            contentValues.put(CELL_DURATION, duration);
        if (classRoom != null && !classRoom.isEmpty())
            contentValues.put(CLASS_LOCATION, classRoom);
        if (day >= 0)
            contentValues.put(CELL_DAY, day);

        db.update(TIMETABLE_TABLE_NAME, contentValues, CELL_ID + " = " + modifiedID, null);

        db.close();
        cursor.close();

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

    public void deleteStudent(int studentID){
        String argument = STUDENT_ID + " = " + "'" + studentID + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TIMETABLE_TABLE_NAME, argument, null);
    }


    public int getTableLengthForStudentID(int studentID){
        String query = "Select * FROM " + TIMETABLE_TABLE_NAME + " WHERE " + STUDENT_ID + " = " + "'" + studentID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int rowCount = cursor.getCount();
        return rowCount;
    }


}

