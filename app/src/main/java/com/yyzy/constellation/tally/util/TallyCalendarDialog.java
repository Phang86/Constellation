package com.yyzy.constellation.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.TallyJiluActivity;
import com.yyzy.constellation.tally.adapter.CalendarAdapter;
import com.yyzy.constellation.tally.db.TallyManger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TallyCalendarDialog extends Dialog implements View.OnClickListener {
    private GridView gv;
    private ImageView img;
    private LinearLayout linHsv;
    private List<TextView> hsvViewList;
    private List<Integer> yearList;
    private int selectPos = -1;
    private CalendarAdapter adapter;
    private int selMonth = -1;
    private RefreshListener refreshListener;

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface RefreshListener{
        void RefreshData(int selPos,int year,int month);
    }

    public TallyCalendarDialog(@NonNull Context context,int selectPos,int selMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selMonth = selMonth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        initView();
    }

    private void initView() {
        gv = findViewById(R.id.dialog_calendar_gv);
        img = findViewById(R.id.dialog_calendar_iv);
        linHsv = findViewById(R.id.dialog_calendar_hsv);

        img.setOnClickListener(this);

        addViewToLayout();

        initGridView();
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selYear);
        if (selMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        }else{
            adapter.selPos = selMonth-1;
        }
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selPos = position;
                adapter.notifyDataSetInvalidated();
                int year = adapter.year;
                int month = position+1;
                refreshListener.RefreshData(selectPos,year,month);
                cancel();
            }
        });
    }

    private void addViewToLayout() {
        hsvViewList = new ArrayList<>();
        yearList = TallyManger.getYearNumOfJilutb();
        if (yearList == null || yearList.size() == 0){
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }

        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_calendar_hsv, null);
            linHsv.addView(view);
            TextView itemTv = view.findViewById(R.id.item_calendar_hsv_tv);
            itemTv.setText(""+year);
            hsvViewList.add(itemTv);
        }

        if (selectPos == -1) {
            selectPos = hsvViewList.size()-1;
        }
        changTvBg(selectPos);
        setHsvOnClickListener();
    }



    private void changTvBg(int selectPos) {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView textView = hsvViewList.get(i);
            textView.setBackgroundResource(R.drawable.tally_calendar_dialog_bg);
            textView.setTextColor(getContext().getResources().getColor(R.color.black_200));
        }

        TextView selectTv = hsvViewList.get(selectPos);
        selectTv.setTextColor(getContext().getResources().getColor(R.color.black));
        selectTv.setBackgroundResource(R.drawable.tally_calendar_dialog_bg_fs);
    }

    //给横向的ScrollView的每一项设置点击事件
    private void setHsvOnClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView tv = hsvViewList.get(i);
            int pos = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changTvBg(pos);
                    selectPos = pos;
                    adapter.setYear(yearList.get(selectPos));
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        cancel();
    }

    public void setWindowSize(){
        Window window = getWindow();
        Display dp = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = dp.getWidth();
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
