package com.example.assessment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class CellEditor extends AppCompatActivity {

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

        //Get Data for each cell from DB
        final String[] dayList = dbHelper.queryCellDetails("day", selectedStudentID, selectedClass);
        final String[] timeList = dbHelper.queryCellDetails("startTime", selectedStudentID, selectedClass);
        final String[] durationList = dbHelper.queryCellDetails("cellDuration", selectedStudentID, selectedClass);
        final String[] roomList = dbHelper.queryCellDetails("classRoom", selectedStudentID, selectedClass);

        //Init. ListView and CustomObject
        ListView listView = (ListView)findViewById(R.id.listView_cell_editor);
        ArrayList<CustomArrayObject> objects = new ArrayList<>();

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

}
