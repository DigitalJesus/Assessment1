package com.example.assessment1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForSavedStudentID();
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
        TextView title = (TextView) findViewById(R.id.timetable_title);
        title.setText(mPrefs.getString("studentID", "Timetable"));

        startTimetable();
    }

    private void startTimetable() {
        String weekDayIndex[] = {"mon_hr", "tues_hr", "wed_hr", "thurs_hr", "fri_hr"};

        dbHelper = new DBHelper(this);

        int classTime[] = {5, 1, 2, 6, 8, 5, 1, 2, 6, 1};
        int classDuration[] = {1, 1, 3, 2, 2, 2, 1, 2, 2, 1};
        int classDay[] = {0, 1, 1, 1, 1, 2, 3, 3, 3, 4};
        int classID[] = {0, 1, 0, 2, 3, 3, 1, 2, 1, 1};
        String classRoom[] = {"C304", "C117", "B106", "B107", "C305", "T403", "C117", "B107", "T403", "T401"};
        String classTutor[] = {"", "", "", "", "", "", "", "", "", ""};


        for (int i = 0; i < classTime.length; i++) {

            String finalID = weekDayIndex[classDay[i]] + classTime[i];

            int resID = getResources().getIdentifier(finalID, "id", getPackageName());
            TextView selectedTime = ((TextView) findViewById(resID));

//          This for loop colours in the hours after a class depending on the duration of the class.
            for (int j = 0; j < (classDuration[i]); j++) {
                String modifiedIDString = weekDayIndex[classDay[i]] + (classTime[i] + (j));
                int durationID = getResources().getIdentifier(modifiedIDString, "id", getPackageName());
                TextView durationEdit = ((TextView) findViewById(durationID));
                createTimetableBox("", "", "", getClassColor(classID[i]), durationEdit);
            }

            createTimetableBox(getClassName(classID[i]), classRoom[i], classTutor[i], getClassColor(classID[i]), selectedTime);

        }
    }
    private int[] getClassColor(int classID) {
        int rgb[][] = {
                {255, 209, 102},
                {94, 214, 182},
                {239, 105, 136},
                {67, 146, 216}
        };
        return rgb[classID];
    }

    private String getClassName(int classID) {
        String classNames[] = {"Mobile App", "Math", "Embedded Sys", "Sys Analysis"};
        //                          0           1           2                3

        return classNames[classID];
    }

    private void createTimetableBox(String className, String room, String tutor, int[] rgb, TextView selectedTime) {
        String outputText = className + "\n" + room + "\n" + tutor;
        selectedTime.setText(outputText);
        selectedTime.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
    }

    private void checkForSavedStudentID() {
        SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);

        if(mPrefs.getString("studentID", "").isEmpty()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
