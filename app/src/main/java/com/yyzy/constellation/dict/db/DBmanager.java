package com.yyzy.constellation.dict.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.yyzy.constellation.dict.entity.ChengyuInfoEntity;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.dict.entity.WordEntity;
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
        String sql = "select * from pywordtb where py=? or py like? or py like? or py like? limit?,?";
        int start = (page-1)*pageSize;
        int end = page*pageSize;
        String type1 = py+",%";
        String type2 = "%,"+py+",%";
        String type3 = "%,"+py;
        Cursor cursor = database.rawQuery(sql, new String[]{py,type1,type2,type3, start + "", end + ""});
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


    public static void insertWordToWordtb(WordEntity.ResultBean bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("zi",bean.getZi());
        values.put("py",bean.getPy());
        values.put("wubi",bean.getWubi());
        values.put("pinyin",bean.getPinyin());
        values.put("bushou",bean.getBushou());
        values.put("bihua",bean.getBihua());
        //将集合转换成字符串
        String jijie = listToString(bean.getJijie());
        String xiangjie = listToString(bean.getXiangjie());
        values.put("jijie",jijie);
        values.put("xiangjie",xiangjie);
        database.insert(CommonUtils.TABLE_WORDTB,null,values);
    }

    //将List集合转换成字符串
    public static String listToString(List<String> list){
        StringBuilder sb = new StringBuilder();
        if (!list.isEmpty() && list != null){
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                s += "|";
                sb.append(s);
            }
        }
        return list.toString();
    }

    //将字符串转换成List集合
    public static List<String> stringToList(String str){
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] arr = str.split("\\|");
            for (int i = 0; i < arr.length; i++) {
                String s = arr[i].trim();
                if (!TextUtils.isEmpty(s)){
                    list.add(s);
                }
            }
        }
        return list;
    }

    public static WordEntity.ResultBean queryWordFromWordtb(String word){
        String sql = "select * from wordtb where zi=?";
        Cursor cursor = database.rawQuery(sql, new String[]{word});
        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            String jijie = cursor.getString(cursor.getColumnIndex("jijie"));
            String xiangjie = cursor.getString(cursor.getColumnIndex("xiangjie"));
            List<String> jijieList = stringToList(jijie);
            List<String> xiangjieList = stringToList(xiangjie);
            WordEntity.ResultBean resultBean = new WordEntity.ResultBean(id,zi,py1,wubi,pinyin,bushou,bihua,jijieList,xiangjieList);
            return resultBean;
        }
        return null;
    }

    //插入成语的相关信息到成语表当中
    public static void insertCyToCyutb(ChengyuInfoEntity.ResultBean bean){
        ContentValues values = new ContentValues();
        values.put("name",bean.getName());
        values.put("pinyin",bean.getPinyin());
        String jbsyList = listToString(bean.getJbsy());
        values.put("jbsy",jbsyList);
        String xxsyList = listToString(bean.getXxsy());
        values.put("xxsy",xxsyList);
        values.put("chuchu",bean.getChuchu());
        values.put("liju",bean.getLiju());
        String jycList = listToString(bean.getJyc());
        values.put("jyc",jycList);
        String fycList = listToString(bean.getFyc());
        values.put("fyc",fycList);
        database.insert(CommonUtils.TABLE_CYUTB,null,values);
    }

    //查询成语的相关信息
    public static ChengyuInfoEntity.ResultBean queryCyFromCyutb(String cyu){
        String sql = "select * from cyutb where name=?";
        Cursor cursor = database.rawQuery(sql, new String[]{cyu});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String jbsy = cursor.getString(cursor.getColumnIndex("jbsy"));
            List<String> jbsyList = stringToList(jbsy);
            String xxsy = cursor.getString(cursor.getColumnIndex("xxsy"));
            List<String> xxsyList = stringToList(xxsy);
            String chuchu = cursor.getString(cursor.getColumnIndex("chuchu"));
            String liju = cursor.getString(cursor.getColumnIndex("liju"));
            String jyc = cursor.getString(cursor.getColumnIndex("jyc"));
            List<String> jycList = stringToList(jyc);
            String fyc = cursor.getString(cursor.getColumnIndex("fyc"));
            List<String> fycList = stringToList(fyc);
            ChengyuInfoEntity.ResultBean bean = new ChengyuInfoEntity.ResultBean(name,pinyin,jbsyList,xxsyList,chuchu,liju,jycList,fycList);
            return  bean;
        }
        return null;
    }


    public static List<String> queryAllCyFromCyutb(){
        List<String> list = new ArrayList<>();
        String sql = "select name from cyutb";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        return list;
    }
}
