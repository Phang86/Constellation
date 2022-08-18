package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.DiyProgressDialog;
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

    EditText edUser,edPhone,edPwd,edConfigNewPwd;
    Button btnConfigPwd;
    TextView tvBack;
    private String findUserName;
    private String findUserPhone;
    DiyProgressDialog mDialog;
    ImageView ivBack;

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
        ivBack = findViewById(R.id.config_iv_back);
        edPwd.addTextChangedListener(this);
        edConfigNewPwd.addTextChangedListener(this);
        tvBack.setOnClickListener(this);
        btnConfigPwd.setOnClickListener(this);
        ivBack.setOnClickListener(this);
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
                //tvBack.setTextColor(getResources().getColor(R.color.red));
                finish();
                break;
            case R.id.config_btn_pwd:
                String user = edUser.getText().toString().trim();
                String phone = edPhone.getText().toString().trim();
                String newPwd = edPwd.getText().toString().trim();
                String configNewPwd = edConfigNewPwd.getText().toString().trim();
                requestNet(user,phone,newPwd,configNewPwd);
                break;
            case R.id.config_iv_back:
                finish();
                break;
        }
    }

    private void requestNet(String user, String phone, String newPwd, String configNewPwd) {
        if (TextUtils.isEmpty(newPwd)) {
            MyToast.showText(this,"必填项不能为空哦！");
            return;
        }else if (TextUtils.isEmpty(configNewPwd)){
            MyToast.showText(this,"必填项不能为空哦！");
            return;
        }else if (!checkPassword(newPwd)){
            MyToast.showText(this,"密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }else if (TextUtils.isEmpty(user) || TextUtils.isEmpty(phone)){
            MyToast.showText(ConfigPwdActivity.this,"密码已找回，自动清空默认项。",true);
            return;
        }else if (!newPwd.equals(configNewPwd)){
            MyToast.showText(this,"两次输入的密码不一致！",false);
            return;
        }
        mDialog = new DiyProgressDialog(ConfigPwdActivity.this,"正在加载中...");
        mDialog.setCancelable(false);//设置不能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
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
                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.cancel();
                        showDiyDialog(ConfigPwdActivity.this,"找回失败！服务器连接超时！");
                        if (!TextUtils.isEmpty(edPwd.getText()) && !TextUtils.isEmpty(edConfigNewPwd.getText())){
                            btnConfigPwd.setEnabled(true);
                        }else {
                            btnConfigPwd.setEnabled(false);
                        }
                    }
                });
                Looper.loop();
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
                                    if (result.equals("success")) {
                                        MyToast.showText(ConfigPwdActivity.this,"找回失败！手机号有误！",false);
                                        mDialog.cancel();
                                        btnConfigPwd.setEnabled(true);
                                        return;
                                    } else if (result.equals("su_error")) {
                                        showDiyDialog(ConfigPwdActivity.this,"此账号不存在！");
                                        mDialog.cancel();
                                        btnConfigPwd.setEnabled(true);
                                        return;
                                    } else{
                                        List<User> dataEntity = new Gson().fromJson(result, new TypeToken<List<User>>() {
                                        }.getType());
                                        List<User> data = new ArrayList<>();
                                        data = dataEntity;
                                        if (data.size() > 0 && data != null) {
                                            requestPassPwd(mDialog,configNewPwd);
                                            btnConfigPwd.setEnabled(true);
                                            mDialog.cancel();
                                        } else {
                                            showDiyDialog(ConfigPwdActivity.this,"找回失败！服务器连接超时！");
                                            btnConfigPwd.setEnabled(true);
                                            mDialog.cancel();
                                            return;
                                        }
                                    }
                                }


                            },2000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void requestPassPwd(DiyProgressDialog mDialog, String configNewPwd) {
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
                Looper.prepare();
                showDiyDialog(ConfigPwdActivity.this,"找回失败！服务器连接超时！");
                mDialog.cancel();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("success")) {
                            MyToast.showText(ConfigPwdActivity.this,"账号找回成功！",true);
                            edPwd.setText("");
                            edConfigNewPwd.setText("");
                            edUser.setText("");
                            edPhone.setText("");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mDialog.cancel();
                                }
                            },1000);
                        } else if (result.equals("error")) {
                            MyToast.showText(ConfigPwdActivity.this,"账号找回失败！",false);
                            mDialog.cancel();
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
}