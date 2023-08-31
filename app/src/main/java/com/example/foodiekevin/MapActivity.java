package com.example.foodiekevin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Listviews.CommentListActivity;
import Listviews.NearbyLocationsActivity;
import Listviews.RouteResultActivity;
import Listviews.TravelRouteListActivity;
import database.DatabaseServer;
import items.Locations;
import items.LocationsInfo;
import overlayutil.PoiOverlay;
import overlayutil.TransitRouteOverlay;
import overlayutil.WalkingRouteOverlay;

public class MapActivity extends Activity {
    //    those are the code to store the information added by user in the form of Strings
//    into database
    List<String> mark_points = new ArrayList<String>();
    List<String> mark_lines = new ArrayList<String>();
    List<String> mark_notes = new ArrayList<String>();
    UiSettings mUiSettings;
    public static String username;
    static int id;
    private boolean drawable = false;
    static String spS = "上地-地铁站";
    static String epS = "北工大西门-地铁站";
    public LocationClient mLocationClient = null;
    TransitRouteOverlay overlay;
    BaiduMap mBaiduMap, mBaiduMap2;
    PoiSearch mPoiSearch;
    SuggestionSearch mSuggestionSearch;
    TextView tv_usn;
    private ImageView tool1, tool2, tool3;
    private EditText et_ps1;
    private EditText et_ps2;
    private EditText et_newln;
    private EditText et_newl;
    private EditText et_city;
    private EditText et_end_city;
    private String location;
    private String location_name;
    private String search_type = "餐厅";
    private ArrayList<String> results = new ArrayList<String>();
    private ListView lv1;
    private Button bt1;
    private Button bt2;
    private Button bt_remove_all, bt_draw, bt_save_route;
    private String ps1;
    private String ps2;
    private Button bt_find_location;
    int routeLocations = 0;
    private ArrayList<LatLng> routeLocationsList = new ArrayList<LatLng>();
    private ArrayList<LatLng> drawPointsList = new ArrayList<LatLng>();
    TransitRouteResult mtransitRouteResult;
    LatLng ll = null;
    // those are the parameters for maping
    private RoutePlanSearch mSearch;
    private MapView mMapView = null;
    private MapView mMapView2 = null;
    private MyLocationListener myListener = new MyLocationListener();
    private Button bt_search, bt_transform;
    private EditText et_sp;
    private EditText et_ep;
    private List<Locations> list = new ArrayList<>();
    private boolean isFirstLoc = true;
    public static List<LocationsInfo> LocationsInfoList = new ArrayList<>();
    private int searchScale;
    PoiOverlay poiOverlay;
    private Spinner sp1;
    private float scale;
    private Button bt_collect;
    private String draw_act = "";
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OnClick onClick = new OnClick();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.GCJ02);
        SDKInitializer.setHttpsEnable(true);

//        mBaiduMap.setMyLocationEnabled(true);


        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.tab1, tabHost.getTabContentView());
        inflater.inflate(R.layout.tab3, tabHost.getTabContentView());
        inflater.inflate(R.layout.tab2, tabHost.getTabContentView());
        inflater.inflate(R.layout.tab4, tabHost.getTabContentView());
//        inflater.inflate(R.layout.tab1, tabHost.getTabContentView());
//        add tabs to the mapActivity
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.tab1)).setContent(R.id.left));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.tab2)).setContent(R.id.right));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.tab3)).setContent(R.id.middle));
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("", getResources().getDrawable(R.drawable.tab4)).setContent(R.id.right_right));

        iniviews();
        inimap();


        Spinner spinner = (findViewById(R.id.sp_type));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] autoInfo = getResources().getStringArray(R.array.auto);

