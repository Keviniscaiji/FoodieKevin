package Listviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.foodiekevin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import items.LocationsInfo;
//  create a list to show the locations nearby a route
public class NearbyLocationsActivity extends AppCompatActivity {
    public static List<LocationsInfo> LocationsInfoList = new ArrayList<>();
    ListView lv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_locations);
        lv3 = findViewById(R.id.lv_3);
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < LocationsInfoList.size()&& i < 10; i++) {
            HashMap<String, Object> item = new HashMap<>();

            item.put("location_name", LocationsInfoList.get(i).getLocationName());
            item.put("rate", LocationsInfoList.get(i).getRate());
            data.add(item);
        }
//      Use SimpleAdapter To realize the function of list
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_nearby_locations_item,
                new String[]{"location_name","rate"}, new int[]{R.id.et_nearby_item_location_name, R.id.et_nearby_item_rate});
        lv3.setAdapter(adapter);
        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra("poi",LocationsInfoList.get(position).getLocationId());
                intent.setClass(NearbyLocationsActivity.this, CommentListActivity.class);
                startActivity(intent);
            }
        });

    }
}