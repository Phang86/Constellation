package com.yyzy.constellation.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.user.logoutUser.CancelActivity;
import com.yyzy.constellation.user.alertPwd.SwipeCheckActivity;
import com.yyzy.constellation.user.alertPhone.UpdatePhoneActivity;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.MyToast;

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

public class AppInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView titleTv,userNameTv,updateTimeTv,versionTv,phoneTv,createTimeTv;
    private CardView versionLayout,updatePwdLayout,cancelUserLayout,userPhoneLayout;
    private ImageView backImg;
    private String version;
    private SmartRefreshLayout swipeRefresh;
    private List<User> data;

    @Override
    protected int initLayout() {
        return R.layout.activity_app_info;
    }

    @Override
    protected void initView() {
        titleTv = findViewById(R.id.details_title);
        backImg = findViewById(R.id.details_back);
        userNameTv = findViewById(R.id.appInfo_tv_user);
        updateTimeTv = findViewById(R.id.appInfo_tv_updateTime);
        versionTv = findViewById(R.id.appInfo_tv_version);
        phoneTv = findViewById(R.id.appInfo_tv_phone);
        versionLayout = findViewById(R.id.appInfo_layout_version);
        updatePwdLayout = findViewById(R.id.appInfo_layout_update);
        cancelUserLayout = findViewById(R.id.appInfo_layout_cancel);
        swipeRefresh = findViewById(R.id.appInfo_refresh);
        userPhoneLayout = findViewById(R.id.appInfo_layout_phone);
        createTimeTv = findViewById(R.id.appInfo_tv_createTime);
        cancelUserLayout.setOnClickListener(this);
        updatePwdLayout.setOnClickListener(this);
        versionLayout.setOnClickListener(this);
        backImg.setOnClickListener(this);
        userPhoneLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titleTv.setText(R.string.appInfo);
        //获取应用版本号
        version = getVersion();
        refreshData();
    }

    private void refreshData(){
        //下拉刷新
        swipeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
                refreshLayout.finishRefresh(100);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.appInfo_layout_version:
                MeFragment.showDialogSure(this,"当前应用版本为\t"+getVersion());
                break;
            case R.id.appInfo_layout_update:
                intentJump(SwipeCheckActivity.class);
                break;
            case R.id.appInfo_layout_cancel:
                intentJump(CancelActivity.class);
                break;
            case R.id.appInfo_layout_phone:
                showDefaultDialog();
                break;
        }
    }

    private void showDefaultDialog() {
        AlertDialogUtils dialogUtils = AlertDialogUtils.getInstance();
        AlertDialogUtils.showConfirmDialog(this,"是否更换手机号？","是","否");
        dialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(AlertDialog dialog) {
                intentJump(UpdatePhoneActivity.class);
                dialog.dismiss();
            }

            @Override
            public void onNegativeButtonClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }


    private void loadData() {
        loading();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", base_user_names);
        formbody.add("pwd", base_pass_words);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/user/login")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        MyToast.showText(AppInfoActivity.this,"加载失败！服务器连接超时！",false);
                        swipeRefresh.setEnableRefresh(false);
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
                            stopLoading();
                            if (resultStr.equals("su_error")) {
                                clearUserInfo();
                                clearNotificationManger();
                                showSureDialog(AppInfoActivity.this,"此用户不存在！");
                                return;
                            }
                            if (resultStr.equals("success")){
                                clearUserInfo();
                                clearNotificationManger();
                                showSureDialog(AppInfoActivity.this,"身份已失效，请前往重新登录！");
                                return;
                            }
                            List<User> dataEntity = new Gson().fromJson(resultStr, new TypeToken<List<User>>() {
                            }.getType());
                            data = new ArrayList<>();
                            data = dataEntity;
                            if (data.size() > 0 && data != null) {
                                //数据获取成功
                                versionTv.setText(version);
                                updateTimeTv.setText(data.get(0).getUpdateTime());
                                userNameTv.setText(data.get(0).getUserName());
                                createTimeTv.setText(data.get(0).getCreateTime());
                                String mobile = data.get(0).getMobile();
                                String replacePhone = mobile.replace(" ", "");
                                //进行加密
                                String myPhone = replacePhone.substring(0,3)+"****"+replacePhone.substring(7,replacePhone.length());
                                phoneTv.setText(myPhone);
                                swipeRefresh.setEnableRefresh(true);
                                return;
                            }
                            clearUserInfo();
                            clearNotificationManger();
                            showSureDialog(AppInfoActivity.this,"登录失败！服务器连接超时！");
                            swipeRefresh.setEnableRefresh(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }


    private void showSureDialog(Context context, String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
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
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        content.setText(msg);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AppInfoActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

}