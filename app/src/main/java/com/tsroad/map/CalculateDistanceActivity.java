package com.tsroad.map;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.tsroad.map.util.AMapUtil;
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
 * Created by tsroad on 5/6/15.
 */

public class CalculateDistanceActivity extends FragmentActivity implements AMap.OnMapClickListener,LocationSource,AMapLocationListener,View.OnClickListener {

    private final static double PI = 3.14159265358979323;// 圆周率
    private final static double EARTH_RADIUS = 6371229;  // 地球的半径
    private AMapLocation aLocation=null;
    private AMap aMap=null;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private Marker marker;// 定位雷达小图标
    private Marker startMarker;
    private Marker nextMarker;
    private LatLng startLatLon;
    private LatLng nextLatLon;
    private double sumDistance=0;
    private TextView textView=null;
    private int PointNumber=0;
    private Button calcButton=null;
    List<LatLng> list =new ArrayList<LatLng>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_distance);
        mapView= (MapView)findViewById(R.id.calc_dis);
        mapView.onCreate(savedInstanceState);
        textView =(TextView)findViewById(R.id.calc_dis_text);
        calcButton=(Button)findViewById(R.id.calc_dis_but);
        calcButton.setOnClickListener(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate(); //关闭监听
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        activate(mListener);// 设置定位监听

    }


//    在给定2点的经纬度，通过java代码和oracle存储过程来计算出点的距离 单位是(米)
//    oracle存储过程：
//    create or replace procedure SP_GET_DISTANCE
//            (cx in number,cy in number,sx in number, sy in number,distance out varchar2)
//    is
//    d number;
//    x number;
//    y number;
//    r number;
//    pi number;
//    begin
//    --开始计算
//    r:=6371229;--地球半径
//    pi:=3.14159265358979323;--圆周率
//    x:=(sx-cx)*pi*r*cos((sy+cy)/2*pi/180)/180;
//    y:=(sy-cy)*pi*r/180;
//    d:=SQRT(power(x,2)+power(y,2));
//    distance:=to_char(d,9999999999999.99);
//    end SP_GET_DISTANCE;


 //Java.java
//    package com.wpn.web.util;
//
//    public class Distance {
//        private final static double PI = 3.14159265358979323;// 圆周率
//        private final static double R = 6371229;  // 地球的半径
//
//        private Distance() {
//        }
//
//        /**
//         * 纬度lat 经度lon
//         * @param longt1
//         * @param lat1
//         * @param longt2
//         * @param lat2
//         * @return
//         */
//        public static double getDistance(double longt1, double lat1, double longt2, double lat2) {
//            double x, y, distance;
//            x = (longt2 - longt1) * PI * R * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
//            y = (lat2 - lat1) * PI * R / 180;
//            distance = Math.hypot(x, y);
//            return distance;
//        }
//
//    /*public enum GaussSphere {
//        Beijing54, Xian80, WGS84,
//    }
//
//    private static double Rad(double d) {
//        return d * Math.PI / 180.0;
//    }
//    public static double DistanceOfTwoPoints(double lng1, double lat1, double lng2, double lat2, GaussSphere gs) {
//        double radLat1 = Rad(lat1);
//        double radLat2 = Rad(lat2);
//        double a = radLat1 - radLat2;
//        double b = Rad(lng1) - Rad(lng2);
//        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
//        s = s * (gs == GaussSphere.WGS84 ? 6378137.0 : (gs == GaussSphere.Xian80 ? 6378140.0 : 6378245.0));
//        s = Math.round(s * 10000) / 10000;
//        return s;
//    }*/
//
//        public static void main(String[] arg){
//            double longt1 = 116.515502;
//            double lat1 = 39.863898;
//            double longt2 = 116.304187;
//            double lat2 = 40.052584;
//            System.out.println(getDistance(longt1,lat1,longt2,lat2));
//        }
//    }

    public void setStartPoint()
    {
         textView.setText(R.string.action_calc_dis1);


    }
    public void setNextPoint()
    {
        textView.setText(R.string.action_calc_dis2);


    }

    //计算距离的函数
