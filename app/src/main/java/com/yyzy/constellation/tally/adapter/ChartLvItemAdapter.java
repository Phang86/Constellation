package com.yyzy.constellation.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.tally.bean.ChartLvItemBean;
import com.yyzy.constellation.tally.util.FloatUtils;

import java.util.List;

public class ChartLvItemAdapter extends BaseAdapter {
    private Context context;
    private List<ChartLvItemBean> mData;

    public ChartLvItemAdapter(Context context, List<ChartLvItemBean> mData) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chart_frag_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ChartLvItemBean bean = mData.get(position);
        holder.img.setImageResource(bean.getImg());
        holder.tvType.setText(bean.getType());
        String radioPer = (bean.getRatio()+"%");
        holder.tvRatio.setText(radioPer);
        holder.tvSumMoney.setText("￥"+bean.getSumMoney());
        return convertView;
    }

    class ViewHolder{
        ImageView img;
        TextView tvRatio,tvType,tvSumMoney;
        ViewHolder(View view){
            img = view.findViewById(R.id.item_chart_frag_img_title);
            tvRatio = view.findViewById(R.id.item_chart_frag_tv_ratio);
            tvType = view.findViewById(R.id.item_chart_frag_tv_type);
            tvSumMoney = view.findViewById(R.id.item_chart_frag_tv_money);
        }
    }
}
