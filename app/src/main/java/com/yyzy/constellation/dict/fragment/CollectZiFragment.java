package com.yyzy.constellation.dict.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.dict.activity.ChengYuInfoActivity;
import com.yyzy.constellation.dict.activity.WordInfoActivity;
import com.yyzy.constellation.dict.db.DBmanager;

import java.util.ArrayList;
import java.util.List;

public class CollectZiFragment extends Fragment {
    private SwipeRefreshLayout refreshLayout;
    private GridView gv;
    private String type;
    private List<String> mData;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_zi, container, false);
        gv = view.findViewById(R.id.collect_zi_frag);
        refreshLayout = view.findViewById(R.id.collect_refresh_layout);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        mData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.search_right_item_gv, R.id.search_right_item_tv, mData) ;
        gv.setAdapter(adapter);
        //给GridView设置点击事件
        setGvListener();
        initData();
        return view;
    }

    private void initData() {
        //设置下拉出现小圆圈是否是缩放出现,出现的位置,最大的下拉位置
        refreshLayout.setProgressViewOffset(true,-120,200);
        //设置下拉圆圈的大小,两个值 LARGE大,DEFAULT默认
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //设置下拉圆圈上的颜色:蓝色、绿色、橙色、红色
        refreshLayout.setColorSchemeResources(R.color.blue);
        //通过setEnabled设置禁用下拉刷新
        refreshLayout.setEnabled(true);
        //设置下拉圆圈的背景
        refreshLayout.setProgressBackgroundColor(R.color.white);
        //设置手势下拉刷新的监听,一般这里重新请求一下接口
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新动画开始后回调到此方法
                //Toast.makeText(RefreshActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        //停止刷新,圆圈消失
                        refreshLayout.setRefreshing(false);
                    }
                },500);
            }
        });
    }


    public void loadData() {
        mData.clear();
        List<String> list;
        if (type.equals("成语")){
            list = DBmanager.queryAllInCollCyutb();
        }else{
            list = DBmanager.queryAllInCollwordtb();
        }
        mData.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void setGvListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (type.equals("成语")){
                    String cyu = mData.get(position);
                    intent.setClass(getContext(), ChengYuInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("chengyu",cyu);
                    startActivity(intent);
                }else {
                    String zi = mData.get(position);
                    intent.setClass(getContext(), WordInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("zi",zi);
                    startActivity(intent);
                }
            }
        });
    }


    //当页面启动时，自动刷新加载数据
    @Override
    public void onStart() {
        super.onStart();
        refreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                refreshLayout.setRefreshing(false);
            }
        }, 500);
    }
}