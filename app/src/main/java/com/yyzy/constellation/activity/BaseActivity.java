package com.yyzy.constellation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.textclassifier.TextLinks;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
}
