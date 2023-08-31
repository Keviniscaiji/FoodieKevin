package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseServer;

public class RegisterActivity extends AppCompatActivity {
        private Button mbt_submit_reg;
        private EditText met_username_reg;
        private EditText met_password_reg1;
        private EditText met_password_reg2;
        private String pw1;
        private String pw2;
        private String username;
        private TextView back;
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            initViews();
            mbt_submit_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw1 = met_password_reg1.getText().toString();
                    pw2 = met_password_reg2.getText().toString();
                    username = met_username_reg.getText().toString();
                    DatabaseServer ds = new DatabaseServer(getApplicationContext());
                    if(pw1.equals(pw2) && pw1.length() >= 6 && username.length()>=5 &&ds.checkUnique(username)){
                        ds.insertUser(username,pw1);
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, "Successfully register", Toast.LENGTH_SHORT).show();;
                    }else{
                        Toast.makeText(RegisterActivity.this, "Your Username and password do not " +
                                "meet the requirement", Toast.LENGTH_SHORT).show();
                    }
                }});
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
        }public void initViews(){
            met_password_reg1 = findViewById(R.id.et_password_reg1);
            met_password_reg2 = findViewById(R.id.et_password_reg2);
            met_username_reg = findViewById(R.id.et_username_reg);
            mbt_submit_reg = findViewById(R.id.bt_submit_reg);
            back = findViewById(R.id.tv_back_main);
        }
    }