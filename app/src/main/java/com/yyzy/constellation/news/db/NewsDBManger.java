package com.yyzy.constellation.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yyzy.constellation.news.bean.TypeBean;
import com.yyzy.constellation.utils.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsDBManger {
    public static SQLiteDatabase database;

    public static void initDB(Context context){
        NewsDBHelper newsDBHelper = new NewsDBHelper(context);
        database = newsDBHelper.getWritableDatabase();
    }

    //查询新闻列表所有类型
    public static List<TypeBean> getAllTypeList(){
        List<TypeBean> list = new ArrayList<>();
        Cursor cursor = database.query("itype", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String showStr = cursor.getString(cursor.getColumnIndex("isshow"));
            boolean isshow = Boolean.valueOf(showStr);
            TypeBean typeBean = new TypeBean(id,title,url,isshow);
            list.add(typeBean);
        }
        return list;
    }

    //修改新闻列表类型
    public static void updateTypeList(List<TypeBean> list){
        for (int i = 0; i < list.size(); i++) {
            TypeBean typeBean = list.get(i);
            String title = typeBean.getTitle();
            ContentValues values = new ContentValues();
            values.put("isshow",String.valueOf(typeBean.isShow()));
            database.update("itype",values,"title=?",new String[]{title});
        }
    }


    //条件查询：只查询状态为true的列表
    public static List<TypeBean> selectTypeIsShow(){
        List<TypeBean> list = new ArrayList<>();
        Cursor cursor = database.query("itype", null, "isshow='true'", null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            TypeBean typeBean = new TypeBean(id,title,url,true);
            list.add(typeBean);
        }
        return list;
    }
}
