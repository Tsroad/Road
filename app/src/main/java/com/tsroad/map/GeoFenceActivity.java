package com.tsroad.map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;

/**
 * Created by tsroad on 25/04/15.
 */
public class GeoFenceActivity extends FragmentActivity implements AMap.OnMapClickListener, AMapLocationListener,GestureDetector.OnGestureListener{

    MapView mapView = null;
    LocationManagerProxy mLocationManagerProxy =null;
    String GEOFENCE_BROADCAST_ACTION="com.tsroad.map";
    PendingIntent mPendingIntent =null;
    private AMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //引入地图
        setContentView(R.layout.fence_trace);
        mapView = (MapView) findViewById(R.id.trace_mapView);
        mapView.onCreate(savedInstanceState);
        //设置监听
        mMap= mapView.getMap();
        mMap.setOnMapClickListener(this);
        //构建定位控制类
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);

        //构建地理围栏广播
        Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,intent, 0);

        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(GEOFENCE_BROADCAST_ACTION);

        this.registerReceiver(mGeoFenceReceiver,intentFilter);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 15,this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(GeoFenceActivity.this, "请点击屏幕一点，设置地理围栏", Toast.LENGTH_LONG).show();
    }

    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int i =  intent.getIntExtra("status",-1);
            Log.e("-----------------", "收到广播");

            if (1==i)
                Toast.makeText(GeoFenceActivity.this, "在地里围栏内部", Toast.LENGTH_SHORT).show();
            else if(0==i )
                Toast.makeText(GeoFenceActivity.this, "在地里围栏外部", Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        this.unregisterReceiver(mGeoFenceReceiver);
    }
    private int clickFlag=0;
    @Override
    public void onMapClick(LatLng latLng) {

       // if(clickFlag<=1)
        {
            Log.e("--------------------","lat:"+latLng.latitude);
            Log.e("--------------------","lon:"+latLng.longitude);
            mMap.addCircle(new CircleOptions().center(latLng).radius(10000));
            //注册地理围栏监听
            mLocationManagerProxy.addGeoFenceAlert(latLng.latitude, latLng.longitude, 10000, 1000 * 60*30 , mPendingIntent);
            clickFlag++;
        }

        
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

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


}
