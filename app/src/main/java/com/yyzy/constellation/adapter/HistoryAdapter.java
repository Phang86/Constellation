package com.yyzy.constellation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.HistoryEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryEntity.ResultBean> mData;

    public HistoryAdapter(Context context, List<HistoryEntity.ResultBean> mData) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_timeline_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HistoryEntity.ResultBean resultBean = mData.get(position);
//        判断当前位置的年份和上一个位置是否相同
        if (position != 0) {
            HistoryEntity.ResultBean lastBean = mData.get(position - 1);
            if (resultBean.getYear() == lastBean.getYear()) {
                holder.timeLayout.setVisibility(View.GONE);
            } else {
                holder.timeLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.timeLayout.setVisibility(View.VISIBLE);
        }

        holder.timeTv.setText(resultBean.getYear() + "-" + resultBean.getMonth() + "-" + resultBean.getDay());
        holder.titleTv.setText(resultBean.getTitle());
        String picURL = resultBean.getPic();
        if (TextUtils.isEmpty(picURL)) {
            holder.picIv.setVisibility(View.GONE);
        } else {
            holder.picIv.setVisibility(View.VISIBLE);
            Glide.with(context).load(picURL).into(holder.picIv);
        }
        return convertView;
    }
}

class ViewHolder{
    TextView timeTv,titleTv;
    ImageView picIv;
    LinearLayout timeLayout;
    public ViewHolder(View itemView){
        timeTv = itemView.findViewById(R.id.item_main_time);
        titleTv = itemView.findViewById(R.id.item_main_title);
        picIv = itemView.findViewById(R.id.item_main_pic);
        timeLayout = itemView.findViewById(R.id.item_main_ll);
    }

}
