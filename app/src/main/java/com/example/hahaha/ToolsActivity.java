package com.example.hahaha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.hahaha.Adapter.ToolsAdapter;
import com.example.hahaha.Class.MapTools;

import java.util.ArrayList;
import java.util.List;


public class ToolsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSettings;
    private List<MapTools> toolsList = new ArrayList<>();
    private boolean locationSucceed;
    private double nowLongitude;
    private double nowLatitude;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        Intent intent=getIntent();
        locationSucceed=intent.getBooleanExtra("locationSucceed",false);
        nowLatitude=intent.getDoubleExtra("nowLatitude",39.926516);
        nowLongitude=intent.getDoubleExtra("nowLongitude",116.389366);
        cityName=intent.getStringExtra("cityName");
        initTools();
        initview();
    }

    private void initTools() {
        MapTools tool1 = new MapTools("计算两点之间距离", R.drawable.calculator_distance);
        toolsList.add(tool1);
        MapTools tool2 = new MapTools("查看当地天气", R.drawable.weather);
        toolsList.add(tool2);
        MapTools tool3 = new MapTools("行政区划", R.drawable.area_division);
        toolsList.add(tool3);
        MapTools tool4 = new MapTools("坐标转换", R.drawable.transform);
        toolsList.add(tool4);
        MapTools tool5 = new MapTools("获取POI", R.drawable.poi);
        toolsList.add(tool5);
        MapTools tool6 = new MapTools("查看速度", R.drawable.poi);
        toolsList.add(tool6);
    }

    private void initview() {
        recyclerViewSettings = (RecyclerView) findViewById(R.id.recyclerTools);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ToolsActivity.this);
        recyclerViewSettings.setLayoutManager(layoutManager);
        ToolsAdapter adapter=new ToolsAdapter(toolsList);
        recyclerViewSettings.setAdapter(adapter);
        adapter.setOnItemClickListener(new ToolsAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(View v, int position) {
                //Toast.makeText(ToolsActivity.this,"position"+position,Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent intent=new Intent(ToolsActivity.this,CalculateDistanceActivity.class);
                        intent.putExtra("locationSucceed",locationSucceed);
                        intent.putExtra("nowLatitude",nowLatitude);
                        intent.putExtra("nowLongitude",nowLongitude);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(ToolsActivity.this,WeatherSearchActivity.class);
                        intent1.putExtra("locationSucceed",locationSucceed);
                        intent1.putExtra("nowLatitude",nowLatitude);
                        intent1.putExtra("nowLongitude",nowLongitude);
                        intent1.putExtra("cityName",cityName);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2=new Intent(ToolsActivity.this,SearchDistrictBoundryActivity.class);
                        startActivity(intent2);
                        break;
                    case 4:
                        Intent intent4=new Intent(ToolsActivity.this,PoiKrywordSearchActivity.class);
                        intent4.putExtra("nowLatitude",nowLatitude);
                        intent4.putExtra("nowLongitude",nowLongitude);
                        intent4.putExtra("cityName",cityName);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5=new Intent(ToolsActivity.this,MeasureSpeedActivity.class);
                        intent5.putExtra("nowLatitude",nowLatitude);
                        intent5.putExtra("nowLongitude",nowLongitude);
                        intent5.putExtra("cityName",cityName);
                        startActivity(intent5);
                        break;
                        default:
                            break;
                }
            }
        });

    }
}
