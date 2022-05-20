package com.yyzy.constellation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.StarDetailsEntity;

import java.util.List;

public class StarDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<StarDetailsEntity> mDatas;

    public StarDetailsAdapter(Context context, List<StarDetailsEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StarDetailsEntity entity = mDatas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.details_listview_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(entity.getTitle());
        holder.content.setText(entity.getContent());
        holder.content.setBackgroundResource(entity.getColor());
        return convertView;
    }

    class ViewHolder{
        TextView title,content;
        public ViewHolder(View view){
            title = view.findViewById(R.id.list_tv_title);
            content = view.findViewById(R.id.list_tv_content);
        }
    }
}
