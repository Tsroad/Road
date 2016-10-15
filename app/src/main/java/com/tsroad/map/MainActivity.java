package com.tsroad.map;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tsroad.map.route.RouteActivity;
import com.tsroad.map.util.AMapUtil;
import com.tsroad.map.util.ToastUtil;
import java.util.ArrayList;

/**
% @authors Keung Charteris & T.s.road CZQ
% @version 1.0 ($Revision$)
% @addr. GUET, Gui Lin, 540001,  P.R.China
% @contact : cztsiang@gmail.com
% @date Copyright(c)  2016-2020,  All rights reserved.
% This is an open access code distributed under the Creative Commons Attribution License, which permits 
% unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
*/

public class MainActivity extends ActionBarActivity implements LocationSource,
        AMapLocationListener,AMap.OnMapClickListener ,GeocodeSearch.OnGeocodeSearchListener {

    private Button drawerButton1=null;
    private Button drawerButton2=null;
    private Button drawerButton3=null;
    private Button drawerButton4=null;

    AMapLocation aLocation=null;
    LatLng desLat=null;
    private int buttonFlag=0;
    private AMap aMap=null;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private Marker marker;// 定位雷达小图标
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    //    private Marker geoMarker;
    private Marker regeoMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //启动动画
        Intent intent00 = new Intent();
        intent00.setClass(MainActivity.this,Welcome.class);
        MainActivity.this.startActivity(intent00);


        mapView= (MapView)findViewById(R.id.main_mapView);
        mapView.onCreate(savedInstanceState);
        drawerButton1=(Button)findViewById(R.id.drawer_button1);
        drawerButton2=(Button)findViewById(R.id.drawer_button2);
        drawerButton3=(Button)findViewById(R.id.drawer_button3);
        drawerButton4=(Button)findViewById(R.id.drawer_button4);
        drawerButton1.setOnClickListener(new  drawerButton1Listener());
        drawerButton2.setOnClickListener(new  drawerButton2Listener());
        drawerButton3.setOnClickListener(new  drawerButton3Listener());
        drawerButton4.setOnClickListener(new  drawerButton4Listener());

         /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir = OffLineMapUtils.getSdCacheDir(this);
        init();

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
    public void onMapClick(LatLng latLng) {
        this.desLat= latLng;
        latLonPoint= new LatLonPoint(latLng.latitude, latLng.longitude);
        getAddress(latLonPoint);
    }

    //启动定位程序
    class drawerButton1Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            buttonFlag= 1;
            onButtonChoose();
        }
    }

    class drawerButton2Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            buttonFlag= 2;
            onButtonChoose();
        }
    }

//    class startTrace implements Runnable
//    {
//        @Override
//        public void run() {
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this,GeoFenceActivity.class);
//            MainActivity.this.startActivity(intent);
//        }
//    }
    //启动轨迹记录
    class drawerButton3Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
      //    new Thread(new startTrace()).start();
            buttonFlag= 3;
            onButtonChoose();
        }
    }

    class drawerButton4Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            buttonFlag= 4;
            onButtonChoose();
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }


    private void onButtonChoose()
    {

        switch (buttonFlag)
        {

            case 1://附近

                if(aLocation==null);
                    Double lon= aLocation.getLongitude();
                    Double lat= aLocation.getLatitude();
                if(0<lat&&lat<90&&lon>0&&lon<180 );
                else
                {

                    lon=116.48047;
                    lat=39.9875;

                }


//                Uri content_url1 = Uri.parse("http://www.baidu.com");
                Uri content_url1 = Uri.parse("http://m.amap.com/around/?"+
                        "locations="+Double.toString(lon)+","+Double.toString(lat)+
                        "&keywords=美食,酒店,娱乐,公交站" +
                        "&key=2f36e9477592f2a71597905e39ee2eb9"+
                        "&defaultView=list&defaultIndex=1&searchRadius=1000"
                       );

                Intent intent1 = new Intent();
                intent1.setData(content_url1);
                intent1.setClass(MainActivity.this,Around.class);
                MainActivity.this.startActivity(intent1);
                break;
//            case 2://导航
//                Uri content_url2 = Uri.parse("http://m.amap.com/navi/?" +
//                        "dest="+Double.toString(aLocation.getLongitude())+","+Double.toString(aLocation.getLatitude())+
//                        "&destName=电子科大研究生院" +
//                        "&hideRouteIcon=0" +
//                        "&key=2f36e9477592f2a71597905e39ee2eb9");
            case 2://导航
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this,RouteActivity.class);
                MainActivity.this.startActivity(intent2);
                break;
            case 3://室内

//                Uri content_url3 = Uri.parse("http://m.amap.com/navi/?" +
//                        "start="+Double.toString(aLocation.getLongitude())+","+Double.toString(aLocation.getLatitude())+
//                        "&destName=电子科大研究生院"+
//                        "&key=2f36e9477592f2a71597905e39ee2eb9");

                Uri content_url3 = Uri.parse("file:///android_asset/index.html" );
                Intent intent3 = new Intent();
                intent3.setData(content_url3);
                intent3.setClass(MainActivity.this,IndoorActivity.class);
                MainActivity.this.startActivity(intent3);
                break;
            case 4://我的
                Intent intent4 = new Intent();
                intent4.setClass(MainActivity.this,MyActivity.class);
                MainActivity.this.startActivity(intent4);
                break;
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
            //设置地理反解析标识
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
            setUpMap();

        }
        Toast.makeText(MainActivity.this, "请打开GPS", Toast.LENGTH_LONG).show();


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
                marker.setPosition(latng);
                marker.setTitle("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad());
//                marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
//                        .icons(giflist)
//                        .position(latng).title("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad()));

//                    marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
//                            .position(latng).title("我的位置:"+this.aLocation.getCity()+this.aLocation.getDistrict()+this.aLocation.getRoad()));

                marker.showInfoWindow();
                aMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition( latng, 16f, 38.5f, 30000)));


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

//------------------------------------------------------------
//------------------------------------------------------------
//-------------------------以上是定位程序-----------------------
//------------------------------------------------------------
//------------------------------------------------------------
//------------------------------------------------------------




//------------------------------------------------------------
//------------------------------------------------------------
//-------------------------以下是地理解析程序-----------------------
//------------------------------------------------------------
//------------------------------------------------------------
//------------------------------------------------------------



//
//    /**
//     * 响应地理编码
//     */
//    public void getLatlon(final String name) {
//        showDialog();
//
//        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
//        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
//    }

    /**
     * 响应逆地理编码
     */

    public void getAddress(LatLonPoint latLonPoint) {
//        showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
//        dismissDialog();
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        AMapUtil.convertToLatLng(latLonPoint), 15));
                aMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(aMap.getCameraPosition()));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
//                ToastUtil.show(MainActivity.this, addressName);

//                if(dispFlag==1)
//                {
                    regeoMarker.setTitle(addressName);
                    regeoMarker.showInfoWindow();
//                }

            } else {
                ToastUtil.show(MainActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(MainActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(MainActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(MainActivity.this,
                    getString(R.string.error_other) + rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

//-------------------------以上是地理解析程序-----------------------

}
