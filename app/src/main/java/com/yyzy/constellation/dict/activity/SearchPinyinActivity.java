package com.yyzy.constellation.dict.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.BaseSearchActivity;
import com.yyzy.constellation.dict.adapter.SearchLeftAdapter;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.entity.PinBuEntity;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.CommonUtils;
import com.yyzy.constellation.utils.URLContent;

import java.util.ArrayList;
import java.util.List;

import static com.yyzy.constellation.utils.CommonUtils.FILE_PINYIN;
import static com.yyzy.constellation.utils.CommonUtils.TYPE_BUSHOU;
import static com.yyzy.constellation.utils.CommonUtils.TYPE_PINYIN;

public class SearchPinyinActivity extends BaseSearchActivity {
    String url;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv.setText(R.string.pinyinQuery);
        initData(CommonUtils.FILE_PINYIN, CommonUtils.TYPE_PINYIN);
        setExlvListener(CommonUtils.TYPE_PINYIN);
        listView.expandGroup(0);//默认展开第一组
        word = "a";      //进去时获取的是第一个a
        url = URLContent.getPinyinurl(word,page,pageSize);
        //加载网络数据
        loadData(url);
        loadNetDataResult(this,url);
        setGVListener(CommonUtils.TYPE_PINYIN);
    }

    //网络获取失败时，调用的接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        List<PinBuWordEntity.ResultBean.ListBean> list = DBmanager.queryPyWordFromPywordtb(word, page, pageSize);
        refreshDataByGV(list);
    }
}