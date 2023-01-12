package com.yyzy.constellation.user;

import androidx.annotation.NonNull;

import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private EditText edRegisterUser, edRegisterPwd, edRegisterPhone;
    private TextView mbtnRegister;
    private TextView tv;

    private TextView validateNum_btn;
    private EditText validateNum;
    private EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器
    private String users, pwds, phones;


    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        MobSDK.init(this);
        MobSDK.submitPolicyGrantResult(true);
        tv = findViewById(R.id.btnRegister_tv_login);
        edRegisterUser = findViewById(R.id.edRegister_user);
        edRegisterPwd = findViewById(R.id.edRegister_pwd);
        edRegisterPhone = findViewById(R.id.edRegister_phone);
        mbtnRegister = findViewById(R.id.btnRegister_register);
        validateNum_btn = findViewById(R.id.validateNum_btn);
        validateNum = findViewById(R.id.validateNum);
        mbtnRegister.setEnabled(false);
        validateNum_btn.setEnabled(false);
        edRegisterUser.addTextChangedListener(this);
        edRegisterPwd.addTextChangedListener(this);
        validateNum.addTextChangedListener(this);
        validateNum_btn.setOnClickListener(this);
        addTextListener();
        //设置下划线
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //下划线抗锯齿
        tv.getPaint().setAntiAlias(true);
        mTimeCount = new TimeCount(60000, 1000);
    }

    private void addTextListener() {
        edRegisterPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mbtnRegister.setEnabled(false);
                validateNum_btn.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                try {
                    if (charSequence == null || charSequence.length() == 0) return;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < charSequence.length(); i++) {
                        if (i != 3 && i != 8 && charSequence.charAt(i) == ' ') {
                            continue;
                        } else {
                            sb.append(charSequence.charAt(i));
                            if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                                sb.insert(sb.length() - 1, ' ');
                            }
                        }
                    }
                    if (!sb.toString().equals(charSequence.toString())) {
                        int index = start + 1;
                        if (sb.charAt(start) == ' ') {
                            if (before == 0) {
                                index++;
                            } else {
                                index--;
                            }
                        } else {
                            if (before == 1) {
                                index--;
                            }
                        }
                        edRegisterPhone.setText(sb.toString());
                        edRegisterPhone.setSelection(index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(edRegisterUser.getText()) && !TextUtils.isEmpty(edRegisterPwd.getText()) && !TextUtils.isEmpty(edRegisterPhone.getText()) && !TextUtils.isEmpty(validateNum.getText())) {
                    mbtnRegister.setEnabled(true);
                } else {
                    mbtnRegister.setEnabled(false);
                }
                if (edRegisterPhone.getText().length() == 13) {
                    validateNum_btn.setEnabled(true);
                    return;
                }
                validateNum_btn.setEnabled(false);
            }
        });
    }


    @Override
    protected void initData() {
        sendMsg();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edRegisterUser.getText().toString().trim();
                String pwd = edRegisterPwd.getText().toString().trim();
                String phone = edRegisterPhone.getText().toString().trim();
                String valNum = validateNum.getText().toString().trim();
                register(user, pwd, phone, valNum);
            }
        });

    }

    private void register(String user, String pwd, String phone, String varNum) {
        //判断用户输入的密码、账号、手机号：1、是否为空；2、是否符合账号注册标准(严格使用正则表达式)
        if (TextUtils.isEmpty(user)) {
            MyToast.showText(this, "注册账号不能为空哦！");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            MyToast.showText(this, "注册密码不能为空哦！");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            MyToast.showText(this, "手机号不能为空哦！");
            return;
        }
        if (!checkUsername(user)) {
            MyToast.showText(this, "用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            return;
        }
        if (!checkPassword(pwd)) {
            MyToast.showText(this, "密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }
        if (!checkPhone(phone)) {
            MyToast.showText(this, "手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
            return;
        }
        if (!TextUtils.isEmpty(phone)) {
            if (checkPhone(phone)) {
                if (!TextUtils.isEmpty(varNum)) {
                    loading();
                    users = user;
                    pwds = pwd;
                    phones = phone;
                    Log.e("TAG", "点击了注册: 用户名：" + users + "密码：" + pwds + "手机号：" + phones + "验证码：" + varNum);
                    SMSSDK.submitVerificationCode("+86", phones, varNum);//提交验证
                    return;
                }
                MyToast.showText(this, "请输入验证码!");
                return;
            }
            MyToast.showText(this, "手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
        }

    }

    private void sendMsg() {
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("TAG", "run: 用户名：" + users + "密码：" + pwds + "手机号：" + phones);
                                loadNetData(users, pwds, phones);
                                return;
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(RegisterActivity.this, "语音验证发送！", Toast.LENGTH_SHORT);
                                return;
                            }
                        });
                        //获取验证码成功
                        MyToast.showText(RegisterActivity.this, "获取验证码成功！", Toast.LENGTH_SHORT);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("TAG", "run: 验证码已发送");
                                stopLoading();
                                MyToast.showText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT);
                                return;
                            }
                        });
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    Log.i("1234", throwable.toString());
                    try {
                        JSONObject obj = new JSONObject(throwable.getMessage());
                        final String des = obj.optString("detail");
                        if (!TextUtils.isEmpty(des)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("TAG", "run: " + des);
                                    stopLoading();
                                    MyToast.showText(RegisterActivity.this, des);
                                    return;
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void loadNetData(String user, String pwd, String phone) {
        //请求本地后台服务器，再进行下一步判断，从数据库筛选用户名是否存在；
        // 一切要求符合，则进行数据库添加数据
        mbtnRegister.setEnabled(false);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", user);
        formbody.add("pwd", pwd);
        formbody.add("mobile", phone);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        MyToast.showText(RegisterActivity.this, "注册失败！服务器连接超时！", false);
                        if (!TextUtils.isEmpty(edRegisterUser.getText()) && !TextUtils.isEmpty(edRegisterPwd.getText()) && !TextUtils.isEmpty(edRegisterPhone.getText()) && !TextUtils.isEmpty(validateNum.getText())) {
                            mbtnRegister.setEnabled(true);
                            return;
                        }
                        mbtnRegister.setEnabled(false);
                    }
                });
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
                                stopLoading();
                                if (resultStr.equals("success")) {
                                    MyToast.showText(RegisterActivity.this, "恭喜" + edRegisterUser.getText().toString().trim() + "！您已注册成功，赶紧前往登录吧！");
                                    finish();
                                    overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                                    return;
                                }
                                if (resultStr.equals("error")) {
                                    MyToast.showText(RegisterActivity.this, "此用户名已存在！请更换用户名！");
                                    mbtnRegister.setEnabled(true);
                                    return;
                                }
                                MyToast.showText(RegisterActivity.this, "注册失败！服务器连接超时！");
                                mbtnRegister.setEnabled(true);
                            }
                        }, 500);
                    }
                });
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mbtnRegister.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edRegisterUser.getText()) && !TextUtils.isEmpty(edRegisterPwd.getText()) && !TextUtils.isEmpty(edRegisterPhone.getText()) && !TextUtils.isEmpty(validateNum.getText())) {
            mbtnRegister.setEnabled(true);
        } else {
            mbtnRegister.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validateNum_btn:
                String phone = edRegisterPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    if (checkPhone(phone)) {
                        Log.e("TAG", phone + "onClick: " + users + pwds + phones);
                        loading();
                        String replacePhone = phone.replace(" ", "").replace(" ", "");
                        phones = replacePhone;
                        Log.e("TAG", "onClick: 验证码手机号：" + phones);
                        SMSSDK.getVerificationCode("+86", phones);//获取验证码
                        mTimeCount.start();
                        return;
                    }
                    MyToast.showText(RegisterActivity.this, "请输入正确的手机号码");
                    return;
                }
                MyToast.showText(RegisterActivity.this, "请输入手机号码");
                break;
        }
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            validateNum_btn.setEnabled(false);
            validateNum_btn.setText("重新获取" + "(" + l / 1000 + ")");
        }

        @Override
        public void onFinish() {
            if (edRegisterPhone.getText().length() == 13 && !TextUtils.isEmpty(edRegisterPhone.getText())) {
                validateNum_btn.setEnabled(true);
            } else {
                validateNum_btn.setEnabled(false);
            }
            validateNum_btn.setText("重新获取");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}