//传两个经纬度
    public double calculateJWD(double lng_a, double lat_a, double lng_b,
                               double lat_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    @Override
    public void onMapClick(LatLng latLng) {

        if(PointNumber==0)
        {
            this.startLatLon= latLng;
            PointNumber=1;
            startMarker=mapView.getMap().addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.nav_route_result_start_point)));
            aMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(aMap.getCameraPosition()));
            startMarker.setPosition(latLng);
//                ToastUtil.show(MainActivity.this, addressName);

//                if(dispFlag==1)
//                {
            startMarker.setTitle("起点");
            startMarker.showInfoWindow();
            setNextPoint();

            return;
        }
        if(PointNumber>0)
        {
            PointNumber++;
            this.nextLatLon= latLng;
            mapView.getMap().addPolyline(new PolylineOptions().add(this.startLatLon,this.nextLatLon).color(Color.BLUE));
            sumDistance+=calculateJWD(this.nextLatLon.longitude,this.nextLatLon.latitude,this.startLatLon.longitude,this.startLatLon.latitude);
            this.startLatLon= latLng;
            nextMarker=mapView.getMap().addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.point5)));
            nextMarker.setPosition(latLng);

            aMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(aMap.getCameraPosition()));
//            nextMarker.setPosition(latLng);
//                ToastUtil.show(MainActivity.this, addressName);

//                if(dispFlag==1)
//                {
            if(sumDistance<1000)
            nextMarker.setTitle(sumDistance+"米");
            else
                nextMarker.setTitle(sumDistance/1000 + "公里");
            nextMarker.showInfoWindow();
        }
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
            aMap.setOnMapClickListener(this);
            //设置3D地图中心位置
            aMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition(new LatLng(39.9875,
                            116.48047), 16f, 38.5f, 30000)));

            setUpMap();

        }
//        Toast.makeText(CalculateDistanceActivity.this, "请打开GPS", Toast.LENGTH_LONG).show();


    }
    ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
    private void setUpMap() {

        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
//        giflist.add(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

//        marker = mapView.getMap().addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        marker = mapView.getMap().addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icons(giflist).period(50));
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setCompassEnabled(true);// 设置默认定位按钮是否显示
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.point));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.describeContents();
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);


        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setCompassEnabled(true);// 设置默认定位按钮是否显示
//        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);



        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        setStartPoint();

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
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        activate(mListener);
        if (mListener != null && aLocation != null) {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
//            marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation
//                    .getLongitude()));// 定位雷达小图标
            if(this.aLocation == null || this.aLocation.getStreet() != aLocation.getStreet())
            {
                this.aLocation = aLocation;

            }
//                latLonPoint= new LatLonPoint(latng.latitude, latng.longitude);
//                getAddress(latLonPoint);
//                if(addressName!=null)
//                {
//                    myAddressName=this.addressName;
////                marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
////                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
////                        .position(latng).title("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad()));
//
//                    marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                            .icon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                            .position(latng)
//                            .title("我的位置:"+myAddressName));
//                }
//                else

            LatLng latng = new LatLng(aLocation.getLatitude(),aLocation.getLongitude());
            aMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition( latng, 16f, 38.5f, 30000)));
            marker.setPosition(latng);
            marker.setTitle("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad());

//                    marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
//                            .position(latng).title("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad()));

            marker.showInfoWindow();



            float bearing = aMap.getCameraPosition().bearing;
            aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
        }
        deactivate();
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
                    LocationProviderProxy.AMapNetwork, 1000, 10, this);
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
    public void onClick(View v) {
        PointNumber=0;
        sumDistance=0;
        aMap.clear();
        setStartPoint();
        setUpMap();
    }

//------------------------------------------------------------
//------------------------------------------------------------
//-------------------------以上是定位程序-----------------------
//------------------------------------------------------------
//------------------------------------------------------------
//------------------------------------------------------------

}
