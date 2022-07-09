package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.entity.ChengyuInfoEntity;
import com.yyzy.constellation.utils.MyGridView;
import com.yyzy.constellation.utils.URLContent;

import java.util.ArrayList;
import java.util.List;

public class ChengYuInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backImg,collectImg;
    private TextView oneTv,twoTv,threeTv,fourTv,pinyinTv,chuTv,lijuTv,yufaTv,yinzhengTv,shiliTv,biaoyuTv,jbyyTv;
    private MyGridView tyGv,fyGv;
    private LinearLayout ziLayout;
    private String chengyu;
    private List<String> jycData = new ArrayList<>();
    private List<String> fycData = new ArrayList<>();
    private ArrayAdapter<String> tyAdapter;
    private ArrayAdapter<String> fyAdapter;
    private List<String> jbsy = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_cheng_yu_info;
    }

    @Override
    protected void initView() {
        backImg = findViewById(R.id.chengyuInfo_iv_back);
        oneTv = findViewById(R.id.chengyuInfo_tv_one);
        twoTv = findViewById(R.id.chengyuInfo_tv_two);
        threeTv = findViewById(R.id.chengyuInfo_tv_three);
        fourTv = findViewById(R.id.chengyuInfo_tv_four);
        pinyinTv = findViewById(R.id.chengyuInfo_tv_pinyin);
        chuTv = findViewById(R.id.chengyuinfo_tv_chuchu_js);
        lijuTv = findViewById(R.id.chengyuinfo_tv_liju_js);
        yufaTv = findViewById(R.id.chengyuinfo_tv_yufa_js);
        yinzhengTv = findViewById(R.id.chengyuinfo_tv_yinzheng_js);
        shiliTv = findViewById(R.id.chengyuinfo_tv_shili_js);
        collectImg = findViewById(R.id.chengyuInfo_iv_collect);
        ziLayout = findViewById(R.id.chengyuInfo_layout_zi);
        biaoyuTv = findViewById(R.id.chengyuInfo_tv_biaoyu);
        tyGv = findViewById(R.id.chengyuInfo_gv_tongyici);
        fyGv = findViewById(R.id.chengyuInfo_gv_fanyici);
        jbyyTv = findViewById(R.id.chengyuInfo_tv_jbyy_content);
        collectImg.setOnClickListener(this);
        backImg.setOnClickListener(this);
        Intent intent = getIntent();
        chengyu = intent.getStringExtra("chengyu");
    }

    @Override
    protected void initData() {
        String chengyuUrl = URLContent.getChengyuurl(chengyu);
        loadDatas(chengyuUrl);
        setGvListener();
    }

    //给GridView的item设置监听
    private void setGvListener() {
        tyGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String jinyi = jycData.get(position);
                startPage(jinyi);
                loadDatas(jinyi);
            }
        });
        fyGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fanyi = fycData.get(position);
                startPage(fanyi);
                loadDatas(fanyi);
            }
        });
    }

    @Override
    public void onSuccess(String result) {
        ChengyuInfoEntity bean = new Gson().fromJson(result, ChengyuInfoEntity.class);
        ChengyuInfoEntity.ResultBean beanResult = bean.getResult();
        String chuchu = beanResult.getChuchu();
        String pinyin = beanResult.getPinyin();
        jbsy = beanResult.getJbsy();
        String liju = beanResult.getLiju();
        jycData = beanResult.getJyc();
        fycData = beanResult.getFyc();
        List<String> xxsy = beanResult.getXxsy();
        String biaoyu = xxsy.get(0);
        String chuzi = xxsy.get(1);
        String shili = xxsy.get(2);
        String yufa = xxsy.get(3);
        chuTv.setText(chuchu);
        pinyinTv.setText("拼音："+pinyin);
        lijuTv.setText(liju);
        yufaTv.setText(yufa);
        biaoyuTv.setText(biaoyu);
        yinzhengTv.setText(chuzi);
        shiliTv.setText(shili);
        jbyyTv.setText(String.valueOf(jbsy));
        tyAdapter = new ArrayAdapter<>(this, R.layout.item_chengyu_gv, R.id.item_chengyu_tv, jycData);
        fyAdapter = new ArrayAdapter<>(this, R.layout.item_chengyu_gv, R.id.item_chengyu_tv, fycData);
        tyGv.setAdapter(tyAdapter);
        fyGv.setAdapter(fyAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chengyuInfo_iv_back:
                finish();
                break;
        }
    }

    private void startPage(String chengyu){
        Intent intent = new Intent(this, ChengYuInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("chengyu",chengyu);
        startActivity(intent);
        finish();
    }
}