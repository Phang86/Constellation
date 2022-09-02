package com.yyzy.constellation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.adapter.HistoryAdapter;
import com.yyzy.constellation.entity.HistoryEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MoreHistoryActivity extends BaseActivity{

    private ListView lv;
    private TextView tv;
    private List<HistoryEntity.ResultBean> mData;
    private HistoryAdapter adapter;
    private HistoryEntity bean;

    @Override
    protected int initLayout() {
        return R.layout.activity_more_history;
    }

    @Override
    protected void initView() {
        lv = findViewById(R.id.more_history_lv);
        tv = findViewById(R.id.more_history_tv);
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
}