package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.internal.Utils;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.dict.entity.WordEntity;
import com.yyzy.constellation.utils.HttpUtils;
import com.yyzy.constellation.utils.URLContent;

import java.util.ArrayList;
import java.util.List;

public class WordInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack,ivCollect;
    private TextView tvMax,tvduyin,tvBushou,tvBihua,tvWubi,tvJs,tvXxjs;
    private ListView lvJs;
    private String zi;
    private List<String> mDatas = new ArrayList<>();    //ListView的数据源
    private List<String> jijie;
    private List<String> xiangjie;
    private ArrayAdapter<String> adapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_word_info;
    }

    @Override
    protected void initView() {
        //接收上一个页面传过来的数据
        Intent intent = getIntent();
        zi = intent.getStringExtra("zi");
        //把要查询的汉字拼接到网络地址中
        String url = URLContent.getWordurl(zi);

        ivBack = findViewById(R.id.wordInfo_iv_back);
        ivCollect = findViewById(R.id.wordInfo_iv_collect);
        tvMax = findViewById(R.id.wordInfo_tv_max);
        tvduyin = findViewById(R.id.wordInfo_tv_duyin);
        tvBihua = findViewById(R.id.wordInfo_tv_bihua);
        tvBushou = findViewById(R.id.wordInfo_tv_bushou);
        tvWubi = findViewById(R.id.wordInfo_tv_wubi);
        tvJs = findViewById(R.id.wordInfo_tv_js);
        tvXxjs = findViewById(R.id.wordInfo_tv_xxjs);
        lvJs = findViewById(R.id.wordInfo_listView);

        ivBack.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
        tvXxjs.setOnClickListener(this);
        tvJs.setOnClickListener(this);

        //设置适配器
        String s = mDatas.toString();
        adapter = new ArrayAdapter<String>(this, R.layout.item_word_jslv,R.id.item_word_tv, mDatas);
        lvJs.setAdapter(adapter);
        //加载网络数据
        loadDatas(url);
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wordInfo_iv_back:
                finish();
                break;
            case R.id.wordInfo_iv_collect:

                break;
            case R.id.wordInfo_tv_js:
                tvJs.setTextColor(Color.parseColor("#6495ED"));
                tvXxjs.setTextColor(Color.BLACK);
                //清空之前的数据
                if (mDatas != null && mDatas.size() > 0) {
                    mDatas.clear();
                    mDatas.addAll(jijie);
                    adapter.notifyDataSetChanged();
                }else {
                    return;
                }
                break;
            case R.id.wordInfo_tv_xxjs:
                tvXxjs.setTextColor(Color.parseColor("#6495ED"));
                tvJs.setTextColor(Color.BLACK);
                if (mDatas != null && mDatas.size() > 0) {
                    mDatas.clear();
                    mDatas.addAll(xiangjie);
                    adapter.notifyDataSetChanged();
                }else {
                    return;
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        WordEntity json = new Gson().fromJson(result, WordEntity.class);
        WordEntity.ResultBean bean = json.getResult();
        //插入数据库
        DBmanager.insertWordToWordtb(bean);
        //将数据显示在View控件上
        notifyView(bean);
    }

    private void notifyView(WordEntity.ResultBean bean) {
        tvMax.setText(bean.getZi());
        tvduyin.setText(bean.getPinyin());
        tvBushou.setText("部首："+bean.getBushou());
        tvBihua.setText("笔画："+bean.getBihua()+"画");
        tvWubi.setText("五笔："+bean.getWubi());
        jijie = bean.getJijie();
        xiangjie = bean.getXiangjie();
        //默认进入显示基本释义
        mDatas.clear();
        mDatas.addAll(jijie);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        //Log.e("TAG", "onError: "+"失败");
        //联网失败，获取数据库里面的数据
        WordEntity.ResultBean bean = DBmanager.queryWordFromWordtb(zi);
        if (bean != null){
            notifyView(bean);
        }else {
            showToast("今日接口访问次数已上限！");
            return;
        }
    }
}