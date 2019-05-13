package com.example.assessment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForSavedStudentID();

    }
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
        setToolbarText(getStudentID());

        dbInsertData();

        buildTimetable(Integer.parseInt(Objects.requireNonNull(mPrefs.getString("studentID", ""))));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeStudentID:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }


    private void dbInsertData() {

        int studentID = 2173727;

        String classNames[] = {"Mobile App", "Math", "Embedded Sys", "Sys Analysis"};

        String hex[] = {
                "#00A878",
                "#55DDE0",
                "#FFB238",
                "#ED474A"
        };

        String classRoom[] = {"C304", "C117", "B106", "B107", "C305", "T403", "C117", "B107", "T403", "T401"};


        int timetableData[][] = {
                {0, 1, 0, 2, 3, 3, 1, 2, 1, 1}, // CourseID 0
                {0, 1, 1, 1, 1, 2, 3, 3, 3, 4}, // Day      1
                {5, 1, 2, 6, 8, 5, 1, 2, 6, 1}, // Time     2
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
    }

    private void buildTimetable(int studentID) {
        DBHelper dbHelper = new DBHelper(this);
        Cell cell = dbHelper.queryCellData(studentID, 0);

        if(cell == null){
            Toast.makeText(this, "No timetable for this ID", Toast.LENGTH_LONG).show();
            return;
        }

        String weekDayIndex[] = {"mon_hr", "tues_hr", "wed_hr", "thurs_hr", "fri_hr"};

        for (int i = 0; i < dbHelper.getTableLength(); i++) {

            if (cell.getStudentID() == getStudentIDInt()){
                for (int j = 0; j < 1; j++) {
                    String finalID = weekDayIndex[cell.getDay()] + cell.getStartTime();

                    int resID = getResources().getIdentifier(finalID, "id", getPackageName());
                    TextView selectedTime = ((TextView) findViewById(resID));

//                  This for loop colours in the hours after a class depending on the duration of the class.
                    for (int k = 0; k < (cell.getDuration()); k++) {
                        String modifiedIDString = weekDayIndex[cell.getDay()] + (cell.getStartTime() + (k));
                        int durationID = getResources().getIdentifier(modifiedIDString, "id", getPackageName());
                        TextView durationEdit = ((TextView) findViewById(durationID));
                        createTimetableCell("", "", cell.getClassColour(), durationEdit);
                    }
                    createTimetableCell(cell.getClassName(), cell.getClassRoom(), cell.getClassColour(), selectedTime);
                }

                cell = dbHelper.queryCellData(studentID, i);
                }
            }

    }

    private String getStudentID() {
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
        return mPrefs.getString("studentID", "");
    }

    private void setToolbarText(String methodInput) {
        TextView title = (TextView) findViewById(R.id.timetable_title);
        title.setText(methodInput);
    }

    private void createTimetableCell(String className, String room, String hex, TextView selectedTime) {
        String outputText = className + "\n" + room;
        selectedTime.setText(outputText);
        selectedTime.setBackgroundColor(Color.parseColor(hex));
    }

    private void checkForSavedStudentID() {
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);

        if (mPrefs.getString("studentID", "").isEmpty() || Objects.equals(mPrefs.getString("studentID", ""), "")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void insertCell(int studentID, String className, String classRoom, String classColour, int startTime, int duration, int day) {
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.insertCell(studentID, className, classRoom, classColour, startTime, duration, day);

    }

    private int getStudentIDInt() {
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
        return Integer.parseInt(Objects.requireNonNull(mPrefs.getString("studentID", "")));
    }
}