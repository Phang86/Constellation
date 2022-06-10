package com.yyzy.constellation.dict.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.dict.BaseSearchActivity;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.utils.CommonUtils;
import com.yyzy.constellation.utils.HttpUtils;
import com.yyzy.constellation.utils.URLContent;

import java.util.List;

public class SearchBushouActivity extends BaseSearchActivity {

    String url;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv.setText(R.string.bushouQuery);
        initData(CommonUtils.FILE_BUSHOU, CommonUtils.TYPE_BUSHOU);
        setExlvListener(CommonUtils.TYPE_BUSHOU);
        listView.expandGroup(0);        //默认展开第一组
        word = "丨";      //进去时获取的是第一个a
        url = URLContent.getBushouurl(word,page,pageSize);
        //加载网络数据
        loadData(url);
        loadNetDataResult(this,url);
    }




}