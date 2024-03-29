package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.internal.Utils;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.dict.entity.WordEntity;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.HttpUtils;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.Mydialog;
import com.yyzy.constellation.utils.URLContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordInfoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layoutBottom;
    private LinearLayout noDataLayout,noLvDataLayout;
    private ImageView ivBack,ivCollect;
    private TextView tvMax,tvduyin,tvBushou,tvBihua,tvWubi,tvJs,tvXxjs;
    private ListView lvJs;
    private String zi;
    private List<String> mDatas = new ArrayList<>();    //ListView的数据源
    private List<String> jijie;
    private List<String> xiangjie;
    private ArrayAdapter<String> adapter;
    //设置标志是否被收藏
    private boolean isCollect = false;
    //判断这个汉字是否在数据库已经存在
    private boolean isExist = false;
    private String url;
    private DiyProgressDialog dialog;

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
        url = URLContent.getWordurl(zi);

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
        layoutBottom = findViewById(R.id.word_info_layout_bottom);
        noDataLayout = findViewById(R.id.no_data_layout);
        noLvDataLayout = findViewById(R.id.no_data_lv_layout);
        ivBack.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
        tvXxjs.setOnClickListener(this);
        tvJs.setOnClickListener(this);

        layoutBottom.setVisibility(View.GONE);
        ivCollect.setVisibility(View.GONE);

        dialog = new DiyProgressDialog(this);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
    }

    //根据收藏状态改变收藏星星的背景
    private void setCollectBtnColor() {
        if (isCollect) {
            ivCollect.setImageResource(R.mipmap.ic_collection_fs);
        }else {
            ivCollect.setImageResource(R.mipmap.ic_collection);
        }
    }

    //当Activity销毁时，将收藏的汉字插入或删除
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isExist && !isCollect) {
            //原来数据收藏，现在取消删除
            DBmanager.deleteZiToCollwordtb(zi);
        }
        if (!isExist && isCollect) {
            //原来数据未收藏，现在添加到收藏
            DBmanager.insertZiToCollwordtb(zi);
        }
    }

    @Override
    protected void initData() {
        //设置适配器
        //String s = mDatas.toString();
        adapter = new ArrayAdapter<String>(this, R.layout.item_word_jslv,R.id.item_word_tv, mDatas);
        lvJs.setAdapter(adapter);
        //加载网络数据
        loadDatas(url);
        //判断此文字是否被收藏
        isExist = DBmanager.isExistZiInCollwordtb(zi);
        isCollect = isExist;
        //设置收藏按钮的颜色
        setCollectBtnColor();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wordInfo_iv_back:
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.wordInfo_iv_collect:
                //如果状态发生改变则进行取反
                isCollect = !isCollect;
                setCollectBtnColor();
                break;
            case R.id.wordInfo_tv_js:
                tvJs.setTextColor(Color.parseColor("#6495ED"));
                tvXxjs.setTextColor(Color.BLACK);
                //清空之前的数据
//                if (mDatas != null && mDatas.size() > 0) {
//                    mDatas.clear();
//                    mDatas.addAll(jijie);
//                    adapter.notifyDataSetChanged();
//                }else {
//                    return;
//                }
                lvNotOrHave(jijie);
                break;
            case R.id.wordInfo_tv_xxjs:
                tvXxjs.setTextColor(Color.parseColor("#6495ED"));
                tvJs.setTextColor(Color.BLACK);
                lvNotOrHave(xiangjie);
                break;
        }
    }

    private void lvNotOrHave(List<String> list){
        if (mDatas != null && mDatas.size() > 0) {
            String repList = list.toString().replace("[]", "");
            Log.e("TAG", "notifyView: list"+repList.toString()+repList.toString().length());
            if (repList.toString().length() > 0 && !repList.toString().isEmpty()){
                lvJs.setVisibility(View.VISIBLE);
                noLvDataLayout.setVisibility(View.GONE);
                mDatas.clear();
                mDatas.addAll(Collections.singleton(repList));
                adapter.notifyDataSetChanged();
                return;
            }
            lvJs.setVisibility(View.INVISIBLE);
            noLvDataLayout.setVisibility(View.VISIBLE);
        }else {
            return;
        }
    }

    @Override
    public void onSuccess(String result) {
        if (!result.isEmpty()) {
            WordEntity json = new Gson().fromJson(result, WordEntity.class);
            WordEntity.ResultBean bean = json.getResult();
            //插入数据库
            DBmanager.insertWordToWordtb(bean);
            //将数据显示在View控件上
            notifyView(bean);
            showOrHide(true);
        }else{
            return;
        }
    }

    private void notifyView(WordEntity.ResultBean bean) {
        tvMax.setText(bean.getZi());
        tvduyin.setText(bean.getPinyin());
        tvBushou.setText("部首："+bean.getBushou());
        tvBihua.setText("笔画："+bean.getBihua()+"画");
        tvWubi.setText("五笔："+bean.getWubi());
        jijie = bean.getJijie();
        xiangjie = bean.getXiangjie();
//        Log.e("TAG", "notifyView: jijie"+jijie.toString());
//        Log.e("TAG", "notifyView: xiangjie"+xiangjie.toString());
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
        if (DBmanager.queryWordFromWordtb(zi) != null){
            WordEntity.ResultBean bean = DBmanager.queryWordFromWordtb(zi);
            notifyView(bean);
            showOrHide(true);
        }else if (!isOnCallback){
            Log.e("TAG", "onError: "+isOnCallback);
            MyToast.showText(getBaseContext(),"网络错误！请查看网络是否连接！", Gravity.BOTTOM);
            showOrHide(false);
            //dialog.cancel();
            return;
        }else {
            MyToast.showText(getBaseContext(),"接口访问次数上限！",Gravity.BOTTOM);
            showOrHide(false);
        }
    }

    public void showOrHide(boolean show){
        if (show) {
            layoutBottom.setVisibility(View.VISIBLE);
            ivCollect.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }else{
            ivCollect.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
        dialog.dismiss();
    }
}