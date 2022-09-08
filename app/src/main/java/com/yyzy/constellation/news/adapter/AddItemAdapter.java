package com.yyzy.constellation.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.news.bean.TypeBean;
import com.yyzy.constellation.utils.MyToast;

import java.util.List;

public class AddItemAdapter extends BaseAdapter {
    private Context context;
    private List<TypeBean> mData;

    public AddItemAdapter(Context context, List<TypeBean> mData) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.add_item_lv,null);
        TextView tvName = convertView.findViewById(R.id.add_item_lv_tv);
        ImageView imgShow = convertView.findViewById(R.id.add_item_lv_img);
        TypeBean typeBean = mData.get(position);
        tvName.setText(typeBean.getTitle());
        if (typeBean.isShow()) {
            imgShow.setBackgroundResource(R.mipmap.subscribe_checked);
            //flag = true;
        }else{
            imgShow.setBackgroundResource(R.mipmap.subscribe_unchecked);
            //flag = false;
        }
        if (position == 0 || position == 1){
            imgShow.setVisibility(View.INVISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToast.showText(context,"此纪录不可修改！");
                }
            });
        }else {
            imgShow.setVisibility(View.VISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeBean.setShow(!typeBean.isShow());  //改变选中状态
                    if (typeBean.isShow()) {
                        imgShow.setImageResource(R.mipmap.subscribe_checked);
                    } else {
                        imgShow.setImageResource(R.mipmap.subscribe_unchecked);
                    }
                }
            });
        }
        return convertView;
    }
}
