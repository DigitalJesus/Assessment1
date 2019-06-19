package com.example.assessment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CellEditor extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_cell_editor);

      Button cancel = findViewById(R.id.cancelBtn);
      Button save = findViewById(R.id.saveBtn);

      Bundle extras = getIntent().getExtras();
      assert extras != null;
      final String cellID = extras.getString("id");

      final DBHelper dbHelper = new DBHelper(this);

      Cell cell = dbHelper.getSingleRecord(cellID);

      final TextView cellid_ = findViewById(R.id.cellID);
      TextView studentID = findViewById(R.id.studentID);

      final EditText className = findViewById(R.id.className);
      final EditText roomName = findViewById(R.id.roomName);
      final EditText day = findViewById(R.id.dayofClass);
      final EditText startTime = findViewById(R.id.startTime);
      final EditText duration = findViewById(R.id.duration);

      cellid_.setText(Integer.toString(cell.getRecordID()));
      studentID.setText(Integer.toString(cell.getStudentID()));
      className.setText(cell.getClassName());
      roomName.setText(cell.getClassRoom());
      day.setText(Integer.toString(cell.getDay()));
      startTime.setText(Integer.toString(cell.getStartTime()));
      duration.setText(Integer.toString(cell.getDuration()));


      cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });

      save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            dbHelper.updateRow(Integer.parseInt(cellid_.getText().toString()),
                    className.getText().toString(),
                    Integer.parseInt(day.getText().toString()),
                    roomName.getText().toString(),
                    Integer.parseInt(startTime.getText().toString()),
                    Integer.parseInt(duration.getText().toString()));
            finish();
         }
      });
   }
}