//  set the search type of locations near a point
                switch (pos) {
                    case 0:
                        search_type = "";
                        bt_search.setText("Search");
                        et_sp.setEnabled(true);
                        break;
                    case 1:
                        search_type = "餐厅";
                        bt_search.setText("Find");
                        et_sp.setEnabled(false);

                        break;
                    case 2:

                        search_type = "地铁";
                        bt_search.setText("Find");
                        et_sp.setEnabled(false);

                        break;
                    case 3:
                        search_type = "公园";
                        bt_search.setText("Find");
                        et_sp.setEnabled(false);
                        break;
                    case 4:
                        search_type = "大学";
                        bt_search.setText("Find");
                        et_sp.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        et_sp.setText(spS);
        et_ep.setText(epS);
        mBaiduMap.setIndoorEnable(true);
//show the hello to users
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        tv_usn.setText("Hello " + username + " !");

//set listeners for buttons
        tool1.setOnClickListener(onClick);
        tool2.setOnClickListener(onClick);
        tool3.setOnClickListener(onClick);
        bt_search.setOnClickListener(onClick);
        bt_transform.setOnClickListener(onClick);
        bt1.setOnClickListener(onClick);
        bt2.setOnClickListener(onClick);
        bt_remove_all.setOnClickListener(onClick);
        bt_draw.setOnClickListener(onClick);
        bt_save_route.setOnClickListener(onClick);
//initiate the database server and initiate the listview of the locations tab
        DatabaseServer ds = new DatabaseServer(getApplicationContext());
        Cursor cursor = ds.getLocations(username);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Locations(
                    cursor.getInt(cursor.getColumnIndex("Location_id")),
                    cursor.getString(cursor.getColumnIndex("location_name")),
                    cursor.getString(cursor.getColumnIndex("location"))
            ));
            cursor.moveToNext();
        }
//      Load the data into the object to materialize it
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("location_name", list.get(i).location_name);
            item.put("location", list.get(i).location);
            data.add(item);
        }
//      Use SimpleAdapter To realize the function of list
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.location_item,
                new String[]{"location_name", "location"}, new int[]{R.id.tv_location_name, R.id.tv_location});
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                MapActivity.id = list.get(position).id;
                intent.putExtra("location", list.get(position).location);
                intent.setClass(MapActivity.this, LocationDetailActivity.class);
                startActivity(intent);
            }
        });
        mPoiSearch = PoiSearch.newInstance();

        OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {


                    poiOverlay = new PoiOverlay(mBaiduMap);

                    //设置Poi检索数据
                    poiOverlay.setData(poiResult);

                    //将poiOverlay添加至地图并缩放至合适级别
                    poiOverlay.addToMap();
//                    poiOverlay.zoomToSpan();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }

            //废弃
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(listener);

//        mPoiSearch.searchNearby(new PoiNearbySearchOption()
//                .location(new LatLng(39.915446, 116.403869))
//                .radius(100).keyword("餐厅").pageNum(0));


    }

    //initiate views
    private void iniviews() {
        tv_usn = findViewById(R.id.tv_change_password_username);
        et_ps1 = findViewById(R.id.et_new_password1);
        et_ps2 = findViewById(R.id.et_new_password2);
        bt1 = findViewById(R.id.bt_new_password);
        bt2 = findViewById(R.id.bt_new_location);
        et_newl = findViewById(R.id.et_new_location);
        et_newln = findViewById(R.id.et_new_locationame);
        lv1 = findViewById(R.id.lv_1);
        mMapView = findViewById(R.id.mapView);
        et_sp = findViewById(R.id.et_start_point);
        et_ep = findViewById(R.id.et_end_point);
        bt_search = findViewById(R.id.bt_search);
        bt_transform = findViewById(R.id.bt_transform);
        et_city = findViewById(R.id.et_city);
        sp1 = findViewById(R.id.sp_type);
        bt_remove_all = findViewById(R.id.bt_remove_all);
        bt_draw = findViewById(R.id.bt_draw);
        tool1 = findViewById(R.id.bt_tool_1);
        tool2 = findViewById(R.id.bt_tool_2);
        tool3 = findViewById(R.id.bt_tool_3);
        bt_save_route = findViewById(R.id.bt_save_route);
    }

    //  initiate the map functions
    private void inimap() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setTrafficEnabled(true);
//        Cancels the zoom in event when you double-click the map


        mLocationClient = new LocationClient(this);
//      Set the parameters of location
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // Open gps
        option.setCoorType("WGS84"); // set the class of coordinate
        option.setScanSpan(1000);
