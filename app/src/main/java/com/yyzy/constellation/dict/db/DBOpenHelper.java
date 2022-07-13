package com.yyzy.constellation.dict.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(@Nullable Context context) {
        super(context, "dict.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建拼音表
        String sql = "create table pywordtb(_id integer primary key autoincrement, id varchar(20) ," +
                "zi varchar(10) unique not null, py varchar(10), wubi varchar(10), pinyin varchar(10), bushou varchar(10), bihua varchar(10))";
        db.execSQL(sql);

        //创建文字表
        sql = "create table wordtb(_id integer primary key autoincrement,id varchar(20),zi varchar(10) unique not null,py varchar(10), wubi varchar(10), pinyin varchar(10), bushou varchar(10), bihua varchar(10),jijie text,xiangjie text)";
        db.execSQL(sql);

        //创建成语表
        sql = "create table cyutb(_id integer primary key autoincrement,name varchar(30) unique not null,pinyin varchar(50),jbsy text,xxsy text,chuchu text,liju text,jyc text,fyc text)";
        db.execSQL(sql);

        //创建收藏文字表
        sql = "create table collwordtb(_id integer primary key autoincrement,zi varchar(10) unique not null)";
        db.execSQL(sql);

        //创建收藏成语表
        sql = "create table collcyutb(_id integer primary key autoincrement,chengyu varchar(20) unique not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
