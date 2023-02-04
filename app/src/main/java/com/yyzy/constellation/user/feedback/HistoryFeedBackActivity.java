package com.yyzy.constellation.user.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.entity.User;
import com.yyzy.constellation.user.CallBackOkhttp;
import com.yyzy.constellation.user.feedback.adapter.HistoryFeedBackAdapter;
import com.yyzy.constellation.user.feedback.bean.FeedBackBean;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;

public class HistoryFeedBackActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private RecyclerView rv;
    private HistoryFeedBackAdapter adapter;
    private List<FeedBackBean> mData = new ArrayList<>();
    private LinearLayout linNoDataLayout;
    private SmartRefreshLayout refreshLayout;
    private StaggeredGridLayoutManager manager;
    private ImageView iv;
    private SharedPreferences spf;
    private boolean isShowGirdOrList;

    @Override
    protected int initLayout() {
        return R.layout.activity_history_feed_back;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        rv = findViewById(R.id.history_feedback_rv);
        linNoDataLayout = findViewById(R.id.history_feedback_notdata_layout);
        refreshLayout = findViewById(R.id.smr_refreshLayout);
        iv = findViewById(R.id.details_right_iv);
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        spf = getSharedPreferences("constellation", MODE_PRIVATE);
        isShowGirdOrList = spf.getBoolean("isShowGirdOrList", true);
    }

    @Override
    protected void initData() {
        tvTitle.setText(getResources().getString(R.string.label_star_feedback));
        requestNet();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                if (mData.size() > 0) {
                    mData.clear();
                    adapter.notifyDataSetChanged();
                }
                //tvTitle.setText(getResources().getString(R.string.label_star_feedback)+"（"+mData.size()+"条）");
                requestNet();
                refreshLayout.finishRefresh(200);
            }
        });
    }

    private void requestNet() {
        loading();
        requestOkhttpLoadFeedback("","","user", base_user_names, "/user/findFeedBack", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                //没有反馈信息
                if (resultStr.equals("error")) {
                    linNoDataLayout.setVisibility(View.VISIBLE);
                    refreshDatas();
                    return;
                }
                //有反馈信息
                List<FeedBackBean> dataEntity = new Gson().fromJson(resultStr, new TypeToken<List<FeedBackBean>>() {
                }.getType());
                showData(dataEntity);
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(HistoryFeedBackActivity.this, "服务器连接超时！");
                Log.e("TAG", "LoadData_onError: " + e.getMessage());
                linNoDataLayout.setVisibility(View.VISIBLE);
                refreshDatas();
            }
        });
    }

    private void showData(List<FeedBackBean> dataEntity) {
        mData = dataEntity;
        adapter = new HistoryFeedBackAdapter(HistoryFeedBackActivity.this, mData, new OnLongClick() {
            @Override
            public void onLongClick(FeedBackBean bean, View v) {
                //进行逻辑删除处理
                showContextMenu(bean, v);
            }
        });
        isShowGirdOrList = spf.getBoolean("isShowGirdOrList", true);
        showLayoutStyle(isShowGirdOrList);
        rv.setAdapter(adapter);

        refreshDatas();
    }

    private void showLayoutStyle(boolean isShowGirdOrList) {
        if (isShowGirdOrList) {
            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            iv.setImageResource(R.mipmap.finder_sort_grid);
        } else {
            manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            iv.setImageResource(R.mipmap.finder_sort_list);
        }
    }

    private void showContextMenu(FeedBackBean bean, View v) {
        QuickPopupBuilder.with(this)
                .contentView(R.layout.show_popupmenu)
                .config(new QuickPopupConfig()
                        .backgroundColor(Color.TRANSPARENT)
                        .gravity(Gravity.CENTER_VERTICAL)
                        .withClick(R.id.tv_del, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delUserFeedback(bean);
                            }
                        }, true)

                )
                .show(v);
    }

    private void delUserFeedback(FeedBackBean bean) {
        loading();
        Integer id = bean.getId();
        String strId = String.valueOf(id);
        //发起网络请求
        requestOkhttpLoadFeedback("","","id", strId, "/user/del/feedback", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                if (resultStr.equals("error")) {
                    MyToast.showText(HistoryFeedBackActivity.this, "删除失败！");
                    return;
                }
                mData.remove(bean);
                adapter.notifyDataSetChanged();
                refreshDatas();
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(HistoryFeedBackActivity.this, "服务器连接超时！");
                Log.e("TAG", "DELETE_onError: " + e.getMessage());
            }
        });
    }

    private void refreshDatas() {
        if (mData.size() > 0) {
            tvTitle.setText(getResources().getString(R.string.label_star_feedback) + "（" + mData.size() + "条）");
            linNoDataLayout.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText(getResources().getString(R.string.label_star_feedback));
            linNoDataLayout.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.details_right_iv:
                isListOrGridShow(isShowGirdOrList);
                break;
        }
    }


    private void isListOrGridShow(boolean flag) {
        SharedPreferences.Editor edit = spf.edit();
        if (flag) {
            iv.setImageResource(R.mipmap.finder_sort_grid);
            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            isShowGirdOrList = false;
            iv.setImageResource(R.mipmap.finder_sort_list);
            manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
        } else {
            iv.setImageResource(R.mipmap.finder_sort_list);
            manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            isShowGirdOrList = true;
            iv.setImageResource(R.mipmap.finder_sort_grid);
            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
        }
        edit.putBoolean("isShowGirdOrList", isShowGirdOrList);
        edit.commit();
    }


}