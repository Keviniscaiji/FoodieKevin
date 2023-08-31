package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import database.DatabaseServer;

// enable user to add comment to a certain point

public class CommentActivity extends AppCompatActivity {
    RatingBar rb_comment;
    EditText et_title,et_body;
    Button bt_submit_comment;
    String comment,title;
    String username,location_name,locationId;
    double latitude,longitude;
    float rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        init();
        bt_submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = et_body.getText().toString();
                title = et_title.getText().toString();
                rating =rb_comment.getRating();
                if(!comment.isEmpty()&&!title.isEmpty()&&rating>=1){
                DatabaseServer ds = new DatabaseServer(getApplicationContext());

                Intent intent = getIntent();
                username = intent.getStringExtra("username");
                longitude = intent.getDoubleExtra("poi_longitude",0.00);
                latitude = intent.getDoubleExtra("poi_latitude",0.00);
                location_name = intent.getStringExtra("poi_name");
                locationId = intent.getStringExtra("poi");

                if(!ds.addComment(locationId,location_name,username,comment,title,rating)){
                    Toast.makeText(CommentActivity.this, "You have already comment the place", Toast.LENGTH_SHORT).show();
                    intent.setClass(CommentActivity.this,MapActivity.class);
                    startActivity(intent);
                }else{
                ds.addLocationInfo(locationId,rating,longitude,latitude,location_name);
                intent.setClass(CommentActivity.this,MapActivity.class);
                startActivity(intent);
                }
            }else{
                Toast.makeText(CommentActivity.this,"Please check your comment",Toast.LENGTH_SHORT).show();
            }}
        });


    }
    public void init(){
        rb_comment = findViewById(R.id.rb_star);
        et_title = findViewById(R.id.et_comment_title);
        et_body = findViewById(R.id.et_comment_body);
        bt_submit_comment = findViewById(R.id.bt_submit_comment);


    }
}