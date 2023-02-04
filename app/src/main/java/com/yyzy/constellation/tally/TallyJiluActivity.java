package com.yyzy.constellation.tally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.tally.adapter.TallyLVAdapter;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.tally.util.TallyCalendarDialog;
import com.yyzy.constellation.tally.util.TallyInfoDialog;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TallyJiluActivity extends BaseActivity implements View.OnClickListener {

    private SmartRefreshLayout smartRefreshLayout;
    private ImageView imgBack,imgCalendar;
    private ListView lv;
    private TextView tvTitle;
    private List<TallyLvItemBean> mData = new ArrayList<>();
    private TallyLVAdapter adapter;
    private int year,month;
    private String time;
    private Calendar ca = Calendar.getInstance();
    private LinearLayout linearLayout;
    private DiyProgressDialog progressDialog;
    private int dialogSelPos = -1;
    private int dialogSelMonth = -1;
    private TextView tvNoData,tvResult;

    @Override
    protected int initLayout() {
        return R.layout.activity_tally_jilu;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.tally_jilu_img_back);
        imgCalendar = findViewById(R.id.tally_jilu_img_calendar);
        tvTitle = findViewById(R.id.tally_jilu_tv_title);
        lv = findViewById(R.id.tally_jilu_lv);
        linearLayout = findViewById(R.id.tally_jilu_no_data);
        smartRefreshLayout = findViewById(R.id.tally_jilu_smr_refreshLayout);
        tvNoData = findViewById(R.id.local_music_tv);
        tvResult = findViewById(R.id.tv_result);

        imgBack.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);

        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH)+1;

        time = year+"年"+month+"月";

        tvTitle.setText(time);

        adapter = new TallyLVAdapter(this,mData);
        lv.setAdapter(adapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                loadData(year,month);
                tvTitle.setText(time);

                refreshLayout.finishRefresh(500);
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(500);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TallyLvItemBean bean = mData.get(position);
                TallyInfoDialog dialog = new TallyInfoDialog(TallyJiluActivity.this, bean);
                dialog.show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TallyLvItemBean tallyLvItemBean = mData.get(position);
                Log.e("TAG", "onItemLongClick: "+tallyLvItemBean);
                AlertDialogUtils dialogUtils = AlertDialogUtils.getInstance();
                AlertDialogUtils.showConfirmDialog(TallyJiluActivity.this,"是否删除此纪录？","是","否");
                dialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog dialog) {
                        dialog.cancel();
                        if (TallyManger.delToData(tallyLvItemBean.getId())) {
                            loadData(year,month);
                            MyToast.showText(getBaseContext(),"删除成功！", Gravity.BOTTOM);
                        }else{
                            MyToast.showText(getBaseContext(),"删除失败！",Gravity.BOTTOM);
                        }
                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog dialog) {
                        dialog.cancel();
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        loadData(year,month);
    }


    private void loadData(int year,int month) {
        progressDialog = new DiyProgressDialog(this);
        linearLayout.setVisibility(View.GONE);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<TallyLvItemBean> list = TallyManger.getItemOfMonth(year, month);
                if (list.size() > 0) {
                    linearLayout.setVisibility(View.GONE);
                    tvResult.setVisibility(View.VISIBLE);
                    tvResult.setText("共有 "+list.size()+" 条记录...");
                }else{
                    linearLayout.setVisibility(View.VISIBLE);
                    tvNoData.setText(year+"年"+month+"月，"+"暂无数据！");
                    tvResult.setVisibility(View.GONE);
                }
                mData.clear();
                mData.addAll(list);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        },300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tally_jilu_img_back:
                finish();
                break;
            case R.id.tally_jilu_img_calendar:
                showCalendarDialog();
                break;
//            case R.id.tally_jilu_img_resetTime:
//                this.month = ca.get(Calendar.MONTH)+1;
//                this.year = ca.get(Calendar.YEAR);
//                loadData(year,month);
//                time = year+"年"+month+"月";
//                tvTitle.setText(time);
//                break;
        }
    }

    private void showCalendarDialog() {
        TallyCalendarDialog dialog = new TallyCalendarDialog(TallyJiluActivity.this,dialogSelPos,dialogSelMonth);
        dialog.show();
        dialog.setWindowSize();
        dialog.setRefreshListener(new TallyCalendarDialog.RefreshListener() {
            @Override
            public void RefreshData(int selPos, int year, int month) {
                TallyJiluActivity.this.year = year;
                TallyJiluActivity.this.month = month;
                time = TallyJiluActivity.this.year+"年"+TallyJiluActivity.this.month+"月";
                //MyToast.showText(TallyJiluActivity.this,time);
                tvTitle.setText(time);
                loadData(year,month);
                dialogSelPos = selPos;
                dialogSelMonth = month;
            }
        });
    }
}