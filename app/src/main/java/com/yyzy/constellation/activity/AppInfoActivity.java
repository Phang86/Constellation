package com.yyzy.constellation.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.receiver.IntentReceiver;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.FourFiguresNumberCode;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.SPUtils;
import com.yyzy.constellation.utils.SwipeCaptchaView;
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

import static com.yyzy.constellation.utils.URLContent.BASE_URL;

public class AppInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView titleTv,userNameTv,updateTimeTv,versionTv,phoneTv,createTimeTv;
    private RelativeLayout versionLayout,updatePwdLayout,cancelUserLayout,userPhoneLayout;
    private ImageView backImg;
    private String name,version;
    private SmartRefreshLayout swipeRefresh;
    private String passWord;
    private List<User> data;
    private NotificationManager NotiManager;

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

//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        this.registerReceiver(receiver,filter);
    }

    @Override
    protected void initData() {
        titleTv.setText(R.string.appInfo);
        name = findByKey("name");
        //userNameTv.setText(name);
        //createTimeTv.setText(findByKey("createTime"));
        passWord = findByKey("passWord");
        //获取应用版本号
        version = getVersion();

        refreshData();
    }

    protected String findByKey(String key) {
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.appInfo_layout_version:
                MeFragment.showDialogSure(this,"应用版本","当前应用版本为\t"+getVersion());
                break;
            case R.id.appInfo_layout_update:
                intent.setClass(this, SwipeCheckActivity.class);
                intent.putExtra("userName",name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.appInfo_layout_cancel:
                //showAlertDialog();
                intent.setClass(this, CancelActivity.class);
                intent.putExtra("MyPhone",data.get(0).getMobile());
                intent.putExtra("name",name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.appInfo_layout_phone:
                showDefaultDialog(intent,this);
                break;
        }
    }

    private void showDefaultDialog(Intent intent,Context context) {
        AlertDialogUtils dialogUtils = AlertDialogUtils.getInstance();
        AlertDialogUtils.showConfirmDialog(context,"友情提示","是否更换手机号？","是","否");
        dialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(AlertDialog dialog) {
                intent.setClass(context, UpdatePhoneActivity.class);
                intent.putExtra("updatePhone",data.get(0).getMobile());
                intent.putExtra("userName",data.get(0).getUserName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void onNegativeButtonClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }


    private void refreshData(){
        //下拉刷新
        swipeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //Toast.makeText(MainActivity.this, "顶部", Toast.LENGTH_SHORT).show();
                loadData();
                refreshLayout.finishRefresh(200);
            }
        });

    }

    private void loadData() {
        DiyProgressDialog mDialog = new DiyProgressDialog(AppInfoActivity.this, "加载中...");
        try {
            mDialog.setCancelable(false);//设置能通过后退键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }


        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("user", name);
        formbody.add("pwd", passWord);
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
                        MyToast.showText(AppInfoActivity.this,"加载失败！服务器连接超时！",false);
                        cancelUserLayout.setEnabled(false);
                        userPhoneLayout.setEnabled(false);
                        mDialog.cancel();
                        swipeRefresh.setEnableRefresh(false);
                        return;
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
                            //获取存储在sp里面的用户名和密码以及两个复选框状态
                            SharedPreferences sharedPreferences = getSharedPreferences("busApp", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            if (resultStr.equals("su_error")) {
                                if (ed != null && editor != null){
                                    ed.clear();
                                    ed.commit();
                                    editor.clear();
                                    editor.commit();
                                }
                                SPUtils.remove("imageUrl",AppInfoActivity.this);
                                NotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotiManager.cancel(1);
                                showSureDialog(AppInfoActivity.this,"此用户不存在！");
                                //MyToast.showText(getBaseContext(),"此用户不存在！");
                                cancelUserLayout.setEnabled(false);
                                userPhoneLayout.setEnabled(false);
                                updatePwdLayout.setEnabled(false);
                                versionLayout.setEnabled(false);
                                mDialog.cancel();
                                swipeRefresh.setEnableRefresh(false);
                                return;
                            }else if (resultStr.equals("success")){
                                if (ed != null && editor != null){
                                    ed.clear();
                                    ed.commit();
                                    editor.clear();
                                    editor.commit();
                                }
                                SPUtils.remove("imageUrl",AppInfoActivity.this);
                                NotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotiManager.cancel(1);
                                showSureDialog(AppInfoActivity.this,"登录密码错误！身份已失效，请前往重新登录！");
                                cancelUserLayout.setEnabled(false);
                                userPhoneLayout.setEnabled(false);
                                updatePwdLayout.setEnabled(false);
                                versionLayout.setEnabled(false);
                                mDialog.cancel();
                                swipeRefresh.setEnableRefresh(false);
                                return;
                            }else {
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
                                    Log.e("TAG", "run: ");
                                    String mobile = data.get(0).getMobile();
                                    String replacePhone = mobile.replace(" ", "");
                                    //进行加密
                                    String myPhone = replacePhone.substring(0,3)+"****"+replacePhone.substring(7,replacePhone.length());
                                    phoneTv.setText(myPhone);
                                    cancelUserLayout.setEnabled(true);
                                    userPhoneLayout.setEnabled(true);
                                    updatePwdLayout.setEnabled(true);
                                    versionLayout.setEnabled(true);
                                    swipeRefresh.setEnableRefresh(true);
                                    mDialog.cancel();
                                } else {
                                    if (ed != null && editor != null){
                                        ed.clear();
                                        ed.commit();
                                        editor.clear();
                                        editor.commit();
                                    }
                                    SPUtils.remove("imageUrl",AppInfoActivity.this);
                                    NotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    NotiManager.cancel(1);
                                    showSureDialog(AppInfoActivity.this,"登录失败！服务器连接超时！");
                                    mDialog.cancel();
                                    swipeRefresh.setEnableRefresh(false);
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
    protected void onStart() {
        super.onStart();
        //swipeRefresh.setRefreshing(true);
        loadData();
        //swipeRefresh.setRefreshing(false);
    }


    private void showSureDialog(Context context, String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
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
        title.setText("温馨提示");
        content.setText(msg);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AppInfoActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

}