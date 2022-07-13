package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.adapter.CollectFragmentAdapter;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.fragment.CollectZiFragment;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends BaseActivity implements View.OnClickListener {


    private ImageView collectImg;
    private ViewPager vp;
    private TabLayout tab;
    private List<Fragment> mData;
    private String[] titles = {"文字","成语"};
    private CollectFragmentAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {
        collectImg = findViewById(R.id.collect_iv_back);
        tab = findViewById(R.id.collect_tab);
        vp = findViewById(R.id.collect_vp);
        tab.setOnClickListener(this);
        collectImg.setOnClickListener(this);
        initPager();
    }

    //初始化ViewPager
    private void initPager() {
        mData = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Fragment fragment = new CollectZiFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type",titles[i]);
            fragment.setArguments(bundle);
            mData.add(fragment);
        }
        adapter = new CollectFragmentAdapter(getSupportFragmentManager(), mData, titles);
        vp.setAdapter(adapter);
        //将上下绑定
        tab.setupWithViewPager(vp);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_iv_back:
                finish();
                break;
        }
    }
}