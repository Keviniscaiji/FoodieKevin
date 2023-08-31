package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
// the the detail of the comment
public class CommentDetailActivity extends AppCompatActivity {
TextView tv_author,tv_title,tv_comment,tv_rate;
String author,title,comment;
float rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        Intent intent = getIntent();
        init();
        author = intent.getStringExtra("comment_username");
        title = intent.getStringExtra("title");
        comment = intent.getStringExtra("comment");
        rate = intent.getFloatExtra("rate",0);
        tv_rate.setText(""+rate);
        tv_author.setText(author);
        tv_title.setText(title);
        tv_comment.setText(comment);
    }
    public void init(){
        tv_rate = findViewById(R.id.tv_comment_detail_rate);
        tv_author = findViewById(R.id.tv_comment_detail_author);
        tv_title = findViewById(R.id.tv_comment_detail_title);
        tv_comment = findViewById(R.id.tv_comment_detail_body);
    }
}