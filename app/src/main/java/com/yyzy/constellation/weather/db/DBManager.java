package com.yyzy.constellation.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    public static SQLiteDatabase database;

    //初始化数据库信息
    public static void initDB(Context context){
        DBHelper helper = new DBHelper(context);
        database = helper.getWritableDatabase();
    }

    //查询数据库当中城市的方法
    public static List<String> queryAllCityName(){
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        List<String> cityList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }

    //根据城市名称，替换信息内容
    public static int updateInfoByCity(String city,String content){
        ContentValues values = new ContentValues();
        values.put("content",content);
        return database.update("info",values,"city=?",new String[]{city});
    }

    //新增一条城市记录
    public static long addCityInfo(String city,String content){
        ContentValues values = new ContentValues();
        values.put("city",city);
        values.put("content",content);
        return database.insert("info",null,values);
    }

    //根据城市名，查找数据库当中的内容
    public static String queryInfoByCity(String city){
        Cursor cursor = database.query("info", null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
        return null;
    }

    //获取数据库表中的数据记录数量
    public static int getCityCount(){
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        return cursor.getCount();
    }

    //查询数据库表中的所有信息
    public static List<DatabaseEntity> queryAllInfo(){
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        List<DatabaseEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            DatabaseEntity entity = new DatabaseEntity(id, city, content);
            list.add(entity);
        }
        return list;
    }
}
