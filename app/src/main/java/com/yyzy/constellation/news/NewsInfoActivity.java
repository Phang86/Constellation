package com.yyzy.constellation.news;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.news.util.NetJavaScriptInterface;
import com.yyzy.constellation.utils.DiyProgressDialog;

public class NewsInfoActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private String url;
    private DiyProgressDialog mDialog;
    private String title;
    private TextView tvTitle;
    private ImageView imgBack;

    @Override
    protected int initLayout() {
        return R.layout.activity_news_info;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.news_info_tv);
        webView = findViewById(R.id.news_info_webView);
        imgBack = findViewById(R.id.news_info_img);
        imgBack.setOnClickListener(this);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        Log.e("TAG", "webViewUrl: "+url);
        tvTitle.setText(title);
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
        //使页面具有缩放功能，在缩放的同时显示缩放按钮
        webSet.setBuiltInZoomControls(true);
        //多方功能开始时，隐藏缩放按钮（按钮看起来很丑）
        webSet.setDisplayZoomControls(false);
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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //监听webView已经将网页加载完成；给网络图片添加点击事件
                addImgClickListener(view);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //加载完成
                if (newProgress == 100){
                    closeDialog(newProgress);
                //正在加载中
                }else{
                    loadProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.addJavascriptInterface(new NetJavaScriptInterface(this),"listener");
    }

    //给网页中的图片添加点击事件
    private void addImgClickListener(WebView view) {
        view.loadUrl("javascript:(function(){" +
                "            var objs = document.getElementsByTagName(\"img\");" +
                "            var arr = [];" +
                "            for(var i = 1; i < objs.length; i++){" +
                "                arr[i] = objs[i].getAttribute('src');" +
                "            }" +
                "            for(var i = 0; i < objs.length; i++){" +
                "                objs[i].onclick = function(){" +
                "                    window.listener.openImage(arr,this.getAttribute('src')" +
                "                }" +
                "" +
                "            }" +
                "        })()");
    }


    public void loadProgress(int newProgress){
        if (mDialog == null) {
            mDialog = new DiyProgressDialog(NewsInfoActivity.this, "加载中...");
            mDialog.setCancelable(true);//设置能通过后退键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }else{
            mDialog.dismiss();
        }
    }

    private void closeDialog(int newProgress) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.canGoBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        //避免内存泄露
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}