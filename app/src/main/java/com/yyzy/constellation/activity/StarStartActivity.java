package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.PartnerAsyncEntity;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.LoadDataAsyncTask;
import com.yyzy.constellation.utils.URLContent;

import java.io.Serializable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StarStartActivity extends AppCompatActivity implements View.OnClickListener,LoadDataAsyncTask.OnGetNetDataListener{

    private TextView manTv,womanTv,pdTv,vsTv,pfTv,bzTv,jxTv,zyTv,titleTv;
    private CircleImageView cvMan,cvWoman;
    private ImageView ivBack;
    private String woman_name,man_name;
    private String woman_logo,man_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_start);
        initView();
        loadLastData();
        //加载网络数据
        loadNetData();
    }

    private void loadNetData() {
        String url = URLContent.getPartnerURL(man_name, woman_name);
        LoadDataAsyncTask task = new LoadDataAsyncTask(this, this, true);
        task.execute(url);
    }

    @Override
    public void onSuccess(String json) {
        if (!TextUtils.isEmpty(json)) {
            PartnerAsyncEntity entity = new Gson().fromJson(json, PartnerAsyncEntity.class);
            PartnerAsyncEntity.ResultDTO result = entity.getResult();

            pfTv.setText("配对："+result.getZhishu()+" "+result.getJieguo());
            bzTv.setText("星座比重："+result.getBizhong());
            jxTv.setText("解析：\n\n"+result.getLianai());
            zyTv.setText("注意事项：\n\n"+result.getZhuyi());
        }
    }

    //加载上一个页面传过来的数据
    private void loadLastData() {
        Intent intent = getIntent();
        woman_name = intent.getStringExtra("woman_name");
        man_name = intent.getStringExtra("man_name");
        woman_logo = intent.getStringExtra("woman_logo");
        man_logo = intent.getStringExtra("man_logo");
        womanTv.setText(woman_name);
        manTv.setText(man_name);

        Map<String, Bitmap> man = AssetsUtils.getContentLogoImgMap();
        Bitmap manMap = man.get(man_logo);
        cvMan.setImageBitmap(manMap);

        Map<String, Bitmap> woman = AssetsUtils.getContentLogoImgMap();
        Bitmap womanMap = woman.get(woman_logo);
        cvWoman.setImageBitmap(womanMap);

        pdTv.setText("星座配对："+man_name+"和"+woman_name+"配对");
        vsTv.setText("VS："+man_name+" vs "+woman_name);
    }

    //初始化控件
    private void initView() {
        manTv = findViewById(R.id.starStart_tv_man);
        womanTv = findViewById(R.id.starStart_tv_woman);
        pdTv = findViewById(R.id.starStart_tv_pd);
        vsTv = findViewById(R.id.starStart_tv_vs);
        pfTv = findViewById(R.id.starStart_tv_pf);
        bzTv = findViewById(R.id.starStart_tv_bz);
        jxTv = findViewById(R.id.starStart_tv_jx);
        zyTv = findViewById(R.id.starStart_tv_zy);
        titleTv = findViewById(R.id.details_title);
        cvMan = findViewById(R.id.starStart_cv_man);
        cvWoman = findViewById(R.id.starStart_cv_woman);
        ivBack = findViewById(R.id.details_back);

        ivBack.setOnClickListener(this);

        titleTv.setText("配对分析");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
        }
    }


}