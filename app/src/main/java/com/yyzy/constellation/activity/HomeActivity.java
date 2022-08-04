package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.yyzy.constellation.R;

public class HomeActivity extends BaseActivity {
    private TextView tv;
    private int time = 5;
    private SharedPreferences firstSpf,userNameSpf, spf;
    private String username;
    private String password;
    private boolean auto;
    private boolean remenber,isFirst;


    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        tv = findViewById(R.id.home_tv);
        handler.sendEmptyMessageDelayed(1,1000);
        firstSpf = getSharedPreferences("first_spf",MODE_PRIVATE);
        userNameSpf = getSharedPreferences("busApp", MODE_PRIVATE);
        spf = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        username = userNameSpf.getString("username", "");
        password = userNameSpf.getString("password", "");
        //点击定时装置进行相应的逻辑判断    判断密码和账号是否注销
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //如账号、密码未注销，则直接跳到应用主界面
                isFirst = firstSpf.getBoolean("isFirst", true);
                if (isFirst){
                    intent.setClass(HomeActivity.this,WelcomeActivity.class);
                    //为了下一次不进行跳转引导界面，把状态设置为false
                    SharedPreferences.Editor edit = firstSpf.edit();
                    edit.putBoolean("isFirst",false);
                    edit.commit();
                    handler.removeCallbacksAndMessages(null);
                }else {
                    String name = spf.getString("name", "");
                    String phone = spf.getString("phone", "");
                    String createTime = spf.getString("createTime", "");
                    if (!name.isEmpty() || !phone.isEmpty() || !createTime.isEmpty()){
                        intent.setClass(HomeActivity.this, MainActivity.class);
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        //不是第一次进入  直接跳过引导页面进入
                        intent.setClass(HomeActivity.this, LoginActivity.class);
                        handler.removeCallbacksAndMessages(null);
                    }
                }
                startActivity(intent);
                finish();
            }
        });
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
                    if (time == -1) {
                        //判断是否第一次进入
                        isFirst = firstSpf.getBoolean("isFirst", true);
                        Intent intent = new Intent();
                        if (isFirst){
                            intent.setClass(HomeActivity.this,WelcomeActivity.class);
                            //为了下一次不进行跳转引导界面，把状态设置为false
                            SharedPreferences.Editor edit = firstSpf.edit();
                            edit.putBoolean("isFirst",false);
                            edit.commit();
                        }else {
                            try {
                                String name = spf.getString("name", "");
                                String phone = spf.getString("phone", "");
                                String createTime = spf.getString("createTime", "");
//                            if (!username.trim().isEmpty() || !password.trim().isEmpty()) {
//                                intent.setClass(HomeActivity.this, MainActivity.class);
//                            }
                                if (!name.isEmpty() || !phone.isEmpty() || !createTime.isEmpty()){
                                    intent.setClass(HomeActivity.this, MainActivity.class);
                                }
                                else {
                                    //不是第一次进入  直接跳过引导页面进入
                                    intent.setClass(HomeActivity.this, LoginActivity.class);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        startActivity(intent);
                        finish();
                    }else {
                        tv.setText("跳过\t"+String.valueOf(time));
                        handler.sendEmptyMessageDelayed(1,1000);
                    }
                }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyCode == KeyEvent.KEYCODE_HOME) {
            handler.removeCallbacksAndMessages(null);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}