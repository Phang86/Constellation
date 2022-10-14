package com.yyzy.constellation.tally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yyzy.constellation.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TallyDBOpenHelper extends SQLiteOpenHelper {
    public TallyDBOpenHelper(Context context) {
        super(context, "tally.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table typetb(_id Integer primary key autoincrement,typeName varchar(10),selectImg Integer,unSelectImg Integer,outOrIn Integer)";
        db.execSQL(sql);
        String sqlo = "create table jilutb(_id Integer primary key autoincrement,typeName varchar,beizhu varchar,img int,money varchar,time varchar,year int,month int,day int,outOrIn int)";
        String sqlt = "create table state(_id Integer primary key autoincrement,_state varchar)";
        db.execSQL(sqlt);
        insertData(db);
        db.execSQL(sqlo);
    }

    private void insertData(SQLiteDatabase db) {
        String add = "insert into typetb values(null,?,?,?,?)";
        db.execSQL(add,new Object[]{"其他", R.drawable.ic_qita_fs,R.drawable.ic_qita,0});
        db.execSQL(add,new Object[]{"餐饮", R.drawable.ic_canyin_fs,R.drawable.ic_canyin,0});
        db.execSQL(add,new Object[]{"交通", R.drawable.ic_jiaotong_fs,R.drawable.ic_jiaotong,0});
        db.execSQL(add,new Object[]{"购物", R.drawable.ic_gouwu_fs,R.drawable.ic_gouwu,0});
        db.execSQL(add,new Object[]{"服饰", R.drawable.ic_fushi_fs,R.drawable.ic_fushi,0});
        db.execSQL(add,new Object[]{"日用品", R.drawable.ic_riyong_fs,R.drawable.ic_riyong,0});
        db.execSQL(add,new Object[]{"娱乐", R.drawable.ic_yule_fs,R.drawable.ic_yule,0});
        db.execSQL(add,new Object[]{"零食", R.drawable.ic_lingshi_fs,R.drawable.ic_lingshi,0});
        db.execSQL(add,new Object[]{"烟酒", R.drawable.ic_yanjiu_fs,R.drawable.ic_yanjiu,0});
        db.execSQL(add,new Object[]{"学习", R.drawable.ic_xuexi_fs,R.drawable.ic_xuexi,0});
        db.execSQL(add,new Object[]{"医疗", R.drawable.ic_yiliao_fs,R.drawable.ic_yiliao,0});
        db.execSQL(add,new Object[]{"住宿", R.drawable.ic_zhusu_fs,R.drawable.ic_zhusu,0});
        db.execSQL(add,new Object[]{"水电", R.drawable.ic_shuidian_fs,R.drawable.ic_shuidian,0});
        db.execSQL(add,new Object[]{"通讯", R.drawable.ic_tongxun_fs,R.drawable.ic_tongxun,0});
        db.execSQL(add,new Object[]{"人情", R.drawable.ic_renqing_fs,R.drawable.ic_renqing,0});

        db.execSQL(add,new Object[]{"其他", R.drawable.icon_qita_fs,R.drawable.ic_qita,1});
        db.execSQL(add,new Object[]{"薪资", R.drawable.icon_xinzi_fs,R.drawable.icon_xinzi,1});
        db.execSQL(add,new Object[]{"奖金", R.drawable.icon_jiangjin_fs,R.drawable.icon_jiangjin,1});
        db.execSQL(add,new Object[]{"借入", R.drawable.icon_jieru_fs,R.drawable.icon_jieru,1});
        db.execSQL(add,new Object[]{"收债", R.drawable.icon_shouzhai_fs,R.drawable.icon_shouzhai,1});
        db.execSQL(add,new Object[]{"利息收入", R.drawable.icon_lixi_fs,R.drawable.icon_lixi,1});
        db.execSQL(add,new Object[]{"投资", R.drawable.icon_touzi_fs,R.drawable.icon_touzi,1});
        db.execSQL(add,new Object[]{"二手交易", R.drawable.icon_ershou_fs,R.drawable.icon_ershou,1});
        db.execSQL(add,new Object[]{"意外所得", R.drawable.icon_yiwai_fs,R.drawable.icon_yiwai,1});

        String sql = "insert into state values(null,?)";
        db.execSQL(sql,new Object[]{"false"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
