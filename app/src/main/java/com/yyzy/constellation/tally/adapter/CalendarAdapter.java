package com.yyzy.constellation.tally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.yyzy.constellation.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    public int year;
    private List<String> mData;
    public int selPos = -1;

    public void setYear(int year) {
        this.year = year;

        mData.clear();
        loadDatas(year);
        notifyDataSetChanged();
    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        loadDatas(year);
    }

    private void loadDatas(int year) {
        mData = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            String data = year+"/"+i;
            mData.add(data);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar_gv,parent,false);
        TextView tv = convertView.findViewById(R.id.item_calendar_tv);
        CardView cv = convertView.findViewById(R.id.item_calendar_cv);
        tv.setText(mData.get(position));
//        cv.setCardBackgroundColor();
        tv.setTextColor(Color.BLACK);
        if (position == selPos) {
            cv.setCardBackgroundColor(context.getResources().getColor(R.color.pink));
            tv.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}
