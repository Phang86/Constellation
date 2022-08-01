package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class FindPwdActivity extends BaseActivity implements View.OnClickListener {

    EditText userEt,phoneEt;
    Button findBtn;
    DiyProgressDialog mDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_find_pwd;
    }

    @Override
    protected void initView() {
        userEt = findViewById(R.id.find_user);
        phoneEt = findViewById(R.id.find_phone);
        findBtn = findViewById(R.id.find_btn);
        findBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_btn:
                String user = userEt.getText().toString().trim();
                String phone = phoneEt.getText().toString().trim();
                findPwd(user,phone);
                break;
        }
    }

    private void findPwd(String user, String phone) {
        if (TextUtils.isEmpty(user)){
            showToast("用户名不能空哦！");
            return;
        }else if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能空哦！");
            return;
        }else if (!checkUsername(user)) {
            showToast("用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            return;
        }else if (!checkPassword(phone)){
            showToast("手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
            return;
        }
        mDialog = new DiyProgressDialog(FindPwdActivity.this,"正在加载中...");
        mDialog.setCancelable(false);//设置不能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", user);
        formbody.add("phone",phone);
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
                                            //intentJump(MainActivity.class);
                                            showToast("密码已找回");
                                            //finish();
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
}