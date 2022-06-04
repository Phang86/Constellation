package com.yyzy.constellation.weather.fragment;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.HttpUtils;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.weather.db.DBManager;
import com.yyzy.constellation.weather.entity.WeatherEntity;
import com.yyzy.constellation.weather.entity.WeatherIndexEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    private TextView tempTv, cityTv, conditionTv, windTv, dateTv, tempRangeTv, clothIndexTv, carIndexTv, coldIndexTv, sportIndexTv, raysIndexTv, airIndexTv;
    private LinearLayout futureLayout;
    private WeatherIndexEntity.ResultDTO.LifeDTO life;   //网络天气指数存放类
    private int error_code = 10012;        //网络请求失败码
    private String city;
    private int bgNum;
    private ScrollView scrollView;
    private SharedPreferences bg_pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        getActivityData();
        return view;
    }

    //获取activity传过来的数据
    private void getActivityData() {
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        String url = URLContent.getTemp_url(city);
        //获取父类加载数据的方法
        loadData(url);
        //获取网络天气指数信息
        String indexUrl = URLContent.getIndex_url(city);
        loadNetIndexData(indexUrl);
    }

    private void loadNetIndexData(final String indexUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtils.getJSONFromNet(indexUrl);
                WeatherIndexEntity fromJson = new Gson().fromJson(json, WeatherIndexEntity.class);
                if (fromJson.getReason().equals("超过每日可允许请求次数!") || fromJson.getError_code() == error_code || fromJson.getResult() == null){
                    showToast("超过每日可允许请求次数!");
                    return;
                }
                life = fromJson.getResult().getLife();
            }
        }).start();
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        //当获取数据成功时，解析并展示数据
        parseShowData(result);
        //更新数据
        int i = DBManager.updateInfoByCity(city, result);
        if (i <= 0) {
            //表示更新城市数据失败，则应该添加此城市的数据信息
            DBManager.addCityInfo(city,result);
        }

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        //去数据库查找上一次信息显示在Fragment中
        String s = DBManager.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)) {
            parseShowData(s);
        }
    }

    private void parseShowData(String result) {
        //使用json解析数据
        WeatherEntity jhEntity = new Gson().fromJson(result, WeatherEntity.class);
        WeatherEntity.ResultDTO jhResult = jhEntity.getResult();
        //获取今天时间信息
        dateTv.setText(jhResult.getFuture().get(0).getDate());
        //获取城市信息
        cityTv.setText(jhResult.getCity());
        //获取今天的天气情况
        WeatherEntity.ResultDTO.FutureDTO jhTodayFuture = jhResult.getFuture().get(0);
        WeatherEntity.ResultDTO.RealtimeDTO jhRealtime = jhResult.getRealtime();
        //获取风向的信息
        windTv.setText(jhRealtime.getDirect() + jhRealtime.getPower());
        //获取温度区间的信息
        tempRangeTv.setText(jhTodayFuture.getTemperature());
        //获取今天天气情况信息
        conditionTv.setText(jhRealtime.getInfo());
        //获取实时温度情况
        tempTv.setText(jhRealtime.getTemperature() + "℃");
        //获取未来五天的天气情况，并保存到layout中
        List<WeatherEntity.ResultDTO.FutureDTO> futureList = jhResult.getFuture();
        futureList.remove(0);
        for (int i = 0; i < futureList.size(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_layout, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(view);
            TextView iDateTv = view.findViewById(R.id.item_center_tv_date);
            TextView iconTv = view.findViewById(R.id.item_center_tv_con);
            TextView windTv = view.findViewById(R.id.item_center_tv_wind);
            TextView iTempRangeTv = view.findViewById(R.id.item_center_tv_temp);
            //获取对应的位置的天气情况
            WeatherEntity.ResultDTO.FutureDTO dataEntity = futureList.get(i);
            iDateTv.setText(dataEntity.getDate());
            iconTv.setText(dataEntity.getWeather());
            iTempRangeTv.setText(dataEntity.getTemperature());
            windTv.setText(dataEntity.getDirect());
        }
    }

    //初始化控件
    private void initView(View view) {
        tempTv = view.findViewById(R.id.cityFrag_tv_temp);
        cityTv = view.findViewById(R.id.cityFrag_tv_city);
        conditionTv = view.findViewById(R.id.cityFrag_tv_condition);
        dateTv = view.findViewById(R.id.cityFrag_tv_date);
        windTv = view.findViewById(R.id.cityFrag_tv_wind);
        tempRangeTv = view.findViewById(R.id.cityFrag_tv_tempRange);
        clothIndexTv = view.findViewById(R.id.cityFrag_tv_cloth);
        carIndexTv = view.findViewById(R.id.cityFrag_tv_washCar);
        coldIndexTv = view.findViewById(R.id.cityFrag_tv_cold);
        sportIndexTv = view.findViewById(R.id.cityFrag_tv_sport);
        raysIndexTv = view.findViewById(R.id.cityFrag_tv_rays);
        airIndexTv = view.findViewById(R.id.cityFrag_tv_air);
        futureLayout = view.findViewById(R.id.cityFrag_center_layout);
        scrollView = view.findViewById(R.id.cityFrag_scroll);

        //为控件添加点击事件
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
        airIndexTv.setOnClickListener(this);

        changeBg();
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.cityFrag_tv_cloth:
                builder.setTitle("穿衣指数");
                String msg = "暂无信息！";
                if (life != null) {
                    msg = life.getChuanyi().getV() + "\n" + life.getChuanyi().getDes();
                }
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
            case R.id.cityFrag_tv_washCar:
                builder.setTitle("洗车指数");
                msg = "暂无信息！";
                if (life != null) {
                    msg = life.getXiche().getV() + "\n" + life.getXiche().getDes();
                }
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
            case R.id.cityFrag_tv_cold:
                builder.setTitle("感冒指数");
                msg = "暂无信息！";
                if (life != null) {
                    msg = life.getGanmao().getV() + "\n" + life.getGanmao().getDes();
                }
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
            case R.id.cityFrag_tv_sport:
                builder.setTitle("运动指数");
                msg = "暂无信息！";
                if (life != null) {
                    msg = life.getYundong().getV() + "\n" + life.getYundong().getDes();
                }
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
            case R.id.cityFrag_tv_rays:
                builder.setTitle("紫外线指数");
                msg = "暂无信息！";
                if (life != null) {
                    msg = life.getZiwaixian().getV() + "\n" + life.getZiwaixian().getDes();
                }
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);

                break;
            case R.id.cityFrag_tv_air:
                builder.setTitle("空调指数");
                msg = "暂无信息！";
                if (life != null) {
                    msg = life.getKongtiao().getV() + "\n" + life.getKongtiao().getDes();
                }
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
        }
        builder.create().show();
    }

    //换壁纸的方法
    public void changeBg(){
        bg_pref = getActivity().getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = bg_pref.getInt("bg", 2);
        switch (bgNum) {
            case 0:
                scrollView.setBackgroundResource(R.mipmap.bg4);
                break;
            case 1:
                scrollView.setBackgroundResource(R.mipmap.bg);
                break;
            case 2:
                scrollView.setBackgroundResource(R.mipmap.bg2);
                break;
            case 3:
                scrollView.setBackgroundResource(R.mipmap.bg3);
                break;
            case 4:
                scrollView.setBackgroundResource(R.mipmap.bg5);
                break;
        }
    }
}