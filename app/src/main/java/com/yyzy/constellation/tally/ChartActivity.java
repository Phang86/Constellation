package com.yyzy.constellation.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.adapter.ChartVpAdapter;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.tally.fragment.InComeChartFragment;
import com.yyzy.constellation.tally.fragment.BaseChartFragment;
import com.yyzy.constellation.tally.fragment.OutComeChartFragment;
import com.yyzy.constellation.tally.util.TallyCalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack,imgCalendar;
    private TextView tvMonthBill,tvOutBill,tvInBill;
    private Button btnOut,btnIn;
    private ViewPager vp;
    private Calendar ca;
    private int year;
    private int month;
    private float outBillOfMonth;
    private List<TallyLvItemBean> outBean;
    private float inBillOfMonth;
    private List<TallyLvItemBean> inBean;
    private int dialogSelPos = -1;
    private List<Fragment> fragList;
    private ChartVpAdapter chartVpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initView();
        ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH)+1;
        initData(year,month);
        initFrag(year,month);
    }

    private void initFrag(int year,int month) {
        fragList = new ArrayList<>();
        OutComeChartFragment outComeChartFragment = new OutComeChartFragment(year,month,0);
        InComeChartFragment inComeChartFragment = new InComeChartFragment(year,month,1);
        fragList.add(outComeChartFragment);
        fragList.add(inComeChartFragment);
        chartVpAdapter = new ChartVpAdapter(getSupportFragmentManager(), fragList);
        vp.setAdapter(chartVpAdapter);
        //初始化fragment页面，默认第一个页面
        vp.setCurrentItem(0);
        //初始化button样式，同样默认支出页面
        setButtonStyle(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                vp.setCurrentItem(position);
            }

            @Override
            public void onPageSelected(int position) {
//                vp.setCurrentItem(position);
                setButtonStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        imgBack = findViewById(R.id.chart_img_back);
        imgCalendar = findViewById(R.id.chart_img_calendar);
        tvMonthBill = findViewById(R.id.chart_tv_monthBill);
        tvOutBill = findViewById(R.id.chart_tv_outBill);
        tvInBill = findViewById(R.id.chart_tv_inBill);
        btnOut = findViewById(R.id.chart_btn_out);
        btnIn = findViewById(R.id.chart_btn_in);
        vp = findViewById(R.id.chart_vp);

        btnIn.setOnClickListener(this);
        btnOut.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);
    }

    private void initData(int year,int month) {
        tvMonthBill.setText(year+"年"+month+"月账单");
        //支出月账单,0:表示支出
        outBillOfMonth = TallyManger.getSumMoneyOneMonth(year, month, 0);
        outBean = TallyManger.getOutOrInJilutbAll(year,month,0);
        tvOutBill.setText("共有"+outBean.size()+"笔支出，￥"+outBillOfMonth);
        //收入月账单,1:表示收入
        inBillOfMonth = TallyManger.getSumMoneyOneMonth(year, month, 1);
        inBean = TallyManger.getOutOrInJilutbAll(year,month,1);
        tvInBill.setText("共有"+inBean.size()+"笔收入，￥"+inBillOfMonth);
//        chartVpAdapter.notifyDataSetChanged();
        initFrag(year,month);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chart_img_back:
                finish();
                break;
            case R.id.chart_img_calendar:
                showCalendarDialog();
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                vp.setCurrentItem(0);
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                vp.setCurrentItem(1);
                break;
        }
    }

    private void showCalendarDialog() {
        TallyCalendarDialog calendarDialog = new TallyCalendarDialog(this, dialogSelPos, month);
        calendarDialog.show();
        calendarDialog.setWindowSize();
        calendarDialog.setRefreshListener(new TallyCalendarDialog.RefreshListener() {
            @Override
            public void RefreshData(int selPos, int year, int month) {
                ChartActivity.this.dialogSelPos = selPos;
                ChartActivity.this.month = month;
                initData(year,month);
            }
        });
    }

    private void setButtonStyle(int outOrIn){
        if (outOrIn == 0){
            btnOut.setBackground(getDrawable(R.drawable.tally_calendar_dialog_bg_fs));
            btnOut.setTextColor(getResources().getColor(R.color.white));
            btnIn.setBackground(getDrawable(R.drawable.tally_calendar_dialog_bg));
            btnIn.setTextColor(getResources().getColor(R.color.pink));
        }else if (outOrIn == 1){
            btnOut.setBackground(getDrawable(R.drawable.tally_calendar_dialog_bg));
            btnOut.setTextColor(getResources().getColor(R.color.pink));
            btnIn.setBackground(getDrawable(R.drawable.tally_calendar_dialog_bg_fs));
            btnIn.setTextColor(getResources().getColor(R.color.white));
        }
    }
}