//      Pass parameters into the list
        mLocationClient.setLocOption(option);
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();
//     when the users click the button, it will return the latitude and longtitude of the location



        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                                            @Override
                                            public void onMapClick(LatLng latLng) {
                                                if (drawable) {
                                                    BitmapDescriptor bitmap;
                                                    bitmap = BitmapDescriptorFactory
                                                            .fromResource(R.drawable.icon_mark);
                                                    OverlayOptions option = new MarkerOptions()
                                                            .position(latLng)
                                                            .icon(bitmap);
                                                    mBaiduMap.addOverlay(option);
                                                    mark_points.add(latLng.latitude + "");
                                                    mark_points.add(latLng.longitude + "");
                                                    Log.e("TAG", mark_points.toString());
                                                } else {
                                                    Toast.makeText(MapActivity.this, "Click on map latitude: " + latLng.latitude + ", longitude: " + latLng.longitude, Toast.LENGTH_SHORT).show();

                                                }

                                            }

                                            //      when user click on a item, it will return the name of the item like xx subway station
                                            @Override
                                            public boolean onMapPoiClick(MapPoi mapPoi) {
                                                if (!drawable) {
                                                    Toast.makeText(MapActivity.this, "Click on POI item names：" + mapPoi.getName(), Toast.LENGTH_SHORT).show();
                                                    AlertDialog alertDialog2 = new AlertDialog.Builder(MapActivity.this)
                                                            .setTitle("Activity on the location")
                                                            .setMessage("You clock on location " + mapPoi.getName())
                                                            .setIcon(R.mipmap.ic_launcher)
                                                            .setPositiveButton("Show comments", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                    Intent intent = getIntent();
                                                                    intent.setClass(MapActivity.this, CommentListActivity.class);
                                                                    intent.putExtra("poi", mapPoi.getUid());
                                                                    startActivity(intent);
                                                                }
                                                            })

                                                            .setNegativeButton("Add comments", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Intent intent = getIntent();
                                                                    intent.setClass(MapActivity.this, CommentActivity.class);
                                                                    intent.putExtra("poi", mapPoi.getUid());
                                                                    intent.putExtra("poi_name", mapPoi.getName());
                                                                    intent.putExtra("poi_latitude", mapPoi.getPosition().latitude);
                                                                    intent.putExtra("poi_longitude", mapPoi.getPosition().longitude);
                                                                    startActivity(intent);
                                                                }
                                                            })

                                                            .create();
                                                    alertDialog2.show();
                                                } else {
                                                    BitmapDescriptor bitmap;
                                                    bitmap = BitmapDescriptorFactory
                                                            .fromResource(R.drawable.icon_mark);
                                                    OverlayOptions option = new MarkerOptions()
                                                            .position(mapPoi.getPosition())
                                                            .icon(bitmap);
                                                    mBaiduMap.addOverlay(option);
                                                    mark_points.add(mapPoi.getPosition().latitude + "");
                                                    mark_points.add(mapPoi.getPosition().longitude + "");
                                                    Log.e("TAG", mark_points.toString());
                                                    }

                                                return true;
                                            }
                                        }
        );
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (!drawable) {

                } else {
                    draw_act = "line";
//                this allow users to draw on the map, use a list to store latitude and longitude information
                    mark_lines.add(latLng.latitude + "");
                    mark_lines.add(latLng.longitude + "");
                    drawPointsList.add(latLng);
                    if (drawPointsList.size() >= 2) {
                        OverlayOptions mOverlayOptions = new PolylineOptions()
                                .width(10)
                                .color(0xAAFF0000)
                                .points(drawPointsList);
                        Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
                        drawPointsList.remove(0);
                        Log.e("TAG", mark_lines.toString());
                    }
                }
            }

        });

