package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {

    TextView pwdTv;
    ImageView backImg;
    EditText userEt,oldPwdEt,newPwdEt,configNewPwdEt;
    Button updateBtn;
    private String oldPwd;
    private String newPwd;
    private String configNewPwd;
    private String userName;

    @Override
    protected int initLayout() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void initView() {
        pwdTv = findViewById(R.id.details_title);
        backImg = findViewById(R.id.details_back);
        userEt = findViewById(R.id.update_user);
        updateBtn  = findViewById(R.id.update_btn);
        oldPwdEt  = findViewById(R.id.old_pwd);
        newPwdEt  = findViewById(R.id.new_pwd);
        configNewPwdEt  = findViewById(R.id.new_pwd_config);
        updateBtn.setOnClickListener(this);
        userEt.setEnabled(false);
        backImg.setOnClickListener(this);
        pwdTv.setText("修改密码");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userEt.setText(userName);
    }

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
                updatePwd(oldPwd,newPwd,configNewPwd);
                break;
        }
    }

    private void updatePwd(String oldPwd, String newPwd, String configNewPwd) {
        if (TextUtils.isEmpty(oldPwd)){
            showToast("必填信息不能空哦！");
            return;
        }else if (TextUtils.isEmpty(newPwd)){
            showToast("必填信息不能空哦！");
            return;
        }else if (TextUtils.isEmpty(configNewPwd)){
            showToast("必填信息不能空哦！");
            return;
        }else if(oldPwd.equals(newPwd)){
            showToast("原密码与新密码不能一致！");
            return;
        }else if(oldPwd.equals(configNewPwd)){
            showToast("原密码与新密码不能一致！");
            return;
        }else if (!newPwd.equals(configNewPwd)){
            showToast("两次输入的密码不一致！");
            return;
        }else if (!checkPassword(oldPwd)){
            showToast("原密码输入的格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }else if (!checkPassword(newPwd)){
            showToast("新密码输入的格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }else if (!checkPassword(configNewPwd)){
            showToast("新密码输入的格式不正确！密码只限大小写字母、数字组合，且长度为8~16位！");
            return;
        }else{
            if (newPwd.equals(configNewPwd)) {
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody.Builder formbody = new FormBody.Builder();
                formbody.add("user", userName);
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

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DiyProgressDialog mDialog = new DiyProgressDialog(UpdatePwdActivity.this,"正在加载中...");
                                    mDialog.setCancelable(false);//设置不能通过后退键取消
                                    mDialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (result.equals("error")){
                                                showToast("密码修改失败！原始密码不正确！");
                                                oldPwdEt.setText("");
                                                mDialog.cancel();
                                                return;
                                            }else{
                                                List<User> dataEntity = new Gson().fromJson(result, new TypeToken<List<User>>() {
                                                }.getType());
                                                if (dataEntity.size() > 0 && dataEntity != null) {
                                                    mDialog.cancel();
                                                    updateRquestNet();
                                                }else {
                                                    showToast("登录失败！服务器连接超时！");
                                                    return;
                                                }
                                            }
                                        }
                                    },2000);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private void updateRquestNet(){
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder formbody = new FormBody.Builder();
            formbody.add("user", userName);
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

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.equals("success")) {
                                showToast("密码修改成功！");
                                oldPwdEt.setText("");
                                newPwdEt.setText("");
                                configNewPwdEt.setText("");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDiyDialog();
                                    }
                                },1000);
                            } else if (result.equals("error")) {
                                showToast("密码修改失败！");
                            }
                        }
                    });
                }
            });
    }

    private void showDiyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.diy_alert_dialog_sure, null);
        TextView content = (TextView) view.findViewById(R.id.dialog_two_content);
        TextView title = (TextView) view.findViewById(R.id.dialog_two_title);
        Button btn_sure = (Button) view.findViewById(R.id.dialog_two_btn_sure);
        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        title.setText("温馨提示");
        content.setText("用户身份信息已过期！请前往重新登录。");
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(UpdatePwdActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                dialog.cancel();
                startActivity(intent);
            }
        });
    }
}