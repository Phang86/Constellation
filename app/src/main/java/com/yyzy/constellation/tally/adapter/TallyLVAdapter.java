package com.yyzy.constellation.tally.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;

import java.util.List;

public class TallyLVAdapter extends BaseAdapter {
    private Context context;
    private List<TallyLvItemBean> mData;

    public TallyLVAdapter(Context context, List<TallyLvItemBean> mData) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.tally_lv_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        TallyLvItemBean bean = mData.get(position);
        holder.imgTitle.setBackgroundResource(bean.getImg());
        holder.tvTitle.setText(bean.getTypeName());
        holder.tvMoney.setText("￥"+bean.getMoney());
        if (TextUtils.isEmpty(bean.getBeizhu())){
            holder.tvBeizhu.setText("无备注！");
        }else{
            holder.tvBeizhu.setText(bean.getBeizhu());
        }
        if (bean.getOutOrIn() == 0) {
            holder.tvOutOrIn.setText("来源：支出");
        }else{
            holder.tvOutOrIn.setText("来源：收入");
        }
        holder.tvTime.setText(bean.getTime());
        return convertView;
    }

    class ViewHolder{

        ImageView imgTitle;
        TextView tvTitle,tvBeizhu,tvMoney,tvTime,tvOutOrIn;

        public ViewHolder(View itemView){
            imgTitle = itemView.findViewById(R.id.tally_item_lv_img);
            tvTitle = itemView.findViewById(R.id.tally_item_lv_tv_title);
            tvBeizhu = itemView.findViewById(R.id.tally_item_lv_tv_beizhu);
            tvMoney = itemView.findViewById(R.id.tally_item_lv_tv_money);
            tvTime = itemView.findViewById(R.id.tally_item_lv_tv_time);
            tvOutOrIn = itemView.findViewById(R.id.tally_item_lv_tv_outOrIn);
        }
    }
}
