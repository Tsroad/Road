package com.tsroad.map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by tsroad on 01/05/15.
 */

public class SetSosNumberActivity  extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_number);
        ArrayList<HashMap<String,String>> list =new ArrayList< HashMap<String, String> >();
        HashMap<String,String>  map1 = new HashMap<String ,String>();
        HashMap<String,String>  map2 = new HashMap<String ,String>();
        HashMap<String,String>  map3 = new HashMap<String ,String>();


        map1.put("user_name","何老师");
        map1.put("user_ip","15717737557");
        map2.put("user_name","增少");
        map2.put("user_ip","15717737557");
        map3.put("user_name","辉少");
        map3.put("user_ip","15717737557");

        list.add(map1);
        list.add(map2);
        list.add(map3);

        SimpleAdapter listAdapt= new SimpleAdapter(this,list,R.layout.user2,new String[] {"user_name","user_ip"}, new int []{R.id.user_name,R.id.user_ip});
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

            case 0:break;
            case 1:  //设置电子围栏
                break;
            case 2:break;
            case 3:break;
            case 4:break;

            case 5://关于我们

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
