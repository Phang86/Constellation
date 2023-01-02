package com.yyzy.constellation.activity;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.FourFiguresNumberCode;
import com.yyzy.constellation.utils.MyToast;
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

    private TextView tvBack;
    private EditText userEt, phoneEt, verCodeEt;
    private TextView findBtn;
    private DiyProgressDialog mDialog;
    private ImageView ivCode;

    @Override
    protected int initLayout() {
        return R.layout.activity_find_pwd;
    }

    @Override
    protected void initView() {
        userEt = findViewById(R.id.find_user);
        phoneEt = findViewById(R.id.find_phone);
        findBtn = findViewById(R.id.find_btn);
        tvBack = findViewById(R.id.find_tv_login);
        ivCode = findViewById(R.id.find_iv_code);
        verCodeEt = findViewById(R.id.find_et_verCode);
        tvBack.setOnClickListener(this);
        findBtn.setOnClickListener(this);
        ivCode.setOnClickListener(this);
        findBtn.setEnabled(false);
        tvBack.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvBack.getPaint().setAntiAlias(true);//抗锯齿
        userEt.addTextChangedListener(we);
        verCodeEt.addTextChangedListener(we);
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                findBtn.setEnabled(false);
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
                        phoneEt.setText(sb.toString());
                        phoneEt.setSelection(index);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(phoneEt.getText()) && !TextUtils.isEmpty(userEt.getText()) && !TextUtils.isEmpty(verCodeEt.getText())) {
                    findBtn.setEnabled(true);
                } else {
                    findBtn.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        //重置验证码
        ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
    }

    private TextWatcher we = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            findBtn.setEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(userEt.getText()) && !TextUtils.isEmpty(phoneEt.getText()) && !TextUtils.isEmpty(verCodeEt.getText())) {
                findBtn.setEnabled(true);
            } else {
                findBtn.setEnabled(false);
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_btn:
                String user = userEt.getText().toString().trim();
                String phone = phoneEt.getText().toString().trim();
                String verCode = verCodeEt.getText().toString().trim();
                String code = FourFiguresNumberCode.getInstance().getCode();
                findPwd(user, phone, verCode, code);
                break;
            case R.id.find_tv_login:
                //tvBack.setTextColor(getResources().getColor(R.color.red));
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.find_iv_code:
                //重置验证码
                ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                break;
        }
    }

    private void findPwd(String user, String phone, String verCode, String code) {
        if (TextUtils.isEmpty(user)) {
            //showToast("用户名不能空哦！");
            MyToast.showText(FindPwdActivity.this,"用户名不能空哦！");
            return;
        } else if (TextUtils.isEmpty(phone)) {
            //showToast("手机号不能空哦！");
            MyToast.showText(FindPwdActivity.this,"手机号不能空哦！");
            return;
        } else if (!checkUsername(user)) {
            //showToast("用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            MyToast.showText(FindPwdActivity.this,"用户名输入格式不正确！用户名只限大小写字母，且长度为6~12位！");
            return;
        } else if (!checkPhone(phone)) {
            //showToast("手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
            MyToast.showText(FindPwdActivity.this,"手机号输入格式不正确！手机号必须由1开头，且长度为11位！");
            return;
        }else if (verCode.isEmpty()){
//            showToast("验证码不能为空！");
            MyToast.showText(FindPwdActivity.this,"验证码不能为空！");
            return;
        }else if (!verCode.equals(code)){
            //重置验证码
            ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
            //showToast("验证码有误！");
            MyToast.showText(FindPwdActivity.this,"验证码错误！",false);
            return;
        }
        DiyProgressDialog mDialog = new DiyProgressDialog(FindPwdActivity.this, "正在加载中...");
        mDialog.setCancelable(false);//设置不能通过后退键取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        findBtn.setEnabled(false);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", user);
        formbody.add("phone", phone);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + "/user/find/pwd")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                FindPwdActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDiyDialog(FindPwdActivity.this,"找回失败！服务器连接超时！");
                        if (!TextUtils.isEmpty(userEt.getText()) && !TextUtils.isEmpty(phoneEt.getText())) {
                                ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                findBtn.setEnabled(true);
                            } else {
                                findBtn.setEnabled(false);
                            }
                        mDialog.dismiss();
                    }
                });
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
                                    if (result.equals("success")) {
                                        MyToast.showText(FindPwdActivity.this,"找回失败！你输入的手机号有误！",false);
                                        ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                        mDialog.cancel();
                                        findBtn.setEnabled(true);
                                        return;
                                    } else if (result.equals("su_error")) {
                                        showDiyDialog(FindPwdActivity.this,"此账号不存在！");
                                        ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                        mDialog.cancel();
                                        findBtn.setEnabled(true);
                                        return;
                                    } else {
                                        List<User> dataEntity = new Gson().fromJson(result, new TypeToken<List<User>>() {
                                        }.getType());
                                        List<User> data = new ArrayList<>();
                                        data = dataEntity;
                                        if (data.size() > 0 && data != null) {
                                            Intent intent = new Intent(FindPwdActivity.this, ConfigPwdActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("findUserName", data.get(0).getUserName());
                                            intent.putExtra("findUserPhone", data.get(0).getMobile());
                                            startActivity(intent);
                                            //finish();
                                            mDialog.cancel();
                                            findBtn.setEnabled(true);
                                            finish();
                                            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                                        } else {
                                            showDiyDialog(FindPwdActivity.this,"密码找回失败！服务器连接超时！");
                                            mDialog.cancel();
                                            findBtn.setEnabled(true);
                                            ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
                                            return;
                                        }
                                    }
                                }
                            }, 1000);
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
        //userEt.setText("");
        //phoneEt.setText("");
        //tvBack.setTextColor(getResources().getColor(R.color.grey));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ivCode.setImageBitmap(FourFiguresNumberCode.getInstance().createBitmap());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}