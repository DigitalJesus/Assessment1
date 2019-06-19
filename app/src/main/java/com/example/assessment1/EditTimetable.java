package com.example.assessment1;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class EditTimetable extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_edit_timetable);

   }

   @Override
   protected void onStart() {
      super.onStart();
      setContentView(R.layout.activity_edit_timetable);
      Toolbar toolbar = findViewById(R.id.app_bar);
      setSupportActionBar(toolbar);
      Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
      Toast.makeText(this, "Tap a cell to Edit", Toast.LENGTH_LONG).show();

      Bundle extras = getIntent().getExtras();
      assert extras != null;
      int selectedStudentID = extras.getInt("studentID");

      setToolbarText(Integer.toString(selectedStudentID));

      buildTimetable(selectedStudentID);
   }

   void setToolbarText(String methodInput) {
      TextView title = findViewById(R.id.timetable_title);
      title.setText(methodInput);
   }

   void buildTimetable(int studentID) {
      DBHelper dbHelper = new DBHelper(this);
      Cell[] cell = dbHelper.queryCellData(studentID);

      if (cell == null) {
         Toast.makeText(this, "No timetable for this ID", Toast.LENGTH_LONG).show();
         return;
      }

      String[] weekDayIndex = {"mon_hr", "tues_hr", "wed_hr", "thurs_hr", "fri_hr"};

      for (int i = 0; i < dbHelper.getTableLengthForStudentID(studentID); i++) {
         String currentTextviewID = weekDayIndex[cell[i].getDay()] + cell[i].getStartTime();
         TextView selectedTime = (findViewById(getResources().getIdentifier(currentTextviewID, "id", getPackageName())));
         createTimetableCell(cell[i].getClassName(), cell[i].getClassRoom(), cell[i].getClassColour(), selectedTime);
         //Add ID as Tag for use in editor.
         selectedTime.setTag(cell[i].getRecordID());

         //This for loop colours in the hours after a class depending on the duration of the class.
         for (int j = 1; j < (cell[i].getDuration()); j++) {
            String modifiedIDString = weekDayIndex[cell[i].getDay()] + (cell[i].getStartTime() + (j));
            TextView durationEdit = (findViewById(getResources().getIdentifier(modifiedIDString, "id", getPackageName())));
            durationEdit.setTag(cell[i].getRecordID());

            try {
               createTimetableCell("", "", cell[i].getClassColour(), durationEdit);
            } catch (Exception e) {
               Log.d(TAG, "buildTimetable: Class duration extended beyond the end of the day");
               break;
            }
         }
      }
      dbHelper.close();
   }

   public void itemTapped(View view) {
      if (view.getTag() == null) {
         Toast.makeText(this, "No class selected, try again :(", Toast.LENGTH_SHORT).show();
      }else{
         Log.d(TAG, "itemTapped: tagis:" + view.getTag());

         Intent intent = new Intent(this, CellEditor.class);
         intent.putExtra("id", view.getTag().toString());
         startActivity(intent);
      }
   }

   private void createTimetableCell(String className, String room, String hex, TextView selectedTime) {
      String outputText = className + "\n" + room;
      selectedTime.setText(outputText);
      selectedTime.setBackgroundColor(Color.parseColor(hex));
   }
}
