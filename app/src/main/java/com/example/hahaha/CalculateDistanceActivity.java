package com.example.hahaha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**
 * 计算两点间的距离
 */
public class CalculateDistanceActivity extends AppCompatActivity implements AMap.OnMarkerDragListener {

    private AMap aMap;
    private MapView mapView;
    private LatLng latlngA;
    private LatLng latlngB;
    private Marker markerA;
    private Marker markerB;
    private TextView displayDistance;
    private float distance;
    private boolean locationSucceed;
    private double nowLatitude;
    private  double nowLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_distance);
        mapView=(MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        init();
        distance = AMapUtils.calculateLineDistance(markerA.getPosition(), markerB.getPosition());
        displayDistance.setText("长按Marker可拖动\n两点间距离为："+distance+"m");

    }

    private void init() {

        Intent intent=getIntent();
        locationSucceed=intent.getBooleanExtra("locationSucceed",false);
        nowLatitude=intent.getDoubleExtra("nowLatitude",31.539992);
        nowLongitude=intent.getDoubleExtra("nowLongitude",104.68562);

        latlngA=new LatLng(nowLatitude,nowLongitude);
        latlngB=new LatLng(31.549992,104.69562);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        displayDistance = (TextView) findViewById(R.id.displayDistance);
    }


    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);
        /*markerA = aMap.addMarker(new MarkerOptions().position(latlngA)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));*/

        markerA = aMap.addMarker(new MarkerOptions().position(latlngA)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.from)));
       markerB = aMap.addMarker(new MarkerOptions().position(latlngB)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.to)));
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.539992,
                104.68562), 15));

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

        distance=AMapUtils.calculateLineDistance(markerA.getPosition(),markerB.getPosition());
        displayDistance.setText("长按Marker可拖动\n两点间距离为："+distance+"m");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }


    /**
     * 此方法必须重载
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    /**
     * 此方法必须重载
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 此方法必须重载
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    /**
     * 此方法必须重载
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
