package com.yyzy.constellation.weather.activity;


import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.weather.adapter.CityManagerAdapter;
import com.yyzy.constellation.weather.db.DBManager;
import com.yyzy.constellation.weather.db.DatabaseEntity;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backImg,deleteImg;
    private TextView tvAddCity;
    private ListView listView;
    //显示列表数据源
    private List<DatabaseEntity> mData;
    private CityManagerAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_city_manager;
    }

    @Override
    protected void initView() {
        backImg = findViewById(R.id.city_iv_back);
//        deleteImg = findViewById(R.id.city_iv_delete);
        tvAddCity = findViewById(R.id.city_tv_add);
        listView = findViewById(R.id.city_lv);
        mData = new ArrayList<>();

        backImg.setOnClickListener(this);
//        deleteImg.setOnClickListener(this);
        tvAddCity.setOnClickListener(this);

        //设置适配器
        adapter = new CityManagerAdapter(this, mData);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CityManagerActivity.this,DeleteCityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                return true;
            }
        });
    }

    //获取数据库真实的数据源，添加到原有数据源中，并提示适配器更新
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseEntity> entityList = DBManager.queryAllInfo();
        Log.e("TAG", "onResume: "+entityList.toString());
        mData.clear();
        mData.addAll(entityList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.city_iv_back:
                intent.setClass(CityManagerActivity.this, WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
                break;
//            case R.id.city_iv_delete:
//                //intentJump(DeleteCityActivity.class);
//                intent.setClass(this,DeleteCityActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
//                break;
            case R.id.city_tv_add:
                int cityCount = DBManager.getCityCount();
                if (cityCount < 5) {
                    //intentJump(SearchCityActivity.class);
                    intent.setClass(this,SearchCityActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //finish();
                }else {
                    showToast("城市数量已超过5个！");
                }
                break;
        }
    }

    //重写默认键盘
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //重写手机自带返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(CityManagerActivity.this, WeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}