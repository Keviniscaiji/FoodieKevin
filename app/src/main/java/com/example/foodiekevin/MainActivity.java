package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseServer;

public class MainActivity extends AppCompatActivity {
    private EditText met_username;
    private EditText met_password;
    private Button mbt_login;
    private TextView mtv_register;
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        met_username = findViewById(R.id.et_username);
        met_password = findViewById(R.id.et_password);
        mbt_login = findViewById(R.id.bt_login);
        mtv_register = findViewById(R.id.tv_register);

        final DatabaseServer ds = new DatabaseServer(MainActivity.this);
        mbt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = met_username.getText().toString();
                password = met_password.getText().toString();
                if(ds.login(username,password)){
                    Toast.makeText(MainActivity.this, "Successfully login", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent();
                    intent2.putExtra("username",username);
                    intent2.setClass(MainActivity.this,MapActivity.class);
                    startActivity(intent2);

                }else{
                    Toast.makeText(MainActivity.this, "Please check your password or username", Toast.LENGTH_SHORT).show();}
            }
        });
        mtv_register.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}