package com.example.assessment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class CellEditor extends AppCompatActivity {

    private static final String TAG = "CELLEDITOR";
    public int studentID;
    public String className;

    DBHelper dbHelper = new DBHelper(this);
    final String[] globalDayList = dbHelper.queryCellDetails("day", studentID, className);
    final String[] globalTimeList = dbHelper.queryCellDetails("startTime", studentID, className);
    final String[] globalDurationList = dbHelper.queryCellDetails("cellDuration", studentID, className);
    final String[] globalRoomList = dbHelper.queryCellDetails("classRoom", studentID, className);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_editor);
        DBHelper dbHelper = new DBHelper(this);

        Button btnSave = findViewById(R.id.btnSave);

        //Import Selections from previous classes.
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int selectedStudentID = extras.getInt("studentID");
        String selectedClass = extras.getString("className");

        studentID = selectedStudentID;
        className = selectedClass;

        //Get Data for each cell from DB
        final String[] dayList = dbHelper.queryCellDetails("day", selectedStudentID, selectedClass);
        final String[] timeList = dbHelper.queryCellDetails("startTime", selectedStudentID, selectedClass);
        final String[] durationList = dbHelper.queryCellDetails("cellDuration", selectedStudentID, selectedClass);
        final String[] roomList = dbHelper.queryCellDetails("classRoom", selectedStudentID, selectedClass);

        //Init. ListView and CustomObject
        ListView listView = findViewById(R.id.listView_cell_editor);
        final ArrayList<CustomArrayObject> objects = new ArrayList<>();

        //For each List item, add to the custom object the four data point for each cell
        for (int i = 0; i < dayList.length; i++) {
            CustomArrayObject item1 = new CustomArrayObject(dayList[i], timeList[i], durationList[i], roomList[i]);
            objects.add(item1);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        //Display the list using the custom adapter
        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        listView.setAdapter(customAdapter);

        //Be kind, rewind.
        dbHelper.close();
    }

    private void saveData() {
        DBHelper dbHelper = new DBHelper(this);

        for (int i = 0; i < globalDayList.length; i++) {
            dbHelper.updateRow(studentID, className, Integer.parseInt(globalDayList[i]), globalRoomList[i], Integer.parseInt(globalTimeList[i]), Integer.parseInt(globalDurationList[i]), i);
        }
    }

    public void modifyDayData(String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        globalDayList[entryNumber] = updatedValue;
    }

    public void modifyDurationData(String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        globalDurationList[entryNumber] = updatedValue;
    }

    public void modifyClassRoomData(String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        globalRoomList[entryNumber] = updatedValue;
    }

    public void modifyStartTimeData(String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        globalTimeList[entryNumber] = updatedValue;
    }
}
