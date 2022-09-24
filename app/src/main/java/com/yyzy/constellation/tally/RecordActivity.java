package com.yyzy.constellation.tally;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.tally.adapter.ReCordPagerAdapter;
import com.yyzy.constellation.tally.fragment.InComeFragment;
import com.yyzy.constellation.tally.fragment.OutComeFragment;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack;
    private TabLayout tab;
    private ViewPager vp;

    @Override
    protected int initLayout() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.record_img_back);
        tab = findViewById(R.id.record_tab);
        vp = findViewById(R.id.record_vp);

        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //给ViewPager设置加载页面
        initPager();
    }

    private void initPager() {
        //适配器数据源
        List<Fragment> mData = new ArrayList<>();
        OutComeFragment outComeFragment = new OutComeFragment();
        InComeFragment inComeFragment = new InComeFragment();
        mData.add(outComeFragment);
        mData.add(inComeFragment);

        //设置适配器
        ReCordPagerAdapter reCordPagerAdapter = new ReCordPagerAdapter(getSupportFragmentManager(), mData);
        vp.setAdapter(reCordPagerAdapter);
        //tab与下面的viewpager联动
        tab.setupWithViewPager(vp);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}