package com.yyzy.constellation.tally.fragment;

import android.icu.util.ValueIterator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.adapter.ChartLvItemAdapter;
import com.yyzy.constellation.tally.bean.ChartLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseChartFragment extends Fragment {

    private ListView lv;
    public List<ChartLvItemBean> mData;
    private ChartLvItemAdapter chartLvItemAdapter;
    public TextView tv;
    public BarChart barChart;
    private int year,month;
    public LinearLayout noDataLayout;

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
        tv = view.findViewById(R.id.local_music_tv);
        noDataLayout = view.findViewById(R.id.no_data_layout);
        mData = new ArrayList<>();
        chartLvItemAdapter = new ChartLvItemAdapter(getContext(), mData);
        lv.setAdapter(chartLvItemAdapter);
        //添加头布局（柱状图）
        addLVHeaderView();
    }

    private void addLVHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.item_chart_frag_lv_headerview, null);
        lv.addHeaderView(view);
        barChart = view.findViewById(R.id.item_chart_frag_chart);
        //设置柱状图不显示描述
        barChart.getDescription().setEnabled(false);
        //设置柱状图的内边距
        barChart.setExtraOffsets(20,20,20,20);
        //设置坐标轴
        setAxis(year,month);
        //设置坐标轴的数据
        setAxisData(year,month);
    }

    protected abstract void setAxisData(int year, int month);

    //设置柱状图坐标轴的显示
    protected void setAxis(int year, int month) {
        //设置X轴
        XAxis xAxis = barChart.getXAxis();
        //设置X轴在下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置绘制该轴的网格线
        xAxis.setDrawGridLines(true);
        //设置X轴标签数
        xAxis.setLabelCount(31);
        //设置X轴标签大小
        xAxis.setTextSize(12f);
        //设置X轴显示值的样式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if (val == 0) {
                    return month+"-1";
                }
                if (val == 14){
                    return month+"-15";
                }

                //根据不同的月份，显示最后一天的位置
                if (month == 2){
                    if (val == 27){
                        return month+"-28";
                    }
                }else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                    if (val == 30){
                        return month+"-31";
                    }
                }else if (month == 4 || month == 6 || month == 9 || month == 11){
                    if (val == 29){
                        return month+"-30";
                    }
                }
                return "";
            }
        });
        //设置标签对x轴的偏移量，垂直方向
        xAxis.setYOffset(10);

        //Y轴在子类的位置
        setYAxis(year,month);
    }

    protected abstract void setYAxis(int year, int month);

    public void setData(int year,int month){
        this.year = year;
        this.month =month;
    }

    public void loadData(int year, int month, int outOrIn) {
        this.year = year;
        List<ChartLvItemBean> bean = TallyManger.getChartLvItemListToJilutb(year, month, outOrIn);
        mData.clear();
        mData.addAll(bean);
        chartLvItemAdapter.notifyDataSetChanged();
    }
}