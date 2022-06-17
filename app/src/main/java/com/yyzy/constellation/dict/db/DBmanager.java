package com.yyzy.constellation.dict.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.utils.CommonUtils;
import com.yyzy.constellation.weather.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class DBmanager {
    private static SQLiteDatabase database;

    //初始化数据库信息
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);
        database = helper.getWritableDatabase();
    }

    private String id;
    private String zi;
    private String py;
    private String wubi;
    private String pinyin;
    private String bushou;
    private String bihua;

    //执行插入数据到Pywordtb表中，插入一个对象的方法
    public static void insertWordToPywordtb(PinBuWordEntity.ResultBean.ListBean entity){
        ContentValues values = new ContentValues();
        values.put("id",entity.getId());
        values.put("zi",entity.getZi());
        values.put("py",entity.getPy());
        values.put("wubi",entity.getWubi());
        values.put("pinyin",entity.getPinyin());
        values.put("bushou",entity.getBushou());
        values.put("bihua",entity.getBihua());
        database.insert(CommonUtils.TABLE_PYWORDTB,null,values);
    }

    //执行插入数据到Pywordtb表中，插入List集合的方法
    public static void insertListToPywordtb(List<PinBuWordEntity.ResultBean.ListBean> list){
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                //获取集合当中的每一个对象
                PinBuWordEntity.ResultBean.ListBean bean = list.get(i);
                //调用单个对象插入的方法
                try {
                    insertWordToPywordtb(bean);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("dict", "insertListToPywordtb: "+bean.getZi()+"已存在！");
                }
            }
        }
    }

    //查询pywordtb表中指定拼音的数据
    public static List<PinBuWordEntity.ResultBean.ListBean> queryPyWordFromPywordtb(String py,int page, int pageSize){
        List<PinBuWordEntity.ResultBean.ListBean> list = new ArrayList<>();
        String sql = "select * from pywordtb where py=? limit?,?";
        int start = (page-1)*pageSize;
        int end = page*pageSize;
        Cursor cursor = database.rawQuery(sql, new String[]{py, start + "", end + ""});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinBuWordEntity.ResultBean.ListBean bean = new PinBuWordEntity.ResultBean.ListBean(id, zi, py1, wubi, pinyin, bushou, bihua);
            list.add(bean);
        }
        return list;
    }


    //查询pywordtb表中指定部首的数据
    public static List<PinBuWordEntity.ResultBean.ListBean> queryBsWordFromPywordtb(String bs,int page, int pageSize){
        List<PinBuWordEntity.ResultBean.ListBean> list = new ArrayList<>();
        String sql = "select * from pywordtb where bushou=? limit?,?";
        int start = (page-1)*pageSize;
        int end = page*pageSize;
        Cursor cursor = database.rawQuery(sql, new String[]{bs, start + "", end + ""});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinBuWordEntity.ResultBean.ListBean bean = new PinBuWordEntity.ResultBean.ListBean(id, zi, py1, wubi, pinyin, bushou, bihua);
            list.add(bean);
        }
        return list;
    }

}
