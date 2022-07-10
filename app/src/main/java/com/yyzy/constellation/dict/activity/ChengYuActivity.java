package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.entity.ChengyuInfoEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChengYuActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private ImageView backImg;
    private EditText searchEt;
    private GridView gv;
    private ArrayList<String> mData = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Intent intent;

    @Override
    protected int initLayout() {
        return R.layout.activity_cheng_yu;
    }

    @Override
    protected void initView() {
        backImg = findViewById(R.id.chengyu_iv_back);
        searchEt = findViewById(R.id.chengyu_et);
        gv = findViewById(R.id.chengyu_gv);
        backImg.setOnClickListener(this);
        searchEt.setOnEditorActionListener(this);
    }

    @Override
    protected void initData() {
        adapter = new ArrayAdapter<>(this, R.layout.item_chengyu_gv, R.id.item_chengyu_tv, mData);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = mData.get(position);
                searchEt.setText(s);
                startPage(s);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchEt.setText("");
        mData.clear();
        List<String> list = DBmanager.queryAllCyFromCyutb();
        mData.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chengyu_iv_back:
                finish();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        downOption(actionId);
        return false;
    }

    private void downOption(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                String text = searchEt.getText().toString().trim();
                if (text.isEmpty()){
                    Toast.makeText(context, "搜索框不能为空哦！", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //把文本输入的信息添加到集合

                    //跳转页面
                    startPage(text);
                    //清空文本框
                    searchEt.setText("");
                }
                break;
        }
    }

    private void startPage(String name){
        //跳转到成语详情界面
        intent = new Intent(this, ChengYuInfoActivity.class);
        intent.putExtra("chengyu",name);
        startActivity(intent);
    }
}