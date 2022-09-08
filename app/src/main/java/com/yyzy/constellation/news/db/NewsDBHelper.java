package com.yyzy.constellation.news.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yyzy.constellation.utils.URLContent;


public class NewsDBHelper extends SQLiteOpenHelper {
    public NewsDBHelper(Context context) {
        super(context, "news.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table itype(id int primary key,title varchar(10) unique not null,url text not null,isshow varchar(10) not null)";
        db.execSQL(sql);
        try {
            String sqlString = "insert into itype values(?,?,?,?)";
            db.execSQL(sqlString,new Object[]{1,"头条", URLContent.headline_url,"true"});
            db.execSQL(sqlString,new Object[]{2,"社会", URLContent.society_url,"true"});
            db.execSQL(sqlString,new Object[]{3,"国内", URLContent.home_url,"true"});
            db.execSQL(sqlString,new Object[]{4,"国际", URLContent.internation_url,"true"});
            db.execSQL(sqlString,new Object[]{5,"娱乐", URLContent.entertainment_url,"true"});
            db.execSQL(sqlString,new Object[]{6,"体育", URLContent.sport_url,"false"});
            db.execSQL(sqlString,new Object[]{7,"军事", URLContent.military_url,"false"});
            db.execSQL(sqlString,new Object[]{8,"科技", URLContent.science_url,"false"});
            db.execSQL(sqlString,new Object[]{9,"财经", URLContent.finance_url,"false"});
            db.execSQL(sqlString,new Object[]{10,"时尚", URLContent.fashion_url,"false"});
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
