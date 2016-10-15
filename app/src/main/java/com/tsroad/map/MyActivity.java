package com.tsroad.map;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

/**
% @authors Keung Charteris & T.s.road CZQ
% @version 1.0 ($Revision$)
% @addr. GUET, Gui Lin, 540001,  P.R.China
% @contact : cztsiang@gmail.com
% @date Copyright(c)  2016-2020,  All rights reserved.
% This is an open access code distributed under the Creative Commons Attribution License, which permits 
% unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
 * Created by tsroad on 30/04/15.
 */

public class MyActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ArrayList<HashMap<String,String>> list =new ArrayList< HashMap<String, String> >();
        HashMap<String,String>  map1 = new HashMap<String ,String>();
        HashMap<String,String>  map2 = new HashMap<String ,String>();
        HashMap<String,String>  map3 = new HashMap<String ,String>();
        HashMap<String,String>  map4 = new HashMap<String ,String>();
        HashMap<String,String>  map5 = new HashMap<String ,String>();
        HashMap<String,String>  map6 = new HashMap<String ,String>();

        map1.put("user_name","足可追踪");
        map2.put("user_name","电子围栏");
        map3.put("user_name","亲情号码");
        map4.put("user_name","离线地图");
        map5.put("user_name","距离测量");
        map6.put("user_name","关于我们");

        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);

        SimpleAdapter listAdapt= new SimpleAdapter(this,list,R.layout.user,new String[] {"user_name","user_ip"}, new int []{R.id.user_name,R.id.user_ip});
        setListAdapter(listAdapt);
    }

    @Override
    protected void onListItemClick(ListView l,View v,int position,long id)
    {
        super.onListItemClick(l,v,position,id);
        System.out.println("id--------------" + id);
        System.out.println("position--------"+position);
        switch (position)
        {
            case 0://我的足迹
                Intent intent0 = new Intent();
                intent0.setClass(MyActivity.this,SetTraceActivity.class);
                MyActivity.this.startActivity(intent0);
                break;
            case 1:  //设置电子围栏
                Intent intent1 = new Intent();
                intent1.setClass(MyActivity.this,SetFenceActivity.class);
                MyActivity.this.startActivity(intent1);
                break;
            case 2://设置亲情号码
                Intent intent2 = new Intent();
                intent2.setClass(MyActivity.this,SetSosNumberActivity.class);
                MyActivity.this.startActivity(intent2);break;
            case 3://下载离线地图
                Intent intent3 = new Intent();
                intent3.setClass(MyActivity.this,OfflineMapActivity.class);
                MyActivity.this.startActivity(intent3);break;
            case 4://距离测量
                Intent intent4 = new Intent();
                intent4.setClass(MyActivity.this,CalculateDistanceActivity.class);
                MyActivity.this.startActivity(intent4);
                break;
            case 5://关于我们
                Intent intent7 = new Intent();
                intent7.setClass(MyActivity.this,About.class);
                MyActivity.this.startActivity(intent7);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
