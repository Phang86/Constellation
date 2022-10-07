package com.yyzy.constellation.tally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yyzy.constellation.tally.bean.ChartLvItemBean;
import com.yyzy.constellation.tally.bean.GvTypeBean;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;
import com.yyzy.constellation.tally.util.FloatUtils;

import java.util.ArrayList;
import java.util.List;

public class TallyManger {
    private static SQLiteDatabase database;

    public static void initDB(Context context){
        TallyDBOpenHelper he = new TallyDBOpenHelper(context);
        database = he.getWritableDatabase();
    }

    //条件查询：（0：表示支出、1：表示收入）
    public static List<GvTypeBean> getOutOrInTypetbAll(int outOrIn){
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

    //根据年、月、日去查找记录
    public static List<TallyLvItemBean> getItemAll(int years,int months,int days){
        List<TallyLvItemBean> list = new ArrayList<>();
        String sql = "select * from  jilutb where year=? and month=? and day=? order by _id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{""+years,""+months,""+days});
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
        cursor.close();
        return list;
    }

    //根据年、月去查找记录
    public static List<TallyLvItemBean> getItemOfMonth(int years,int months){
        List<TallyLvItemBean> list = new ArrayList<>();
        String sql = "select * from  jilutb where year=? and month=? order by _id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{""+years,""+months});
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
        cursor.close();
        return list;
    }

    //查询所有
    public static List<TallyLvItemBean> getAll(){
        List<TallyLvItemBean> list = new ArrayList<>();
        //String sql = "select * from jilutb";
        Cursor cursor = database.query("jilutb",null,null,null,null,null,null);
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
        cursor.close();
        return list;
    }

    //查询所有
    public static List<TallyLvItemBean> getOutOrInJilutbAll(int years,int months,int outAndIn){
        List<TallyLvItemBean> list = new ArrayList<>();
        String sql = "select * from jilutb where year=? and month=? and outOrIn=?";
        Cursor cursor = database.rawQuery(sql,new String[]{years+"",months+"",outAndIn+""});
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
        cursor.close();
        return list;
    }

    //根据备注模糊查询
    public static List<TallyLvItemBean> findBeizhuList(String beiz){
        List<TallyLvItemBean> list = new ArrayList<>();
        String sql = "select * from jilutb where beizhu like ?";
        Cursor cursor = database.rawQuery(sql,new String[]{"%"+beiz+"%"});
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
        cursor.close();
        return list;
    }

    //添加数据
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

    //根据id删除记录信息
    public static boolean delToData(int id){
        int num = database.delete("jilutb", "_id=?", new String[]{"" + id});
        if (num > 0){
            return true;
        }else{
            return false;
        }
    }

    //查询某一天的消费金额总数
    public static float getSumMoneyOneDay(int year,int month,int day,int outOrIn){
        float total = 0.0f;
        String sql = "select sum(money) from jilutb where year=? and month=? and day=? and outOrIn=?";
        Cursor cursor = database.rawQuery(sql, new String[]{"" + year, "" + month, "" + day, "" + outOrIn});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        cursor.close();
        return total;
    }

    //查询某一月的消费金额总数
    public static float getSumMoneyOneMonth(int year,int month,int outOrIn){
        float total = 0.0f;
        String sql = "select sum(money) from jilutb where year=? and month=? and outOrIn=?";
        Cursor cursor = database.rawQuery(sql, new String[]{"" + year, "" + month, "" + outOrIn});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        cursor.close();
        return total;
    }

    //查询某一年的消费金额总数
    public static float getSumMoneyOneYear(int year,int outOrIn){
        float total = 0.0f;
        String sql = "select sum(money) from jilutb where year=? and outOrIn=?";
        Cursor cursor = database.rawQuery(sql, new String[]{"" + year,"" + outOrIn});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        cursor.close();
        return total;
    }

    //查询有几个年份
    public static List<Integer> getYearNumOfJilutb(){
        List<Integer> list = new ArrayList<>();
        String sql = "select distinct(year) from jilutb order by year asc";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        cursor.close();
        return list;
    }

    //删除全部记录
    public static void delAllForJilutb(){
        String sql = "delete from jilutb";
        database.execSQL(sql);
    }

    public static List<ChartLvItemBean> getChartLvItemListToJilutb(int year,int month,int outOrIn){
        List<ChartLvItemBean> list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, outOrIn);
        String sql = "select img,typeName,sum(money)as total from jilutb where year=? and month=? and outOrIn=? group by typeName order by total desc";
        Cursor cursor = database.rawQuery(sql, new String[]{year + "", month + "", outOrIn + ""});
        while (cursor.moveToNext()) {
            int img = cursor.getInt(cursor.getColumnIndex("img"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            float radio = FloatUtils.div(total, sumMoneyOneMonth);
            Log.e("TAG", "getChartLvItemListToJilutb: "+radio);
            ChartLvItemBean bean = new ChartLvItemBean(img, typeName, radio, total);
            list.add(bean);
        }
        return list;
    }


}
