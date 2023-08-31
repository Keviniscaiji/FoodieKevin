package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseServer;

//enable user to edit his favorite locations
public class LocationDetailActivity extends AppCompatActivity {
    private Button bt1,bt2,bt3,bt4;
    EditText et1;
    EditText et2;
    EditText et3,et4;
    String locn,loc;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        iniViews();


        id = MapActivity.id;
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.setClass(LocationDetailActivity.this,MapActivity.class);
                DatabaseServer ds = new DatabaseServer(getApplicationContext());
                ds.removeLcation(id);
                intent.putExtra("username",MapActivity.username);
                startActivity(intent);
                Toast.makeText(LocationDetailActivity.this, "Successfully edit the location", Toast.LENGTH_SHORT).show();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locn = et1.getText().toString();
                loc = et2.getText().toString();
                DatabaseServer ds = new DatabaseServer(getApplicationContext());
                ds.removeLcation(id);
                ds.addLocation(MapActivity.username,locn,loc);
                Intent intent = new Intent(LocationDetailActivity.this,MapActivity.class);
                intent.putExtra("username",MapActivity.username);
                Toast.makeText(LocationDetailActivity.this, "Successfully remove the location", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= getIntent();
                loc = intent.getStringExtra("location");
                intent = new Intent(LocationDetailActivity.this,MapActivity.class);
                intent.putExtra("username",MapActivity.username);
                MapActivity.spS = loc;
                startActivity(intent);



            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= getIntent();
                loc = intent.getStringExtra("location");
                intent = new Intent(LocationDetailActivity.this,MapActivity.class);
                intent.putExtra("username",MapActivity.username);
                MapActivity.epS = loc;
                startActivity(intent);
            }
        });


    }private void iniViews(){
        bt1 = findViewById(R.id.bt_remove_location);
        bt2 = findViewById(R.id.bt_edit_location);
        bt3 = findViewById(R.id.bt_start_location);
        bt4 = findViewById(R.id.bt_end_location);
        et1 = findViewById(R.id.et_edit_locationame);
        et2 = findViewById(R.id.et_edit_location);
        et3 = findViewById(R.id.et_start_point);
        et4 = findViewById(R.id.et_end_point);
    }
}