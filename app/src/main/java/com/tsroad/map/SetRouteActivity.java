package com.tsroad.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import com.tsroad.map.util.ToastUtil;

/**
% @authors Keung Charteris & T.s.road CZQ
% @version 1.0 ($Revision$)
% @addr. GUET, Gui Lin, 540001,  P.R.China
% @contact : cztsiang@gmail.com
% @date Copyright(c)  2016-2020,  All rights reserved.
% This is an open access code distributed under the Creative Commons Attribution License, which permits 
% unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
 * Created by tsroad on 5/5/15.
 */

public class SetRouteActivity extends Activity implements GestureDetector.OnGestureListener ,View.OnClickListener{

    WebView myWebView = null;
    private ImageButton routeSearchImagebtn;
    private EditText startTextView;
    private EditText endTextView;
    private String strStart;
    private String strEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_route);

        myWebView = (WebView) findViewById(R.id.route);
        routeSearchImagebtn = (ImageButton) findViewById(R.id.imagebtn_roadsearch_search);
        routeSearchImagebtn.setOnClickListener(this);
        startTextView = (EditText) findViewById(R.id.autotextview_roadsearch_start);
        endTextView = (EditText) findViewById(R.id.autotextview_roadsearch_goals);

        WebSettings ws = myWebView.getSettings();
        // support js
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        // access Assets and resources
        ws.setAllowFileAccess(true);
        // allow zoom in or zoom out
        ws.setSupportZoom(true);
        // set xml dom cache
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // FIXME 设置浏览器静态的http头信息，可能会导致部分的机子不兼容
//        ws.setUserAgentString("Mozilla/4.0 (Linux; U; Android 2.3; zh-CN; MI-ONEPlus) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.6.0.199 U3/0.8.0 Mobile Safari/534.13");

        myWebView.setWebViewClient(new MyWebViewClient());

        // String url=getActivity().getIntent().getStringExtra("url");
//        String url="https://www.baidu.com/";
//        webView.loadUrl(url);

        Intent intent = getIntent();
        String uri = intent.getData().toString();

        myWebView.loadUrl(uri);

    }

    //点击搜索
    @Override
    public void onClick(View v) {
        strStart = startTextView.getText().toString();
        strEnd = endTextView.getText().toString();
        if(strEnd.isEmpty())
        {
                ToastUtil.show(SetRouteActivity.this, "大侠，你要去哪儿");
                return;
        }

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




    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            myWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

    }
}
