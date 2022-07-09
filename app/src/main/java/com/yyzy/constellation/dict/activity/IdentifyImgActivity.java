package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;

import java.util.ArrayList;

public class IdentifyImgActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backImg;
    private GridView gv;
    private ArrayList<String> wordList ;


    @Override
    protected int initLayout() {
        return R.layout.activity_identify_img;
    }

    @Override
    protected void initView() {
        backImg = findViewById(R.id.identify_iv_back);
        gv = findViewById(R.id.identify_gv);
        backImg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        wordList = bundle.getStringArrayList("wordList");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.search_right_item_gv, R.id.search_right_item_tv, wordList);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = wordList.get(position);
                Intent intent = new Intent(IdentifyImgActivity.this, WordInfoActivity.class);
                intent.putExtra("zi", word);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identify_iv_back:
                finish();
                break;
        }
    }
}