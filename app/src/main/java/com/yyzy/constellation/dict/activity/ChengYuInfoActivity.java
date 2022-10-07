package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.entity.ChengyuInfoEntity;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyGridView;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class ChengYuInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView nullImg;
    private ScrollView scroll;
    private TextView tv;
    private ImageView backImg,collectImg;
    private TextView oneTv,twoTv,threeTv,fourTv,pinyinTv,chuTv,lijuTv,yufaTv,yinzhengTv,shiliTv,biaoyuTv,jbyyTv;
    private MyGridView tyGv,fyGv;
    private String chengyu;
    private List<String> jycData = new ArrayList<>();
    private List<String> fycData = new ArrayList<>();
    private ArrayAdapter<String> tyAdapter;
    private ArrayAdapter<String> fyAdapter;
    private List<String> jbsy = new ArrayList<>();
    private String biaoyu;
    private String chuzi;
    private String shili;
    private String yufa;
    private DiyProgressDialog dialog;
    //设置标志是否被收藏
    private boolean isCollect = false;
    //判断这个汉字是否在数据库已经存在
    private boolean isExist = false;

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
        biaoyuTv = findViewById(R.id.chengyuInfo_tv_biaoyu);
        tyGv = findViewById(R.id.chengyuInfo_gv_tongyici);
        fyGv = findViewById(R.id.chengyuInfo_gv_fanyici);
        jbyyTv = findViewById(R.id.chengyuInfo_tv_jbyy_content);
        scroll = findViewById(R.id.chengyu_info_scroll);
        tv = findViewById(R.id.chengyu_info_tv);
        nullImg = findViewById(R.id.chengyu_info_img);
        collectImg.setOnClickListener(this);
        backImg.setOnClickListener(this);
        scroll.setVisibility(View.GONE);
        collectImg.setVisibility(View.GONE);
        Intent intent = getIntent();
        chengyu = intent.getStringExtra("chengyu");
        dialog = new DiyProgressDialog(this, "加载中...");
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void initData() {
        String chengyuUrl = URLContent.getChengyuurl(chengyu);
        loadDatas(chengyuUrl);
        isExist = DBmanager.isExistCyuInCollCyutb(chengyu);
        isCollect = isExist;
        //设置收藏按钮的颜色
        setCollectBtnColor();
    }

    //根据收藏状态改变收藏星星的背景
    private void setCollectBtnColor() {
        if (isCollect) {
            collectImg.setImageResource(R.mipmap.ic_collection_fs);
        }else {
            collectImg.setImageResource(R.mipmap.ic_collection);
        }
    }

    @Override
    public void onSuccess(String result) {
        ChengyuInfoEntity bean = new Gson().fromJson(result, ChengyuInfoEntity.class);
        ChengyuInfoEntity.ResultBean beanResult = bean.getResult();
        try {
            if (bean.getError_code() == 0 ) {
                DBmanager.insertCyToCyutb(beanResult);
                showDatasView(beanResult);
                showOrHide(true);
            } else if (bean.getError_code() == 10012) {
                if (DBmanager.queryCyFromCyutb(chengyu) != null){
                    ChengyuInfoEntity.ResultBean entity = DBmanager.queryCyFromCyutb(chengyu);
                    showDatasView(entity);
                    showOrHide(true);
                }else{
                    MyToast.showText(ChengYuInfoActivity.this, "今日接口访问次数已上限！");
                    Log.e("TAG", "onSuccess: 000000");
                    showOrHide(false);
                    tv.setText("抱歉，暂无数据！");
                }
            } else {
                if (DBmanager.queryCyFromCyutb(chengyu) != null){
                    ChengyuInfoEntity.ResultBean entity = DBmanager.queryCyFromCyutb(chengyu);
                    showDatasView(entity);
                    showOrHide(true);
                }else{
                    MyToast.showText(ChengYuInfoActivity.this, "无法查询此成语，请更换需查询的成语！");
                    showOrHide(false);
                    tv.setText("抱歉，无法查询此成语！");
                }
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
            //当网络获取数据失败时，到数据库去查找
            if (DBmanager.queryCyFromCyutb(chengyu) != null){
                ChengyuInfoEntity.ResultBean bean = DBmanager.queryCyFromCyutb(chengyu);
                showDatasView(bean);
                showOrHide(true);
            }else if (!isOnCallback){
                MyToast.showText(this,"网络连接失败，请检查WLAN是否开启！");
                showOrHide(false);
                tv.setText("网络未开启"+isOnCallback);
            }else{
                MyToast.showText(this,"今日接口访问次数已上限！");
                showOrHide(false);
                tv.setText("非常抱歉，接口今日访问次数受限！");
            }
    }

    public void showOrHide(boolean show){
        if (show) {
            collectImg.setVisibility(View.VISIBLE);
            scroll.setVisibility(View.VISIBLE);
        }else{
            collectImg.setVisibility(View.GONE);
            scroll.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("抱歉，暂无数据！");
            nullImg.setVisibility(View.VISIBLE);
        }
        dialog.dismiss();
    }

//    @Override
//    public void onFinished() {
//            if (DBmanager.queryCyFromCyutb(chengyu) != null){
//                ChengyuInfoEntity.ResultBean bean = DBmanager.queryCyFromCyutb(chengyu);
//                showDatasView(bean);
//                showOrHide(true);
//            }else{
//                MyToast.showText(this,"网络连接失败！");
//                Log.e("TAG", "onFinished: "+"网络连接失败！");
//                showOrHide(false);
//            }
//            return;
//    }

    //将网络获取到的数据显示在控件上
    private void showDatasView(ChengyuInfoEntity.ResultBean beanResult) {
        String chuchu = beanResult.getChuchu();
        String pinyin = beanResult.getPinyin();
        String name = beanResult.getName();
        jbsy = beanResult.getJbsy();
        //将list集合转换成字符串
        String strJbsy = DBmanager.listToString(jbsy);
        String replaceJbsy = strJbsy.replace("[", "").replace("]", "");
        String liju = beanResult.getLiju();
        jycData = beanResult.getJyc();
        fycData = beanResult.getFyc();
        List<String> xxsy = beanResult.getXxsy();
        //Log.e("TAG", "showDatasView: "+xxsy.size());
        if (xxsy.size() > 1) {
            biaoyu = xxsy.get(0);
            chuzi = xxsy.get(1);
            shili = xxsy.get(2);
            yufa = xxsy.get(3);
        }else if (xxsy.size() == 1){
            biaoyu = xxsy.toString();
            chuzi = xxsy.toString();
            shili = xxsy.toString();
            yufa = xxsy.toString();
        }
        String strbiaoyu = biaoyu.replace("[", "").replace("]", "");
        chuTv.setText(chuchu);
        pinyinTv.setText("拼音："+pinyin);
        lijuTv.setText(liju);
        yufaTv.setText(yufa);
        biaoyuTv.setText(strbiaoyu);
        yinzhengTv.setText(chuzi);
        shiliTv.setText(shili);
        jbyyTv.setText(replaceJbsy);
        oneTv.setText(String.valueOf(name.charAt(0)));
        twoTv.setText(String.valueOf(name.charAt(1)));
        threeTv.setText(String.valueOf(name.charAt(2)));
        fourTv.setText(String.valueOf(name.charAt(3)));
        tyAdapter = new ArrayAdapter<>(this, R.layout.item_chengyu_gv, R.id.item_chengyu_tv, jycData);
        fyAdapter = new ArrayAdapter<>(this, R.layout.item_chengyu_gv, R.id.item_chengyu_tv, fycData);
        tyGv.setAdapter(tyAdapter);
        fyGv.setAdapter(fyAdapter);

        tyGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String jyText = jycData.get(position);
                MyToast.showText(getBaseContext(),""+jyText);
            }
        });

        fyGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fyText = fycData.get(position);
                MyToast.showText(getBaseContext(),""+fyText);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chengyuInfo_iv_back:
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.chengyuInfo_iv_collect:
                isCollect = !isCollect;
                setCollectBtnColor();
                break;
        }
    }

//    private void startPage(String chengyu){
//        Intent intent = new Intent(this, ChengYuInfoActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("chengyu",chengyu);
//        startActivity(intent);
//        finish();
//    }

    //当Activity销毁时，将收藏的汉字插入或删除
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isExist && !isCollect) {
            //原来数据已收藏，现在取消又收藏，则从收藏中进行移除
            DBmanager.deleteCyuToCollCyutb(chengyu);
        }
        if (!isExist && isCollect) {
            //原来数据未收藏，现在想收藏，则添加到收藏
            DBmanager.insertCyuToCollCyutb(chengyu);
        }
    }
}