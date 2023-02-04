package com.yyzy.constellation.weather.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.activity.MainActivity;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.weather.adapter.CityFragmentPagerAdapter;
import com.yyzy.constellation.weather.db.DBManager;
import com.yyzy.constellation.weather.entity.WeatherEntity;
import com.yyzy.constellation.weather.fragment.CityWeatherFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends BaseActivity implements View.OnClickListener{
    private ImageView imgBack,addCityImg,moreImg;
    private LinearLayout pointLayout;
    private ViewPager viewPager;
    //ViewPager的数据源
    private List<Fragment> fragmentList = new ArrayList<>();
    //表示需要显示城市的集合
    private List<String> cityList = DBManager.queryAllCityName();    //获取数据库包含的城市信息列表
    //表示ViewPager的页数指示器显示的集合
    private List<ImageView> imgList = new ArrayList<>();
    private CityFragmentPagerAdapter adapter;
    private SharedPreferences bg_pref;
    private int bgNum;
    private RelativeLayout relativeLayout;

    @Override
    protected int initLayout() {
        return R.layout.activity_weather;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.weather_iv_back);
        addCityImg = findViewById(R.id.weather_iv_add);
        moreImg = findViewById(R.id.weather_iv_more);
        pointLayout = findViewById(R.id.weather_layout_point);
        viewPager = findViewById(R.id.weather_viewPager);
        relativeLayout = findViewById(R.id.weather_layout);
        addCityImg.setOnClickListener(this);
        moreImg.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        changeBg();
    }


    @Override
    protected void initData() {
        if (cityList.size() == 0){
            cityList.add(base_ipCity);
        }
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if (!cityList.contains(city) && !TextUtils.isEmpty(city)){
            cityList.add(city);
        }

        //创建Fragment对象，添加到ViewPager数据源中
        initPager();

        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        //创建小圆点指示器
        initPoint();
        //设置显示最后一个城市信息
        viewPager.setCurrentItem(imgList.size()-1);
        //设置ViewPager页面监听
        setPagerListener();
    }


    private void initPager() {
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    private void setPagerListener() {
        //给viewPager设置监听器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当滑动到当前页面的时候  改变小圆点为深色  否则为白色
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.mipmap.a1_);
                }
                imgList.get(position).setImageResource(R.mipmap.a2_);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        //创建小圆点 ViewPager页面的指示器的函数
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView img = new ImageView(this);
            img.setImageResource(R.mipmap.a1_);
            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(img);
            //把小圆点添加到layout中
            pointLayout.addView(img);
        }
        imgList.get(imgList.size() - 1).setImageResource(R.mipmap.a2_);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.weather_iv_back:
                finish();
                break;
            case R.id.weather_iv_more:
                intent.setClass(this,MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.weather_iv_add:
                intent.setClass(this,CityManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

    //换壁纸的方法
    public void changeBg(){
        bg_pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = bg_pref.getInt("bg", 2);
        switch (bgNum) {
            case 0:
                relativeLayout.setBackgroundResource(R.mipmap.bg4);
                break;
            case 1:
                relativeLayout.setBackgroundResource(R.mipmap.bg);
                break;
            case 2:
                relativeLayout.setBackgroundResource(R.mipmap.bg2);
                break;
            case 3:
                relativeLayout.setBackgroundResource(R.mipmap.bg3);
                break;
            case 4:
                relativeLayout.setBackgroundResource(R.mipmap.bg5);
                break;
        }
    }

}