package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.adapter.LuckItemAdapter;
import com.yyzy.constellation.adapter.LuckItemLvAdapter;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.entity.LuckItemEntity;
import com.yyzy.constellation.entity.LuckNetEntity;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.HttpUtils;
import com.yyzy.constellation.utils.LoadDataAsyncTask;
import com.yyzy.constellation.utils.URLContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LuckDetailsActivity extends BaseActivity implements View.OnClickListener,LoadDataAsyncTask.OnGetNetDataListener {

    private TextView title;
    private ImageView ivBack;
    private CircleImageView cv;
    private ListView listView;
    private String starName;
    private List<LuckItemEntity> mData = new ArrayList<>();
    private LuckItemLvAdapter adapter;
    private String url;


    @Override
    protected int initLayout() {
        return R.layout.activity_luck_details;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.details_title);
        ivBack = findViewById(R.id.details_back);
        cv = findViewById(R.id.luck_details_cv);
        listView = findViewById(R.id.luck_details_lv);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //获取上一个页面传过来的数据
        Intent intent = getIntent();
        starName = intent.getStringExtra("starName");
        String starLogoName = intent.getStringExtra("starLogoName");
        Map<String, Bitmap> contentLogoImgMap = AssetsUtils.getContentLogoImgMap();
        Bitmap bitmap = contentLogoImgMap.get(starLogoName);
        cv.setImageBitmap(bitmap);
        title.setText(starName);
        loadNetData();
    }


    private void loadNetData() {
        url = URLContent.getLuckURL(starName);
        LoadDataAsyncTask task = new LoadDataAsyncTask(this, this, true);
        task.execute(url);
    }

    @Override
    public void onSuccess(String json) {
        if (!TextUtils.isEmpty(json)) {
            LuckNetEntity netEntity = new Gson().fromJson(json, LuckNetEntity.class);
            addDataToList(netEntity);
        }
    }

    //添加数据到list,重新整理，方便设置控件的颜色
    private void addDataToList(LuckNetEntity netEntity) {
        if (netEntity.getError_code() == 0 || netEntity.getResultcode().equals("200")) {
            LuckItemEntity a = new LuckItemEntity("综合运势",netEntity.getMima().getText().get(0), Color.RED);
            LuckItemEntity b = new LuckItemEntity("爱情运势",netEntity.getLove().get(0), Color.GREEN);
            LuckItemEntity c = new LuckItemEntity("事业学业",netEntity.getCareer().get(0), Color.BLUE);
            LuckItemEntity d = new LuckItemEntity("健康运势",netEntity.getHealth().get(0), Color.MAGENTA);
            LuckItemEntity e = new LuckItemEntity("财富运势",netEntity.getFinance().get(0), Color.CYAN);
            mData.add(a);
            mData.add(b);
            mData.add(c);
            mData.add(d);
            mData.add(e);
            adapter = new LuckItemLvAdapter(this,mData);
            listView.setAdapter(adapter);
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String json = HttpUtils.getJSONFromNet(url);
                    LuckNetEntity entity = new Gson().fromJson(json, LuckNetEntity.class);
                    try {
                        if (entity.getError_code() == 10012) {
                            Looper.prepare();
                            Toast.makeText(context, "请求接口次数今日已上限！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            return;
                        }else {
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

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