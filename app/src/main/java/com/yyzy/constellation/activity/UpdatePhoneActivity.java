package com.yyzy.constellation.activity;

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

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.MyEditText;
import com.yyzy.constellation.utils.MyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class UpdatePhoneActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView tvSendValNumTo;
    private ImageView imgBack;
    private TextView tvTitle;
    private Button btnNext;
    private EditText etValNum;
    private TextView tvSendValNum;

    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器

    private String replacePhone;
    private String userName;


    @Override
    protected int initLayout() {
        return R.layout.activity_update_phone;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        tvSendValNumTo = findViewById(R.id.updatePhone_send_tv_to);
        btnNext = findViewById(R.id.updatePhone_btn_next);
        etValNum = findViewById(R.id.updatePhone_et_valNum);
        tvSendValNum = findViewById(R.id.updatePhone_tv_send_valNum);

        imgBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        tvSendValNum.setOnClickListener(this);
        etValNum.addTextChangedListener(this);
        btnNext.setEnabled(false);

        tvTitle.setText("修改手机号码");
        Intent intent = getIntent();
        String phone = intent.getStringExtra("updatePhone");
        userName = intent.getStringExtra("userName");
        replacePhone = phone.replace(" ", "");
        Log.e("TAG", "现手机号为："+phone);
        String enPhone = replacePhone.substring(0, 3) + "****" + replacePhone.substring(7, replacePhone.length());
        tvSendValNumTo.setText("\t" + enPhone);

        mTimeCount = new TimeCount(60000,1000);
    }

    @Override
    protected void initData() {
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loadNetData(user,pwd,phone);
                                //MyToast.showText(CancelidActivity.this,"验证成功",true);
                                //showAlertDialog();
                                //showDefaultDialog();
                                Intent intent = new Intent();
                                intent.setClass(UpdatePhoneActivity.this,UpdatePhoneOutActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("updatePhone",replacePhone);
                                intent.putExtra("userName",userName);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(UpdatePhoneActivity.this, "语音验证发送！", Toast.LENGTH_SHORT);
                            }
                        });
                        //获取验证码成功
                        MyToast.showText(UpdatePhoneActivity.this, "获取验证码成功！", Toast.LENGTH_SHORT);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(UpdatePhoneActivity.this,"验证码已发送",Toast.LENGTH_SHORT);
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
                                    MyToast.showText(UpdatePhoneActivity.this,des);
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
            case R.id.updatePhone_tv_send_valNum:
                //Log.e("TAG", "要修改的手机号为："+replacePhone);
                if (!TextUtils.isEmpty(replacePhone)){
                    //发送验证码
                    SMSSDK.getVerificationCode("+86",replacePhone);//获取验证码
                    mTimeCount.start();
                }else{
                    MyToast.showText(this,"手机号为空！");
                    return;
                }
                break;
            case R.id.updatePhone_btn_next:
                String valNum = etValNum.getText().toString().trim();
                if (!TextUtils.isEmpty(valNum)){
                    //进行验证码核验
                    SMSSDK.submitVerificationCode("+86",replacePhone,valNum);
                }else{
                    MyToast.showText(this,"验证码不能为空！");
                    return;
                }
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
            tvSendValNum.setClickable(false);
            tvSendValNum.setText(l/1000 + "秒后重新获取");
            tvSendValNum.setTextColor(getResources().getColor(R.color.grey));
        }

        @Override
        public void onFinish() {
            tvSendValNum.setClickable(true);
            tvSendValNum.setText("重新获取");
            tvSendValNum.setTextColor(getResources().getColor(R.color.zhuBlue));
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnNext.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String valNum = etValNum.getText().toString().trim();
        if (valNum.length() == 6) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}