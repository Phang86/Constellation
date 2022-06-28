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
        String sql = "create table pywordtb(_id integer primary key autoincrement, id varchar(20) ," +
                "zi varchar(10) unique not null, py varchar(10), wubi varchar(10), pinyin varchar(10), bushou varchar(10), bihua varchar(10))";
        db.execSQL(sql);

        String sqlt = "create table wordtb(_id integer primary key autoincrement,id varchar(20),zi varchar(10) unique not null,py varchar(10), wubi varchar(10), pinyin varchar(10), bushou varchar(10), bihua varchar(10),jijie text,xiangjie text)";
        db.execSQL(sqlt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
