package com.tsroad.map;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
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

public class SearchActivity extends Activity implements AMapLocationListener,PoiSearch.OnPoiSearchListener,View.OnClickListener {


    MapView mapView = null;
    Button poiButton= null;
    //初始化定位对象
    private  LocationManagerProxy mLocationManagerProxy = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.main_mapView);
        mapView.onCreate(savedInstanceState);

        //初始化定位对象
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //                                                            混合定位  定位时间间隔  定位距离间隔Gps  回调监听
        //注册定位
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,2000, 15, this);
        poiButton= (Button)findViewById(R.id.drawer_button1);
        poiButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManagerProxy.destroy();
    }

    AMapLocation amapLocation;
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        this.amapLocation = amapLocation;
        //定位回调
//        System.out.println(amapLocation.getAMapException().getErrorCode());
//        System.out.println("-------------------"+amapLocation.toString());
        if(amapLocation != null && amapLocation.getAMapException().getErrorCode()==0)
        {
            Log.e("-------------", amapLocation.toString());

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
    public void onPoiSearched(PoiResult poiResult, int i) {

        if (0==i)//获取成功
        {

            List <PoiItem> list= poiResult.getPois();
            for (int ii=0;ii<list.size();ii++)
            {
                PoiItem item= list.get(ii);
                Log.e("------------->",item.toString());
            }
        }

//        dissmissProgressDialog();// 隐藏对话框
//        if (rCode == 0) {
//            if (result != null && result.getQuery() != null) {// 搜索poi的结果
//                if (result.getQuery().equals(query)) {// 是否是同一条
//                    poiResult = result;
//                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//                    if (poiItems != null && poiItems.size() > 0) {
//                        aMap.clear();// 清理之前的图标
//                        poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();
//
//                        nextButton.setClickable(true);// 设置下一页可点
//                    } else if (suggestionCities != null
//                            && suggestionCities.size() > 0) {
//                        showSuggestCity(suggestionCities);
//                    } else {
//                        ToastUtil.show(PoiAroundSearchActivity.this,
//                                R.string.no_result);
//                    }
//                }
//            } else {
//                ToastUtil
//                        .show(PoiAroundSearchActivity.this, R.string.no_result);
//            }
//        } else if (rCode == 27) {
//            ToastUtil
//                    .show(PoiAroundSearchActivity.this, R.string.error_network);
//        } else if (rCode == 32) {
//            ToastUtil.show(PoiAroundSearchActivity.this, R.string.error_key);
//        } else {
//            ToastUtil.show(PoiAroundSearchActivity.this,getString(R.string.error_other) + rCode);
//        }

    }

    public void search()
    {

        PoiSearch.Query query = new PoiSearch.Query("kfc","餐饮","桂林市");
        query.setPageSize(10);  // 设置每页最多返回多少条poiitem
        query.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);

        LatLonPoint lp = new LatLonPoint(this.amapLocation.getLatitude(),this.amapLocation.getLongitude());
//        LatLonPoint lp = new LatLonPoint(39.989708,116.480462);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 2000, true));//lp为圆心，2000m半径范围
        poiSearch.searchPOIAsyn();

    }
    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

    }

    @Override
    public void onClick(View v) {
        search();
    }
}
