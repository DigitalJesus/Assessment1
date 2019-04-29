package com.example.assessment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForSavedStudentID();
        setContentView(R.layout.activity_main);
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
        TextView title = (TextView) findViewById(R.id.timetable_title);
        title.setText(mPrefs.getString("studentID", ""));

//        Toolbar toolbar = findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);

        startTimetable();


    }


    private void startTimetable() {

        String weekDayIndex[] = {"mon_hr", "tues_hr", "wed_hr", "thurs_hr", "fri_hr"};

        int timetableData[][] = {
                {0, 1, 0, 2, 3, 3, 1, 2, 1, 1}, // CourseID 0
                {0, 1, 1, 1, 1, 2, 3, 3, 3, 4}, // Day      1
                {5, 1, 2, 6, 8, 5, 1, 2, 6, 1}, // Time     2
                {1, 1, 3, 2, 2, 2, 1, 2, 2, 1}  // Duration 3
        };

        String classRoom[] = {"C304", "C117", "B106", "B107", "C305", "T403", "C117", "B107", "T403", "T401"};

        for (int i = 0; i < timetableData[0].length; i++) {

            String finalID = weekDayIndex[timetableData[1][i]] + timetableData[2][i];

            int resID = getResources().getIdentifier(finalID, "id", getPackageName());
            TextView selectedTime = ((TextView) findViewById(resID));

//          This for loop colours in the hours after a class depending on the duration of the class.
            for (int j = 0; j < (timetableData[3][i]); j++) {
                String modifiedIDString = weekDayIndex[timetableData[1][i]] + (timetableData[2][i] + (j));
                int durationID = getResources().getIdentifier(modifiedIDString, "id", getPackageName());
                TextView durationEdit = ((TextView) findViewById(durationID));
                createTimetableBox("", "", getClassColor(timetableData[0][i]), durationEdit);
            }

            createTimetableBox(getClassName(timetableData[0][i]), classRoom[i], getClassColor(timetableData[0][i]), selectedTime);

        }
    }

    private int[] getClassColor(int courseID) {
        int rgb[][] = {
                {255, 209, 102},
                {94, 214, 182},
                {239, 105, 136},
                {67, 146, 216}
        };
        return rgb[courseID];
    }

    private String getClassName(int courseID) {
        String classNames[] = {"Mobile App", "Math", "Embedded Sys", "Sys Analysis"};
        //                          0           1           2                3
        return classNames[courseID];
    }

    private void createTimetableBox(String className, String room, int[] rgb, TextView selectedTime) {
        String outputText = className + "\n" + room;
        selectedTime.setText(outputText);
        selectedTime.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
    }

    private void checkForSavedStudentID() {
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);

        if (mPrefs.getString("studentID", "").isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.timetable_screen_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.changeStudentID:
//                Intent login = new Intent(this, LoginActivity.class);
//                startActivity(login);
//                break;
//            case R.id.editTimetable:
//                Intent editTimetable = new Intent(this, EditTimetable.class);
//                startActivity(editTimetable);
//                break;
//            default:
//                //ignore
//        }
//        return super.onOptionsItemSelected(item);
//    }

}