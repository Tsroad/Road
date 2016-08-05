package com.tsroad.map;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.tsroad.map.util.AMapUtil;
import com.tsroad.map.util.ToastUtil;

import java.util.List;

/**
 * Created by tsroad on 5/3/15.
 */
public class AroundHere extends FragmentActivity implements
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AdapterView.OnItemSelectedListener,
        PoiSearch.OnPoiSearchListener, AMap.OnMapClickListener, AMap.OnInfoWindowClickListener,
        View.OnClickListener {
    private AMap aMap;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private Spinner selectDeep;// 选择城市列表
    private String[] itemDeep = {  "酒店","餐饮", "景区", "影院" };
    private Spinner selectType;// 选择返回是否有团购，优惠
    private String[] itemTypes = { "所有poi", "有团购", "有优惠", "有团购或者优惠" };
    private String deepType = "";// poi搜索类型
    private int searchType = 0;// 搜索类型
    private int tsearchType = 0;// 当前选择搜索类型
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp = new LatLonPoint(39.908127, 116.375257);// 默认西单广场
    private Marker locationMarker; // 选择的点
    private PoiSearch poiSearch;
    private PoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据
    private Marker detailMarker;// 显示Marker的详情
    private Button nextButton;// 下一页

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
//
//        keyWord = AMapUtil.checkEditText(searchText);
//        if ("".equals(keyWord)) {
//            ToastUtil.show(PoiKeywordSearchActivity.this, "请输入搜索关键字");
//            return;
//        } else {
//            doSearchQuery();
//        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

    }
}
