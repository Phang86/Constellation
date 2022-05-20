package com.yyzy.constellation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.utils.AssetsUtils;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LuckItemAdapter extends BaseAdapter {
    private final Map<String, Bitmap> contentLogoImgMap;
    private Context context;
    private List<StarInfoEntity.StarinfoDTO> mData;

    public LuckItemAdapter(Context context, List<StarInfoEntity.StarinfoDTO> infoBean) {
        this.context = context;
        this.mData = infoBean;
        contentLogoImgMap = AssetsUtils.getContentLogoImgMap();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_luck_gv,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        StarInfoEntity.StarinfoDTO bean = mData.get(position);
        holder.itemTv.setText(bean.getName());
        Bitmap bitmap = contentLogoImgMap.get(bean.getLogoname());
        holder.itemCv.setImageBitmap(bitmap);
        return convertView;
    }

    class ViewHolder{
        CircleImageView itemCv;
        TextView itemTv;
        public ViewHolder(View view){
            itemCv = view.findViewById(R.id.item_luck_cv);
            itemTv = view.findViewById(R.id.item_luck_tv);
        }
    }
}
