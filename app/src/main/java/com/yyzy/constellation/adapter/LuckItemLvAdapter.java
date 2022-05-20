package com.yyzy.constellation.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.LuckItemEntity;

import java.util.List;

public class LuckItemLvAdapter extends BaseAdapter {
    private Context context;
    private List<LuckItemEntity> mData;

    public LuckItemLvAdapter(Context context, List<LuckItemEntity> mData) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.luck_details_lv,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        LuckItemEntity entity = mData.get(position);
        holder.title.setText(entity.getTitle());
        holder.content.setText(entity.getContent());
        GradientDrawable drawable = (GradientDrawable)holder.title.getBackground();
        drawable.setColor(entity.getColor());
        return convertView;
    }

    class ViewHolder{
        TextView title,content;
        public ViewHolder(View view){
            title = view.findViewById(R.id.luck_details_lv_title);
            content = view.findViewById(R.id.luck_details_lv_content);
        }
    }
}
