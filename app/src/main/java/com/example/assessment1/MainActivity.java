package com.example.assessment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      checkForSavedStudentID();

      File database = getApplicationContext().getDatabasePath("timetable.db");

      if (!database.exists()) {
         dbInsertData();
         Log.i("Database", "Not Found");
      } else {
         Log.i("Database", "Found");
      }

   }

   @Override
   protected void onStart() {
      super.onStart();
      setContentView(R.layout.activity_main);
      Toolbar toolbar = findViewById(R.id.app_bar);
      setSupportActionBar(toolbar);
      Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

      setToolbarText(getStudentID());

      if (!getStudentID().isEmpty())
         buildTimetable(getStudentIDInt());
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == R.id.changeStudentID) {
         Intent intent = new Intent(this, LoginActivity.class);
         startActivity(intent);
         return true;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.overflow_menu, menu);
      return true;
   }

   private void buildTimetable(int studentID) {
      DBHelper dbHelper = new DBHelper(this);
      Cell[] cell = dbHelper.queryCellData(studentID);

      if (cell == null) {
         Toast.makeText(this, "No timetable for this ID", Toast.LENGTH_LONG).show();
         return;
      }

      String[] weekDayIndex = {"mon_hr", "tues_hr", "wed_hr", "thurs_hr", "fri_hr"};

      for (int i = 0; i < dbHelper.getTableLengthForStudentID(getStudentIDInt()); i++) {
         String currentTextviewID = weekDayIndex[cell[i].getDay()] + cell[i].getStartTime();
         TextView selectedTime = (findViewById(getResources().getIdentifier(currentTextviewID, "id", getPackageName())));

         createTimetableCell(cell[i].getClassName(), cell[i].getClassRoom(), cell[i].getClassColour(), selectedTime);

         //This for loop colours in the hours after a class depending on the duration of the class.
         for (int j = 1; j < (cell[i].getDuration()); j++) {
            String modifiedIDString = weekDayIndex[cell[i].getDay()] + (cell[i].getStartTime() + (j));
            TextView durationEdit = (findViewById(getResources().getIdentifier(modifiedIDString, "id", getPackageName())));

            try {
               createTimetableCell("", "", cell[i].getClassColour(), durationEdit);
            } catch (Exception e) {
               Log.d(TAG, "buildTimetable: Class duration extended beyond the end of the day");
               break;
            }
         }
      }
   }

   private void makeToast(String toastString) {
      Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
   }

   private String getStudentID() {
      SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
      return mPrefs.getString("studentID", "");
   }

   private void setToolbarText(String methodInput) {
      TextView title = findViewById(R.id.timetable_title);
      title.setText(methodInput);
   }

   private void createTimetableCell(String className, String room, String hex, TextView selectedTime) {
      String outputText = className + "\n" + room;
      selectedTime.setText(outputText);
      selectedTime.setBackgroundColor(Color.parseColor(hex));
   }

   private void checkForSavedStudentID() {
      SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);

      if (Objects.requireNonNull(mPrefs.getString("studentID", "")).isEmpty() || Objects.equals(mPrefs.getString("studentID", ""), "")) {
         Intent intent = new Intent(this, LoginActivity.class);
         startActivity(intent);
      }
   }

   public void insertCell(int studentID, String className, String classRoom, String classColour, int startTime, int duration, int day) {
      DBHelper dbHelper = new DBHelper(this);
      dbHelper.insertCell(studentID, className, classRoom, classColour, startTime, duration, day);
      dbHelper.close();
   }

   public int getStudentIDInt() {
      SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
      return Integer.parseInt(Objects.requireNonNull(mPrefs.getString("studentID", "")));
   }

   public Cell getNextClass(int studentID, Context context){
      DBHelper dbHelper = new DBHelper(context);

      Cell[] cell = dbHelper.queryCellData(studentID);

      Date currentTime = Calendar.getInstance().getTime();

      int currentDay = getCurrentDay(currentTime.toString());
      Log.d(TAG, "currentDay: " + currentDay);

      String strDateFormat = "hh a";
      DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
      String formattedDate= dateFormat.format(currentTime);
      int currentHour = getCurrentHour(formattedDate);

      Log.d(TAG, "getNextClass: FormattedDate: "+formattedDate);
      Log.d(TAG, "currentHour: " + currentHour);
      Log.d(TAG, "numberOfCells: " +cell.length);

      Cell nextClass = cell[0];

      for (int i = 0; i < cell.length; i++) {
         Log.d(TAG, "getNextClass: loop#:"+i);
         if (cell[i].getDay() > currentDay && cell[i].getDay() < nextClass.getDay()){
            Log.d(TAG, cell[i].getDay() + " is closer to " + currentDay + " than " + nextClass.getDay());
            if (cell[i].getStartTime() > currentHour && cell[i].getStartTime() < nextClass.getStartTime()){
               Log.d(TAG, cell[i].getStartTime() + " is closer to " + currentHour + " than " + nextClass.getStartTime());

               nextClass = cell[i];
            }
         }

      }
      return nextClass;
   }

   private int getCurrentHour(String formattedDate) {
      int temp = -1;
      if (formattedDate.endsWith("am")){
         temp = Integer.parseInt(formattedDate.split(" ")[0]);
      }else if (formattedDate.endsWith("pm")){
         temp = Integer.parseInt(formattedDate.split(" ")[0]) + 12;
      }
      return temp;
   }

   private int getCurrentDay(String currentTime) {
      if (currentTime.startsWith("Mon")){
         return 0;
      }else if ( currentTime.startsWith("Tues")){
         return 1;
      }else if ( currentTime.startsWith("Wed")){
         return 2;
      }else if ( currentTime.startsWith("Thur")){
         return 3;
      }else if ( currentTime.startsWith("Fri")){
         return 4;
      }else if ( currentTime.startsWith("Sat")){
         return 5;
      }else if ( currentTime.startsWith("Sun")){
         return 6;
      }else
         return -1;
   }

   private void dbInsertData() {

      String[] hex = {
              "#00A878",
              "#55DDE0",
              "#FFB238",
              "#ED474A"
      };

      //Harry's Timetable
      int studentID = 2173727;

      String[] classNames = {"Mobile App", "Math", "Embedded Sys", "Sys Analysis"};

      String[] classRoom = {"C304", "C117", "B106", "B107", "C305", "T403", "C117", "B107", "T403", "T401"};


      int[][] timetableData = {
              {0, 1, 0, 2, 3, 3, 1, 2, 1, 1}, // CourseID 0
              {0, 1, 1, 1, 1, 2, 3, 3, 3, 4}, // Day      1
              {12, 8, 9, 13, 15, 12, 8, 9, 13, 8}, // Time     2
              {1, 1, 3, 2, 2, 2, 1, 2, 2, 1}  // Duration 3
      };

      for (int i = 0; i < timetableData[0].length; i++) {
         insertCell(studentID,                       //StudentID
                 classNames[timetableData[0][i]],    //CourseName
                 classRoom[i],                       //Classroom
                 hex[timetableData[0][i]],           //Colour
                 timetableData[2][i],                //Time
                 timetableData[3][i],                //Duration
                 timetableData[1][i]);               //Day

      }

      //Drew Mays' Timetable
      studentID = 2160696;

      classNames = new String[]{"Math", "Networking", "App Dev", "Sys Ana"};

      classRoom = new String[]{"C304", "C117", "B106", "B107", "C305", "T403", "C117", "B107", "T403", "T401", "B107"};


      timetableData = new int[][]{
              {1, 2, 0, 2, 3, 3, 0, 1, 0, 0, 1}, // CourseID 0
              {0, 0, 1, 1, 1, 2, 3, 3, 3, 4, 4}, // Day      1
              {10, 12, 8, 9, 15, 12, 8, 9, 12, 8, 12}, // Time     2
              {1, 1, 1, 3, 2, 2, 1, 2, 2, 1, 2}  // Duration 3
      };

      for (int i = 0; i < timetableData[0].length; i++) {
         insertCell(studentID,                       //StudentID
                 classNames[timetableData[0][i]],    //CourseName
                 classRoom[i],                       //Classroom
                 hex[timetableData[0][i]],           //Colour
                 timetableData[2][i],                //Time
                 timetableData[3][i],                //Duration
                 timetableData[1][i]);               //Day

      }
   }
}