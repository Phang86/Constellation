package com.yyzy.constellation.tally;

import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.tally.adapter.TallyLVAdapter;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TallyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack,imgAdd,imgMore;
    private TextView tvTitle;
    private ListView lv;
    private List<TallyLvItemBean> mData;
    private TallyLVAdapter adapter;
    private SmartRefreshLayout refreshLayout;

    @Override
    protected int initLayout() {
        return R.layout.activity_tally;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.tally_img_back);
        tvTitle = findViewById(R.id.tally_tv_title);
        imgAdd = findViewById(R.id.tally_img_add);
        imgMore = findViewById(R.id.tally_img_more);
        lv = findViewById(R.id.tally_lv);
        refreshLayout = findViewById(R.id.tally_refreshLayout);
        imgBack.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        imgMore.setOnClickListener(this);
        tvTitle.setText(getResources().getString(R.string.lifeTally));
    }

    @Override
    protected void initData() {
        mData = TallyManger.getItemAll();
        adapter = new TallyLVAdapter(this,mData);
        lv.setAdapter(adapter);
        lvAddHeaderView();
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(500);
                //数据源更新
                TallyActivity.this.onRefresh();
            }
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(500);
            }
        });
    }

    private void lvAddHeaderView() {
        View view = LayoutInflater.from(this).inflate(R.layout.tally_lv_header,null);
        lv.addHeaderView(view);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    //MyToast.showText(context,"此纪录不可删除哦！");
                }else{
                    AlertDialogUtils dialog = AlertDialogUtils.getInstance();
                    AlertDialogUtils.showConfirmDialog(context,"温馨提示","确定删除此纪录吗？","确定","取消");
                    dialog.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick(AlertDialog dialog) {
                            TallyLvItemBean bean = mData.get(position-1);

                            if (TallyManger.delToData(bean.getId())) {
                                MyToast.showText(context,"删除成功！");
                                onRefresh();
                            }else{
                                MyToast.showText(context,"删除失败！");
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClick(AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tally_img_back:
                finish();
                break;
            case R.id.tally_img_add:
                Intent intent = new Intent(this,RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tally_img_more:

                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRefresh();
    }

    private void onRefresh(){
        mData.clear();
        mData = TallyManger.getItemAll();
        adapter = new TallyLVAdapter(this,mData);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}