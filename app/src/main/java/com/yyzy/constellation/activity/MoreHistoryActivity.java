package com.yyzy.constellation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.history.adapter.HistoryAdapter;
import com.yyzy.constellation.history.bean.HistoryEntity;
import com.yyzy.constellation.history.HistoryInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class MoreHistoryActivity extends BaseActivity{

    private ListView lv;
    private TextView tv;
    private List<HistoryEntity.ResultBean> mData;
    private HistoryAdapter adapter;
    private HistoryEntity bean;
    private ImageView imgBack;
    private TextView tvTitle;

    @Override
    protected int initLayout() {
        return R.layout.activity_more_history;
    }

    @Override
    protected void initView() {
        lv = findViewById(R.id.more_history_lv);
        tv = findViewById(R.id.more_history_tv);
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        tvTitle.setText(getResources().getString(R.string.more));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        });
        mData = new ArrayList<>();
        adapter = new HistoryAdapter(this, mData);
        lv.setAdapter(adapter);
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            bean = (HistoryEntity) bundle.getSerializable("historyBean");
            List<HistoryEntity.ResultBean> result = bean.getResult();
            mData.addAll(result);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoreHistoryActivity.this, HistoryInfoActivity.class);
                HistoryEntity.ResultBean resultBean = mData.get(position);
                if (resultBean != null) {
                    String info_id = resultBean.get_id();
                    intent.putExtra("info_id",info_id);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            //return true;
            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
        }
        return super.onKeyDown(keyCode, event);

    }
}