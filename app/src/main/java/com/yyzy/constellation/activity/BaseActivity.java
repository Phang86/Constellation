package com.yyzy.constellation.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.MyToast;

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
        MyToast.showText(this, msg, Toast.LENGTH_SHORT);
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
        //^[1][3,4,5,6,7,8,9][0-9]{9}$
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

    protected static void showDiyDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        title.setText("温馨提示");
        content.setText(msg);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