//        Allows you to add text messages when you double-click a location

        BaiduMap.OnMapDoubleClickListener listener = new BaiduMap.OnMapDoubleClickListener() {
            @Override
//            if user double click on a item, enable user to add comment on the position
            public void onMapDoubleClick(LatLng point) {
                if (drawable) {

                    EditText et = new EditText(MapActivity.this);
                    et.setHint("Please enter your notes");
                    EditText et1 = new EditText(MapActivity.this);
                    et1.setHint("Please enter nodes angle");
                    et1.setText("0");
                    LinearLayout ll = new LinearLayout(MapActivity.this);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(et);
                    ll.addView(et1);
                    Toast.makeText(MapActivity.this, "You double click the point", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    builder.setView(ll)
                            .setTitle("Add text note")
                            .setMessage("Would you like to add a comment？")
                            // Add action buttons
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        OverlayOptions mTextOptions = new TextOptions()

                                                .text(et.getText().toString().replace(","," ")) //文字内容
                                                .bgColor(0xAAFFFF00) //背景色
                                                .fontSize(30) //字号
                                                .fontColor(0xFFFF00FF)
                                                .rotate(Float.parseFloat(et1.getText().toString())) //旋转角度
                                                .position(point);
                                        Overlay mText = mBaiduMap.addOverlay(mTextOptions);
                                        mark_notes.add(et.getText().toString().replace(","," "));
                                        mark_notes.add(et1.getText().toString().replace(","," "));
                                        mark_notes.add(point.latitude + "");
                                        mark_notes.add(point.longitude + "");
                                        draw_act = "note";
                                    } catch (Exception e) {
                                        Toast.makeText(MapActivity.this, "please check your note!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .create().show();
                }
            }
        };
        mBaiduMap.setOnMapDoubleClickListener(listener);
    }


    //      manage the life circle of the activity
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();

    }

    //    set the onclick listeners of the activity
    private class OnClick implements View.OnClickListener {
        Intent intent;
        DatabaseServer ds = new DatabaseServer(getApplicationContext());

        public void onClick(View view) {
            intent = null;
            switch (view.getId()) {
//On the basis of the original mark, I added a new text mark to facilitate users to explain their mark route.
// Double click the map to pop up a dialog box. The first line can be filled with text information,
// and the second line is the angle at which users want the text to rotate
                case R.id.bt_save_route:
                    EditText et = new EditText(MapActivity.this);
                    et.setHint("Please enter your Title");
                    Toast.makeText(MapActivity.this, "You double click the point", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    builder.setView(et)
                            .setTitle("Add a title")
                            .setMessage("Add a title for your route:-)\"")
                            // Add action buttons
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    if (et.getText().toString() != "") {
                                        if (mark_points.size() != 0 && mark_points.size() != 0 && mark_points.size() != 0) {
                                            if (ds.addRoute(mark_points.toString(), mark_lines.toString(), mark_notes.toString(), et.getText().toString().replace(","," "))) {
                                                Toast.makeText(MapActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                                                routeLocationsList = new ArrayList<LatLng>();
                                                routeLocations = 0;
                                                drawPointsList = new ArrayList<LatLng>();
                                                mBaiduMap.clear();
                                                mark_lines = new ArrayList<String>();
                                                mark_notes = new ArrayList<String>();
                                                mark_points = new ArrayList<String>();
                                            } else {
                                                Toast.makeText(MapActivity.this, "Upload fail", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(MapActivity.this, "Please add something to your route", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MapActivity.this, "Please enter an title", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            }).create().show();

                    break;


                case R.id.bt_tool_1:
                    Intent intent_tool1 = getIntent();
                    intent_tool1.setClass(MapActivity.this, ShortestPathActivity.class);
                    startActivity(intent_tool1);
                    break;
                case R.id.bt_tool_2:
                    Intent intent_tool2 = getIntent();
                    intent_tool2.setClass(MapActivity.this, TravelRouteListActivity.class);
                    startActivity(intent_tool2);
                    break;
                case R.id.bt_tool_3:
                    Intent intent_tool3 = getIntent();
                    intent_tool3.setClass(MapActivity.this, WebsidesActivity.class);
                    startActivity(intent_tool3);
                    break;
                case R.id.bt_remove_all:
                    Toast.makeText(MapActivity.this, "Remove all overlays from map", Toast.LENGTH_SHORT).show();
                    routeLocationsList = new ArrayList<LatLng>();
                    routeLocations = 0;
                    drawPointsList = new ArrayList<LatLng>();
                    mBaiduMap.clear();
                    mark_lines = new ArrayList<String>();
                    mark_notes = new ArrayList<String>();
                    mark_points = new ArrayList<String>();
                    break;

                case R.id.bt_draw:
                    drawable = !drawable;
                    if (drawable) {
                        mUiSettings = mBaiduMap.getUiSettings();
                        mUiSettings.setAllGesturesEnabled(false);
                        Toast.makeText(MapActivity.this, "Enable Drawing", Toast.LENGTH_SHORT).show();
                    } else {
                        mUiSettings = mBaiduMap.getUiSettings();
                        mUiSettings.setAllGesturesEnabled(true);
                        Toast.makeText(MapActivity.this, "Disable Drawing", Toast.LENGTH_SHORT).show();
                        mark_lines.add("B");
                        mark_lines.add("B");
                        drawPointsList = new ArrayList<LatLng>();
                    }
                    break;
                case R.id.bt_search:
                    drawable = false;
                    if (bt_search.getText() == "Search") {
                        LocationsInfoList = new ArrayList<LocationsInfo>();
//                  In Baidu map API, there is a kind of thing called overlay which you can use it to cover the map
//                  object
                        try {
                            mBaiduMap.clear();
                        } catch (Exception e) {
                        }
//      I have carefully modified and adapted the function methods provided in the official documents
                        mSearch = RoutePlanSearch.newInstance();
                        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
                            @Override
//                        sometimes the query for route have no result, thus I need to fix the exception
                            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                                if (walkingRouteResult.getRouteLines() == null) {
                                    Toast.makeText(MapActivity.this, "Can not get result, check your input", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (walkingRouteResult.getRouteLines().size() > 0) {
                                    overlay.setData(walkingRouteResult.getRouteLines().get(0));
                                    overlay.addToMap();
                                }
                            }

                            @Override
                            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {


                                if (transitRouteResult.getRouteLines() == null) {
                                    Toast.makeText(MapActivity.this, "Can not get result", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String a = "";
                                for (int j = 0; j < transitRouteResult.getRouteLines().size(); j++) {
                                    a = "";
                                    for (int i = 0; i < transitRouteResult.getRouteLines().get(j).getAllStep().size(); i++) {
                                        a += transitRouteResult.getRouteLines().get(j).getAllStep().get(i).getInstructions();
                                    }
                                    results.add(a);
                                }


                                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                                if (transitRouteResult.getRouteLines().size() > 0) {
//                                Log.e("map", String.valueOf(transitRouteResult.writeToParcel()));
                                    overlay.setData(transitRouteResult.getRouteLines().get(0));
                                    overlay.addToMap();
                                    overlay.zoomToSpan();

                                    Toast.makeText(MapActivity.this, "Route plan success", Toast.LENGTH_SHORT).show();
                                    List<TransitRouteLine.TransitStep> allStep = transitRouteResult.getRouteLines().get(0).getAllStep();
                                    List<LatLng> latLngs = transitRouteResult.getRouteLines().get(0).getAllStep().get(0).getWayPoints();
                                    for (int i = 0; i < allStep.size(); i++) {
                                        for (int j = 0; j < latLngs.size(); j++) {

                                            boolean find = false;

                                            double lon = latLngs.get(j).longitude;
                                            double la = latLngs.get(j).latitude;


                                            DatabaseServer ds = new DatabaseServer(getApplicationContext());
                                            Cursor cursor = ds.getScaleLocation(la, lon, 0.006);

                                            if (cursor.moveToFirst()) {
                                                while (!cursor.isAfterLast()) {
                                                    for (int k = 0; k < LocationsInfoList.size(); k++) {
                                                        if (cursor.getString(cursor.getColumnIndexOrThrow("location_name")).equals(LocationsInfoList.get(k).getLocationName())) {
                                                            find = true;
                                                        }
                                                    }
                                                    if (!find) {
                                                        String li = cursor.getString(cursor.getColumnIndexOrThrow("location_id"));
                                                        String ln = cursor.getString(cursor.getColumnIndexOrThrow("location_name"));
                                                        Double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                                                        Double longt = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                                                        Float rate = cursor.getFloat(cursor.getColumnIndexOrThrow("rate"));
                                                        LocationsInfo Lifo = new LocationsInfo(li, ln, rate, longt, lat);
                                                        LocationsInfoList.add(Lifo);
                                                    }
                                                    find = false;
                                                    cursor.moveToNext();
                                                }
                                            }

                                        }
                                    }
                                    Collections.sort(LocationsInfoList);
                                    for (int z = 0; z < LocationsInfoList.size(); z++) {
                                        Log.e("TAG", LocationsInfoList.get(z).getRate() + "");
                                        LatLng point = new LatLng(LocationsInfoList.get(z).getLocationLatitude(), LocationsInfoList.get(z).getLocationLongitude());

                                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                                .fromResource(R.drawable.icon_mark);

                                        OverlayOptions option = new MarkerOptions()
                                                .position(point)
                                                .icon(bitmap);

                                        mBaiduMap.addOverlay(option);
                                    }


                                }

                                AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this)
                                        .setTitle("Get locations success")
                                        .setMessage("Show the result?")
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setPositiveButton("Yes!!!", new DialogInterface.OnClickListener() {
                                            @Override

                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = getIntent();
                                                NearbyLocationsActivity.LocationsInfoList = MapActivity.LocationsInfoList;
                                                intent.setClass(MapActivity.this, NearbyLocationsActivity.class);
                                                startActivity(intent);
                                            }
                                        })

                                        .setNegativeButton("Route", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                intent = getIntent();
                                                intent.putExtra("routeresult", results);
                                                intent.setClass(MapActivity.this, RouteResultActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .create();

                                alertDialog.show();

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
                        Toast.makeText(MapActivity.this, "form " + et_sp.getText().toString() + " to: " + et_ep.getText().toString(), Toast.LENGTH_SHORT).show();
//                    Set start and end points
                        PlanNode stNode = PlanNode.withCityNameAndPlaceName(et_city.getText().toString(), et_sp.getText().toString());
                        PlanNode enNode = PlanNode.withCityNameAndPlaceName(et_city.getText().toString(), et_ep.getText().toString());
//                    Query route
                        mSearch.transitSearch((new TransitRoutePlanOption())
                                .from(stNode)
                                .to(enNode).city(et_city.getText().toString())
                        );
                        mSearch.destroy();
                        break;
//                I have carefully modified and adapted the function methods provided in the official documents
//
                    } else if (bt_search.getText().toString() == "Find") {
                        sp1.setEnabled(false);
                        Toast.makeText(MapActivity.this, "Transform the location into latitude and longitude", Toast.LENGTH_SHORT).show();
                        RelativeLayout TabLay = findViewById(R.id.left);
                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

                        LinearLayout ly = (LinearLayout) inflater.inflate(
                                R.layout.simple_spinner, null, false).findViewById(
                                R.id.linear_Spinner);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.topMargin = 200;
                        TabLay.addView(ly, lp);
                        Spinner spinner = findViewById(R.id.sp_scale);
//                        add thr spinner to the map if user click find and enable user to set scale size
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        searchScale = 5000;
                                        break;
                                    case 1:
                                        searchScale = 3000;
                                        break;
                                    case 2:
                                        searchScale = 1000;
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

//                        try to remove all the overlay on the map, used some codes from official file of baidu map
                        try {
                            mBaiduMap.clear();
                        } catch (Exception e) {
                        }

                        mSuggestionSearch = SuggestionSearch.newInstance();

                        OnGetSuggestionResultListener suglistener = new OnGetSuggestionResultListener() {
                            @Override
                            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                                try {
                                    ll = suggestionResult.getAllSuggestions().get(0).pt;
                                } catch (NullPointerException e) {
                                    Toast.makeText(MapActivity.this, "Please check your input for start point", Toast.LENGTH_SHORT).show();
                                }

                            }
                        };

                        mSuggestionSearch.setOnGetSuggestionResultListener(suglistener);
                        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                                .city(et_city.getText().toString())
                                .keyword(et_sp.getText().toString()));
                        mSuggestionSearch.destroy();
                        bt_search.setText("Get");

                    } else if (ll != null) {
//                        remove the spinner from the map
                        RelativeLayout TabLay = findViewById(R.id.left);
                        TabLay.removeView(findViewById(R.id.linear_Spinner));
                        sp1.setEnabled(true);
                        Toast.makeText(MapActivity.this, "Start to search the relate positions" + ll.latitude + " " + ll.longitude, Toast.LENGTH_SHORT).show();
                        mPoiSearch = PoiSearch.newInstance();
                        OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
                            @Override
                            public void onGetPoiResult(PoiResult poiResult) {
                                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                                    PoiOverlay poiOverlay = new PoiOverlay(mBaiduMap);
                                    List<PoiAddrInfo> p = poiResult.getAllAddr();
                                    poiOverlay.setData(poiResult);
                                    poiOverlay.addToMap();
                                    poiOverlay.zoomToSpan();
                                }
                            }

                            @Override
                            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

                            }

                            @Override
                            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

                            }

                            //废弃
                            @Override
                            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

                            }
                        };

                        mPoiSearch.setOnGetPoiSearchResultListener(listener);
                        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
                        nearbySearchOption.location(ll);
                        nearbySearchOption.keyword(search_type);
                        nearbySearchOption.radius((int) searchScale);// Search radium
                        nearbySearchOption.pageNum(0);
                        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
                        ll = null;
                        et_sp.setEnabled(true);
                        bt_search.setText("Search");
                    } else {
                        Toast.makeText(MapActivity.this, "Transforming, please wait", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.bt_transform:
                    drawable = false;
                    if (et_sp.getText().toString() == "" || et_ep.getText().toString() == "") {
                        Toast.makeText(MapActivity.this, "You need to fill the blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSuggestionSearch = SuggestionSearch.newInstance();
//transform the words into formal format
                    OnGetSuggestionResultListener suglistener1 = new OnGetSuggestionResultListener() {
                        @Override
                        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                            try {
//                              This function will return a series of place associations and select the closest one
                                et_sp.setText(suggestionResult.getAllSuggestions().get(0).getKey());
                            } catch (NullPointerException e) {
                                Toast.makeText(MapActivity.this, "Please check your input for start point", Toast.LENGTH_SHORT).show();
                            }

                        }
                    };

                    mSuggestionSearch.setOnGetSuggestionResultListener(suglistener1);
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                            .city(et_city.getText().toString())
                            .keyword(et_sp.getText().toString()));
                    mSuggestionSearch.destroy();

                    mSuggestionSearch = SuggestionSearch.newInstance();
                    OnGetSuggestionResultListener suglistener2 = new OnGetSuggestionResultListener() {
                        @Override
                        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                            try {
                                et_ep.setText(suggestionResult.getAllSuggestions().get(0).getKey());
                            } catch (NullPointerException e) {
                                Toast.makeText(MapActivity.this, "Please check your input for end point", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    mSuggestionSearch.setOnGetSuggestionResultListener(suglistener2);
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                            .city(et_city.getText().toString())
                            .keyword(et_ep.getText().toString()));
                    mSuggestionSearch.destroy();
                    break;
//                Check whether the two passwords entered by the user are consistent
                case R.id.bt_new_password:
                    ps1 = et_ps1.getText().toString();
                    ps2 = et_ps2.getText().toString();
//                Check the length of the password entered by the user
                    if (ps1.equals(ps2) && ps1.length() >= 6) {
                        ds.changePassWord(username, ps1);
                        Toast.makeText(MapActivity.this, "Successfully change the password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MapActivity.this, "Fail to change the password", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.bt_new_location:
//                    Add a new location to the code
                    location = et_newl.getText().toString();
                    location_name = et_newln.getText().toString();
                    ds.addLocation(username, location_name, location);
                    Intent intent = new Intent(MapActivity.this, MapActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    break;
            }

        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null || mMapView == null) {
                return;
            }
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    .direction(location.getDirection()).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            mBaiduMap.setMyLocationData(locData);

//            Set the positioning function of the phone
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection()).latitude(39.879180)
                    .longitude(116.47841).build();
            mBaiduMap.setMyLocationData(locData);
        }
    }

}
