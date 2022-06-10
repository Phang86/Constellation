package com.yyzy.constellation.dict.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;

import java.util.List;

//拼音查询、部首查询页面右侧的pullToRefreshGridView的适配器
public class SearchRightAdapter extends BaseAdapter {
    private Context context;
    private List<PinBuWordEntity.ResultBean.ListBean> mDatas;
    private LayoutInflater inflater;

    public SearchRightAdapter(Context context,List<PinBuWordEntity.ResultBean.ListBean> mDatas){
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
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
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.search_right_item_gv,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        PinBuWordEntity.ResultBean.ListBean bean = mDatas.get(position);
        String zi = bean.getZi();
        holder.tv.setText(zi);
        return convertView;
    }

    class ViewHolder{
        TextView tv;
        public ViewHolder(View view){
            tv = view.findViewById(R.id.search_right_item_tv);
        }
    }
}
