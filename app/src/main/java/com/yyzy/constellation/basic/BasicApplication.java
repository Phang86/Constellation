package com.yyzy.constellation.basic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.news.db.NewsDBManger;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.weather.db.DBManager;

import org.litepal.LitePal;
import org.xutils.x;

/**
 * 工程管理
 *
 * @author llw
 */
public class BasicApplication extends Application {
    private static ActivityManager activityManager;
    private static BasicApplication application;
    private static Context context;
    private static RequestQueue httpQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        //声明Activity管理
        activityManager = new ActivityManager();
        context = getApplicationContext();
        application = this;
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

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                ActivityManager.setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    /**
     * 内容提供器
     * @return
     */
    public static Context getContext() {
        return context;
    }

    public static BasicApplication getApplication() {
        return application;
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
