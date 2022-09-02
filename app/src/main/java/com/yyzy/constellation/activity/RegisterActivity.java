package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.yyzy.constellation.R;
import com.yyzy.constellation.receiver.IntentReceiver;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.StringUtils;
import com.yyzy.constellation.utils.URLContent;

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


public class RegisterActivity extends BaseActivity implements TextWatcher,View.OnClickListener{
    private EditText edRegisterUser, edRegisterPwd, edRegisterPhone;
    private Button mbtnRegister;
    private TextView tv;
    //private DiyProgressDialog mDialog;
    private ImageView ivBack;

    private Button validateNum_btn;
    private EditText validateNum;
    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器


    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        MobSDK.submitPolicyGrantResult(true);
        tv = findViewById(R.id.btnRegister_tv_login);
        edRegisterUser = findViewById(R.id.edRegister_user);
        edRegisterPwd = findViewById(R.id.edRegister_pwd);
        edRegisterPhone = findViewById(R.id.edRegister_phone);
        mbtnRegister = findViewById(R.id.btnRegister_register);
        ivBack = findViewById(R.id.register_iv_back);
        validateNum_btn = findViewById(R.id.validateNum_btn);
        validateNum = findViewById(R.id.validateNum);
        mbtnRegister.setEnabled(false);
        validateNum_btn.setEnabled(false);
        edRegisterUser.addTextChangedListener(this);
        edRegisterPwd.addTextChangedListener(this);
        validateNum.addTextChangedListener(this);
        ivBack.setOnClickListener(this);
        validateNum_btn.setOnClickListener(this);

        mTimeCount = new TimeCount(60000, 1000);
        //设置下划线
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //下划线抗锯齿
        tv.getPaint().setAntiAlias(true);

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
                }catch (Exception e){
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
                if (edRegisterPhone.getText().length() == 13){
                    validateNum_btn.setEnabled(true);
                }else{
                    validateNum_btn.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void initData() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            //showToast("");
            MyToast.showText(this,"注册账号不能为空哦！");
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            //showToast("注册密码不能为空哦！");
            MyToast.showText(this,"注册密码不能为空哦！");
            return;
        } else if (TextUtils.isEmpty(phone)) {
            //showToast("手机号不能为空哦！");
            MyToast.showText(this,"手机号不能为空哦！");
            return;
        } else if (!checkUsername(user)) {
            //showToast("用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            MyToast.showText(this,"用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            return;
        } else if (!checkPassword(pwd)) {
            //showToast("密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            MyToast.showText(this,"密码输入格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        } else if (!checkPhone(phone)) {
            //showToast("手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
            MyToast.showText(this,"手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
            return;
        }else if (!TextUtils.isEmpty(phone)){
            if (checkPhone(phone)){
                if (!TextUtils.isEmpty(varNum)){
                    SMSSDK.submitVerificationCode("+86",phone,varNum);//提交验证
                }else{
                    //showToast("请输入验证码!");
                    MyToast.showText(this,"请输入验证码!");
                    return;
                }
            }else{
                //showToast("手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
                MyToast.showText(this,"手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
                return;
            }
        }
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadNetData(user,pwd,phone);
                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(RegisterActivity.this, "语音验证发送！", Toast.LENGTH_SHORT);
                            }
                        });
                        //获取验证码成功
                        MyToast.showText(RegisterActivity.this, "获取验证码成功！", Toast.LENGTH_SHORT);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(RegisterActivity.this,"验证码已发送",Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    Log.i("1234",throwable.toString());
                    try {
                        JSONObject obj = new JSONObject(throwable.getMessage());
                        final String des = obj.optString("detail");
                        if (!TextUtils.isEmpty(des)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(RegisterActivity.this,des,Toast.LENGTH_SHORT).show();
                                    MyToast.showText(RegisterActivity.this,des);
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

    private void loadNetData(String user,String pwd, String phone) {
        //请求本地后台服务器，再进行下一步判断，从数据库筛选用户名是否存在；
        // 一切要求符合，则进行数据库添加数据

        DiyProgressDialog mDialog = new DiyProgressDialog(RegisterActivity.this, "正在加载中...");
        mDialog.setCancelable(true);//设置能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
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
                        //showToast("注册失败！服务器连接超时！");
                        MyToast.showText(RegisterActivity.this,"注册失败！服务器连接超时！",false);
                        //mDialog.cancel();
                        mDialog.cancel();
                        if (!TextUtils.isEmpty(edRegisterUser.getText()) && !TextUtils.isEmpty(edRegisterPwd.getText()) && !TextUtils.isEmpty(edRegisterPhone.getText()) && !TextUtils.isEmpty(validateNum.getText())) {
                            mbtnRegister.setEnabled(true);
                            //return;
                        } else {
                            mbtnRegister.setEnabled(false);
                            return;
                        }

                    }
                });
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
                                    MyToast.showText(RegisterActivity.this,"恭喜" + edRegisterUser.getText().toString().trim() + "！您已注册成功，赶紧前往登录吧！");
                                    edRegisterUser.setText("");
                                    edRegisterUser.setHint("");
                                    edRegisterPwd.setText("");
                                    edRegisterPwd.setHint("");
                                    edRegisterPhone.setText("");
                                    edRegisterPhone.setHint("");
                                    validateNum.setText("");
                                    validateNum.setHint("");

                                    //mbtnRegister.setEnabled(false);
                                    mDialog.cancel();
                                    //return;
                                }else if (resultStr.equals("error")) {
                                    MyToast.showText(RegisterActivity.this,"此用户名已存在！请更换用户名！");
                                    mbtnRegister.setEnabled(true);
                                    mDialog.cancel();
                                    //return;
                                } else {
                                    MyToast.showText(RegisterActivity.this,"注册失败！服务器连接超时！");
                                    mbtnRegister.setEnabled(true);
                                    mDialog.cancel();
                                    //return;
                                }
                            }
                        }, 1000);
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
                if(!TextUtils.isEmpty(phone)){
                    if (checkPhone(phone)) {
                        String replacePhone = phone.replace(" ", "").replace(" ", "");
                        SMSSDK.getVerificationCode("+86",replacePhone);//获取验证码
                        mTimeCount.start();
                    }else{
                        //Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        MyToast.showText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT);
                    }
                }else{
                    //Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    MyToast.showText(RegisterActivity.this,"请输入手机号码",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.register_iv_back:
                finish();
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
            validateNum_btn.setText("重新获取"+"("+l/1000+")");
        }

        @Override
        public void onFinish() {
            if (edRegisterPhone.getText().length() == 13 && !TextUtils.isEmpty(edRegisterPhone.getText())){
                validateNum_btn.setEnabled(true);
            }else{
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
}