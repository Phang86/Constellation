package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.DiyProgressDialog;

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

import static com.yyzy.constellation.utils.URLContent.BASE_URL;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv;
    private EditText edUser, edPwd;
    private Button btnLogin;
    private List<User> data = new ArrayList<>();
    private String userPassword;
    private DiyProgressDialog mDialog;

    private CheckBox mRemenber,mAutoLogin;
    private boolean mPasswordFlag = false;//记住密码标志
    private boolean mAutoLoginFlag = false;//自动登录标志


    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        tv = findViewById(R.id.btnLogin_tv_register);
        edUser = findViewById(R.id.edLogin_user);
        edPwd = findViewById(R.id.edLogin_pwd);
        btnLogin = findViewById(R.id.btnLogin_login);
        mRemenber = findViewById(R.id.cbLogin_rememberPwd);
        mAutoLogin = findViewById(R.id.cbLogin_autoLogin);
        tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
        //如果不为空
        if (sharedPreferences != null) {
            String userName = sharedPreferences.getString("username", "");
            userPassword = sharedPreferences.getString("password", "");
            mPasswordFlag = sharedPreferences.getBoolean("remenber", false);
            mAutoLoginFlag = sharedPreferences.getBoolean("auto", false);
            edUser.setText(userName);
        }

        //确定为true获取 记住密码，打钩
        if (mPasswordFlag) {
            mRemenber.setChecked(true);
            edPwd.setText(userPassword);
        }
        //选择了自动登录后直接登录
        if (mAutoLoginFlag){
            mAutoLogin.setChecked(true);
            String username = edUser.getText().toString();
            String password = edPwd.getText().toString();
            login(username,password);
        }

        mRemenber.setOnClickListener(mListener);
        mRemenber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //如果是选中记住密码，取消记住密码、自动登录
                if (!isChecked){
                    mAutoLogin.setChecked(false);
                    //清空密码输入框
                    edPwd.setText("");
                }
            }
        });
        btnLogin.setOnClickListener(mListener);
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //设置登录监听
            switch (v.getId()) {
                case R.id.btnLogin_login:
                    String user = edUser.getText().toString().trim();
                    String pwd = edPwd.getText().toString().trim();
                    SharedPreferences sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //2  创建Editor对象，写入值
                    editor.putString("username", user);
                    if (mRemenber.isChecked()) {
                        if (!mPasswordFlag) {
                            mPasswordFlag = true;
                        }
                        editor.putBoolean("remenber", mPasswordFlag);
                        editor.putString("password", pwd);
                        //选中自动登录
                        if (mAutoLogin.isChecked()) {
                            mAutoLoginFlag = true;
                        } else {
                            mAutoLoginFlag = false;
                        }
                        editor.putBoolean("auto", mAutoLoginFlag);
                    } else {
                        if (!mPasswordFlag) {
                            showToast("请勾选复选框！");
                            Log.e("TAG", "!mPasswordFlag: " + "异常");
                            return;
                        }
                        //取消自动登录和记住密码,清空密码
                        mPasswordFlag = false;
                        mAutoLoginFlag = false;
                        editor.putString("password", "");
                        editor.putBoolean("remenber", mPasswordFlag);
                        editor.putBoolean("auto", mAutoLoginFlag);
                    }
                    editor.commit();
                    //登陆
                    login(user, pwd);
            }
        }

    };

    private void login(String user, String pwd) {
        if (TextUtils.isEmpty(user)) {
            showToast("账号不能为空哦！");
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            showToast("密码不能为空哦！");
            return;
        }else if (!checkUsername(user)){
            showToast("用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            return;
        }else if (!checkPassword(pwd)){
            showToast("密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", user);
        formbody.add("pwd", pwd);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/user/login")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                Log.e("TAG", "onFailure: " + e.getMessage().toString());
                showToast("登录失败！服务器连接超时！");
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String resultStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (resultStr.equals("error")){
                                showToast("登录失败！账号或密码不正确！");
                                return;
                            }else{
                                List<User> dataEntity = new Gson().fromJson(resultStr, new TypeToken<List<User>>() {
                                }.getType());
                                Log.e("TAG", "run: "+dataEntity.toString() );
                                data = dataEntity;
                                if (data.size() > 0 && data != null) {
                                    //拿到Username
                                    String user = data.get(0).getUserName();
                                    mDialog = new DiyProgressDialog(LoginActivity.this,"正在登录中...");
                                    mDialog.setCancelable(false);//设置不能通过后退键取消
                                    mDialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            intentJump(MainActivity.class);
                                            finish();
                                            showToast("您已登录成功！");
                                            SharedPreferences sp = getSharedPreferences("sp_ttit",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit(); // 获取编辑器
                                            editor.putString("name",edUser.getText().toString().trim());
                                            editor.putString("createTime",data.get(0).getCreateTime());
                                            editor.putString("phone",data.get(0).getMobile());
                                            // 存入数据
                                            editor.commit();
                                            mDialog.cancel();
                                        }
                                    },2000);
                                } else {
                                    showToast("登录失败！服务器连接超时！");
                                    return;
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin_tv_register:
                tv.setTextColor(getResources().getColor(R.color.red));
                //跳转注册页面
                intentJump(RegisterActivity.class);
                finish();
                break;
        }
    }
}