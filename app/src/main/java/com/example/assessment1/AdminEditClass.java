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

public class AdminEditClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_class);

        final DBHelper dbHelper = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        final int selectedStudentID = extras.getInt("studentID");

        Log.d("HECK", "studentEditor: " + selectedStudentID);

        final String[] classList = dbHelper.queryStringAttributeForStudent("className", selectedStudentID);

        final ListView listView = (ListView)findViewById(R.id.listView_admin_class_editor);
        final ArrayAdapter<String> classArray = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.textView_admin_editor, classList);
        listView.setAdapter(classArray);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedClass = (classList[position]);
                Intent intent = new Intent(AdminEditClass.this, CellEditor.class);
                intent.putExtra("studentID", selectedStudentID);
                intent.putExtra("className", selectedClass);
                startActivity(intent);
                dbHelper.close();
            }
        });

        dbHelper.close();
    }
}
