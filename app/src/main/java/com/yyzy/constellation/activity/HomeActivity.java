package com.yyzy.constellation.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yyzy.constellation.R;
import com.yyzy.constellation.receiver.IntentReceiver;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private TextView tv;
    private int time = 4;
    private SharedPreferences firstSpf,userNameSpf, spf;
    private String username;
    private String password;
    private boolean auto;
    private boolean remenber,isFirst;
    private RelativeLayout reBg;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET};

    private AlertDialog dialog;

    private static final List<String> URLS = Arrays.asList(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-090916%2Ferppivcyduverppivcyduv.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389041&t=3110820cc7477a9c7626021f4a127c2d"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-090811%2Fo2zgduf2fwjo2zgduf2fwj.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389221&t=3e1653bd2a673a03e863dbeb71c6e848"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091022%2Fmkdvml00qvemkdvml00qve.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389575&t=652337c33b8594fbe3575149aeb0dd89"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091121%2Fl34jh3xolcvl34jh3xolcv.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389607&t=04dc7cfd3898e602472bd7973b4a30d9"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091204%2Fhmjyps3tscnhmjyps3tscn.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389637&t=f326e31e0bebf2ceb97be3774d7b1165"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091106%2Fpa4nutbu2mkpa4nutbu2mk.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389650&t=782833b3aa738e84c3c91b80822a2cf1"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091401%2Fkc0gyhfl4itkc0gyhfl4it.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389707&t=ee9309df8b22f55056003a59cd246444"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091322%2Fnxgeou3z5s1nxgeou3z5s1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389963&t=9b15aa0b3403f12c2bf947ad10051cc8"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F1011%2F112G6002358%2F16112F02358-7.jpg&refer=http%3A%2F%2Fpic.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663390140&t=44e9621984c732826a5e97cf8d5ac32c");


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        //overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        reBg = findViewById(R.id.home_layout_relative);
        try {
            Glide.with(this).load(URLS.get((int) (Math.random() * URLS.size()))).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        reBg.setBackground(resource);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        tv = findViewById(R.id.home_tv);
        tv.setVisibility(View.GONE);
        tv.setEnabled(false);
        handler.sendEmptyMessageDelayed(1,1000);
        firstSpf = getSharedPreferences("first_spf",MODE_PRIVATE);
        userNameSpf = getSharedPreferences("busApp", MODE_PRIVATE);
        spf = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        username = userNameSpf.getString("username", "");
        password = userNameSpf.getString("password", "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int l = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            int m = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]);
            int n = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]);
            int a = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED || l != PackageManager.PERMISSION_GRANTED || m != PackageManager.PERMISSION_GRANTED ||
                    n != PackageManager.PERMISSION_GRANTED || a != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                startRequestPermission();
            }
        }
        //点击定时装置进行相应的逻辑判断    判断密码和账号是否注销
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //如账号、密码未注销，则直接跳到应用主界面
                isFirst = firstSpf.getBoolean("isFirst", true);
                if (isFirst){
                    intent.setClass(HomeActivity.this,WelcomeActivity.class);
                    //为了下一次不进行跳转引导界面，把状态设置为false
                    SharedPreferences.Editor edit = firstSpf.edit();
                    edit.putBoolean("isFirst",false);
                    edit.commit();
                    handler.removeCallbacksAndMessages(null);
                }else {
                    String name = spf.getString("name", "");
                    String phone = spf.getString("phone", "");
                    String createTime = spf.getString("createTime", "");
                    if (!name.isEmpty() || !phone.isEmpty() || !createTime.isEmpty()){
                        intent.setClass(HomeActivity.this, MainActivity.class);
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        //不是第一次进入  直接跳过引导页面进入
                        intent.setClass(HomeActivity.this, LoginActivity.class);
                        handler.removeCallbacksAndMessages(null);
                    }
                }
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }


    /**
     * 开始提交请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    // 以前是!b
                    if (b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else{
                        finish();
                    }
                } else {
                    //获取权限成功提示，可以不要
//                    Toast toast = Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.TOP, 0, 0);
//                    toast.show();
                }
            }
        }
    }

    /**
     * 提示用户去应用设置界面手动开启权限
     */
    private void showDialogTipUserGoToAppSettting() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许应用使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                }).setCancelable(false).show();
    }

    /**
     * 跳转到当前应用的设置界面
     */
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //权限管理
        if (requestCode == 123) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    //Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void initData() {

    }

     Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
                if (msg.what == 1) {
                    time--;
                    if (time == -1) {
                        //判断是否第一次进入
                        isFirst = firstSpf.getBoolean("isFirst", true);
                        Intent intent = new Intent();
                        if (isFirst){
                            intent.setClass(HomeActivity.this,WelcomeActivity.class);
                            //为了下一次不进行跳转引导界面，把状态设置为false
                            SharedPreferences.Editor edit = firstSpf.edit();
                            edit.putBoolean("isFirst",false);
                            edit.commit();
                        }else {
                            try {
                                String name = spf.getString("name", "");
                                String phone = spf.getString("phone", "");
                                String createTime = spf.getString("createTime", "");
//                            if (!username.trim().isEmpty() || !password.trim().isEmpty()) {
//                                intent.setClass(HomeActivity.this, MainActivity.class);
//                            }
                                if (!name.isEmpty() || !phone.isEmpty() || !createTime.isEmpty()){
                                    intent.setClass(HomeActivity.this, MainActivity.class);
                                }
                                else {
                                    //不是第一次进入  直接跳过引导页面进入
                                    intent.setClass(HomeActivity.this, LoginActivity.class);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }else {
                        tv.setEnabled(true);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("跳过\t"+String.valueOf(time));
                        handler.sendEmptyMessageDelayed(1,1000);
                    }
                }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyCode == KeyEvent.KEYCODE_HOME) {
            handler.removeCallbacksAndMessages(null);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}