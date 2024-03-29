package com.yyzy.constellation.user.findPwd;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfigPwdActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText edUser,edPhone,edPwd,edConfigNewPwd;
    private TextView btnConfigPwd;
    private TextView tvBack;
    private String findUserName;
    private String findUserPhone;

    @Override
    protected int initLayout() {
        return R.layout.activity_config_pwd;
    }

    @Override
    protected void initView() {
        edUser = findViewById(R.id.config_ed_user);
        edPhone = findViewById(R.id.config_ed_phone);
        btnConfigPwd = findViewById(R.id.config_btn_pwd);
        tvBack = findViewById(R.id.config_tv_login);
        edPwd = findViewById(R.id.config_ed_pwd);
        edConfigNewPwd = findViewById(R.id.config_ed_pwd_two);
        edPwd.addTextChangedListener(this);
        edConfigNewPwd.addTextChangedListener(this);
        tvBack.setOnClickListener(this);
        btnConfigPwd.setOnClickListener(this);
        Intent intent = getIntent();
        findUserName = intent.getStringExtra("findUserName");
        findUserPhone = intent.getStringExtra("findUserPhone");
        edUser.setText(findUserName);
        edPhone.setText(findUserPhone);
        edUser.setEnabled(false);
        edPhone.setEnabled(false);
        btnConfigPwd.setEnabled(false);

        tvBack.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvBack.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.config_tv_login:
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.config_btn_pwd:
                String user = edUser.getText().toString().trim();
                String phone = edPhone.getText().toString().trim();
                String newPwd = edPwd.getText().toString().trim();
                String configNewPwd = edConfigNewPwd.getText().toString().trim();
                requestNet(user,phone,newPwd,configNewPwd);
                break;
        }
    }

    private void requestNet(String user, String phone, String newPwd, String configNewPwd) {
        if (TextUtils.isEmpty(newPwd)) {
            MyToast.showText(this,"必填项不能为空哦！");
            return;
        }
        if (TextUtils.isEmpty(configNewPwd)){
            MyToast.showText(this,"必填项不能为空哦！");
            return;
        }
        if (!checkPassword(newPwd)){
            MyToast.showText(this,"密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(phone)){
            MyToast.showText(ConfigPwdActivity.this,"密码已找回，自动清空默认项。",true);
            return;
        }
        if (!newPwd.equals(configNewPwd)){
            MyToast.showText(this,"两次输入的密码不一致！",false);
            return;
        }
        loading();
        btnConfigPwd.setEnabled(false);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", edUser.getText().toString().trim());
        formbody.add("phone",edPhone.getText().toString().trim());
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + "/user/find/pwd")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        showDiyDialog("找回失败！服务器连接超时！");
                        if (!TextUtils.isEmpty(edPwd.getText()) && !TextUtils.isEmpty(edConfigNewPwd.getText())){
                            btnConfigPwd.setEnabled(true);
                        }else {
                            btnConfigPwd.setEnabled(false);
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stopLoading();
                                    if (result.equals("success")) {
                                        MyToast.showText(ConfigPwdActivity.this,"找回失败！手机号有误！",false);
                                        btnConfigPwd.setEnabled(true);
                                        return;
                                    }
                                    if (result.equals("su_error")) {
                                        showDiyDialog("此账号不存在！");
                                        btnConfigPwd.setEnabled(true);
                                        return;
                                    }
                                    List<User> dataEntity = new Gson().fromJson(result, new TypeToken<List<User>>() {
                                    }.getType());
                                    List<User> data = new ArrayList<>();
                                    data = dataEntity;
                                    if (data.size() > 0 && data != null) {
                                        requestPassPwd(configNewPwd);
                                        btnConfigPwd.setEnabled(true);
                                        return;
                                    }
                                    showDiyDialog("找回失败！服务器连接超时！");
                                    btnConfigPwd.setEnabled(true);
                                }


                            },1000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void requestPassPwd(String configNewPwd) {
        loading();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", findUserName);
        formbody.add("pwd",configNewPwd);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + "/user/update")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ConfigPwdActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        showDiyDialog("找回失败！服务器连接超时！");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        if (result.equals("success")) {
                            MyToast.showText(ConfigPwdActivity.this,"账号找回成功！",true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                                }
                            },500);
                            return;
                        }
                        if (result.equals("error")) {
                            MyToast.showText(ConfigPwdActivity.this,"账号找回失败！",false);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //tvBack.setTextColor(getResources().getColor(R.color.grey));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnConfigPwd.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edPwd.getText()) && !TextUtils.isEmpty(edConfigNewPwd.getText())){
            btnConfigPwd.setEnabled(true);
        }else {
            btnConfigPwd.setEnabled(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}