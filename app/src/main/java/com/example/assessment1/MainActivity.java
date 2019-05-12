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

import java.util.Objects;

import static java.lang.Integer.*;

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

//        dbInsertData();

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
        DBHelper dbHelper = new DBHelper(this);

        String classNames[] = {"Mobile App", "Math", "Embedded Sys", "Sys Analysis"};

        String hex[] = {
                "255, 209, 102",
                "94, 214, 182",
                "239, 105, 136",
                "67, 146, 216"
        };

        int timetableData[][] = {
                {0, 1, 0, 2, 3, 3, 1, 2, 1, 1}, // CourseID 0
                {0, 1, 1, 1, 1, 2, 3, 3, 3, 4}, // Day      1
                {5, 1, 2, 6, 8, 5, 1, 2, 6, 1}, // Time     2
                {1, 1, 3, 2, 2, 2, 1, 2, 2, 1}  // Duration 3
        };

        dbHelper.insertCell(getStudentIDInt(), "HECK", "R202", "#F29559", 1, 2, 0);
        dbHelper.insertCell(1234, "HECL", "R303", "#F24529",1, 2, 1);
    }

    private int getStudentIDInt() {
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
        return Integer.parseInt(Objects.requireNonNull(mPrefs.getString("studentID", "")));
    }


    private void buildTimetable(int studentID) {
        DBHelper dbHelper = new DBHelper(this);

        Cell cell = dbHelper.queryCellData(studentID);

        String weekDayIndex[] = {"mon_hr", "tues_hr", "wed_hr", "thurs_hr", "fri_hr"};

        for (int i = 0; i < 1; i++) {

            String finalID = weekDayIndex[cell.getDay()] + cell.getStartTime();

            int resID = getResources().getIdentifier(finalID, "id", getPackageName());
            TextView selectedTime = ((TextView) findViewById(resID));

//          This for loop colours in the hours after a class depending on the duration of the class.
            for (int j = 0; j < (cell.getDuration()); j++) {
                String modifiedIDString = weekDayIndex[cell.getDay()] + (cell.getStartTime() + (j));
                int durationID = getResources().getIdentifier(modifiedIDString, "id", getPackageName());
                TextView durationEdit = ((TextView) findViewById(durationID));
                createTimetableCell("", "", cell.getClassColour(), durationEdit);
            }

            createTimetableCell(cell.getClassName(), cell.getClassRoom(), cell.getClassColour(), selectedTime);
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
}