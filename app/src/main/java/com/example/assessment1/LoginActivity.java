package com.example.assessment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private EditText loginBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        button = findViewById(R.id.loginButton);
        loginBox = findViewById(R.id.numberInput);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPress();
            }
        });
    }

    public void loginPress() {


        loginBox = findViewById(R.id.numberInput);

        if(!(loginBox.getText().toString().trim().isEmpty()) && Integer.parseInt(loginBox.getText().toString()) > 1){
            SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);

            Toast.makeText(LoginActivity.this, "Logging in with Student ID: "+loginBox.getText().toString(), Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putString("studentID", loginBox.getText().toString()).apply();
            finish();
        } else{
            Toast.makeText(LoginActivity.this, "Please input your Student ID number", Toast.LENGTH_SHORT).show();
        }

    }
}
