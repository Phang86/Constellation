package com.yyzy.constellation.user;

import androidx.annotation.NonNull;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.entity.IpBean;
import com.yyzy.constellation.user.findPwd.FindPwdActivity;
import com.yyzy.constellation.activity.MainActivity;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.FourFiguresNumberCode;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;

import java.io.IOException;
import java.net.URL;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView tv, forgetTv;
    private EditText edUser, edPwd, edValCode;
    private TextView btnLogin;
    private List<User> data = new ArrayList<>();
    private ImageView imgValCode;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        tv = findViewById(R.id.btnLogin_tv_register);
        forgetTv = findViewById(R.id.btnLogin_tv_forget);
        edUser = findViewById(R.id.edLogin_user);
        edPwd = findViewById(R.id.edLogin_pwd);
        btnLogin = findViewById(R.id.btnLogin_login);

        imgValCode = findViewById(R.id.login_iv_code);
        edValCode = findViewById(R.id.login_et_varCode);
        edUser.addTextChangedListener(this);
        edPwd.addTextChangedListener(this);
        edValCode.addTextChangedListener(this);
        btnLogin.setEnabled(false);
        forgetTv.setOnClickListener(this);
        tv.setOnClickListener(this);
        imgValCode.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv.getPaint().setAntiAlias(true);//抗锯齿

        forgetTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        forgetTv.getPaint().setAntiAlias(true);//抗锯齿
    }


    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin_tv_register:
                intentJump(RegisterActivity.class);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.btnLogin_tv_forget:
                //跳转找回密码页面
                intentJump(FindPwdActivity.class);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.login_iv_code:
                imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                break;
            case R.id.btnLogin_login:
                String user = edUser.getText().toString().trim();
                String pwd = edPwd.getText().toString().trim();
                String varCode = FourFiguresNumberCode.getInstance().getCode();
                login(user,pwd,varCode);
                break;
        }

    }

    private void login(String user, String pwd, String valCode) {
        String code = edValCode.getText().toString().trim();
        Log.e("TAG", "login: 验证码："+code+"\n正确验证码："+valCode);
        if (TextUtils.isEmpty(user)) {
            MyToast.showText(LoginActivity.this,"账号不能为空哦！");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            MyToast.showText(LoginActivity.this,"密码不能为空哦！");
            return;
        }
        if (!checkUsername(user)) {
            MyToast.showText(LoginActivity.this,"用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            return;
        }
        if (!checkPassword(pwd)) {
            MyToast.showText(LoginActivity.this,"密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }
        if (TextUtils.isEmpty(valCode)){
            MyToast.showText(LoginActivity.this,"验证码不能为空！");
            return;
        }
        if (!code.equals(valCode)){
            MyToast.showText(LoginActivity.this,"验证码错误！");
            imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
            return;
        }
        loading();
        btnLogin.setEnabled(false);
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
                try {
                    Looper.prepare();
                    Log.e("TAG", "onFailure: " + e.getMessage().toString());
                    showDiyDialog("登录失败！服务器连接超时！");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopLoading();
                            if (!TextUtils.isEmpty(edPwd.getText()) && !TextUtils.isEmpty(edUser.getText()) && !TextUtils.isEmpty(edValCode.getText())) {
                                btnLogin.setEnabled(true);
                                imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                            } else {
                                btnLogin.setEnabled(false);
                            }
                        }
                    });
                    Looper.loop();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String resultStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stopLoading();
                                    if (resultStr.equals("su_error")) {
                                        showDiyDialog("此用户不存在！");
                                        btnLogin.setEnabled(true);
                                        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                        return;
                                    }
                                    if (resultStr.equals("success")){
                                        MyToast.showText(LoginActivity.this,"登录密码错误！",false);
                                        btnLogin.setEnabled(true);
                                        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                        return;
                                    }
                                    List<User> dataEntity = new Gson().fromJson(resultStr, new TypeToken<List<User>>() {
                                    }.getType());
                                    data = dataEntity;
                                    if (data.size() > 0 && data != null) {
                                        loadDatas(URLContent.IP_URL);
//                                        startNext();
                                        return;
                                    }
                                    MyToast.showText(LoginActivity.this,"登录失败！服务器连接超时！");
                                    btnLogin.setEnabled(true);
                                    imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                }
                            }, 500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void startNext(String ipaddress) {
        //跳转页面
        intentJump(MainActivity.class);
        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
        finish();
        SharedPreferences sp = getSharedPreferences("constellation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit(); // 获取编辑器
        editor.putString("name", edUser.getText().toString().trim());
        editor.putString("createTime", data.get(0).getUpdateTime());
        editor.putString("phone", data.get(0).getMobile());
        editor.putString("passWord",edPwd.getText().toString().trim());
        editor.putBoolean("isShowGirdOrList",true);
        editor.putInt("sex",data.get(0).getSex());
        editor.putString("signature",data.get(0).getSignature());
        editor.putString("imgheader",data.get(0).getImgHeader());
        Log.e("TAG", "startNext: "+data.toString());
        // 存入数据
        editor.commit();
        //弹出通知
        showNotification(getBaseContext(), edUser.getText().toString().trim());
        btnLogin.setEnabled(true);
        requestNet(ipaddress);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnLogin.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edUser.getText()) && !TextUtils.isEmpty(edPwd.getText()) && !TextUtils.isEmpty(edValCode.getText())) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        IpBean bean = new Gson().fromJson(result, IpBean.class);
        String ip = null,ipaddress = null, ipCity = null;
        if (bean.getData() != null && bean.getCode() == 1){
            ip = bean.getData().getProvince().replace("省","");
            ipaddress = bean.getData().getProvince()+" · "+bean.getData().getCity()+"（"+bean.getData().getIsp()+" · "+bean.getData().getIp()+"）";
            ipCity = bean.getData().getCity().replace("市","");
            Log.e("TAG", "login: "+bean.toString());
        }else{
            ip = "";
            ipaddress = "";
            ipCity = "杭州";
            Log.e("TAG", "login: != 1"+bean.toString());
        }
        updateSpfUserInfo("ip",ip);
        updateSpfUserInfo("ipaddress",ipaddress);
        updateSpfUserInfo("ipCity",ipCity);
        getUserInfo();
        startNext(ipaddress);
    }

    private void requestNet(String ip) {
        getUserInfo();
        requestOkhttp(base_phones, ip, "wu", "/insert/user/login/info", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                SharedPreferences spf = getSharedPreferences("constellation", MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                if (resultStr.equals("success")) {
                    edit.putBoolean("login_info",true);
                    edit.commit();
                    Log.e("TAG", "onSuccess: 用户登录情况插入成功！");
                    return;
                }
                edit.putBoolean("login_info",false);
                edit.commit();
                Log.e("TAG", "onSuccess: 用户登录情况插入失败！");
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(getApplicationContext(),"服务器连接超时！");
            }
        });
    }

}