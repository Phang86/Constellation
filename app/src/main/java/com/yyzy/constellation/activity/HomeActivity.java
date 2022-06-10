package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.yyzy.constellation.R;

public class HomeActivity extends BaseActivity {
    private TextView tv;
    private int time = 4;
    private SharedPreferences firstSpf;

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        tv = findViewById(R.id.home_tv);
        handler.sendEmptyMessageDelayed(1,1000);
        firstSpf = getSharedPreferences("first_spf",MODE_PRIVATE);
    }

    @Override
    protected void initData() {

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                time--;
                if (time == 0) {
                    //判断是否第一次进入
                    boolean isFirst = firstSpf.getBoolean("isFirst", true);
                    Intent intent = new Intent();
                    if (isFirst){
                        intent.setClass(HomeActivity.this,WelcomeActivity.class);
                        //为了下一次不进行跳转引导界面，把状态设置为false
                        SharedPreferences.Editor edit = firstSpf.edit();
                        edit.putBoolean("isFirst",false);
                        edit.commit();
                    }else {
                        //不是第一次进入  直接跳过引导页面进入
                        intent.setClass(HomeActivity.this,LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }else {
                    tv.setText("倒计时\t"+String.valueOf(time)+"秒");
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }
        }
    };
}