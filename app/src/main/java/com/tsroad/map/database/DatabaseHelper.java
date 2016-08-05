package com.tsroad.map.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by T.s.road on 2015/4/7 0007.
 */

/**
 * Database 作为一个访问SQLite的助手类，提供两个方面的功能
 * 第一，getReadableDatabase(),getWritableDatabase()可以获得SQLiteDatabase对象
 * 第二，提供了onCreate()和onUpdate()两个回调函数，允许我们在创建和升级数据库时，进行
 *
 *
 * */
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



    /**
     * 该函数在第一次创建数据库的时候执行，
     * 实际上是在第一次得到SQLiteDatabase对象时，get之后才会调用这个方法
     *
     * */
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        Log.e("----------------------","Create a Database");
//        db.execSQL("create table user(name varchar(20),number_id varchar(11))");
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.e("----------------------","update a Database");
//    }


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