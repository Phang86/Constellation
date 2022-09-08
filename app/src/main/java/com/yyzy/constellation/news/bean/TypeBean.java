package com.yyzy.constellation.news.bean;

import com.yyzy.constellation.utils.URLContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypeBean implements Serializable {
    private int id;
    private String title;
    private String url;
    private boolean isShow;

    public TypeBean(int id, String title, String url, boolean isShow) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.isShow = isShow;
    }

    public TypeBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public static List<TypeBean> getTypeList(){
        List<TypeBean> mData = new ArrayList<>();
        TypeBean tb1 = new TypeBean(1, "头条", URLContent.headline_url, true);
        TypeBean tb2 = new TypeBean(2, "社会", URLContent.science_url, true);
        TypeBean tb3 = new TypeBean(3, "国内", URLContent.home_url, true);
        TypeBean tb4 = new TypeBean(4, "国际", URLContent.internation_url, true);
        TypeBean tb5 = new TypeBean(5, "娱乐", URLContent.entertainment_url, true);
        TypeBean tb6 = new TypeBean(6, "体育", URLContent.sport_url, true);
        TypeBean tb7 = new TypeBean(7, "军事", URLContent.military_url, true);
        TypeBean tb8 = new TypeBean(8, "科技", URLContent.science_url, true);
        TypeBean tb9 = new TypeBean(9, "财经", URLContent.finance_url, true);
        TypeBean tb10 = new TypeBean(10, "时尚", URLContent.fashion_url, true);
        mData.add(tb1);
        mData.add(tb2);
        mData.add(tb3);
        mData.add(tb4);
        mData.add(tb5);
        mData.add(tb6);
        mData.add(tb7);
        mData.add(tb8);
        mData.add(tb9);
        mData.add(tb10);
        return mData;
    } 
}
