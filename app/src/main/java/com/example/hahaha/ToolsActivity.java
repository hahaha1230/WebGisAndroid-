package com.example.hahaha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hahaha.Adapter.ToolsAdapter;
import com.example.hahaha.Class.MapTools;

import java.util.ArrayList;
import java.util.List;

public class ToolsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSettings;
    private List<MapTools> toolsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
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
    }

    private void initview() {
        recyclerViewSettings = (RecyclerView) findViewById(R.id.recyclerTools);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ToolsActivity.this);
        recyclerViewSettings.setLayoutManager(layoutManager);
        ToolsAdapter adapter=new ToolsAdapter(toolsList);
        recyclerViewSettings.setAdapter(adapter);

    }
}
