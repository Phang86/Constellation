package com.yyzy.constellation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class StarPagerAdapter extends PagerAdapter {
    private Context context;
    private List<ImageView> imgList;

    public StarPagerAdapter(Context context, List<ImageView> img) {
        this.context = context;
        this.imgList = img;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imgList.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView = imgList.get(position);
        container.removeView(imageView);
    }
}
