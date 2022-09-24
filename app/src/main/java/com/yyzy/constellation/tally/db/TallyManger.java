package com.yyzy.constellation.tally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yyzy.constellation.tally.bean.GvTypeBean;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;

import java.util.ArrayList;
import java.util.List;

public class TallyManger {
    private static SQLiteDatabase database;

    public static void initDB(Context context){
        TallyDBOpenHelper he = new TallyDBOpenHelper(context);
        database = he.getWritableDatabase();
    }

    public static List<GvTypeBean> getAll(int outOrIn){
        List<GvTypeBean> list = new ArrayList<>();
        String sql = "select * from typetb where outOrIn="+outOrIn;
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            int selectImg = cursor.getInt(cursor.getColumnIndex("selectImg"));
            int unSelectImg = cursor.getInt(cursor.getColumnIndex("unSelectImg"));
            int outOrIn1 = cursor.getInt(cursor.getColumnIndex("outOrIn"));
            GvTypeBean bean = new GvTypeBean(id,typeName,selectImg,unSelectImg,outOrIn1);
            list.add(bean);
        }
        return list;
    }

    public static List<TallyLvItemBean> getItemAll(){
        List<TallyLvItemBean> list = new ArrayList<>();
        String sql = "select * from  jilutb";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int img = cursor.getInt(cursor.getColumnIndex("img"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            int outOrIn = cursor.getInt(cursor.getColumnIndex("outOrIn"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            TallyLvItemBean bean = new TallyLvItemBean(id,typeName,img,beizhu,money,time,year,month,day,outOrIn);
            list.add(bean);
        }
        return list;
    }

    public static void insertData(TallyLvItemBean bean){
        ContentValues values = new ContentValues();
        values.put("typeName",bean.getTypeName());
        values.put("img",bean.getImg());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("outOrIn",bean.getOutOrIn());
        database.insert("jilutb",null,values);
    }

    public static boolean delToData(int id){
        int num = database.delete("jilutb", "_id=?", new String[]{"" + id});
        if (num > 0){
            return true;
        }else{
            return false;
        }
    }
}
