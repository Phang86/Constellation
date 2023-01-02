package com.yyzy.constellation.weather.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.yyzy.constellation.R;

import java.util.List;


public class DeleteCityAdapter extends BaseAdapter {
    private Context context;
    private List<String> mData;
    private List<String> deleteCity;

    public DeleteCityAdapter(Context context, List<String> mData, List<String> deleteCity) {
        this.context = context;
        this.mData = mData;
        this.deleteCity = deleteCity;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_deletecity,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String city = mData.get(position);
        holder.tv.setText(city);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(city);
                deleteCity.add(city);
                notifyDataSetChanged();      //提示适配器更新
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        CardView iv;
        public ViewHolder(View view){
            tv = view.findViewById(R.id.item_deleteCity_tv);
            iv = view.findViewById(R.id.del_cv);
        }
    }
}
