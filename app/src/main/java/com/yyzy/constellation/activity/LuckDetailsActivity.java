package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.adapter.LuckItemAdapter;
import com.yyzy.constellation.adapter.LuckItemLvAdapter;
import com.yyzy.constellation.entity.LuckItemEntity;
import com.yyzy.constellation.entity.LuckNetEntity;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.LoadDataAsyncTask;
import com.yyzy.constellation.utils.URLContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LuckDetailsActivity extends AppCompatActivity implements View.OnClickListener,LoadDataAsyncTask.OnGetNetDataListener {

    private TextView title;
    private ImageView ivBack;
    private CircleImageView cv;
    private ListView listView;
    private String starName;
    private List<LuckItemEntity> mData = new ArrayList<>();
    private LuckItemLvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_details);
        initView();
        //加载储存在本地的数据
        loadThisData();
        //加载网络数据
        loadNetData();
    }

    private void loadThisData() {
        //获取上一个页面传过来的数据
        Intent intent = getIntent();
        starName = intent.getStringExtra("starName");
        String starLogoName = intent.getStringExtra("starLogoName");
        Map<String, Bitmap> contentLogoImgMap = AssetsUtils.getContentLogoImgMap();
        Bitmap bitmap = contentLogoImgMap.get(starLogoName);
        cv.setImageBitmap(bitmap);
        title.setText(starName);

    }

    private void loadNetData() {
        String url = URLContent.getLuckURL(starName);
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
    }

    //初始化控件
    private void initView() {
        title = findViewById(R.id.details_title);
        ivBack = findViewById(R.id.details_back);
        cv = findViewById(R.id.luck_details_cv);
        listView = findViewById(R.id.luck_details_lv);
        ivBack.setOnClickListener(this);


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