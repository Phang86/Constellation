package com.yyzy.constellation.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.SPUtils;
import com.yyzy.constellation.utils.SwipeCaptchaView;
import com.yyzy.constellation.utils.URLContent;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView titleTv,userNameTv,createTimeTv,versionTv,phoneTv;
    private RelativeLayout versionLayout,updatePwdLayout,cancelUserLayout;
    private ImageView backImg;
    private String name;
    private NotificationManager manager;
    private SharedPreferences sharedPreferences,sp;
    private SharedPreferences.Editor editor,ed;

    @Override
    protected int initLayout() {
        return R.layout.activity_app_info;
    }

    @Override
    protected void initView() {
        titleTv = findViewById(R.id.details_title);
        backImg = findViewById(R.id.details_back);
        userNameTv = findViewById(R.id.appInfo_tv_user);
        createTimeTv = findViewById(R.id.appInfo_tv_createTime);
        versionTv = findViewById(R.id.appInfo_tv_version);
        phoneTv = findViewById(R.id.appInfo_tv_phone);
        versionLayout = findViewById(R.id.appInfo_layout_version);
        updatePwdLayout = findViewById(R.id.appInfo_layout_update);
        cancelUserLayout = findViewById(R.id.appInfo_layout_cancel);
        cancelUserLayout.setOnClickListener(this);
        updatePwdLayout.setOnClickListener(this);
        versionLayout.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titleTv.setText(R.string.appInfo);
        name = findByKey("name");
        userNameTv.setText(name);
        createTimeTv.setText(findByKey("createTime"));
        //获取应用版本号
        String version = getVersion();
        versionTv.setText(version);
        String phone = findByKey("phone");
        if (!phone.isEmpty()) {
            phoneTv.setText(findByKey("phone"));
        }else{
            phoneTv.setText("暂无！");
        }

    }

    protected String findByKey(String key) {
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.appInfo_layout_version:
                MeFragment.showDialogSure(this,"应用版本","当前应用版本为\t"+getVersion());
                break;
            case R.id.appInfo_layout_update:
                Intent intent = new Intent();
                intent.setClass(this, SwipeCheckActivity.class);
                intent.putExtra("userName",name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.appInfo_layout_cancel:
                showAlertDialog();
                break;
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.diy_alert_dialog_two, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //设置隐藏dialog默认的背景
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        content.setText("账号一旦彻底注销，数据无法恢复、且全部清空、且无法找回。确定注销此账号吗？");
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //获取存储在sp里面的用户名和密码以及两个复选框状态
                sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                SPUtils.remove("imageUrl",AppInfoActivity.this);
                //从后台获取到的用户信息
                sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
                ed = sp.edit();

                //清空所有
                //editor.clear();
                //editor.remove("username");
                //editor.remove("password");
                //提交
                //editor.commit();
                DiyProgressDialog loadDialog = new DiyProgressDialog(AppInfoActivity.this,"账号注销中...");
                loadDialog.setCancelable(false);//设置不能通过后退键取消
                loadDialog.setCanceledOnTouchOutside(false);
                loadDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestNet(loadDialog);
                    }
                },2500);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

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
                Looper.prepare();
                showToast("账号注销失败！服务器连接超时！");
                loadDialog.cancel();
                Looper.loop();
                return;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("success")) {
                            loadDialog.cancel();
                            Intent intent = new Intent(AppInfoActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            showToast("账号注销成功！");
                            editor.clear();
                            editor.commit();
                            ed.clear();
                            ed.commit();
                            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            manager.cancel(1);
                        }
                        if (result.equals("error")) {
                            loadDialog.cancel();
                            showToast("账号注销失败！");
                            return;
                        }
                    }
                });
            }
        });
    }
}