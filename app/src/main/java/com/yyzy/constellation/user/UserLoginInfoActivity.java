package com.yyzy.constellation.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.tally.util.SwipeListView;
import com.yyzy.constellation.user.adapter.LoginInfoAdapter;
import com.yyzy.constellation.user.alertPwd.SwipeCheckActivity;
import com.yyzy.constellation.user.bean.LoginInfoBean;
import com.yyzy.constellation.user.feedback.bean.FeedBackBean;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserLoginInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView iv;
    private SmartRefreshLayout refreshLayout;
    private SwipeListView myList;
    private LoginInfoAdapter adapter;
    private List<LoginInfoBean> mData;
    private LinearLayout noDataLayout;


    @Override
    protected int initLayout() {
        return R.layout.activity_user_login_info;
    }

    @Override
    protected void initView() {
        loading();
        tvTitle = findViewById(R.id.details_title);
        iv = findViewById(R.id.details_back);
        iv.setOnClickListener(this);
        refreshLayout = findViewById(R.id.login_info_refreshLayout);
        myList = findViewById(R.id.my_list);
        noDataLayout = findViewById(R.id.login_info_layout);

        View view = LayoutInflater.from(this).inflate(R.layout.list_footer_view,null);
        TextView tv = view.findViewById(R.id.tv_footer);
        tv.setText("没有更多数据啦~");
        myList.addFooterView(view);
    }

    @Override
    protected void initData() {
        tvTitle.setText("登录情况");
        refreshData();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                loading();
                if (mData != null) {
                    mData.clear();
                    adapter.notifyDataSetChanged();
                }
                refreshData();
                refreshLayout.finishRefresh();
            }
        });
    }

    private void refreshData() {
        requestOkhttpLoadFeedback("","","user", base_user_names, "/find/user/login/info", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                //暂无内容
                if (resultStr.equals("error")){
                    //MyToast.showText(UserLoginInfoActivity.this,"服务器连接超时！");
                    showOrHide(false);
                    Log.e("TAG", "onSuccess: "+resultStr.toString());
                    return;
                }
                //有信息
                showOrHide(true);
                List<LoginInfoBean> dataEntity = new Gson().fromJson(resultStr, new TypeToken<List<LoginInfoBean>>() {
                }.getType());
                showData(dataEntity);
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(UserLoginInfoActivity.this,"加载失败！服务器连接超时！");
                noDataLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showData(List<LoginInfoBean> dataEntity) {
        mData = dataEntity;
        Log.e("TAG", "showData: "+mData.toString());
        adapter = new LoginInfoAdapter(this, mData, new LoginInfoAdapter.OnClickDel() {
            @Override
            public void onClickDel(int position, LoginInfoBean bean) {
                //删除
                delLoginInfo(position);
            }

            @Override
            public void onClickUpdatePwd() {
                intentJump(SwipeCheckActivity.class);
            }
        });
        myList.setAdapter(adapter);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
    }

    private void delLoginInfo(int position) {
        loading();
        LoginInfoBean bean = mData.get(position);
        Integer id = bean.getId();
        String strId = String.valueOf(id);
        requestOkhttpLoadFeedback("","","id", strId, "/del/user/login/info", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                if (resultStr.equals("success")) {
                    MyToast.showText(UserLoginInfoActivity.this,"删除成功！", Gravity.BOTTOM);
                    if (mData != null) {
                        mData.clear();
                        adapter.notifyDataSetChanged();
                    }
                    refreshData();
                    return;
                }
                showOrHide(false);
                MyToast.showText(UserLoginInfoActivity.this,"删除失败！");
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(UserLoginInfoActivity.this,"删除失败！服务器连接超时！");
                showOrHide(false);
            }
        });
    }

    private void showOrHide(boolean flag){
        if (flag) {
            myList.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }else{
            myList.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mData != null) {
            mData.clear();
            adapter.notifyDataSetChanged();
        }
        refreshData();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}