package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class ShortestPathActivity extends AppCompatActivity {
    private MapView mMapView = null;
    Button bt_search;
    ImageButton bt_add,bt_remove;
    int edittext_number = 0;


//    et01 and 02 for the starting point and ending point inside of the layout
    EditText et1,et2,et01,et02;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortest_path);
        mMapView = findViewById(R.id.tool1_mapView);
        bt_search = findViewById(R.id.tool1_bt_search);
        bt_add = findViewById(R.id.tool1_bt_add);
        bt_remove = findViewById(R.id.tool1_bt_remove);
        LinearLayout linear=findViewById(R.id.tool1_search_container);
        et01 = findViewById(R.id.tool1_et_start_point);
        et02 = findViewById(R.id.tool1_et_end_point);

        et01.setText("北土城-地铁站");
        et02.setText("军事博物馆-地铁站");
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            //            add middle point textview1
            public void onClick(View v) {
                if(edittext_number ==0){
                    et1 = new EditText(getApplicationContext());
                    et1.setWidth(200);
                    et1.setHeight(130);
                    et1.setHint("MiddlePoint1");
                    et1.setBackgroundColor(Color.GRAY);
                    et1.setId(R.id.tool1_item1);
                    et1.setText("鼓楼大街-地铁站");
                    linear.addView(et1);
                    edittext_number++;
                    return;
                }
                //            add middle point textview2
                else if(edittext_number == 1){
                    et2 = new EditText(getApplicationContext());
                    et2.setWidth(200);
                    et2.setHeight(130);
                    et2.setHint("MiddlePoint2");
                    et2.setBackgroundColor(Color.GRAY);
                    et2.setId(R.id.tool1_item2);
                    et2.setText("西直门-地铁站");
                    linear.addView(et2);
                    edittext_number++;
                    return;
                }else{
                    Toast.makeText(ShortestPathActivity.this,"You can at most add two items",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
//                                remove middle point textview2
            public void onClick(View v) {
                if(edittext_number ==2 ){
                    linear.removeView(findViewById(R.id.tool1_item2));
                    edittext_number--;
//                                remove middle point textview1
                }else if
                (edittext_number == 1){
                    linear.removeView(findViewById(R.id.tool1_item1));
                    edittext_number--;
//                    if there is no more textview to remove make a toast
                }else{
                    Toast.makeText(ShortestPathActivity.this, "There are no more views to be removed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if(edittext_number==0){
                    if(et01.getText().toString()!=""&&et02.getText().toString()!=""){
                      transmitForm(et01,false);
                      intent.putExtra("Route_points",2+"");
                      transmitForm(et02,true);
                    }else{
                        Toast.makeText(ShortestPathActivity.this,"Please enter locations",Toast.LENGTH_SHORT).show();
                    }
                }else if(edittext_number==1){
                    if(et01.getText().toString()!=""&&et02.getText().toString()!=""&&et1.getText().toString()!=""){
                        transmitForm(et01,false);
                        transmitForm(et02,false);
                        intent.putExtra("Route_points",3+"");
                        transmitForm(et1,true);
                    }else{
                        Toast.makeText(ShortestPathActivity.this,"Please enter locations",Toast.LENGTH_SHORT).show();
                    }
                }else if(edittext_number ==2){
                    if(et01.getText().toString()!=""&&et02.getText().toString()!=""&&et1.getText().toString()!=""
                            &&et2.getText().toString()!=""){
                        transmitForm(et01,false);
                        transmitForm(et02,false);
                        transmitForm(et1,false);
                        intent.putExtra("Route_points",4+"");
                        transmitForm(et2,true);
                    }else{
                        Toast.makeText(ShortestPathActivity.this,"Please enter locations",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }
    public void transmitForm(TextView t,Boolean jump){
        SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();
//transform the words into formal format

        final String[] s = {""};
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            @Override
           public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                try {
                   t.setText(suggestionResult.getAllSuggestions().get(0).getKey());
                   Intent intent = getIntent();
                   intent.putExtra(t.getHint().toString(),t.getText().toString());
                   if(jump){
                        LinearLayout layout = findViewById(R.id.tool1_button_container);
                        Button bt_result = new Button(getApplicationContext());
                        bt_result.setText("View Result");
                        bt_result.setGravity(Gravity.CENTER);
                        bt_result.setHeight(50);
                        bt_result.setWidth(100);
//                        bt_result.setBackgroundColor();
                        bt_result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.setClass(ShortestPathActivity.this,ShortestPathResultActivity.class);
                                startActivity(intent);
                                layout.removeView(bt_result);
                            }
                        });
                        layout.addView(bt_result);

                   }
                } catch (NullPointerException e) {
                    Toast.makeText(ShortestPathActivity.this, "Please check your inputs", Toast.LENGTH_SHORT).show();
                }

            }
        };
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .city("北京")
                .keyword(t.getText().toString()));
        mSuggestionSearch.destroy();
    }

}