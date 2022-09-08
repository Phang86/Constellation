package com.yyzy.constellation.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.news.adapter.NewsInfoAdapter;
import com.yyzy.constellation.news.bean.TypeBean;
import com.yyzy.constellation.news.db.NewsDBManger;
import com.yyzy.constellation.news.fragment.NewsInfoFragment;
import com.yyzy.constellation.news.view.PagerSlidingTabStrip;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack,imgAdd;
    private PagerSlidingTabStrip pagerStrip;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<TypeBean> selectTypeList;
    private NewsInfoAdapter newsInfoAdapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_news;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.news_img_back);
        imgAdd = findViewById(R.id.news_img_add);
        pagerStrip = findViewById(R.id.news_pagerStrip);
        viewPager = findViewById(R.id.news_viewPager);
        imgBack.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        fragmentList = new ArrayList<>();
        selectTypeList = new ArrayList<>();
        //初始化页面
        initPager();
        //设置适配器
        newsInfoAdapter = new NewsInfoAdapter(getSupportFragmentManager(),this,fragmentList,selectTypeList);
        viewPager.setAdapter(newsInfoAdapter);
        //关联TabStrip和ViewPager
        pagerStrip.setViewPager(viewPager);
    }

    private void initPager() {
        List<TypeBean> typeList = NewsDBManger.selectTypeIsShow();
        selectTypeList.addAll(typeList);
        for (int i = 0; i < selectTypeList.size(); i++) {
            TypeBean typeBean = selectTypeList.get(i);
            NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("type",typeBean);
            newsInfoFragment.setArguments(bundle);
            fragmentList.add(newsInfoFragment);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_img_back:
                finish();
                break;
            case R.id.news_img_add:
                Intent intent = new Intent(NewsActivity.this, AddItemActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fragmentList.clear();
        selectTypeList.clear();
        initPager();
        newsInfoAdapter.notifyDataSetChanged();
        pagerStrip.notifyDataSetChanged();
    }
}