package com.example.assessment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class CellEditor extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_cell_editor);

      Bundle extras = getIntent().getExtras();
      assert extras != null;
      String cellID = extras.getString("id");

      DBHelper dbHelper = new DBHelper(this);

      Cell cell = dbHelper.getSingleRecord(cellID);

      TextView cellid_ = findViewById(R.id.cellID);
      TextView studentID = findViewById(R.id.studentID);

      EditText className = findViewById(R.id.className);
      EditText roomName = findViewById(R.id.roomName);
      EditText day = findViewById(R.id.dayofClass);
      EditText startTime = findViewById(R.id.startTime);
      EditText duration = findViewById(R.id.duration);

      cellid_.setText(Integer.toString(cell.getRecordID()));
      studentID.setText(Integer.toString(cell.getStudentID()));
      className.setText(cell.getClassName());
      roomName.setText(cell.getClassRoom());
      day.setText(Integer.toString(cell.getDay()));
      startTime.setText(Integer.toString(cell.getStartTime()));
      duration.setText(Integer.toString(cell.getDuration()));
   }
}
