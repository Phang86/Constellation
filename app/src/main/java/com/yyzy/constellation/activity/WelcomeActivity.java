package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyzy.constellation.R;
import com.yyzy.constellation.adapter.WelcomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends BaseActivity {

    private ViewPager viewPager;
    //存放图片
    private int[] img = {R.mipmap.loading1,R.mipmap.loading2,R.mipmap.loading3};
    private List<ImageView> mDatas = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.welcome_vp);

        for (int i = 0; i < img.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(img[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);
            mDatas.add(imageView);
        }
        WelcomeAdapter adapter = new WelcomeAdapter(mDatas);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        //为最有一张页面设置监听事件
        int size = mDatas.size() - 1;
        ImageView imageView = mDatas.get(size);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}