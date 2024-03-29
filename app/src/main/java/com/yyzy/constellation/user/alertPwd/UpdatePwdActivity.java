package com.yyzy.constellation.user.alertPwd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.user.LoginActivity;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.utils.ViewUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {

    TextView pwdTv;
    ImageView backImg;
    EditText oldPwdEt, newPwdEt, configNewPwdEt;
    TextView updateBtn;
    private String oldPwd;
    private String newPwd;
    private String configNewPwd;

    @Override
    protected int initLayout() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void initView() {
        pwdTv = findViewById(R.id.details_title);
        backImg = findViewById(R.id.details_back);
        updateBtn = findViewById(R.id.update_btn);
        oldPwdEt = findViewById(R.id.old_pwd);
        newPwdEt = findViewById(R.id.new_pwd);
        configNewPwdEt = findViewById(R.id.new_pwd_config);
        oldPwdEt.addTextChangedListener(textWatcher);
        newPwdEt.addTextChangedListener(textWatcher);
        configNewPwdEt.addTextChangedListener(textWatcher);
        updateBtn.setOnClickListener(this);

        updateBtn.setEnabled(false);
        backImg.setOnClickListener(this);
        pwdTv.setText("修改密码");
    }

    @Override
    protected void initData() {

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            updateBtn.setEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(oldPwdEt.getText()) && !TextUtils.isEmpty(configNewPwdEt.getText()) && !TextUtils.isEmpty(newPwdEt.getText())) {
                updateBtn.setEnabled(true);
            } else {
                updateBtn.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.update_btn:
                oldPwd = oldPwdEt.getText().toString().trim();
                newPwd = newPwdEt.getText().toString().trim();
                configNewPwd = configNewPwdEt.getText().toString().trim();
//                String user = userEt.getText().toString().trim();
                updatePwd(oldPwd, newPwd, configNewPwd);
                ViewUtil.hideOneInputMethod(UpdatePwdActivity.this, configNewPwdEt);
                break;
        }
    }

    private void updatePwd(String oldPwd, String newPwd, String configNewPwd) {
        if (TextUtils.isEmpty(oldPwd)) {
            MyToast.showText(this, "必填信息不能空哦！");
            return;
        }
        if (TextUtils.isEmpty(base_user_names)) {
            MyToast.showText(this, "默认项为空！");
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            MyToast.showText(this, "必填信息不能空哦！");
            return;
        }
        if (TextUtils.isEmpty(configNewPwd)) {
            MyToast.showText(this, "必填信息不能空哦！");
            return;
        }
        if (oldPwd.equals(newPwd)) {
            MyToast.showText(this, "原密码与新密码不能一致！", false);
            return;
        }
        if (oldPwd.equals(configNewPwd)) {
            MyToast.showText(this, "原密码与新密码不能一致！", false);
            return;
        }
        if (!newPwd.equals(configNewPwd)) {
            MyToast.showText(this, "两次输入的密码不一致！", false);
            return;
        }
        if (!checkPassword(oldPwd)) {
            MyToast.showText(this, "原密码输入的格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }
        if (!checkPassword(newPwd)) {
            MyToast.showText(this, "新密码输入的格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }
        if (!checkPassword(configNewPwd)) {
            MyToast.showText(this, "新密码输入的格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }
        if (newPwd.equals(configNewPwd)) {
            loading();
            updateBtn.setEnabled(false);
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder formbody = new FormBody.Builder();
            formbody.add("user", base_user_names);
            formbody.add("pwd", oldPwd);
            RequestBody requestBody = formbody.build();
            Request request = new Request.Builder()
                    .url(URLContent.BASE_URL + "/user/login")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopLoading();
                            MyToast.showText(UpdatePwdActivity.this, "修改失败！服务器连接超时！", false);
                            if (!TextUtils.isEmpty(oldPwdEt.getText()) && !TextUtils.isEmpty(configNewPwdEt.getText()) && !TextUtils.isEmpty(newPwdEt.getText())) {
                                updateBtn.setEnabled(true);
                            } else {
                                updateBtn.setEnabled(false);
                            }
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
                                        try {
                                            stopLoading();
                                            if (result.equals("success")) {
                                                MyToast.showText(UpdatePwdActivity.this, "原始密码不正确！");
                                                oldPwdEt.setText("");
                                                oldPwdEt.requestFocus();
                                                oldPwdEt.setHint("请重新输入原始密码");
//                                                updateBtn.setEnabled(false);
                                                return;
                                            }
                                            List<User> dataEntity = new Gson().fromJson(result, new TypeToken<List<User>>() {
                                            }.getType());
                                            if (dataEntity.size() > 0 && dataEntity != null) {
                                                loading();
                                                updateRquestNet();
                                                return;
                                            }
                                            MyToast.showText(UpdatePwdActivity.this, "修改失败！服务器连接超时！");
                                            updateBtn.setEnabled(true);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 300);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

    }

    private void updateRquestNet() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", base_user_names);
        formbody.add("pwd", newPwd);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + "/user/update")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UpdatePwdActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        MyToast.showText(UpdatePwdActivity.this, "修改失败！服务器连接超时！", false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        if (result.equals("success")) {
                            MyToast.showText(UpdatePwdActivity.this, "密码修改成功！", true);
                            oldPwdEt.setText("");
                            newPwdEt.setText("");
                            configNewPwdEt.setText("");
                            clearUserInfo();
                            clearNotificationManger();
                            showDiyDialog();
                            return;
                        }
                        if (result.equals("error")) {
                            MyToast.showText(UpdatePwdActivity.this, "修改失败！", false);
                        }
                    }
                });
            }
        });
    }

    private void showDiyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePwdActivity.this);
        LayoutInflater inflater = LayoutInflater.from(UpdatePwdActivity.this);
        View view = inflater.inflate(R.layout.diy_alert_dialog_sure, null);
        TextView content = (TextView) view.findViewById(R.id.dialog_two_content);
        Button btn_sure = (Button) view.findViewById(R.id.dialog_two_btn_sure);
        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        //获取窗口对象
        Window window = dialog.getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
        wlp.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        content.setText("用户身份信息已过期！请前往重新登录。");
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sure.setBackgroundColor(getResources().getColor(R.color.gray_600));
                btn_sure.setBackgroundColor(getResources().getColor(R.color.white));
                Intent intent = new Intent(UpdatePwdActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                dialog.cancel();
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }
}