package com.tsroad.map;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Trace;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

/**
% @authors Keung Charteris & T.s.road CZQ
% @version 1.0 ($Revision$)
% @addr. GUET, Gui Lin, 540001,  P.R.China
% @contact : cztsiang@gmail.com
% @date Copyright(c)  2016-2020,  All rights reserved.
% This is an open access code distributed under the Creative Commons Attribution License, which permits 
% unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
 * Created by tsroad on 26/04/15.
 */

public class TraceActivity extends FragmentActivity implements AMapLocationListener, AMap.OnMapLoadedListener {

    MapView mapView = null;
    List<LatLng>  list =new ArrayList<LatLng>();

    private  LocationManagerProxy mLocationManagerProxy = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fence_trace);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MainActivity.PlaceholderFragment())
//                    .commit();
//        }

        mapView = (MapView) findViewById(R.id.trace_mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMap().setOnMapLoadedListener(this);
        //初始化定位对象
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //                                                            混合定位  定位时间间隔  定位距离间隔Gps  回调监听
        //注册定位
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,2000, 15, this);

//        LatLng latlng = new LatLng(0.0,0.0);
//        list.add(latlng);

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
    }

    AMapLocation amapLocation;
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {


        //定位回调
//        System.out.println(amapLocation.getAMapException().getErrorCode());
//        System.out.println("-------------------"+amapLocation.toString());
//        if()
//        {

            if(amapLocation != null && amapLocation.getAMapException().getErrorCode()==0)
            {
//                if( this.amapLocation.getLatitude() != amapLocation.getLatitude()&&this.amapLocation.getLongitude()!= amapLocation.getLongitude())
//                {
                    this.amapLocation = amapLocation;
                    LatLng latlng = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                    list.add(latlng);
                    Log.e("-------------", amapLocation.toString());
                    mapView.getMap().setOnMapLoadedListener(this);
//                }
            }

//        }

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
    public void onMapLoaded() {

        mapView.getMap().addPolyline(new PolylineOptions().addAll(list).color(Color.GREEN));
    }
}
