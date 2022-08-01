package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.StringUtils;
import com.yyzy.constellation.utils.URLContent;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends BaseActivity {
    private EditText edRegisterUser, edRegisterPwd, edRegisterPhone;
    private Button mbtnRegister;
    private TextView tv;
    private DiyProgressDialog mDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        tv = findViewById(R.id.btnRegister_tv_login);
        edRegisterUser = findViewById(R.id.edRegister_user);
        edRegisterPwd = findViewById(R.id.edRegister_pwd);
        edRegisterPhone = findViewById(R.id.edRegister_phone);
        mbtnRegister = findViewById(R.id.btnRegister_register);
    }

    @Override
    protected void initData() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setTextColor(getResources().getColor(R.color.red));
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edRegisterUser.getText().toString().trim();
                String pwd = edRegisterPwd.getText().toString().trim();
                String phone = edRegisterPhone.getText().toString().trim();
                register(user, pwd, phone);
            }
        });
    }

    private void register(String user, String pwd, String phone) {
        //判断用户输入的密码、账号、手机号：1、是否为空；2、是否符合账号注册标准(严格使用正则表达式)
        if (TextUtils.isEmpty(user)) {
            showToast("注册账号不能为空哦！");
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            showToast("注册密码不能为空哦！");
            return;
        }else if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空哦！");
            return;
        }else if (!checkUsername(user)) {
            showToast("用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            return;
        } else if (!checkPassword(pwd)) {
            showToast("密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }else if (!checkPhone(phone)){
            showToast("手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
            return;
        }
        //请求本地后台服务器，再进行下一步判断，从数据库筛选用户名是否存在；
        // 一切要求符合，则进行数据库添加数据
        mDialog = new DiyProgressDialog(RegisterActivity.this,"正在加载中...");
        mDialog.setCancelable(false);//设置不能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", user);
        formbody.add("pwd", pwd);
        formbody.add("mobile",phone);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + "/user/register")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TAG", "onFailureError: " + e.getMessage());
                Looper.prepare();
                showToast("注册失败！服务器连接超时！");
                mDialog.cancel();
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String resultStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (resultStr.equals("success")) {
                                    showToast("恭喜，您已注册成功！赶紧前往登录吧！");
                                    edRegisterUser.setText("");
                                    edRegisterPwd.setText("");
                                    edRegisterPhone.setText("");
                                } else if (resultStr.equals("error")) {
                                    showToast("此用户名已存在！请更换用户名！");
                                } else {
                                    showToast("注册失败！服务器连接超时！");
                                }
                                mDialog.cancel();
                            }
                        }, 1500);
                    }
                });
            }
        });
    }
}