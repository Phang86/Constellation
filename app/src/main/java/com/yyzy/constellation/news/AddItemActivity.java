package com.yyzy.constellation.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.news.adapter.AddItemAdapter;
import com.yyzy.constellation.news.bean.TypeBean;
import com.yyzy.constellation.news.db.NewsDBManger;
import com.yyzy.constellation.weather.db.DBHelper;
import com.yyzy.constellation.weather.db.DBManager;

import java.util.List;

public class AddItemActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack;
    private TextView tvTitle;
    private ListView lv;
    private List<TypeBean> mData;
    private AddItemAdapter addItemAdapter;


    @Override
    protected int initLayout() {
        return R.layout.activity_add_item;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        lv = findViewById(R.id.add_item_lv);
        tvTitle.setText(getResources().getString(R.string.channel));
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        mData = NewsDBManger.getAllTypeList();
        addItemAdapter = new AddItemAdapter(this, mData);
        lv.setAdapter(addItemAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                Intent intent = new Intent(AddItemActivity.this, NewsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
        }
    }

    @Override
    protected void onPause() {
        NewsDBManger.updateTypeList(mData);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(AddItemActivity.this, NewsActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}