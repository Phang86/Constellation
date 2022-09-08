package com.yyzy.constellation.news.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.yyzy.constellation.news.bean.TypeBean;

import java.util.List;


public class NewsInfoAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<Fragment> fragmentList;
    private List<TypeBean> typeBeans;

    public NewsInfoAdapter(FragmentManager fm, Context context,List<Fragment> fragmentList, List<TypeBean> typeBeans) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
        this.typeBeans = typeBeans;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TypeBean typeBean = typeBeans.get(position);
        String title = typeBean.getTitle();
        return title;

    }
}
