package com.yyzy.constellation.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class WelcomeAdapter extends PagerAdapter {
    private List<ImageView> mData;

    public WelcomeAdapter(List<ImageView> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull  Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = mData.get(position);
        container.addView(imageView);
        return  imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        ImageView imageView = mData.get(position);
        container.removeView(imageView);
    }
}
