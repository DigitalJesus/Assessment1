package com.example.assessment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    protected static final String ADMIN_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button adminLoginButton = findViewById(R.id.btnAdminLogin);
        Button button = findViewById(R.id.loginButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPress();
            }
        });

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminPress();
            }
        });

    }

    private void adminPress() {
        setContentView(R.layout.activity_admin_login);
        Button adminLoginButton = findViewById(R.id.adminLoginButton);
        Button adminBackButton = findViewById(R.id.adminBackButton);

        adminBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText adminPassword = findViewById(R.id.adminInput);

                if (adminPassword.getText().toString().equals(ADMIN_PASSWORD)){
                    Intent intent = new Intent(LoginActivity.this, AdminEditor.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void loginPress() {
        EditText loginBox = findViewById(R.id.numberInput);

        if(!(loginBox.getText().toString().trim().isEmpty()) && Integer.parseInt(loginBox.getText().toString()) > 1){
            SharedPreferences mPrefs = getSharedPreferences("label", Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mPrefs.edit();

            mEditor.putString("studentID", loginBox.getText().toString()).apply();

            finish();
        } else{
            Toast.makeText(LoginActivity.this, "Please input your Student ID number", Toast.LENGTH_SHORT).show();
        }

    }
}
