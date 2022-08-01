package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.DiyProgressDialog;
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

public class ConfigPwdActivity extends BaseActivity implements View.OnClickListener {

    EditText edUser,edPhone,edPwd,edConfigNewPwd;
    Button btnConfigPwd;
    TextView tvBack;
    private String findUserName;
    private String findUserPhone;
    DiyProgressDialog mDialog;

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
        tvBack.setOnClickListener(this);
        btnConfigPwd.setOnClickListener(this);
        Intent intent = getIntent();
        findUserName = intent.getStringExtra("findUserName");
        findUserPhone = intent.getStringExtra("findUserPhone");
        edUser.setText(findUserName);
        edPhone.setText(findUserPhone);
        edUser.setEnabled(false);
        edPhone.setEnabled(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.config_tv_login:
                tvBack.setTextColor(getResources().getColor(R.color.red));
                finish();
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
            showToast("必填项不能为空哦！");
            return;
        }else if (TextUtils.isEmpty(configNewPwd)){
            showToast("必填项不能为空哦！");
            return;
        }else if (TextUtils.isEmpty(user)){
            showToast("必填项不能为空哦！");
            return;
        }else if (TextUtils.isEmpty(phone)){
            showToast("必填项不能为空哦！");
            return;
        }else if (!newPwd.equals(configNewPwd)){
            showToast("两次输入的密码不一致！");
            return;
        }
        mDialog = new DiyProgressDialog(ConfigPwdActivity.this,"正在加载中...");
        mDialog.setCancelable(false);//设置不能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
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
                showToast("找回失败！服务器连接超时！");
                mDialog.cancel();
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
                                    if (result.equals("error")){
                                        showToast("密码找回失败！用户名或手机号不正确！");
                                        mDialog.cancel();
                                        return;
                                    }else{
                                        List<User> dataEntity = new Gson().fromJson(result, new TypeToken<List<User>>() {
                                        }.getType());
                                        List<User> data = new ArrayList<>();
                                        data = dataEntity;
                                        if (data.size() > 0 && data != null) {
                                            requestPassPwd(mDialog,configNewPwd);
                                            mDialog.cancel();
                                        } else {
                                            showToast("密码找回失败！服务器连接超时！");
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
                showToast("找回失败！服务器连接超时！");
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
                            showToast("账号找回成功！");
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
                            showToast("账号找回失败！");
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
        tvBack.setTextColor(getResources().getColor(R.color.grey));
    }
}