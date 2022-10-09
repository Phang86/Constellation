package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.receiver.IntentReceiver;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.FourFiguresNumberCode;
import com.yyzy.constellation.utils.MyToast;

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

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView tv, forgetTv;
    private EditText edUser, edPwd, edValCode;
    private Button btnLogin;
    private List<User> data = new ArrayList<>();
    private String userPassword;

    private CheckBox mRemenber, mAutoLogin;
    private boolean mPasswordFlag = false;//记住密码标志
    private boolean mAutoLoginFlag = false;//自动登录标志
    private SharedPreferences sharedPreferences,spf;
    private SharedPreferences.Editor editor,ed;

    private ImageView imgValCode;
    private NotificationManager manager;
    private Notification note;

    private BroadcastReceiver receivers = new IntentReceiver();

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
        mRemenber = findViewById(R.id.cbLogin_rememberPwd);
        mAutoLogin = findViewById(R.id.cbLogin_autoLogin);
        imgValCode = findViewById(R.id.login_iv_code);
        edValCode = findViewById(R.id.login_et_varCode);
        edUser.addTextChangedListener(this);
        edPwd.addTextChangedListener(this);
        edValCode.addTextChangedListener(this);
        btnLogin.setEnabled(false);
        forgetTv.setOnClickListener(this);
        tv.setOnClickListener(this);
        imgValCode.setOnClickListener(this);
        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv.getPaint().setAntiAlias(true);//抗锯齿

        forgetTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        forgetTv.getPaint().setAntiAlias(true);//抗锯齿

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receivers,filter);

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
        if (mAutoLoginFlag) {
            mAutoLogin.setChecked(true);
            String username = edUser.getText().toString();
            String password = edPwd.getText().toString();
            login(username, password,"");
        }

        mRemenber.setOnClickListener(mListener);
        mRemenber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //如果是选中记住密码，取消记住密码、自动登录
                if (!isChecked) {
                    mAutoLogin.setChecked(false);
                    //清空密码输入框
                    edPwd.setText("");
                    imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
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
                    sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
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
                            //showToast("");
                            MyToast.showText(LoginActivity.this,"请勾选复选框！");
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
                    String varCode = FourFiguresNumberCode.getInstance().getCode();
                    login(user, pwd, varCode);
            }
        }
    };

    private void clearEditor(){
        if (editor != null){
            editor.clear();
            editor.commit();
        }
        spf = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        ed = spf.edit();
        if ( ed != null){
            ed.clear();
            ed.commit();
        }
    }

    private void login(String user, String pwd, String valCode) {
        String code = edValCode.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            //showToast("账号不能为空哦！");
            MyToast.showText(LoginActivity.this,"账号不能为空哦！");
            clearEditor();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            //showToast("密码不能为空哦！");
            MyToast.showText(LoginActivity.this,"密码不能为空哦！");
            clearEditor();
            return;
        } else if (!checkUsername(user)) {
            //showToast("用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            MyToast.showText(LoginActivity.this,"用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            clearEditor();
            return;
        } else if (!checkPassword(pwd)) {
            //showToast("密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            MyToast.showText(LoginActivity.this,"密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            clearEditor();
            return;
        }else if (TextUtils.isEmpty(valCode)){
            //showToast("验证码不能为空！");
            MyToast.showText(LoginActivity.this,"验证码不能为空！");
            clearEditor();
            return;
        }else if (!code.equals(valCode)){
            //showToast("验证码有误！");
            MyToast.showText(LoginActivity.this,"验证码错误！");
            imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
            clearEditor();
            return;
        }
        DiyProgressDialog mDialog = new DiyProgressDialog(LoginActivity.this, "正在登录中...");
        mDialog.setCancelable(false);//设置不能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
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
                    clearEditor();
                    //showToast("登录失败！服务器连接超时！");
                    showDiyDialog(LoginActivity.this,"登录失败！服务器连接超时！");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.cancel();
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
                                    if (resultStr.equals("su_error")) {
                                        showDiyDialog(LoginActivity.this,"此用户不存在！");
                                        clearEditor();
                                        mDialog.cancel();
                                        btnLogin.setEnabled(true);
                                        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                        return;
                                    }else if (resultStr.equals("success")){
                                        MyToast.showText(LoginActivity.this,"登录密码错误！",false);
                                        clearEditor();
                                        mDialog.cancel();
                                        btnLogin.setEnabled(true);
                                        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                        return;
                                    }else {
                                        List<User> dataEntity = new Gson().fromJson(resultStr, new TypeToken<List<User>>() {
                                        }.getType());
                                        data = dataEntity;
                                        if (data.size() > 0 && data != null) {
                                            //弹出通知
                                            showNotification(getBaseContext(), edUser.getText().toString().trim());
                                            //跳转页面
                                            intentJump(MainActivity.class);
                                            finish();
                                            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                                            SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit(); // 获取编辑器
                                            editor.putString("name", edUser.getText().toString().trim());
                                            editor.putString("createTime", data.get(0).getUpdateTime());
                                            editor.putString("phone", data.get(0).getMobile());
                                            editor.putString("passWord",edPwd.getText().toString().trim());
                                            Log.e("TAG", "登录密码为:"+edPwd.getText().toString().trim());
                                            // 存入数据
                                            editor.commit();
                                            mDialog.cancel();
                                            btnLogin.setEnabled(true);
                                        } else {
                                            MyToast.showText(LoginActivity.this,"登录失败！服务器连接超时！");
                                            //showToast();
                                            mDialog.cancel();
                                            clearEditor();
                                            btnLogin.setEnabled(true);
                                            imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                            return;
                                        }
                                    }
                                }
                            }, 1200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private static void showNotification(Context context, String userName) {
        boolean isVibrate = true;//是否震动
        //1.获取消息服务
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //默认通道是default
        String channelId = "default";
        //2.如果是android8.0以上的系统，则新建一个消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "chat";
            /*
             通道优先级别：
             * IMPORTANCE_NONE 关闭通知
             * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
             * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
             * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
             * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
             */
            NotificationChannel channel = new NotificationChannel(channelId, "消息提醒", NotificationManager.IMPORTANCE_HIGH);
            //设置该通道的描述（可以不写）
            //channel.setDescription("重要消息，请不要关闭这个通知。");
            //是否绕过勿打扰模式
            channel.setBypassDnd(true);
            //是否允许呼吸灯闪烁
            channel.enableLights(true);
            //闪关灯的灯光颜色
            channel.setLightColor(Color.RED);
            //桌面launcher的消息角标
            channel.canShowBadge();
            //设置是否应在锁定屏幕上显示此频道的通知
            //channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            if (isVibrate) {
                //是否允许震动
                channel.enableVibration(true);
                //先震动1秒，然后停止0.5秒，再震动2秒则可设置数组为：new long[]{1000, 500, 2000}
                channel.setVibrationPattern(new long[]{1000, 500, 2000});
            } else {
                channel.enableVibration(false);
                channel.setVibrationPattern(new long[]{0});
            }
            //创建消息通道
            manager.createNotificationChannel(channel);
        }
        //3.实例化通知
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, channelId);
        //通知默认的声音 震动 呼吸灯
        nc.setDefaults(NotificationCompat.DEFAULT_ALL);
        //通知标题
        nc.setContentTitle("服务通知");
        //通知内容
        nc.setContentText("尊敬的" + userName + "先生，您已成功登录星梦缘！");
        //设置通知的小图标
        nc.setSmallIcon(R.mipmap.account);
        //设置通知的大图标
        nc.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_icon));
        //设定通知显示的时间
        nc.setWhen(System.currentTimeMillis());
        //设置通知的优先级
        nc.setPriority(NotificationCompat.PRIORITY_MAX);
        //设置点击通知之后通知是否消失
        nc.setAutoCancel(true);
        //点击通知打开软件
        Context application = context.getApplicationContext();
        Intent resultIntent = new Intent(application, MainActivity.class);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(application, 0, resultIntent, 0);
        nc.setContentIntent(pendingIntent);
        //4.创建通知，得到build
        Notification notification = nc.build();
        //5.发送通知
        manager.notify(1, notification);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btnLogin_tv_register:
                //tv.setTextColor(getResources().getColor(R.color.red));
                //跳转注册页面
                intent.setClass(this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.btnLogin_tv_forget:
                //forgetTv.setTextColor(getResources().getColor(R.color.red));
                //跳转找回密码页面
                intent.setClass(this, FindPwdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.login_iv_code:
                imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imgValCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receivers);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (editor != null) {
                editor.clear();
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        User user = new Gson().fromJson(result, User.class);
        Log.e("TAG", "onSuccess: " + user.toString());
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


}