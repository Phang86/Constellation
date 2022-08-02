package com.yyzy.constellation.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.textclassifier.TextLinks;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseActivity extends AppCompatActivity implements Callback.CacheCallback<String>{
    public Context context;

    public void loadDatas(String url){
        RequestParams params = new RequestParams(url);
        x.http().get(params,this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        context = this;
        initView();
        initData();
    }

    //初始化布局文件
    protected abstract int initLayout();

    //初始化控件
    protected abstract void initView();

    //初始化数据
    protected abstract void initData();

    public void intentJump(Class cla) {
        startActivity(new Intent(this,cla));
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        //获取系统换肤权限，取消系统默认的皮肤
        return SkinAppCompatDelegateImpl.get(this, this);
    }

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
    public static boolean checkPhone(String phone){
        String regexp = "[1][3456789]\\d{1}\\s\\d{4}\\s\\d{4}";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(phone);
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
}
