package com.yyzy.constellation.dict.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CollectFragmentAdapter extends FragmentStatePagerAdapter {
    List<Fragment> list;
    String[] title;

    public CollectFragmentAdapter(@NonNull @NotNull FragmentManager fm,List<Fragment> list,String[] title) {
        super(fm);
        this.list = list;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
