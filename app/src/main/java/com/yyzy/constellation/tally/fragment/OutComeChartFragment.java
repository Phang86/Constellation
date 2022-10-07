package com.yyzy.constellation.tally.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class OutComeChartFragment extends BaseChartFragment{

    int year;
    int month;
    int outOrIn;
    public OutComeChartFragment(int year,int month,int outOrIn) {
        //loadData(year,month,outOrIn);
        this.year = year;
        this.month = month;
        this.outOrIn = outOrIn;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,outOrIn);
        if (mData.size() > 0){
            tv.setVisibility(View.GONE);
        }else{
            tv.setVisibility(View.VISIBLE);
            tv.setText(year+"年"+month+"月，暂无支出记录！");
        }
    }
}
