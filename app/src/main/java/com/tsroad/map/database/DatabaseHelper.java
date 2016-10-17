package com.tsroad.map.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
% @authors Keung Charteris & T.s.road CZQ
% @version 1.0 ($Revision$)
% @addr. GUET, Gui Lin, 540001,  P.R.China
% @contact : cztsiang@gmail.com
% @date Copyright(c)  2016-2020,  All rights reserved.
% This is an open access code distributed under the Creative Commons Attribution License, which permits 
% unrestricted use, distribution, and reproduction in any medium, provided the original work is properly cited. 
 * Created by T.s.road on 2015/4/7 0007.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    //在SQLiteOpenHelper的子类中，必须有该构造函数
    //1.activity对象就是context类型，2.表的名字string，3.  ，4.当前数据库的版本

    /**
     * 四参
     * */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    /**
     * 三参
     * */

    public DatabaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    /**
     * 二参
     * */
    public DatabaseHelper(Context context, String name) {
        this(context, name,VERSION);
    }

        private static final String DB_NAME = "coll.db";
        private static final String TBL_NAME = "CollTbl";
        private static final String CREATE_TBL = " create table "
                + " CollTbl(_id integer primary key autoincrement,name text,url text, desc text) ";

        private SQLiteDatabase db;
        @Override
        public void onCreate(SQLiteDatabase db) {
            this.db = db;
            db.execSQL(CREATE_TBL);
        }
        public void insert(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TBL_NAME, null, values);
            db.close();
        }
        public Cursor query() {
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
            return c;
        }
        public void del(int id) {
            if (db == null)
                db = getWritableDatabase();
            db.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
        }
        public void close() {
            if (db != null)
                db.close();
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
}
