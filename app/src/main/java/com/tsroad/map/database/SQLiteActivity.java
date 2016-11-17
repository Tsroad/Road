package com.tsroad.map.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.tsroad.map.R;

/**
 @authors Keung Charteris & T.s.road CZQ
 @version 1.0 ($Revision$)
 @addr. GUET, Gui Lin, 540001,  P.R.China
 @contact : cztsiang@gmail.com
 @date Copyright(c)  2016-2020,  All rights reserved.
 This is an open access code distributed under the Creative Commons Attribution License, which permits 
 unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
*/

public class SQLiteActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_number);
        DataCreate();
        DataAdd();
    }


        public void DataRead() {
            DatabaseHelper databaseHelper = new DatabaseHelper(SQLiteActivity.this, "user");
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
        }


        public void DataCreate(){
            DatabaseHelper databaseHelper = new DatabaseHelper(SQLiteActivity.this, "user");
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

        }


    //启动定位程序

        public void DataAdd()
        {
            //生成ContentValues对象,键值队，两列,两列的数据类型要一致
            ContentValues values = new ContentValues();
            values.put("name", "Tsroad");
            values.put("number_id", "15717737557");
            DatabaseHelper databaseHelper1 = new DatabaseHelper(SQLiteActivity.this, "user");
            SQLiteDatabase db = databaseHelper1.getWritableDatabase();
            db.insert("user", null, values);//表名，null, values
        }

    //更新数据库相当于执行SQL语句中的update语句
    //UPDATE table_name SET XXCOL=XXX  WHERE XXCOL=XX....
        public void DataUpdater()
        {
            DatabaseHelper databaseHelper = new DatabaseHelper(SQLiteActivity.this, "user");
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", "辉少");
            values.put("number_id", "111111111111");

            //   要更新的表名，ContentValues，where子句，？占位符（未知的参数），几个占位符几个大小
            db.update("user", values,"number_id=???????????",new String[]{"11"});//表名，null, values
            db.update("user", values, "name=????", new String[]{"4"});//表名，null, values
        }
    //.schema  查看当前数据库里的表

    //查找

        public void DataQuery()
        {
            DatabaseHelper databaseHelper = new DatabaseHelper(SQLiteActivity.this, "user");
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor=db.query("user",new String[]{"name","number_id"},"number_id=???????????",new String[]{"11"},null,null,null);
            while(cursor.moveToNext())
            {
                String name =cursor.getString(cursor.getColumnIndex("name"));
                Log.e("--------------",name);

            }

        }
}
