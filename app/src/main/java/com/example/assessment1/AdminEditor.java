package com.example.assessment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

public class AdminEditor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editor);

        final DBHelper dbHelper = new DBHelper(this);

        final int[] studentIDList = dbHelper.queryStudentIDs();

        Arrays.sort(studentIDList);

        String[] stringStudentIDList = new String[studentIDList.length];

        for (int i = 0; i < studentIDList.length; i++)
            stringStudentIDList[i] = Integer.toString(studentIDList[i]);

        final ListView listView = (ListView)findViewById(R.id.listView_admin_editor);
        ArrayAdapter<String> studentArray = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.textView_admin_editor, stringStudentIDList);
        listView.setAdapter(studentArray);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedStudentID = (studentIDList[position]);
                Intent intent = new Intent(AdminEditor.this, AdminEditClass.class);
                intent.putExtra("studentID", selectedStudentID);
                startActivity(intent);
                dbHelper.close();

            }
        });


    }
}
