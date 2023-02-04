package com.yyzy.constellation.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.autofill.OnClickAction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.yyzy.constellation.R;
import com.yyzy.constellation.api.OnClickThree;
import com.yyzy.constellation.entity.IpBean;
import com.yyzy.constellation.receiver.IntentReceiver;
import com.yyzy.constellation.tally.util.OnClickSure;
import com.yyzy.constellation.tally.util.TallyMoneyDialog;
import com.yyzy.constellation.user.AppInfoActivity;
import com.yyzy.constellation.user.CallBackOkhttp;
import com.yyzy.constellation.user.LoginActivity;
import com.yyzy.constellation.user.RegisterActivity;
import com.yyzy.constellation.utils.DeviceUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.Mydialog;
import com.yyzy.constellation.utils.SPUtils;
import com.yyzy.constellation.utils.StatusBarUtils;
import com.yyzy.constellation.utils.URLContent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.net.wifi.WifiConfiguration.Status.ENABLED;

public abstract class BaseActivity extends AppCompatActivity implements Callback.CacheCallback<String> {
    public static Context context;
    public DiyProgressDialog mDialogBase;
    public SharedPreferences base_app;
    public String base_user_names;
    public String base_pass_words;
    public String base_create_times;
    public String base_phones;
    public String base_ip;
    public String base_ipaddress;
    public String base_ipCity;
    public String base_signature,base_imgheader;
    public Integer base_sex;
    public NotificationManager notificationManager;
    public boolean base_isShowGirdOrList, base_login_info;

    public void loadDatas(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        context = this;
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(IntentReceiver.getInstance(), filter);
        getUserInfo();
        mDialogBase = new DiyProgressDialog(context);
        initView();
        initData();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.state_bg), 0);
