package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import overlayutil.TransitRouteOverlay;

public class ShortestPathResultActivity extends AppCompatActivity {
String startS,endS,mid1S,mid2S;
MapView mMapView;
BaiduMap mBaiduMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortest_path_result);
        Intent intent = getIntent();
        mMapView = findViewById(R.id.tool1_mapView);
        mBaiduMap = mMapView.getMap();
        if(intent.getStringExtra("Route_points").equals("2")){
            Toast.makeText(ShortestPathResultActivity.this,"Search type: 2 points", Toast.LENGTH_SHORT).show();
            routePlan(2,0,new int[]{0,0,0,0,0});
        }else if(intent.getStringExtra("Route_points").equals("3")){
            Toast.makeText(ShortestPathResultActivity.this,"Search type: 3 points", Toast.LENGTH_SHORT).show();
            routePlan(3,0,new int[]{0,0,0,0,0});
        }else if(intent.getStringExtra("Route_points").equals("4")){
            Toast.makeText(ShortestPathResultActivity.this,"Search type: 4 points", Toast.LENGTH_SHORT).show();
            routePlan(4,0,new int[]{0,0,0,0,0});
        }
    }
//the first parameter is the number of route points , second is how many time have the
//    method reloaded, the third is for calculate duration
    public void routePlan(int num,int times,int[] duration){
        mBaiduMap.clear();
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
        Intent intent = getIntent();
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }
            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                if(transitRouteResult.getRouteLines()!=null){
                if (transitRouteResult.getRouteLines().size() > 0) {

                    overlay.setData(transitRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
//                    those are for calculate the duration between 4 points
                    if(duration[2]<3&&duration[0]==0){
                        duration[1] += transitRouteResult.getRouteLines().get(0).getDuration();
                        duration[2] ++;
                    }
                    else if(duration[4]<3&&duration[0]==1){
                        duration[3] += transitRouteResult.getRouteLines().get(0).getDuration();
                        duration[4] ++;
                    }
                    if(duration[4]==3 && duration[0]==1){
                        duration[4]++;
                        if(duration[1]>=duration[3]){
                            duration[0]=1;
                            routePlan(4,0,duration);
                            Toast.makeText(ShortestPathResultActivity.this,"S->m2->m1->E is faster",Toast.LENGTH_LONG).show();
                        }else{
                            duration[0] = 0;
                            routePlan(4,0,duration);
                            Toast.makeText(ShortestPathResultActivity.this,"S->m1->m2->E is faster",Toast.LENGTH_LONG).show();
                        }
                    }

                    if(duration[2]==3 && duration[0]==0){
                        duration[0] = 1;
                        duration[2]++;
                        routePlan(4,0,duration);
                    }
// if the result is a null value, try to load again, the software and at most reload for three times
                }else{
                    if(times<3){
                        routePlan(num,times+1,duration);
                        Toast.makeText(ShortestPathResultActivity.this,"Loading",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ShortestPathResultActivity.this,"Sorry, route not found",Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                    if(times<3){
                        routePlan(num,times+1,duration);
                        Toast.makeText(ShortestPathResultActivity.this,"Loading",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ShortestPathResultActivity.this,"Sorry, route not found",Toast.LENGTH_SHORT).show();
                    }
            }
            }
            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        mSearch.setOnGetRoutePlanResultListener(listener);
        PlanNode stNode;
        PlanNode enNode;
        PlanNode mid1Node;
        PlanNode mid2Node;
        if(num == 2){
            stNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("Starting Point"));
            enNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("End Point"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .to(enNode)
                    .city("北京"));
        }else if(num ==3){
            stNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("Starting Point"));
            mid1Node = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("MiddlePoint1"));
            enNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("End Point"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .to(mid1Node)
                    .city("北京"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(mid1Node)
                    .to(enNode)
                    .city("北京"));
//            try to figure out which one is faster
        }else if(num ==4 && duration[0]==0){
            stNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("Starting Point"));
            mid1Node = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("MiddlePoint1"));
            mid2Node = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("MiddlePoint2"));
            enNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("End Point"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .to(mid1Node)
                    .city("北京"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(mid1Node)
                    .to(mid2Node)
                    .city("北京"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(mid2Node)
                    .to(enNode)
                    .city("北京"));
        }else if(num ==4 && duration[0]==1){
            stNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("Starting Point"));
            mid1Node = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("MiddlePoint1"));
            mid2Node = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("MiddlePoint2"));
            enNode = PlanNode.withCityNameAndPlaceName("北京",intent.getStringExtra("End Point"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .to(mid1Node)
                    .city("北京"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(mid2Node)
                    .to(mid1Node)
                    .city("北京"));
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(mid1Node)
                    .to(enNode)
                    .city("北京"));
        }
        mSearch.destroy();

    }
}