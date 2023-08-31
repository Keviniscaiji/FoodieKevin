package Listviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.foodiekevin.CommentDetailActivity;
import com.example.foodiekevin.R;
import com.example.foodiekevin.TravelRouteDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import database.DatabaseServer;
import items.Routes;

public class TravelRouteListActivity extends AppCompatActivity {
    ArrayList<Routes> results = new ArrayList<Routes>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_route_list);
        lv = findViewById(R.id.lv_route);
        DatabaseServer ds = new DatabaseServer(getApplicationContext());
        Cursor cursor = ds.getRoute();
        while (cursor.moveToNext()) {
            Routes r = new Routes(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("route_points")),
                    cursor.getString(cursor.getColumnIndexOrThrow("route_lines")),
                    cursor.getString(cursor.getColumnIndexOrThrow("route_notes")));
            results.add(r);
        }
        cursor.close();
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("title", results.get(i).title);
            item.put("point", results.get(i).route_points);
            item.put("line", results.get(i).route_lines);
            item.put("note", results.get(i).route_notes);
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_travel_route_item,
                new String[]{"title"},
                new int[]{R.id.tv_route_item_title});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra("points", results.get(position).route_points);
                intent.putExtra("lines", results.get(position).route_lines);
                intent.putExtra("notes", results.get(position).route_notes);
                intent.setClass(TravelRouteListActivity.this, TravelRouteDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}