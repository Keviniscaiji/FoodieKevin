package com.example.foodiekevin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class TravelRouteDetailActivity extends AppCompatActivity {
    String[] points;
    String[] lines;
    String[] notes;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_route_detail);
        mMapView = findViewById(R.id.tool2_mapView);
        mBaiduMap = mMapView.getMap();


        Intent intent = getIntent();
        String points_String = intent.getStringExtra("points");
        String lines_String = intent.getStringExtra("lines");
        String notes_String = intent.getStringExtra("notes");
        Log.e("aaa",lines_String);
        String newS = points_String.replace("[", "");
        newS = newS.replace("]", "");
        points = newS.split(",");

        newS = lines_String.replace("[", "");
        newS = newS.replace("]", "");
        lines = newS.split(",");

        newS = notes_String.replace("[", "");
        newS = newS.replace("]", "");
        notes = newS.split(",");

        Log.e("TAG", points[0]);
        Log.e("TAG", lines[0]);
        Log.e("TAG", notes[0]);
//        draw the point in the database to the map
        for (int i = 0; i < points.length; i += 2) {
            LatLng ll = new LatLng(Double.parseDouble(points[i]),
                    Double.parseDouble(points[i + 1]));
            BitmapDescriptor bitmap;
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_mark);
            OverlayOptions option = new MarkerOptions()
                    .position(ll)
                    .icon(bitmap);
            mBaiduMap.addOverlay(option);
        }

//        Log.e("HAHAHA", points[0]);
//        Log.e("HAHAHA", points[1]);
//        Log.e("HAHAHA", notes[0]);
//        Log.e("HAHAHA", notes[1]);
//        Log.e("HAHAHA", notes[2]);
//        Log.e("HAHAHA", notes[3]);

//        draw the lines in the database to the map
        List<LatLng> Latlngpoints = new ArrayList<LatLng>();
        for (int i = 0; i < lines.length; i += 2) {
            if (!lines[i].equals("B")) {
                try{
                Latlngpoints.add(new LatLng(Double.parseDouble(lines[i]),
                        Double.parseDouble(lines[i + 1])));
                }catch (Exception e){
                    try{
                    OverlayOptions mOverlayOptions = new PolylineOptions()
                            .width(10)
                            .color(0xAAFF0000)
                            .points(Latlngpoints);
                    Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
                    Latlngpoints = new ArrayList<LatLng>();
                    }catch (Exception e1){

                    }
                }
            }
        }
        try {
            OverlayOptions mOverlayOptions = new PolylineOptions()
                    .width(10)
                    .color(0xAAFF0000)
                    .points(Latlngpoints);
            Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
        } catch (Exception e) {

        }
        for(int i  =0; i< notes.length;i++){
            Log.e("aaa",notes[i]+"");
        }
        for (int i = 0; i < notes.length; i += 4) {
            LatLng ll = new LatLng(Double.parseDouble(notes[i + 2]),
                    Double.parseDouble(notes[i + 3]));
            try{
            OverlayOptions mTextOptions = new TextOptions()
                    .text(notes[i]) //文字内容
                    .bgColor(0xAAFFFF00) //背景色
                    .fontSize(30) //字号
                    .fontColor(0xFFFF00FF) //文字颜色
                    .rotate(Float.parseFloat(notes[i + 1])) //旋转角度
                    .position(ll);
            Overlay mText = mBaiduMap.addOverlay(mTextOptions);
            }catch (Exception e){
                i -=3;

                OverlayOptions mTextOptions = new TextOptions()
                        .text(notes[i-1]+notes[i]) //文字内容
                        .bgColor(0xAAFFFF00) //背景色
                        .fontSize(30) //字号
                        .fontColor(0xFFFF00FF) //文字颜色
                        .rotate(Float.parseFloat(notes[i + 1])) //旋转角度
                        .position(ll);
            }
        }

    }
}