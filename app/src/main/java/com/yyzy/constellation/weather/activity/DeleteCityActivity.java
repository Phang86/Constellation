package com.yyzy.constellation.weather.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.weather.adapter.DeleteCityAdapter;
import com.yyzy.constellation.weather.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgError,imgRight;
    private ListView listView;
    private DeleteCityAdapter adapter;
    private List<String> mData = DBManager.queryAllCityName();    //数据源
    private List<String> deleteCity = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_delete_city;
    }

    @Override
    protected void initView() {
        imgError = findViewById(R.id.delete_iv_error);
        imgRight = findViewById(R.id.delete_iv_right);
        listView = findViewById(R.id.delete_lv);

        imgError.setOnClickListener(this);
        imgRight.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new DeleteCityAdapter(this,mData,deleteCity);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_iv_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("温馨提示！")
                        .setMessage("确定退出删除界面吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //intentJump(CityManagerActivity.class);
                                finish();
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.create().show();
                break;
            case R.id.delete_iv_right:
                for (int i = 0; i < deleteCity.size(); i++) {
                    String city = deleteCity.get(i);
                    DBManager.deleteInfoCity(city);
                    adapter.notifyDataSetChanged();
                }
                //intentJump(CityManagerActivity.class);
                finish();
                break;
        }
    }

}