package com.yyzy.constellation.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.bean.GvTypeBean;

import java.util.List;

public class GvTypeAdapter extends BaseAdapter {
    private Context context;
    private List<GvTypeBean> mData;
    public int selectPos = 0;

    public GvTypeAdapter(Context context, List<GvTypeBean> mData) {
        this.context = context;
        this.mData = mData;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_record_frag_gv,parent,false);
        TextView tv = convertView.findViewById(R.id.item_recordFrag_tv);
        ImageView iv = convertView.findViewById(R.id.item_recordFrag_iv);
        GvTypeBean bean = mData.get(position);
        tv.setText(bean.getTypeName());
        if (position == selectPos) {
            iv.setBackgroundResource(bean.getSelectImg());
        }else{
            iv.setBackgroundResource(bean.getUnSelectImg());
        }
        return convertView;
    }
}
