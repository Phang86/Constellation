package com.yyzy.constellation.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;

public class NewsInfoActivity extends BaseActivity {

    private WebView webView;
    private String url;

    @Override
    protected int initLayout() {
        return R.layout.activity_news_info;
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.news_info_webView);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }

    @Override
    protected void initData() {
        WebSettings webSet = webView.getSettings();
        //设置页面支持js交互
        webSet.setJavaScriptEnabled(true);
        //将图片调整到适合webView的大小
        webSet.setUseWideViewPort(true);
        //支持等比例缩放，保证不同型号手机以及不同屏幕分辨率展示的效果达到最佳
        webSet.setLoadWithOverviewMode(true);
        //设置webView的缓存模式
        webSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可以访问文件
        webSet.setAllowFileAccess(true);
        //支持js打开新窗口
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置自动加载图片
        webSet.setLoadsImagesAutomatically(true);
        //设置默认编码格式
        webSet.setDefaultTextEncodingName("utf-8");
        //配置完基本信息，加载网址
        webView.loadUrl(url);
        //一般情况下手机会使用默认浏览器打开网址，为了防止此操作，设置以下：
        //直接加载网址，省略默认浏览器.....
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.canGoBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}