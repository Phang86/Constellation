package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.SPUtils;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.utils.ViewUtil;

import org.jetbrains.annotations.NotNull;
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

public class CancelidActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView tvNum;
    private ImageView imgBack;
    private TextView tvTitle;
    private EditText etNum;
    private Button btnZhuxiao;
    private String myphone;
    private TextView sendNum;

    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器

    private SharedPreferences sharedPreferences,sp;
    private SharedPreferences.Editor editor,ed;
    private String name;
    private NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelid);
        initView();
        initData();
    }

    private void initView() {
        MobSDK.submitPolicyGrantResult(true);
        tvNum = findViewById(R.id.zhuxiao_tv_num_to);
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        etNum = findViewById(R.id.zhuxiao_et);
        btnZhuxiao = findViewById(R.id.zhuxiao_next);
        sendNum = findViewById(R.id.cancelid_send_num);

        etNum.addTextChangedListener(this);
        imgBack.setOnClickListener(this);
        btnZhuxiao.setOnClickListener(this);
        sendNum.setOnClickListener(this);

        Intent intent = getIntent();
        myphone = intent.getStringExtra("Myphone");
        String phone = myphone.substring(0,3)+"****"+myphone.substring(7,myphone.length());
        tvNum.setText(" "+phone);
        tvTitle.setText("短信验证注销");
        btnZhuxiao.setEnabled(false);
        name = intent.getStringExtra("name");
        Log.e("TAG", "需要注销的用户名为："+name);
        mTimeCount = new TimeCount(60000, 1000);
    }

    private void initData() {
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
                                showDefaultDialog();
                                return;
                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(CancelidActivity.this, "语音验证发送！", Toast.LENGTH_SHORT);
                                return;
                            }
                        });
                        //获取验证码成功
                        MyToast.showText(CancelidActivity.this, "获取验证码成功！", Toast.LENGTH_SHORT);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showText(CancelidActivity.this,"验证码已发送",Toast.LENGTH_SHORT);
                                return;
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
                                    MyToast.showText(CancelidActivity.this,des);
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

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            sendNum.setClickable(false);
            sendNum.setText(l/1000 + "秒后重新获取");
            sendNum.setTextColor(getResources().getColor(R.color.grey));
        }

        @Override
        public void onFinish() {
            sendNum.setClickable(true);
            sendNum.setText("重新获取");
            sendNum.setTextColor(getResources().getColor(R.color.zhuBlue));
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnZhuxiao.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String num = etNum.getText().toString().trim();
        if (num.length() == 6){
            btnZhuxiao.setEnabled(true);
            ViewUtil.hideOneInputMethod(CancelidActivity.this, etNum);
        }else {
            btnZhuxiao.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                SMSSDK.unregisterEventHandler(eh);
                break;
            case R.id.zhuxiao_next:
                String valNum = etNum.getText().toString().trim();
                if (!valNum.isEmpty()){
                    SMSSDK.submitVerificationCode("+86",myphone,valNum);
                }else {
                    MyToast.showText(this,"请输入验证码！");
                }
                break;
            case R.id.cancelid_send_num:
                if(!TextUtils.isEmpty(myphone)){
                    SMSSDK.getVerificationCode("+86",myphone);//获取验证码
                    mTimeCount.start();
                }else{
                    //Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    MyToast.showText(CancelidActivity.this,"请输入手机号码",Toast.LENGTH_SHORT);
                }
                break;
        }
    }


    private void showDefaultDialog(){
        AlertDialogUtils dialogUtils = AlertDialogUtils.getInstance();
        AlertDialogUtils.showConfirmDialog(CancelidActivity.this,"温馨提示","账号一旦彻底注销，数据无法恢复、且全部清空、无法找回。确定注销此账号吗？","继续","取消");
        dialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(androidx.appcompat.app.AlertDialog dialog) {
                //点击确认按钮要做的事情
                dialog.dismiss();
                //获取存储在sp里面的用户名和密码以及两个复选框状态
                sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                SPUtils.remove("imageUrl",CancelidActivity.this);
                //从后台获取到的用户信息
                sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
                ed = sp.edit();
                DiyProgressDialog loadDialog = new DiyProgressDialog(CancelidActivity.this,"账号注销中...");
                loadDialog.setCancelable(false);//设置不能通过后退键取消
                loadDialog.setCanceledOnTouchOutside(false);
                loadDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestNet(loadDialog);
                    }
                },1000);
            }

            @Override
            public void onNegativeButtonClick(androidx.appcompat.app.AlertDialog dialog) {
                //点击取消按钮关闭弹框
                dialog.dismiss();
            }
        });
    }


//    private void showAlertDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(CancelidActivity.this);
//        LayoutInflater inflater = LayoutInflater.from(CancelidActivity.this);
//        View v = inflater.inflate(R.layout.diy_alert_dialog_two, null);
//        TextView content = (TextView) v.findViewById(R.id.dialog_content);
//        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
//        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
//        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
//        final Dialog dialog = builder.create();
//        dialog.show();
//        dialog.setCancelable(false);
//        dialog.getWindow().getDecorView().setBackground(null);
//        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
//        //设置隐藏dialog默认的背景
//        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
//        content.setText("账号一旦彻底注销，数据无法恢复、且全部清空、且无法找回。确定注销此账号吗？");
//        btn_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                //获取存储在sp里面的用户名和密码以及两个复选框状态
//                sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
//                editor = sharedPreferences.edit();
//                SPUtils.remove("imageUrl",CancelidActivity.this);
//                //从后台获取到的用户信息
//                sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
//                ed = sp.edit();
//                DiyProgressDialog loadDialog = new DiyProgressDialog(CancelidActivity.this,"账号注销中...");
//                loadDialog.setCancelable(false);//设置不能通过后退键取消
//                loadDialog.setCanceledOnTouchOutside(false);
//                loadDialog.show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        requestNet(loadDialog);
//                    }
//                },1500);
//            }
//        });
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                dialog.dismiss();
//            }
//        });
//    }

    private void requestNet(DiyProgressDialog loadDialog){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", name);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + "/user/delete")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                CancelidActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.showText(CancelidActivity.this,"账号注销失败！服务器连接超时！");
                        loadDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("success")) {
                            loadDialog.cancel();
                            Intent intent = new Intent(CancelidActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            MyToast.showText(CancelidActivity.this,"账号注销成功！",true);
                            editor.clear();
                            editor.commit();
                            ed.clear();
                            ed.commit();
                            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            manager.cancel(1);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                        if (result.equals("error")) {
                            loadDialog.cancel();
                            MyToast.showText(CancelidActivity.this,"账号注销失败！");
                            return;
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