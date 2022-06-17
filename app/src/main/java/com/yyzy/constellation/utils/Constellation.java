package com.yyzy.constellation.utils;

import android.app.Application;
import android.content.SharedPreferences;

import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.weather.db.DBManager;

import org.xutils.x;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;

public class Constellation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //初始化天气有关数据库
        DBManager.initDB(this);
        //初始化字典有关数据库
        DBmanager.initDB(this);
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
        SharedPreferences sp = getSharedPreferences("sp_ttit",MODE_PRIVATE);
        String skin = sp.getString("skin", "");
        if (skin.equals("night")){
            SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
        }else {
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
    }
}
