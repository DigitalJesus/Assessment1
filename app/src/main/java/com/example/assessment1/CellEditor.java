package com.example.assessment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class CellEditor extends AppCompatActivity {

   private static final String TAG = "CELLEDITOR";
//   private Map<Integer, Map<Integer, String>> mapList;
//   private Map<Integer, String> dataMap;


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
      final String[] idList = dbHelper.queryCellDetails("_id", selectedStudentID, selectedClass);
      for (int i = 0; i < idList.length; i++) {
         Log.d(TAG, idList[i]);
      }
      final String[] dayList = dbHelper.queryCellDetails("day", selectedStudentID, selectedClass);
      final String[] timeList = dbHelper.queryCellDetails("startTime", selectedStudentID, selectedClass);
      final String[] durationList = dbHelper.queryCellDetails("cellDuration", selectedStudentID, selectedClass);
      final String[] roomList = dbHelper.queryCellDetails("classRoom", selectedStudentID, selectedClass);

      //dataMap = new Map<Integer, String>;

      //Init. ListView and CustomObject
      ListView listView = findViewById(R.id.listView_cell_editor);
      final ArrayList<CustomArrayObject> objects = new ArrayList<>();

      //For each List item, add to the custom object the four data point for each cell
      for (int i = 0; i < dayList.length; i++) {
         CustomArrayObject item1 = new CustomArrayObject(Integer.parseInt(idList[i]), dayList[i], timeList[i], durationList[i], roomList[i]);
         objects.add(item1);
      }

      //Display the list using the custom adapter
      CustomAdapter customAdapter = new CustomAdapter(this, objects);
      listView.setAdapter(customAdapter);


      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemClick: "+objects.get(position).getId_());
         }
      });

      //Be kind, rewind.
      dbHelper.close();
   }

   // I tried so hard.
//   private void saveData() {
//      DBHelper dbHelper = new DBHelper(this);
//      int mapSize = Objects.requireNonNull(mapList.get(0)).size() +
//                    Objects.requireNonNull(mapList.get(1)).size() +
//                    Objects.requireNonNull(mapList.get(2)).size() +
//                    Objects.requireNonNull(mapList.get(3)).size();
//
//      for (int i = 0; i < mapSize; i++) {
//         Iterator<Map.Entry<Integer, String>> itr = mapList.get(i).entrySet().iterator();
//         while (itr.hasNext()) {
//            Map.Entry<Integer, String> entry = itr.next();
//            switch (i) {
//               case 0:
//                  dbHelper.updateRow(entry.getKey(), Integer.parseInt(entry.getValue()), null, -1, -1);
//                  break;
//               case 1:
//                  dbHelper.updateRow(entry.getKey(), -1, null, Integer.parseInt(entry.getValue()), -1);
//                  break;
//               case 2:
//                  dbHelper.updateRow(entry.getKey(), -1, null, -1, Integer.parseInt(entry.getValue()));
//                  break;
//               case 3:
//                  dbHelper.updateRow(entry.getKey(), -1, entry.getValue(), -1, -1);
//                  break;
//            }
//         }
//      }
//   }
//
//   public void tempData(int id_, Editable s, int type) {
//      dataMap.put(id_, s.toString());
//      mapList.put(type, dataMap);
//   }
}