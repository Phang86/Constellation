package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;

public class SetActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout collectLayout,enjoyLayout,aboutLayout;
    private ImageView backImg;

    @Override
    protected int initLayout() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        backImg = findViewById(R.id.set_iv_back);
        collectLayout = findViewById(R.id.set_layout_collect);
        enjoyLayout = findViewById(R.id.set_layout_enjoy);
//        aboutLayout = findViewById(R.id.set_layout_about);
        backImg.setOnClickListener(this);
        collectLayout.setOnClickListener(this);
        enjoyLayout.setOnClickListener(this);
//        aboutLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        switch (v.getId()) {
            case R.id.set_iv_back:
                finish();
                break;
            case R.id.set_layout_collect:
                intent.setClass(this,CollectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.set_layout_enjoy:
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"应用下载地址");
                startActivity(Intent.createChooser(intent,"分享到"));
                break;
//            case R.id.set_layout_about:
//
//                break;
        }
    }
}