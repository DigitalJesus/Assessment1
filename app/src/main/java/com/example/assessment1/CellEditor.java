package com.example.assessment1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class CellEditor extends AppCompatActivity {

    private static final String TAG = "CELLEDITOR";
    public static int studentID;
    public static String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_editor);
        DBHelper dbHelper = new DBHelper(this);

        //Import Selections from previous classes.
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int selectedStudentID = extras.getInt("studentID");
        String selectedClass = extras.getString("className");

        studentID = selectedStudentID;
        className = selectedClass;

        Log.d(TAG, "Set studentID: " + studentID + " and className: " + className);

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


        //Display the list using the custom adapter
        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        listView.setAdapter(customAdapter);

        //Be kind, rewind.
        dbHelper.close();
    }

    public void modifyDayData(Context context, String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        DBHelper dbHelper = new DBHelper(context);
        Log.d(TAG, "modifyDayData: " + studentID+"::"+className+":::"+entryNumber);


        dbHelper.updateRow(studentID, className, Integer.parseInt(updatedValue), null, -1, -1, entryNumber);
    }

    public void modifyDurationData(Context context, String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        DBHelper dbHelper = new DBHelper(context);

        dbHelper.updateRow(studentID, className, -1, null, -1, Integer.parseInt(updatedValue), entryNumber);
    }

    public void modifyClassRoomData(Context context, String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        DBHelper dbHelper = new DBHelper(context);

        dbHelper.updateRow(studentID, className, -1, updatedValue, -1, -1, entryNumber);
    }

    public void modifyStartTimeData(Context context, String updatedValue, int entryNumber){
        Log.d(TAG, updatedValue + " " + entryNumber);

        DBHelper dbHelper = new DBHelper(context);

        dbHelper.updateRow(studentID, className, -1, null, Integer.parseInt(updatedValue), -1, entryNumber);
    }
}
