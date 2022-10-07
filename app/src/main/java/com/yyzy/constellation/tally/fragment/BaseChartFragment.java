package com.yyzy.constellation.tally.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.adapter.ChartLvItemAdapter;
import com.yyzy.constellation.tally.bean.ChartLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;

import java.util.ArrayList;
import java.util.List;


public class BaseChartFragment extends Fragment {

    private ListView lv;
    public List<ChartLvItemBean> mData;
    private ChartLvItemAdapter chartLvItemAdapter;
    public TextView tv;

    public BaseChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_out_come_chart, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lv = view.findViewById(R.id.chart_frag_lv);
        tv = view.findViewById(R.id.chart_frag_tv);
        mData = new ArrayList<>();
        chartLvItemAdapter = new ChartLvItemAdapter(getContext(), mData);
        lv.setAdapter(chartLvItemAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadData(int year, int month, int outOrIn) {
        List<ChartLvItemBean> bean = TallyManger.getChartLvItemListToJilutb(year, month, outOrIn);
        mData.clear();
        mData.addAll(bean);
        chartLvItemAdapter.notifyDataSetChanged();
    }


}