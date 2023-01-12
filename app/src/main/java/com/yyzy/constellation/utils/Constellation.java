package com.yyzy.constellation.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.db.RecordsDao;
import com.yyzy.constellation.news.db.NewsDBManger;
import com.yyzy.constellation.tally.db.TallyDBOpenHelper;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.weather.db.DBManager;

import org.xutils.x;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;


public class Constellation extends Application {
    private static RequestQueue httpQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //初始化天气有关数据库
        DBManager.initDB(this);
        //初始化字典有关数据库
        DBmanager.initDB(this);
        //初始化新闻列表数据库
        NewsDBManger.initDB(this);
        //初始化记账数据库
        TallyManger.initDB(this);
        //初始化Volley网络请求框架
        httpQueue = Volley.newRequestQueue(this);
        //初始化图片加载框架ImageLoader
        initImageLoader(getApplicationContext());

        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
//        SharedPreferences sp = getSharedPreferences("sp_ttit",MODE_PRIVATE);
//        String skin = sp.getString("skin", "");
//        if (skin.equals("night")){
//            SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
//        }else {
//            SkinCompatManager.getInstance().restoreDefaultTheme();
//        }
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.MAX_PRIORITY).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(configuration);
    }

    public static RequestQueue getHttpQueue(){
        return httpQueue;
    }
}
