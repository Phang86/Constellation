package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;

public class AppInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView titleTv,userNameTv,createTimeTv,versionTv,phoneTv;
    private ImageView backImg;

    @Override
    protected int initLayout() {
        return R.layout.activity_app_info;
    }

    @Override
    protected void initView() {
        titleTv = findViewById(R.id.details_title);
        backImg = findViewById(R.id.details_back);
        userNameTv = findViewById(R.id.appInfo_tv_user);
        createTimeTv = findViewById(R.id.appInfo_tv_createTime);
        versionTv = findViewById(R.id.appInfo_tv_version);
        phoneTv = findViewById(R.id.appInfo_tv_phone);
        backImg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titleTv.setText(R.string.appInfo);
        userNameTv.setText(findByKey("name"));
        createTimeTv.setText(findByKey("createTime"));
        //获取应用版本号
        String version = getVersion();
        versionTv.setText(version);
        String phone = findByKey("phone");
        if (!phone.isEmpty()) {
            phoneTv.setText(findByKey("phone"));
        }else{
            phoneTv.setText("暂无！");
        }

    }

    protected String findByKey(String key) {
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
        }
    }
}