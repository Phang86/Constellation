package com.yyzy.constellation.weather.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.weather.db.DatabaseEntity;
import com.yyzy.constellation.weather.entity.WeatherEntity;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    private Context context;
    private List<DatabaseEntity> mData;

    public CityManagerAdapter(Context context,List<DatabaseEntity> mData){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_manager,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        DatabaseEntity entity = mData.get(position);
        holder.tvCity.setText(entity.getCity());
        WeatherEntity json = new Gson().fromJson(entity.getContent(), WeatherEntity.class);
        WeatherEntity.ResultDTO result = json.getResult();
        WeatherEntity.ResultDTO.RealtimeDTO realtime = result.getRealtime();
        WeatherEntity.ResultDTO.FutureDTO futureDTO = result.getFuture().get(0);

        holder.tvCondition.setText("天气："+realtime.getInfo());
        holder.tvWind.setText(realtime.getDirect()+realtime.getPower());
        holder.tvTemp.setText(realtime.getTemperature()+"℃");
        holder.tvTempRange.setText(futureDTO.getTemperature());
        return convertView;
    }

    class ViewHolder{
        TextView tvCity,tvCondition,tvWind,tvTempRange,tvTemp;
        public ViewHolder(View view){
            tvCity = view.findViewById(R.id.item_city_tv_city);
            tvCondition = view.findViewById(R.id.item_city_tv_condition);
            tvWind = view.findViewById(R.id.item_city_tv_wind);
            tvTempRange = view.findViewById(R.id.item_city_tv_tempRange);
            tvTemp = view.findViewById(R.id.item_city_tv_temp);
        }
    }
}
