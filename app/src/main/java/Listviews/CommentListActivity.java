package Listviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.foodiekevin.CommentDetailActivity;
import com.example.foodiekevin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import database.DatabaseServer;
import items.Comments;
// show the comments on a certain point
public class CommentListActivity extends AppCompatActivity {
    private List<Comments> list = new ArrayList<>();
    private String location_id;
    TextView tv_average_rate;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        lv = findViewById(R.id.lv_2);
        tv_average_rate = findViewById(R.id.tv_comment_average_rate);
        Intent intent = getIntent();
        location_id = intent.getStringExtra("poi");
        //initiate the database server and initiate the listview of the locations tab


        DatabaseServer ds = new DatabaseServer(getApplicationContext());
        Cursor cursor = ds.getLocationInfo(location_id);
        if(cursor.moveToNext()){
        tv_average_rate.setText("Average Score: "+ String.format("%.2f", cursor.getFloat(cursor.getColumnIndexOrThrow("rate"))));
        }
        cursor = ds.getComments(location_id);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Comments(
                    cursor.getInt(cursor.getColumnIndexOrThrow("comment_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("location_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("comment")),
                    cursor.getString(cursor.getColumnIndexOrThrow("comment_title")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("rate"))
            ));
            cursor.moveToNext();
        }
//      Load the data into the object to materialize it
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("username", list.get(i).username);
//            item.put("comment", list.get(i).comment);
            item.put("title", list.get(i).title);
            item.put("rate", list.get(i).rate);
            data.add(item);
        }
//      Use SimpleAdapter To realize the function of list
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.comment_item,
                new String[]{"username", "title","rate"}, new int[]{R.id.et_comment_item_username, R.id.et_comment_item_title,R.id.et_comment_item_rate});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("rate", list.get(position).rate);
                intent.putExtra("comment_username", list.get(position).username);
                intent.putExtra("title", list.get(position).title);
                intent.putExtra("comment", list.get(position).comment);
                intent.setClass(CommentListActivity.this, CommentDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}