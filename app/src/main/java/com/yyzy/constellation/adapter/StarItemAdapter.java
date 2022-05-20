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

public class StarItemAdapter extends BaseAdapter {
    private Context context;
    private List<StarInfoEntity.StarinfoDTO> mDatas;
    private Map<String,Bitmap> logoImg;

    public StarItemAdapter(Context context, List<StarInfoEntity.StarinfoDTO> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        logoImg = AssetsUtils.getLogeImgMap();
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
        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_star_gv,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        StarInfoEntity.StarinfoDTO entity = mDatas.get(position);
        holder.tv.setText(entity.getName());
        //获取图片的名称，根据名称在内存中查找
        String logoname = entity.getLogoname();
        Bitmap bitmap = logoImg.get(logoname);
        holder.iv.setImageBitmap(bitmap);
        return convertView;
    }

    class ViewHolder{
        private TextView tv;
        private CircleImageView iv;
        public ViewHolder(View view) {
            tv = view.findViewById(R.id.item_tv);
            iv = view.findViewById(R.id.item_iv);
        }
    }
}
