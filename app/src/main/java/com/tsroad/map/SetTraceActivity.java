package com.tsroad.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;


import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NavigateArrowOptions;
import com.amap.api.maps.model.PolylineOptions;
/**
 * Created by tsroad on 5/5/15.
 */
public class SetTraceActivity extends FragmentActivity {
    private AMap aMap;
    private Button replayButton;
    private SeekBar processbar;
    private Marker marker = null;// 当前轨迹点图案
    public Handler timer = new Handler();// 定时器
    public Runnable runnable = null;
    // 存放所有坐标的数组
    private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
    private ArrayList<LatLng> latlngList_path = new ArrayList<LatLng>();
    // private ArrayList<LatLng> latlngList_path1 = new ArrayList<LatLng>();

    private static final LatLng marker1 = new LatLng(39.24426, 100.18322);
    private static final LatLng marker2 = new LatLng(39.24426, 104.18322);
    private static final LatLng marker3 = new LatLng(39.24426, 108.18322);
    private static final LatLng marker4 = new LatLng(39.24426, 112.18322);
    private static final LatLng marker5 = new LatLng(39.24426, 116.18322);
    private static final LatLng marker6 = new LatLng(36.24426, 100.18322);
    private static final LatLng marker7 = new LatLng(36.24426, 104.18322);
    private static final LatLng marker8 = new LatLng(36.24426, 108.18322);
    private static final LatLng marker9 = new LatLng(36.24426, 112.18322);
    private static final LatLng marker10 = new LatLng(36.24426, 116.18322);
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_trace);
        context = this;
        init();
        float f = 0;
        for (int i = 0; i < latlngList.size() - 1; i++) {
            f += AMapUtils.calculateLineDistance(latlngList.get(i),
                    latlngList.get(i + 1));
        }
        Log.i("float", String.valueOf(f / 1000));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        replayButton = (Button) findViewById(R.id.btn_replay);
        processbar = (SeekBar) findViewById(R.id.process_bar);
        processbar.setSelected(false);
        processbar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        // 进度条拖动时 执行相应事件
        processbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 复写OnSeeBarChangeListener的三个方法
            // 第一个时OnStartTrackingTouch,在进度开始改变时执行
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            // 第二个方法onProgressChanged是当进度发生改变时执行
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub


                latlngList_path.clear();
                if (progress != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        latlngList_path.add(latlngList.get(i));
                    }
                    drawLine(latlngList_path, progress);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // 第三个是onStopTrackingTouch,在停止拖动时执行
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                latlngList_path.clear();
                int current = seekBar.getProgress();
                if (current != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        latlngList_path.add(latlngList.get(i));
                    }
                    drawLine(latlngList_path, current);
                }
            }
        });

        // 初始化runnable开始
        runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 要做的事情
                handler.sendMessage(Message.obtain(handler, 1));
            }
        };
        // 初始化runnable结束
        // TODO Auto-generated method stub
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (aMap != null) {
                setUpMap();
            }
        }
    }

    private void drawLine(ArrayList<LatLng> list,int current) {
        // TODO Auto-generated method stub
        aMap.clear();
        LatLng replayGeoPoint = latlngList.get(current - 1);
        if (marker != null) {
            marker.destroy();
        }
        // 添加汽车位置
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(replayGeoPoint)
                .title("起点")
                .snippet(" ")
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(), R.drawable.car)))
                .anchor(0.5f, 0.5f);
        marker = aMap.addMarker(markerOptions);
        // 增加起点开始
        aMap.addMarker(new MarkerOptions()
                .position(latlngList.get(0))
                .title("起点")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(
                                getResources(),
                                R.drawable.nav_route_result_start_point))));
        // 增加起点结束
        if (latlngList_path.size() > 1) {
//            PolylineOptions polylineOptions = (new PolylineOptions())
//                    .addAll(latlngList_path)
//                    .color(Color.rgb(9, 129, 240)).width(6.0f);
//            aMap.addPolyline(polylineOptions);

            aMap.addNavigateArrow(new NavigateArrowOptions().addAll(latlngList_path).width(10));

            aMap.getMaxZoomLevel();
        }
        if (latlngList_path.size() == latlngList.size()) {
            aMap.addMarker(new MarkerOptions()
                    .position(latlngList.get(latlngList.size() - 1))
                    .title("终点")
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(
                                    getResources(),
                                    R.drawable.nav_route_result_end_point))));
        }
    }


    // 根据定时器线程传递过来指令执行任务
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int curpro = processbar.getProgress();
                if (curpro != processbar.getMax()) {
                    processbar.setProgress(curpro + 1);
                    timer.postDelayed(runnable, 500);// 延迟0.5秒后继续执行
                } else {
                    Button button = (Button) findViewById(R.id.btn_replay);
                    button.setText(" 回放 ");// 已执行到最后一个坐标 停止任务
                }
            }
        }
    };

    private void setUpMap() {
        // TODO Auto-generated method stub

        // 加入坐标
        latlngList.add(marker1);
        latlngList.add(marker2);
        latlngList.add(marker3);
        latlngList.add(marker4);
        latlngList.add(marker5);
        latlngList.add(marker6);
        latlngList.add(marker7);
        latlngList.add(marker8);
        latlngList.add(marker9);
        latlngList.add(marker10);
        // latlngList_path.add(marker1);
        // 设置进度条最大长度为数组长度
        processbar.setMax(latlngList.size());
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngList.get(0), 4));
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setCompassEnabled(true);// 设置默认定位按钮是否显示

//        aMap.addNavigateArrow(new NavigateArrowOptions().addAll(latlngList).width(20));
//        aMap.moveCamera(CameraUpdateFactory
//                .newCameraPosition(new CameraPosition(marker5, 16f, 38.5f, 300)));
    }

    public void btn_replay_click(View v) {
        // 根据按钮上的字判断当前是否在回放
        if (replayButton.getText().toString().trim().equals("回放")) {
            if (latlngList.size() > 0) {
                // 假如当前已经回放到最后一点 置0
                if (processbar.getProgress() == processbar.getMax()) {
                    processbar.setProgress(0);
                }
                // 将按钮上的字设为"停止" 开始调用定时器回放
                replayButton.setText(" 停止 ");
                timer.postDelayed(runnable, 10);
            }
        } else {
            // 移除定时器的任务
            timer.removeCallbacks(runnable);
            replayButton.setText(" 回放 ");
        }
    }

}