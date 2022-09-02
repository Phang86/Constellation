package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyEditText;
import com.yyzy.constellation.utils.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.yyzy.constellation.utils.URLContent.BASE_URL;

public class UpdatePhoneOutActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ImageView imgBack;
    private TextView tvTitle;
    private EditText etPhone, etValNum;
    private TextView tvSendValNum;
    private Button btnConfirm;
    private String updatePhone;

    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器
    private String userName;


    @Override
    protected int initLayout() {
        return R.layout.activity_update_phone_out;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        etPhone = findViewById(R.id.updateOut_et_phone);
        etValNum = findViewById(R.id.updateOut_et_valNum);
        tvSendValNum = findViewById(R.id.updateOut_tv_send_valNum);
        btnConfirm = findViewById(R.id.updateOut_btn_confirm);

        imgBack.setOnClickListener(this);
        tvSendValNum.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        etValNum.addTextChangedListener(this);

        tvTitle.setText("修改手机号码");

        Intent intent = getIntent();
        updatePhone = intent.getStringExtra("updatePhone");
        userName = intent.getStringExtra("userName");

        Log.e("TAG", "原手机号为：" + updatePhone);
        Log.e("TAG", "用户名为：" + userName);

        btnConfirm.setEnabled(false);
        mTimeCount = new TimeCount(60000, 1000);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnConfirm.setEnabled(false);
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
                        etPhone.setText(sb.toString());
                        etPhone.setSelection(index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ph = etPhone.getText().toString().trim();
                String num = etValNum.getText().toString().trim();
                if (!TextUtils.isEmpty(ph) && !TextUtils.isEmpty(num)) {
                    btnConfirm.setEnabled(true);
                } else {
                    btnConfirm.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //调用接口
                                String newPhone = etPhone.getText().toString().trim();
                                updatePhoneNetRequest(userName,newPhone);
                                Log.e("TAG", "新的手机号码为："+newPhone);
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(UpdatePhoneOutActivity.this, "语音验证发送！", Toast.LENGTH_SHORT);
                            }
                        });
                        //获取验证码成功
                        MyToast.showText(UpdatePhoneOutActivity.this, "获取验证码成功！", Toast.LENGTH_SHORT);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(UpdatePhoneOutActivity.this, "验证码已发送", Toast.LENGTH_SHORT);
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
                                    //Toast.makeText(RegisterActivity.this,des,Toast.LENGTH_SHORT).show();
                                    MyToast.showText(UpdatePhoneOutActivity.this, des);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.updateOut_tv_send_valNum:
                String phone = etPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    //进行下一步判断
                    if (checkPhone(phone)) {
                        //对手机号进行格式效验
                        String replacePhone = phone.replace(" ", "");
                        if (replacePhone.equals(updatePhone)) {
                            //判断修改后的手机号是否与原来手机号相同
                            MyToast.showText(this, "新手机号码不能与旧手机号相同哦！请更换新的手机号码！");
                            return;
                        } else {
                            //对输入框的手机号码进行空格替换

                            //发送验证码
                            SMSSDK.getVerificationCode("+86", replacePhone);
                            mTimeCount.start();
                        }
                    } else {
                        MyToast.showText(this, "手机号格式不正确！", false);
                        return;
                    }
                } else {
                    MyToast.showText(this, "手机号不能为空哦！");
                    return;
                }
                break;
            case R.id.updateOut_btn_confirm:
                String mobile = etPhone.getText().toString().trim();
                String valNum = etValNum.getText().toString().trim();
                if (!TextUtils.isEmpty(mobile)) {
                    if (checkPhone(mobile)) {
                        if (!mobile.equals(updatePhone)) {
                            if (!TextUtils.isEmpty(valNum)) {
                                //进行验证码和手机号效验
                                SMSSDK.submitVerificationCode("+86", mobile, valNum);
                            } else {
                                MyToast.showText(this, "验证码不能为空哦！");
                                return;
                            }
                        } else {
                            MyToast.showText(this, "新手机号码不能与旧手机号相同哦！请更换新的手机号码！");
                            return;
                        }
                    } else {
                        MyToast.showText(this, "手机号码格式不对！");
                        return;
                    }
                } else {
                    MyToast.showText(this, "手机号码不能为空！");
                    return;
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnConfirm.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String ph = etPhone.getText().toString().trim();
        String num = etValNum.getText().toString().trim();
        if (!TextUtils.isEmpty(ph) && !TextUtils.isEmpty(num)) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }
    }


    //计时器
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            tvSendValNum.setClickable(false);
            tvSendValNum.setText(l / 1000 + "秒后重新获取");
            tvSendValNum.setTextColor(getResources().getColor(R.color.grey));
        }

        @Override
        public void onFinish() {
            tvSendValNum.setClickable(true);
            tvSendValNum.setText("重新获取");
            tvSendValNum.setTextColor(getResources().getColor(R.color.zhuBlue));
        }
    }

    private void updatePhoneNetRequest(String name,String newPhone) {
        DiyProgressDialog mDialog = new DiyProgressDialog(UpdatePhoneOutActivity.this, "加载中...");
        mDialog.setCancelable(false);//设置能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", name);
        formbody.add("phone", newPhone);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/update/phone")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.showText(UpdatePhoneOutActivity.this,"加载失败！服务器连接超时！",false);
                        mDialog.cancel();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String resultStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (resultStr.equals("error")) {
                                showDiyDialog(UpdatePhoneOutActivity.this,"修改失败！");
                                mDialog.cancel();
                                return;
                            }else if (resultStr.equals("success")){
//                                List<User> dataEntity = new Gson().fromJson(resultStr, new TypeToken<List<User>>() {
//                                }.getType());
//                                //User data = new Gson().fromJson(resultStr, User.class);
//                                List<User> data = new ArrayList<>();
//                                data = dataEntity;
                                if (!TextUtils.isEmpty(resultStr)) {
                                    MyToast.showText(UpdatePhoneOutActivity.this,"手机号修改完成！",true);
                                    finish();
                                    mDialog.cancel();
                                } else {
                                    MyToast.showText(UpdatePhoneOutActivity.this,"修改失败！服务器连接超时！");
                                    mDialog.cancel();
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}