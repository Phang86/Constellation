package com.yyzy.constellation.tally.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.bean.BarCharItemBean;
import com.yyzy.constellation.tally.db.TallyManger;

import java.util.ArrayList;
import java.util.List;

public class OutComeChartFragment extends BaseChartFragment{

    int year;
    int month;
    int outOrIn;

    public OutComeChartFragment(int year,int month,int outOrIn) {
        this.year = year;
        this.month = month;
        this.outOrIn = outOrIn;
        setData(year,month);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        //获取这个月每天的支出总金额
        List<BarCharItemBean> beans = TallyManger.getSumMoneyOneDayInMonth(year, month, outOrIn);
        if (beans.size() == 0){
            barChart.setVisibility(View.GONE);
        }else{
            barChart.setVisibility(View.VISIBLE);
            List<BarEntry> barEntries = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                //初始化每一根柱子，添加到柱状图当中
                BarEntry barEntry = new BarEntry(i,0.0f);
                barEntries.add(barEntry);
            }

            for (int i = 0; i < beans.size(); i++) {
                BarCharItemBean itemBean = beans.get(i);
                int day = itemBean.getDay();
                //根据天数获取x轴的位置
                int xIndex = day-1;
                BarEntry barEntry = barEntries.get(xIndex);
                barEntry.setY(itemBean.getSumMoney());
            }

            BarDataSet barDataSet1 = new BarDataSet(barEntries, "");
            barDataSet1.setValueTextColor(Color.BLACK); // 值的颜色
            barDataSet1.setValueTextSize(8f); // 值的大小
            barDataSet1.setColor(getResources().getColor(R.color.pink)); // 柱子的颜色

            // 设置柱子上数据显示的格式
            barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 此处的value默认保存一位小数
                    if (value == 0){
                        return "";
                    }
                    return value + "";
                }
            });
            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.4f); // 设置柱子的宽度
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        //获取本月支出最高的一天为多少，将它设置为Y轴的最大值
        float maxMoney = TallyManger.getMaxMoneyOneDayInMonth(year, month, outOrIn);
        float max = (float) Math.ceil(maxMoney);
        // 设置y轴，y轴有两条，分别为左和右
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);  // 不显示左边的y轴

        //设置不显示图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,outOrIn);
        if (mData.size() > 0){
            noDataLayout.setVisibility(View.GONE);
        }else{
            noDataLayout.setVisibility(View.VISIBLE);
            tv.setText(year+"年"+month+"月，暂无支出记录！");
        }
    }
}
