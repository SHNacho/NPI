package com.example.npi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText username_et;
    EditText password_et;
    Button login_btn;
    static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_et = findViewById(R.id.userName);
        password_et = findViewById(R.id.password);
        login_btn = findViewById(R.id.loginBtn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user = username_et.getText().toString();

                openMain(v);
            }
        });
    }

    public void openMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}