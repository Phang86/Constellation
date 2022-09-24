package com.yyzy.constellation.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yyzy.constellation.R;
import com.yyzy.constellation.news.bean.NewsBean;

import java.util.List;

public class NewsItemAdapter extends BaseAdapter {

    private Context context;
    private List<NewsBean.ResultBean.DataBean> mData;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public NewsItemAdapter(Context context, List<NewsBean.ResultBean.DataBean> mData) {
        this.context = context;
        this.mData = mData;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                //正在加载中显示的图片
                .showImageOnLoading(R.mipmap.bg_defualt_220x150)
                //正在加载中遇到空字符串
                .showImageForEmptyUri(null)
                //加载失败显示的图片
                .showImageOnFail(null)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

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
        //判断内存中是否有复用的view
        if (convertView == null) {
            //将布局装换成新的view进行使用
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_lv,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            //如有复用的，直接调用
            holder = (ViewHolder) convertView.getTag();
        }
        //获取指定位置的数据源
        NewsBean.ResultBean.DataBean bean = mData.get(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvSource.setText(bean.getAuthor_name());
        holder.tvTime.setText(bean.getDate());
        //holder.imgTitle.setBackground();
        //获取图片的地址
        String pic1 = bean.getThumbnail_pic_s();
        String pic2 = bean.getThumbnail_pic_s02();
        String pic3 = bean.getThumbnail_pic_s03();
        try {
            if (TextUtils.isEmpty(pic1)){
                holder.carOne.setVisibility(View.GONE);
            }else{
                holder.carOne.setVisibility(View.VISIBLE);
                imageLoader.displayImage(pic1,holder.imgOne,options);
            }

            if (TextUtils.isEmpty(pic2)){
                holder.carTwo.setVisibility(View.GONE);
            }else{
                holder.carTwo.setVisibility(View.VISIBLE);
                imageLoader.displayImage(pic2,holder.imgTwo,options);
            }

            if (TextUtils.isEmpty(pic3)){
                holder.carThree.setVisibility(View.GONE);
            }else{
                holder.carThree.setVisibility(View.VISIBLE);
                imageLoader.displayImage(pic3,holder.imgThree,options);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder{
        TextView tvTitle,tvSource,tvTime;
        ImageView imgOne,imgTwo,imgThree,imgTitle;
        CardView carOne,carTwo,carThree;

        public ViewHolder(View view){
            tvTitle = view.findViewById(R.id.item_news_tv_title);
            tvSource = view.findViewById(R.id.item_news_tv_source);
            tvTime = view.findViewById(R.id.item_news_tv_time);
            imgOne = view.findViewById(R.id.item_news_img1);
            imgTwo = view.findViewById(R.id.item_news_img2);
            imgThree = view.findViewById(R.id.item_news_img3);
            carOne = view.findViewById(R.id.item_news_car1);
            carTwo = view.findViewById(R.id.item_news_car2);
            carThree = view.findViewById(R.id.item_news_car3);
            imgTitle = view.findViewById(R.id.item_news_imgHeader);
        }
    }
}
