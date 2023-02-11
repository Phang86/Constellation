package com.yyzy.constellation.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaeger.library.StatusBarUtil;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.api.OnClickThree;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.OSUtils;
import com.yyzy.constellation.utils.StringUtils;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private CardView cvCodeAddress,cvBlogAddress,cvCopyEmail,cvVersion;
    private TextView tvEmail,tvVersion,tvCheckVersion;
    private ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        initView();
        initData();
    }

    private void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.about_blue), 0);
        toolbar = findViewById(R.id.toolbar_about);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bar = findViewById(R.id.progressBar);
        cvBlogAddress = findViewById(R.id.about_blog_address);
        cvBlogAddress.setOnClickListener(this);
        cvCodeAddress = findViewById(R.id.about_code_address);
        cvCodeAddress.setOnClickListener(this);
        cvCopyEmail = findViewById(R.id.about_email_address);
        cvCopyEmail.setOnClickListener(this);
        cvVersion = findViewById(R.id.about_version);
        cvVersion.setOnClickListener(this);
        tvEmail = findViewById(R.id.about_tv_email);
        tvVersion = findViewById(R.id.about_tv_version);
        tvCheckVersion = findViewById(R.id.about_tv_check_version);
        tvVersion.setText("版本 "+StringUtils.getVersion(this));
        tvCheckVersion.setText("已是最新版本");
        tvEmail.setText(getResources().getString(R.string.Myemail));
        tvEmail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvEmail.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_blog_address:
                startUrl(StringUtils.BLOG_URL);
                break;
            case R.id.about_code_address:
                openBottomDialog("源码地址", "GitHub源码", "GitLab源码", "Gitee源码",true, new OnClickThree() {
                    @Override
                    public void one() {
                        startUrl(StringUtils.GITHUB_CODE_URL);
                    }

                    @Override
                    public void two() {
                        startUrl(StringUtils.GITLAB_CODE_URL);
                    }

                    @Override
                    public void three() {
                        startUrl(StringUtils.GITEE_CODE_URL);
                    }
                });
                break;
            case R.id.about_email_address:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvEmail.getText().toString());
                if (!TextUtils.isEmpty(tvEmail.getText())){
                    MyToast.showText(this,"邮箱复制成功",Gravity.BOTTOM);
                    return;
                }
                MyToast.showText(this,"邮箱复制失败",Gravity.BOTTOM);
                break;
            case R.id.about_version:
                isShowOrHideBar(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isShowOrHideBar(false);
                                tvCheckVersion.setText("已是最新版本");
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    /**
     * 跳转URL
     *
     * @param url 地址
     */
    private void startUrl(String url) {
        if (url != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            MyToast.showText(this, "未找到相关地址");
        }
    }


    private void isShowOrHideBar(boolean flag){
        if (flag) {
            tvCheckVersion.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
        }else{
            tvCheckVersion.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
        }
    }

    public void openBottomDialog(String title, String one, String two,String three, boolean flag, OnClickThree onClick) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomView = getLayoutInflater().inflate(R.layout.dialog_select_photo, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);
        TextView tvTitle = bottomView.findViewById(R.id.tv_title);
        TextView tvThree = bottomView.findViewById(R.id.tv_open_baomi);
        View view = bottomView.findViewById(R.id.bottom_view);
        CardView cv = bottomView.findViewById(R.id.bottom_cv);
        tvTitle.setText(title);
        tvTakePictures.setText(one);
        tvOpenAlbum.setText(two);
        tvThree.setText(three);
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
}