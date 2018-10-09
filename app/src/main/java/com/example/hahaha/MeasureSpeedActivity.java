package com.example.hahaha;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MeasureSpeedActivity extends AppCompatActivity {
    private AMap aMap;
    private MapView mapView;
    private TextView accountResult;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private double nowLatitude;
    private double nowLongitude;
    private   Marker markerNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_speed);
        mapView = (MapView) findViewById(R.id.measureSpeedMap);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        accountResult=(TextView)findViewById(R.id.accountResult);

        init();
        initLocation();
        startLocation();
    }


    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        Intent intent=getIntent();
        nowLatitude=intent.getDoubleExtra("nowLatitude",0.0);
        nowLongitude=intent.getDoubleExtra("nowLongitude",0.0);
        LatLng now=new LatLng(nowLatitude,nowLongitude);

        markerNow = aMap.addMarker(new MarkerOptions().position(now)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.from)));

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(nowLatitude,
                nowLongitude), 15));
    }

    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    private void startLocation() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(true);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

   AMapLocationListener locationListener=new AMapLocationListener() {
       @Override
       public void onLocationChanged(AMapLocation location) {

           if (null != location) {

               LatLng a=new LatLng(43.828, 87.621);
               LatLng b=new LatLng(location.getLatitude(),location.getLongitude());
               List<LatLng>latLngList=new ArrayList<>();
               latLngList.add(b);

               PolylineOptions options=new PolylineOptions();
               options.width(20);//设置宽度
              // options.add(a,b);
               if (latLngList.size()>1) {
                   //for (int i=0;i<latLngList.size();i++) {
                       options.add(latLngList.get(latLngList.size()),latLngList.get(latLngList.size()-1));
                 //  }
               }
               aMap.addPolyline(options);
               StringBuffer sb = new StringBuffer();
               //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
               if(location.getErrorCode() == 0){
                   sb.append("定位成功" + "\n");
                   sb.append("经    度    : " + location.getLongitude() + "\n");
                   sb.append("纬    度    : " + location.getLatitude() + "\n");
                   sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                   // 获取当前提供定位服务的卫星个数
                   sb.append("地    址    : " + location.getAddress() + "\n");
               } else {
                   //定位失败
                   sb.append("定位失败" + "\n");
                   sb.append("错误码:" + location.getErrorCode() + "\n");
                   sb.append("错误信息:" + location.getErrorInfo() + "\n");
                   sb.append("错误描述:" + location.getLocationDetail() + "\n");
               }
               //解析定位结果，
               String result = sb.toString();
               accountResult.setText(result);
           } else {
              accountResult.setText("定位失败，loc is null");
           }
       }
   };

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }


    /**
     * 停止定位
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        destroyLocation();
    }

    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

}
