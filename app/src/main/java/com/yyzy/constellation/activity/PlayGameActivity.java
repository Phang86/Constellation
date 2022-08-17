package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;

public class PlayGameActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView imgBack;

    @Override
    protected int initLayout() {
        return R.layout.activity_play_game;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.details_title);
        imgBack = findViewById(R.id.details_back);
        tvTitle.setText("拼图游戏");
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {

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