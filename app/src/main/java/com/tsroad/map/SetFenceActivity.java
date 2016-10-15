package com.tsroad.map;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import java.util.ArrayList;

/**
% @authors Keung Charteris & T.s.road CZQ
% @version 1.0 ($Revision$)
% @addr. GUET, Gui Lin, 540001,  P.R.China
% @contact : cztsiang@gmail.com
% @date Copyright(c)  2016-2020,  All rights reserved.
% This is an open access code distributed under the Creative Commons Attribution License, which permits 
% unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
 * Created by tsroad on 01/05/15.
 */

public class SetFenceActivity  extends FragmentActivity implements AMap.OnMapClickListener, AMapLocationListener,GestureDetector.OnGestureListener, LocationSource{

    private MapView mapView = null;
    private LocationManagerProxy mLocationManagerProxy =null;
    private String GEOFENCE_BROADCAST_ACTION="com.tsroad.map";
    private PendingIntent mPendingIntent =null;
    private AMap aMap=null;
    private AMap mMap=null;
    private LocationSource.OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private Marker marker;// 定位雷达小图标
    private Button setConfirmButton = null;
    //默认围栏半径2公里
    private double FenceRadius=2000;
    private EditText FenceRadiusInput=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //引入地图
        setContentView(R.layout.set_fence);
        mapView = (MapView) findViewById(R.id.set_center_fence);
        mapView.onCreate(savedInstanceState);
        //设置监听
        mMap= mapView.getMap();
        mMap.setOnMapClickListener(this);
        //设置确认按钮
        setConfirmButton=(Button)findViewById(R.id.set_confirm_button);
        setConfirmButton.setOnClickListener(new ConfirmButtonListener());

        //设置半径输入
        FenceRadiusInput= (EditText)findViewById(R.id.set_radius_fence);



        //构建定位控制类
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);

        //构建地理围栏广播
        Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,intent, 0);

        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(GEOFENCE_BROADCAST_ACTION);

        this.registerReceiver(mSetFenceReceiver,intentFilter);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 15,this);
        init();
        deactivate();

    }

    //设置按钮监听
    class  ConfirmButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            FenceRadius= Double.parseDouble(FenceRadiusInput.getText().toString());
            Toast.makeText(SetFenceActivity.this, "围栏半径设置为"+FenceRadiusInput.getText().toString()+"米"+"\n请点击屏幕一点，设置围栏中心", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(SetFenceActivity.this, "请设置地里围栏的信息", Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mSetFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int i =  intent.getIntExtra("status",-1);
            Log.e("-----------------", "收到广播");

            if (1==i)
                Toast.makeText(SetFenceActivity.this, "在地里围栏内部", Toast.LENGTH_SHORT).show();
            else if(0==i )
                Toast.makeText(SetFenceActivity.this, "在地里围栏外部", Toast.LENGTH_SHORT).show();
        }
    };
//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mSetFenceReceiver);
        mapView.onDestroy();
        deactivate();

    }
    private int clickFlag=0;
    @Override
    public void onMapClick(LatLng latLng) {

        deactivate();
        mMap.clear();
        SetUpMap2();
//         if(clickFlag<=1)
        {
            Log.e("--------------------","lat:"+latLng.latitude);
            Log.e("--------------------","lon:"+latLng.longitude);
            mMap.addCircle(new CircleOptions().center(latLng).radius(FenceRadius).strokeColor(Color.RED));
            Toast.makeText(SetFenceActivity.this, "围栏中心位置:"+latLng.toString(), Toast.LENGTH_SHORT).show();

            //注册地理围栏监听
            mLocationManagerProxy.addGeoFenceAlert(latLng.latitude, latLng.longitude, (float)FenceRadius, 1000 * 60*30 , mPendingIntent);

        }


    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


//------------------------------------------------------------
//------------------------------------------------------------
//-------------------------以下是定位程序-----------------------
//------------------------------------------------------------
//------------------------------------------------------------
//------------------------------------------------------------
    /**
     * 定位初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
//        Toast.makeText(SetFenceActivity.this, "请打开GPS", Toast.LENGTH_LONG).show();

    }
    private void setUpMap() {

        SetUpMap2();

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    private void SetUpMap2()
    {

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));

        //    giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
        marker = mapView.getMap().addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icons(giflist).period(50));
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setMyLocationRotateAngle(180);
        aMap.getUiSettings().setCompassEnabled(true);// 设置默认定位按钮是否显示
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    AMapLocation aLocation=null;
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mListener != null && aLocation != null) {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
            marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation
                    .getLongitude()));// 定位雷达小图标
            float bearing = aMap.getCameraPosition().bearing;
            aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
            aMap.getMinZoomLevel();
            if(this.aLocation == null || this.aLocation.getStreet() != aLocation.getStreet())
            {
                this.aLocation = aLocation;
                LatLng latng = new LatLng(aLocation.getLatitude(),aLocation.getLongitude());
//                marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
//                        .position(latng).title("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad()));
                marker.setPosition(latng);
                marker.setTitle("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad());
                marker.showInfoWindow();
            }

        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            mAMapLocationManager.requestLocationUpdates(
                    LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
    }

//------------------------------------------------------------
//------------------------------------------------------------
//-------------------------以上是定位程序-----------------------
//------------------------------------------------------------
//------------------------------------------------------------
//------------------------------------------------------------


}
