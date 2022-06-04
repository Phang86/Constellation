package com.yyzy.constellation.weather.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.weather.adapter.CityManagerAdapter;
import com.yyzy.constellation.weather.db.DBManager;
import com.yyzy.constellation.weather.db.DatabaseEntity;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backImg,deleteImg,addImg;
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
        deleteImg = findViewById(R.id.city_iv_delete);
        addImg = findViewById(R.id.city_iv_add);
        listView = findViewById(R.id.city_lv);
        mData = new ArrayList<>();

        backImg.setOnClickListener(this);
        deleteImg.setOnClickListener(this);
        addImg.setOnClickListener(this);

        //设置适配器
        adapter = new CityManagerAdapter(this, mData);
        listView.setAdapter(adapter);
    }

    //获取数据库真实的数据源，添加到原有数据源中，并提示适配器更新
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseEntity> entityList = DBManager.queryAllInfo();
        mData.clear();
        mData.addAll(entityList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_iv_back:
                Intent intent = new Intent(CityManagerActivity.this, WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.city_iv_delete:
                intentJump(DeleteCityActivity.class);
                finish();
                break;
            case R.id.city_iv_add:
                int cityCount = DBManager.getCityCount();
                if (cityCount < 5) {
                    intentJump(SearchCityActivity.class);
                    finish();
                }else {
                    showToast("城市数量已超过5个！");
                }
                break;
        }
    }

}