//
    }

    protected void loading() {
        if (mDialogBase != null) {
            mDialogBase.setCancelable(false);
            mDialogBase.setCanceledOnTouchOutside(false);
            mDialogBase.show();
        }
    }

    //初始化布局文件
    protected abstract int initLayout();

    //初始化控件
    protected abstract void initView();

    //初始化数据
    protected abstract void initData();


    //获取用户信息
    protected void getUserInfo() {
        base_app = getSharedPreferences("constellation", MODE_PRIVATE);
        base_user_names = base_app.getString("name", "");
        base_pass_words = base_app.getString("passWord", "");
        base_create_times = base_app.getString("createTime", "");
        base_phones = base_app.getString("phone", "");
        base_ip = base_app.getString("ip", "");
        base_ipaddress = base_app.getString("ipaddress", "");
        base_ipCity = base_app.getString("ipCity", "");
        base_isShowGirdOrList = base_app.getBoolean("isShowGirdOrList", true);
        base_login_info = base_app.getBoolean("login_info", true);
        base_sex = base_app.getInt("sex", 2);
        base_signature = base_app.getString("signature","");
        base_imgheader = base_app.getString("imgheader","");
        Log.e("TAG", "BaseActivity_onCreate: " + base_user_names + base_pass_words + base_create_times + base_phones + base_ipaddress + base_sex+base_signature);
    }

    //删除用户信息
    protected void clearUserInfo() {
        //获取存储在sp里面的用户名和密码以及两个复选框状态
        SharedPreferences spf = getSharedPreferences("constellation", MODE_PRIVATE);
        SharedPreferences.Editor ed = spf.edit();
        if (ed != null) {
            ed.clear();
            ed.commit();
        }
    }

    //修改存在spf中的键值；String
    protected void updateSpfUserInfo(String key, String newPhone) {
        SharedPreferences spf = getSharedPreferences("constellation", MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, newPhone);
        edit.commit();
    }

    //修改存在spf中的键值；boolean
    protected void updateSpfUserInfo(String key, boolean value) {
        SharedPreferences spf = getSharedPreferences("constellation", MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    //修改存在spf中的键值；int
    protected void updateSpfUserInfo(String key, int value) {
        SharedPreferences spf = getSharedPreferences("constellation", MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    protected String findByKey(String key) {
        SharedPreferences sp = getSharedPreferences("constellation", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    //停止加载
    protected void stopLoading() {
        if (mDialogBase != null && mDialogBase.isShowing()) {
            mDialogBase.dismiss();
        }
    }

    //获取当前设备的型号  例：HUAWEI YAL-10
    protected String getDeviceModel() {
        String phoneModel = DeviceUtils.getPhoneModel();
        String deviceManufacturer = DeviceUtils.getDeviceManufacturer();
        return deviceManufacturer + "-" + phoneModel;
//        Log.e("TAG", "phoneModel: 当前手机型号是："+);
    }


    //取消消息横幅弹窗
    protected void clearNotificationManger() {
        SPUtils.remove("imageUrl", this);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    public void intentJump(Class cla) {
        startActivity(new Intent(this, cla));
    }

    public void showToast(String msg) {
        MyToast.showText(this, msg);
    }

//    @NonNull
//    @Override
//    public AppCompatDelegate getDelegate() {
//        //获取系统换肤权限，取消系统默认的皮肤
//        return SkinAppCompatDelegateImpl.get(this, this);
//    }

    @Override
    public boolean onCache(String result) {
        return false;
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(IntentReceiver.getInstance());
    }

    /*
     * 注册：严格使用正则表达式
     **/
    // 验证用户名是否匹配指定格式的方法
    public static boolean checkUsername(String user) {
        //用户名只能大小写字母，长度为6~12位。
        String regexp = "^[a-zA-Z]{6,12}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(user);
        return matcher.matches();
    }

    // 验证密码是否匹配指定格式的方法
    public static boolean checkPassword(String pwd) {
        //密码只能用大小写字母、数字组合，长度为8~16位。
        String regexp = "^[0-9a-zA-Z]{8,16}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }

    // 验证手机号码是否匹配
    /*"[1]"代表第1位为数字1，
     * "[3456789]"代表第二位可以为3、4、5、6、7、8、9中的一个
     * "\\d{9}"代表后面是可以是0～9的数字，有9位。
     */
    public static boolean checkPhone(String phone) {
        //^[1][3,4,5,6,7,8,9][0-9]{9}$
        String regexp = "[1][3456789]\\d{1}\\s\\d{4}\\s\\d{4}";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 判断是否是手机格式 非空格
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    // 验证是否为汉字
    public static boolean checkHanZi(String hanzi) {
        String regexp = "^[\u4e00-\u9fa5]*$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(hanzi);
        return matcher.matches();
    }

    public String getVersion() {
        //获取应用的版本名称
        PackageManager packageManager = getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public void showDiyDialog(String msg) {
        View view = getLayoutInflater().inflate(R.layout.diy_alert_dialog_sure, null);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        TextView content = (TextView) view.findViewById(R.id.dialog_two_content);
        Button btn_sure = (Button) view.findViewById(R.id.dialog_two_btn_sure);
        dialog.setView(view);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        content.setText(msg);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);//设置背景透明
            WindowManager.LayoutParams lp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            lp.width = d.getWidth();
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
        }

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void showNotification(Context context, String userName) {
        boolean isVibrate = true;//是否震动
        //1.获取消息服务
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //默认通道是default
        String channelId = "default";
        //2.如果是android8.0以上的系统，则新建一个消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "chat";
            /*
             通道优先级别：
             * IMPORTANCE_NONE 关闭通知
             * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
             * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
             * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
             * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
             */
            NotificationChannel channel = new NotificationChannel(channelId, "消息提醒", NotificationManager.IMPORTANCE_HIGH);
            //设置该通道的描述（可以不写）
            //channel.setDescription("重要消息，请不要关闭这个通知。");
            //是否绕过勿打扰模式
            channel.setBypassDnd(true);
            //是否允许呼吸灯闪烁
            channel.enableLights(true);
            //闪关灯的灯光颜色
            channel.setLightColor(Color.RED);
            //桌面launcher的消息角标
            channel.canShowBadge();
            //设置是否应在锁定屏幕上显示此频道的通知
            //channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            if (isVibrate) {
                //是否允许震动
                channel.enableVibration(true);
                //先震动1秒，然后停止0.5秒，再震动2秒则可设置数组为：new long[]{1000, 500, 2000}
                channel.setVibrationPattern(new long[]{1000, 500, 2000});
            } else {
                channel.enableVibration(false);
                channel.setVibrationPattern(new long[]{0});
            }
            //创建消息通道
            manager.createNotificationChannel(channel);
        }
        //3.实例化通知
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, channelId);
        //通知默认的声音 震动 呼吸灯
        nc.setDefaults(NotificationCompat.DEFAULT_ALL);
        //通知标题
        nc.setContentTitle("服务通知");
        //通知内容
        String sex = "先生/女士";
        getUserInfo();
        if (base_sex == 0) {
            sex = "先生";
        }
        if(base_sex == 1){
            sex = "女士";
        }
        nc.setContentText("尊敬的" + userName + sex + "，您已成功登录星梦缘！");
        //设置通知的小图标
        nc.setSmallIcon(R.mipmap.account);
        //设置通知的大图标
        nc.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_icon));
        //设定通知显示的时间
        nc.setWhen(System.currentTimeMillis());
        //设置通知的优先级
        nc.setPriority(NotificationCompat.PRIORITY_MAX);
        //设置点击通知之后通知是否消失
        nc.setAutoCancel(true);
        //点击通知打开软件
        Context application = context.getApplicationContext();
        Intent resultIntent = new Intent(application, MainActivity.class);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(application, 0, resultIntent, 0);
        nc.setContentIntent(pendingIntent);
        //4.创建通知，得到build
        Notification notification = nc.build();
        //5.发送通知
        manager.notify(1, notification);
    }

    public void openBottomDialog(String title, String one, String two, boolean flag, OnClickThree onClick) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomView = getLayoutInflater().inflate(R.layout.dialog_select_photo, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);
        TextView tvTitle = bottomView.findViewById(R.id.tv_title);
        View view = bottomView.findViewById(R.id.bottom_view);
        CardView cv = bottomView.findViewById(R.id.bottom_cv);
        tvTitle.setText(title);
        tvTakePictures.setText(one);
        tvOpenAlbum.setText(two);
        if (flag) {
            view.setVisibility(View.VISIBLE);
            cv.setVisibility(View.VISIBLE);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.three();
                    bottomSheetDialog.cancel();
                }
            });
        } else {
            view.setVisibility(View.GONE);
            cv.setVisibility(View.GONE);
        }
        tvTakePictures.setOnClickListener(v -> {
            onClick.one();
            bottomSheetDialog.cancel();
        });

        tvOpenAlbum.setOnClickListener(v -> {
            onClick.two();
            bottomSheetDialog.cancel();
        });
        //取消
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        bottomSheetDialog.show();
    }

    protected void requestOkhttp(String phone, String ip, String content, String url, CallBackOkhttp callback) {
        String deviceModel = getDeviceModel();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        if (TextUtils.isEmpty(phone)) {
            phone = base_phones;
        }
        if (TextUtils.isEmpty(ip)) {
            ip = base_ip;
        }
        Log.e("TAG", "requestOkhttp: " + base_user_names);
        formbody.add("user", base_user_names);
        formbody.add("mobile", phone);
        formbody.add("ipAddress", ip);
        formbody.add("deviceModel", deviceModel);
        formbody.add("feedBackContent", content);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        callback.onError(e);
                    }
                });
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String resultStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        callback.onSuccess(resultStr);
                    }
                });
            }
        });
    }

    protected void requestOkhttpLoadFeedback(String sex,String signature, String user, String username, String url, CallBackOkhttp callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add(user, username);
        formbody.add("sex", sex);
        formbody.add("signature",signature);
        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        callback.onError(e);
                    }
                });
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String resultStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        callback.onSuccess(resultStr);
                    }
                });
            }
        });
    }

    protected void requestImgOkhttp(String url, CallBackOkhttp callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
//        formbody.add("user", base_user_names);
//        RequestBody requestBody = formbody.build();
        Request request = new Request.Builder()
                .url(URLContent.BASE_URL + url)
                .addHeader("user",base_user_names)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        callback.onError(e);
                    }
                });
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String resultStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        callback.onSuccess(resultStr);
                    }
                });
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserInfo();
    }
}
