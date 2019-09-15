package com.example.assessment1;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;
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

      if (!getStudentID().isEmpty()) {
         buildTimetable(Integer.parseInt(getStudentID()));

         //Update the widget whenever mainactivity is shown to the user.
         Intent intent = new Intent(this, NextClassWidget.class);
         intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
         int[] ids = AppWidgetManager.getInstance(getApplication())
                 .getAppWidgetIds(new ComponentName(getApplication(), NextClassWidget.class));
         intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
         sendBroadcast(intent);
      }
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

   void buildTimetable(int studentID) {
      DBHelper dbHelper = new DBHelper(this);
      Cell[] cell = dbHelper.queryCellData(studentID);

      if (cell == null) {
         Toast.makeText(this, "No timetable for this ID", Toast.LENGTH_LONG).show();
         return;
      }

      String[] weekDayIndex = {"mon_hr", "tues_hr", "wed_hr", "thurs_hr", "fri_hr"};

      for (int i = 0; i < dbHelper.getTableLengthForStudentID(Integer.parseInt(getStudentID())); i++) {
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

   void setToolbarText(String methodInput) {
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

   String getStudentID() {
      SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
      return mPrefs.getString("studentID", "");
   }

   public Cell getNextClass(int studentID, Context context){
      DBHelper dbHelper = new DBHelper(context);

      Cell[] cell = dbHelper.queryCellData(studentID);

      Date currentTime = Calendar.getInstance().getTime();
      DateFormat dateFormat = new SimpleDateFormat("hh a");
      String formattedDate= dateFormat.format(currentTime);

      int currentDay = getCurrentDay(currentTime.toString());
      int currentHour = getCurrentHour(formattedDate);

      ArrayList<Cell> classesInDay = new ArrayList<>();

      Cell nextClass = cell[0];
      int loopCount = 0;

      boolean repeat = true;
      do {
         loopCount++;
         if (currentHour >= 17) {
            currentDay++;  //If the current hour is past the end of the day, add 1 to the day so that tomorrow is scanned.
            currentHour = 8;
         }
         if (currentDay > 4) {
            currentDay = 0;
            currentHour = 8;
         }

         Log.d(TAG, "getNextClass: loop! \tDay: " + currentDay + "\t hour: " + currentHour);

         classesInDay.clear();

         for (int i = 0; i < cell.length; i++) {
            if (cell[i].getDay() == currentDay){
               classesInDay.add(cell[i]);
            }
         }
         if (classesInDay.size() == 0){
            currentDay++;
            currentHour = 8;
            continue;
         }
         boolean classesAreAfterCurrentHour = false;
         for (Cell cell1: classesInDay) {
            if (cell1.getStartTime() > currentHour) {
               classesAreAfterCurrentHour = true;
               break;
            }
         }
         if (!classesAreAfterCurrentHour){
            currentDay++;
            currentHour = 8;
            continue;
         }

         nextClass = classesInDay.get(0);
         repeat = false;

         for (int i = 1; i < (classesInDay.size()) ; i++) {
            int diff1 = classesInDay.get(i).getStartTime() - currentHour;
            int diff2 = nextClass.getStartTime() - currentHour;


            if (diff1 < 0){                     //This means that the Test cell is before the current hour

            }else if(diff2 < 0){                //This means that the current choice is before the current hour
               nextClass = classesInDay.get(i);
            }else if (diff1 < diff2){           //This means that the test cell is closer to the current hour than the current choice
               nextClass = classesInDay.get(i);
            }else if (diff2 < diff1){           //This means that the current choice is closer to the current hour than the test cell

            }
         }

      }while(repeat);


      return nextClass;
   }

   private int getCurrentHour(String formattedDate) {
      int temp = -1;
      if (formattedDate.endsWith("am")){
         temp = Integer.parseInt(formattedDate.split(" ")[0]);
      }else if (formattedDate.endsWith("pm")){
         temp = Integer.parseInt(formattedDate.split(" ")[0]) + 12;
      }

      //Fixing above error where midday any midnight were swapped
      if (temp == 12){
         temp = 0;
      }else if(temp == 24){
         temp = 12;
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
      }else if ( currentTime.startsWith("Thu")){
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

      String[] classNames = {"Proj. Management", "Cloud Computing", "Secure Coding", "Game Dev."};

      String[] classRoom = {"C305", "C117", "B101", "C117", "T304", "B107", "C303", "C117", "B102"};


      int[][] timetableData = {
              {0, 1, 2, 2, 0, 1, 0, 3, 3}, // CourseID 0
              {0, 0, 0, 0, 1, 1, 1, 2, 2}, // Day      1
              {9, 12, 13, 16, 9, 12, 15, 12, 14}, // Time     2
              {1, 1, 3, 1, 2, 3, 1, 1, 3}  // Duration 3
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

      classNames = new String[]{"Communications", "Data Science", "Secure Testing", "Game Dev."};

      classRoom = new String[]{"B314", "C117", "C302", "B102", "B101", "C117", "B102", "C305", "C302"};


      timetableData = new int[][]{
              {0, 2, 1, 1, 2, 3, 3, 0, 1}, // CourseID 0
              {0, 0, 1, 1, 2, 2, 2, 3, 4}, // Day      1
              {10, 16, 8, 11, 9, 12, 14, 8, 8}, // Time     2
              {3, 1, 1, 3, 3, 1, 3, 2, 1}  // Duration 3
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