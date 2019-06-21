package com.example.assessment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CellEditor extends AppCompatActivity {

   private int selectedColour;

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

      final Cell cell;

      if(cellID.equals("_newCell")){
         cell = new Cell();
      }else{
         cell = dbHelper.getSingleRecord(cellID);
      }

      final TextView cellid_ = findViewById(R.id.cellID);
      final TextView studentID = findViewById(R.id.studentID);

      final EditText className = findViewById(R.id.className);
      final EditText roomName = findViewById(R.id.roomName);
      final EditText day = findViewById(R.id.dayofClass);
      final EditText startTime = findViewById(R.id.startTime);
      final EditText duration = findViewById(R.id.duration);
      final Button colour = findViewById(R.id.colour);

      cellid_.setText(Integer.toString(cell.getRecordID()));
      studentID.setText(Integer.toString(cell.getStudentID()));
      className.setText(cell.getClassName());
      roomName.setText(cell.getClassRoom());
      colour.setText(cell.getClassColour());
      day.setText(Integer.toString(cell.getDay()));
      startTime.setText(Integer.toString(cell.getStartTime()));
      duration.setText(Integer.toString(cell.getDuration()));

      colour.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            openColourPicker(colour);
         }
      });

      cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });

      save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if (cellID.equals("_newCell")){
               dbHelper.insertCell(Integer.parseInt(studentID.getText().toString()),
                       className.getText().toString(),
                       roomName.getText().toString(),
                       colour.getText().toString(),
                       Integer.parseInt(startTime.getText().toString()),
                       Integer.parseInt(duration.getText().toString()),
                       Integer.parseInt(day.getText().toString()));
            }else{
               dbHelper.updateRow(Integer.parseInt(cellid_.getText().toString()),
                       className.getText().toString(),
                       Integer.parseInt(day.getText().toString()),
                       roomName.getText().toString(),
                       colour.getText().toString(),
                       Integer.parseInt(startTime.getText().toString()),
                       Integer.parseInt(duration.getText().toString()));
            }
            finish();
         }
      });
   }

   private void openColourPicker(final Button colour) {
      final AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, selectedColour, new AmbilWarnaDialog.OnAmbilWarnaListener() {
         @Override
         public void onCancel(AmbilWarnaDialog dialog) {
         }

         @Override
         public void onOk(AmbilWarnaDialog dialog, int color) {
            selectedColour = color;
            String hexColor = String.format("#%06X", (0xFFFFFF & color));
            colour.setText(hexColor);
         }

      });
      ambilWarnaDialog.show();
   }
}
