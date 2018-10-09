package com.example.hahaha;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;


public class DisplayMapActivity extends AppCompatActivity implements AMapLocationListener,
        AMap.OnMapClickListener, View.OnClickListener {


    MapView aMapView = null;
    //初始化地图控制器对象
    AMap aMap;
    private UiSettings uiSettings;
    private AMapLocationClient aMapLocationClient;
    private Marker centerMarker;
    private double nowLatitude;
    private double nowLongitude;
    private boolean locationSucceed=false;
    private String cityName;

    private Button nightMode;
    private Button satelliteMode;
    private Button standardMode;
    private Button getScale;
    //需要进行检测的权限数组
    private String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    //记录需要申请哪些权限
    List<String> permissionList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);

        setTitle("查看地图");
        //获取地图控件
        aMapView = (MapView) findViewById(R.id.map);
        //在activity执行oncreate之时执行aMapView.oncreate(savedInstanceState)，创建地图
        aMapView.onCreate(savedInstanceState);

        getRight();
        initview();
        initMap();
        startLocation();

    }


    /**
     * 检查缺少哪些定位权限并请求权限
     */
    private void getRight() {
        for (int i=0;i<needPermissions.length;i++) {
            if (ContextCompat.checkSelfPermission(DisplayMapActivity.this,
                    needPermissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                permissionList.add(needPermissions[i]);
            }
        }
        //如果缺少权限那么去申请
        if (!permissionList.isEmpty()) {
            String[]permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(DisplayMapActivity.this,permissions,1);
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        aMapLocationClient = new AMapLocationClient(DisplayMapActivity.this);
        aMapLocationClient.setLocationListener(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setLocationCacheEnable(false);
        option.setOnceLocation(true);
        aMapLocationClient.setLocationOption(option);
        aMapLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    private void endLocation() {
        aMapLocationClient.stopLocation();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.myLocationType(MyLocationStyle.location);
        myLocationStyle.interval(3000);
        aMap.setMyLocationStyle(myLocationStyle);
        //uiSettings.setZoomControlsEnabled(true);
        //显示指南针
        uiSettings.setCompassEnabled(true);
        //显示比例尺
        uiSettings.setScaleControlsEnabled(true);
        //是否显示默认的定位按钮
        uiSettings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

    }

    /**
     * 初始化界面
     */
    private void initview() {
        nightMode = (Button) findViewById(R.id.nightMode);
        satelliteMode = (Button) findViewById(R.id.satelliteMode);
        standardMode = (Button) findViewById(R.id.standardMode);
        getScale = (Button) findViewById(R.id.getScale);

        nightMode.setOnClickListener(this);
        satelliteMode.setOnClickListener(this);
        standardMode.setOnClickListener(this);
        getScale.setOnClickListener(this);


        if (aMap == null) {
            aMap = aMapView.getMap();
            uiSettings = aMap.getUiSettings();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nightMode:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图模式
                break;
            case R.id.satelliteMode:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                break;
            case R.id.standardMode:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                break;
            case R.id.getScale:
                float scale = aMap.getScalePerPixel();
                Toast.makeText(this, "每像素代表" + scale + "米", Toast.LENGTH_SHORT).show();
                break;
        }
    }




    /**
     * 位置发生改变事件
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            //setMapCenter(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            //缩放等级
            //aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            aMap.setOnMapClickListener(this);
            nowLatitude=aMapLocation.getLatitude();
            nowLongitude=aMapLocation.getLongitude();
            cityName=aMapLocation.getCity();
            locationSucceed=true;
        } else {
            Toast.makeText(DisplayMapActivity.this, "定位失败，原因为：" +
                    aMapLocation.getErrorCode(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 地图点击事件
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tools,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolsMenu:
                Intent intent=new Intent(DisplayMapActivity.this,ToolsActivity.class);
                intent.putExtra("locationSucceed",locationSucceed);
                intent.putExtra("nowLatitude",nowLatitude);
                intent.putExtra("nowLongitude",nowLongitude);
                intent.putExtra("cityName",cityName);
                startActivityForResult(intent,2);
                break;
        }
        return true;
    }


    /**
     * 处理申请权限后的事件
     * @param requestCode
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result !=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有的权限才能使用该功能",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }
                else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 将定位获取到的经纬度缩放到中心
     * * @param latLng
     */
    private void setMapCenter(LatLng latLng) {
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (centerMarker !=null){
            centerMarker.setPositionNotUpdate(latLng);
        }
        else {
            MarkerOptions markerOptions=new MarkerOptions().icon
                    (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.position(latLng);
            centerMarker=aMap.addMarker(markerOptions);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        endLocation();
        aMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        aMapLocationClient.startLocation();
        aMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        endLocation();
        aMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        aMapView.onSaveInstanceState(outState);
    }
}

