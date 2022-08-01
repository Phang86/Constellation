package com.yyzy.constellation.weather.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.activity.LoginActivity;
import com.yyzy.constellation.utils.DiyProgressDialog;
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
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("温馨提示！")
//                        .setMessage("确定退出删除界面吗？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //intentJump(CityManagerActivity.class);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("取消",null);
//                builder.create().show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.diy_alert_dialog, null);
                TextView content = (TextView) view.findViewById(R.id.dialog_content);
                Button btn_sure = (Button) view.findViewById(R.id.dialog_btn_sure);
                Button btn_cancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
                //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
                final Dialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);
                dialog.getWindow().getDecorView().setBackground(null);
                dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
                //设置隐藏dialog默认的背景
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
                content.setText("确定退出删除界面吗？");
                btn_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       finish();
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });
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