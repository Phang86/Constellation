package com.yyzy.constellation.user.alertPwd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.entity.RandomImgBean;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.SwipeCaptchaView;
import com.yyzy.constellation.utils.URLContent;

import java.util.Arrays;
import java.util.List;

public class SwipeCheckActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack;
    private TextView tvTitle;
    private ImageView imgRefresh;


    private static final List<String> URLS = Arrays.asList("http://juheimg.oss-cn-hangzhou.aliyuncs.com/toh/201108/3/6717173923.jpg");

    private SwipeCaptchaView mSwipeCaptchaView;
    private SeekBar mSeekBar;

    private Animation animation;
    private String imageUrl;
    private RandomImgBean.DataDTO dto;

    @Override
    protected int initLayout() {
        return R.layout.activity_swipe_check;
    }

    @Override
    protected void initView() {
        loadDatas(URLContent.RANDOM_IMG_URL);

        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        mSwipeCaptchaView = findViewById(R.id.swipeCaptchaView);
        mSeekBar = findViewById(R.id.dragBar);
        imgRefresh = findViewById(R.id.swipe_refresh);
        imgBack.setOnClickListener(this);
        imgRefresh.setOnClickListener(this);
        tvTitle.setText("图片验证");

        //创建旋转动画
        animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        ;
        animation.setDuration(500);
        animation.setRepeatCount(1);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        RandomImgBean imgBean = new Gson().fromJson(result, RandomImgBean.class);
        if (imgBean.getData() != null) {
            imageUrl = imgBean.getData().get((int) (Math.random() * imgBean.getData().size())).getImageUrl();
            //加载图片
            showNetImg(imageUrl);
        }
    }

    @Override
    protected void initData() {
        mSwipeCaptchaView.setCurrentSwipeValue(0);
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                mSeekBar.setEnabled(false);
                setResult(RESULT_OK);
                //隐藏刷新图片功能
                imgRefresh.setVisibility(View.INVISIBLE);
                MyToast.showText(SwipeCheckActivity.this, "验证成功！", true);
                //跳转到密码修改页面
                Intent intent = new Intent();
                intent.setClass(SwipeCheckActivity.this, UpdatePwdActivity.class);
                String userName = getIntent().getStringExtra("userName");
                intent.putExtra("userName", userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                MyToast.showText(SwipeCheckActivity.this, "验证失败！",false);
                refreshImg();
                mSeekBar.setProgress(0);
                swipeCaptchaView.resetCaptcha();
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSwipeCaptchaView.matchCaptcha();
            }
        });
        //这里很可惜，使用图片缓存的话会有问题
        //Log.e("SwipeCaptchaView", " System: density:" + getResources().getDisplayMetrics().densityDpi);

    }

    private void showNetImg(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            imgUrl = "https://api.dujin.org/bing/1920.php";
        }
        Glide.with(SwipeCheckActivity.this)
                .asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model,
                                                Target<Bitmap> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                        return true;
                    }

                })
                .into(mSwipeCaptchaView);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.swipe_refresh:
                imgRefresh.startAnimation(animation);//开始动画
                refreshImg();
                break;
        }
    }

    private void refreshImg(){
        loadDatas(URLContent.RANDOM_IMG_URL);
        showNetImg(imageUrl);
        mSeekBar.setProgress(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除动画
        imgRefresh.clearAnimation();
    }
}