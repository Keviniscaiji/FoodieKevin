package Listviews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.foodiekevin.MapActivity;
import com.example.foodiekevin.R;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
// create a list to show the route plan result's detail
public class RouteResultActivity extends AppCompatActivity {
ArrayList<String> results = new ArrayList<String>();
    ListView lv_4;
    Button bt1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_result);
        Intent intent = getIntent();
        results = intent.getStringArrayListExtra("routeresult");
        lv_4 = findViewById(R.id.lv_4);
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("route", results.get(i));
            item.put("title", "Result "+i);
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_route_result_item,
                new String[]{"title","route"},
                new int[]{R.id.tv_route_result_title, R.id.tv_route_result});
        lv_4.setAdapter(adapter);
        lv_4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RouteResultActivity.this);
                EditText et =new EditText(getApplicationContext());
                et.setHint("Enter the name of the file");
                builder.setTitle("Want to export?")
                        .setMessage("Would you like to add a comment？")
                        .setView(et)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (et.getText().toString()!=""){
                                saveFile(et.getText().toString(),results.get(position));}
                                else{
                                    Toast.makeText(RouteResultActivity.this,"Please enter the name of the file",Toast.LENGTH_SHORT);
                                }
                            }
                        }).create().show();




            }


        });
    }

    public void saveFile(String title,String str)
    {
        FileOutputStream fos = null;
        BufferedWriter writer = null;

        try {
            fos = openFileOutput(title+".txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            try {
                writer.write(str);
                Toast.makeText(this,"save file successful", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(writer!=null)
            {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}