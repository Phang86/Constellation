package com.yyzy.constellation.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.yyzy.constellation.R;
import com.yyzy.constellation.receiver.IntentReceiver;
import com.yyzy.constellation.utils.FourFiguresNumberCode;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.SwipeCaptchaView;

import java.util.Arrays;
import java.util.List;

public class SwipeCheckActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack;
    private TextView tvTitle;
    private ImageView imgRefresh;


    //private static final List<String> URLS = Arrays.asList("http://juheimg.oss-cn-hangzhou.aliyuncs.com/toh/201108/3/6717173923.jpg");

    //随便给一个必应的吧
    private static final List<String> URLS = Arrays.asList(
            "https://api.dujin.org/bing/1920.php"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-090916%2Ferppivcyduverppivcyduv.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389041&t=3110820cc7477a9c7626021f4a127c2d"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-090811%2Fo2zgduf2fwjo2zgduf2fwj.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389221&t=3e1653bd2a673a03e863dbeb71c6e848"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091404%2Fodsnxo5lixxodsnxo5lixx.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389247&t=6ce59920275a01455e63d41e49575115"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091121%2F0u5s3hcmfne0u5s3hcmfne.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389294&t=5d247f87c78acdda4a2ac7646eb9a1e6"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091313%2F1x2j2ivbpaj1x2j2ivbpaj.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389310&t=c68737163b61ac7b68a2cd764f8b8de2"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091219%2Fks32cq0qk2vks32cq0qk2v.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389522&t=3dae8f220b2271134db4c4654e0f862a"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091022%2Fmkdvml00qvemkdvml00qve.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389575&t=652337c33b8594fbe3575149aeb0dd89"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091121%2Fl34jh3xolcvl34jh3xolcv.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389607&t=04dc7cfd3898e602472bd7973b4a30d9"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091204%2Fhmjyps3tscnhmjyps3tscn.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389637&t=f326e31e0bebf2ceb97be3774d7b1165"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091106%2Fpa4nutbu2mkpa4nutbu2mk.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389650&t=782833b3aa738e84c3c91b80822a2cf1"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091401%2Fkc0gyhfl4itkc0gyhfl4it.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389707&t=ee9309df8b22f55056003a59cd246444"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091123%2Fiet3f3p0jtoiet3f3p0jto.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389890&t=64497cbd5ea3c6c68d3c2ce912cf628f"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091322%2Fnxgeou3z5s1nxgeou3z5s1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663389963&t=9b15aa0b3403f12c2bf947ad10051cc8"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F1011%2F112G6002358%2F16112F02358-7.jpg&refer=http%3A%2F%2Fpic.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663390140&t=44e9621984c732826a5e97cf8d5ac32c");

    private SwipeCaptchaView mSwipeCaptchaView;
    private SeekBar mSeekBar;

    private Animation animation;

    @Override
    protected int initLayout() {
        return R.layout.activity_swipe_check;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        mSwipeCaptchaView = findViewById(R.id.swipeCaptchaView);
        mSeekBar = findViewById(R.id.dragBar);
        imgRefresh = findViewById(R.id.swipe_refresh);
        imgBack.setOnClickListener(this);
        imgRefresh.setOnClickListener(this);
        tvTitle.setText("图片验证");

        //创建旋转动画
        animation =  AnimationUtils.loadAnimation(this, R.anim.img_animation);;
        animation.setDuration(500);
        animation.setRepeatCount(1);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
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
                imgRefresh.setVisibility(View.GONE);
                //清除动画
                imgRefresh.clearAnimation();
                MyToast.showText(SwipeCheckActivity.this,"验证成功！",true);
                //跳转到密码修改页面
                Intent intent = new Intent();
                intent.setClass(SwipeCheckActivity.this, UpdatePwdActivity.class);
                String userName = getIntent().getStringExtra("userName");
                intent.putExtra("userName",userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                MyToast.showText(SwipeCheckActivity.this,"验证失败！", Toast.LENGTH_SHORT,false);
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
        Log.e("SwipeCaptchaView", " System: density:" + getResources().getDisplayMetrics().densityDpi);
        //加载图片
        showNetImg();
    }

    private void showNetImg() {
        Glide.with(SwipeCheckActivity.this)
                .asBitmap()
                .load(URLS.get((int) (Math.random() * URLS.size())))
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
                showNetImg();
                break;
        }
    }


}