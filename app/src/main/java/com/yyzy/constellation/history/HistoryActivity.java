package com.yyzy.constellation.history;

import androidx.annotation.NonNull;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.activity.MoreHistoryActivity;
import com.yyzy.constellation.history.adapter.HistoryAdapter;
import com.yyzy.constellation.history.bean.HistoryEntity;
import com.yyzy.constellation.history.bean.LaoHuangLiEntity;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;

import org.jetbrains.annotations.NotNull;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends BaseActivity implements View.OnClickListener {

    private SmartRefreshLayout refreshLayout;
    private LinearLayout linLayout;
    private TextView tv;
    private ListView lv;
    private List<HistoryEntity.ResultBean> mData;
    private HistoryAdapter adapter;
    private Calendar calendar;
    private Date date;
    //listView头布局控件
    private TextView tvGongli, tvToday, tvWeek, tvNongli, tvBaiji, tvWuxing, tvChongsha, tvJishen, tvXiongshen, tvJi, tvYi, tvHistoryToday;
    private LinearLayout lin;
    private HistoryEntity entity;
    private ImageView imgBack;
    private TextView tvTitle;

    //listView尾布局控件
    private LinearLayout tvLoadMore;
    private DiyProgressDialog dialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected void initView() {
        lv = findViewById(R.id.history_lv);
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        linLayout = findViewById(R.id.history_layout);
        tv = findViewById(R.id.history_tv);
        refreshLayout = findViewById(R.id.smr_refreshLayout);
        imgBack.setOnClickListener(this);
        showProgress();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        lv.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    private void showProgress(){
        dialog = new DiyProgressDialog(this, "加载中...");
        dialog.show();
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        adapter = new HistoryAdapter(this, mData);
        lv.setAdapter(adapter);

        calendar = Calendar.getInstance();
        date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String url = URLContent.getTodayHistoryURL("1.0", month, day);
        loadDatas(url);
        addHeaderAndFooterView();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String todayUrl = URLContent.getTodayHistoryURL("1.0", month, day);
                        loadDatas(todayUrl);
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String time = format.format(date);
                        Log.e("TAG", "onRefresh: "+time);
                        String laohuangliURL = URLContent.getLaohuangliURL(time);
                        loadLaoHuangliData(laohuangliURL);
                        tvTitle.setText(time);
                        refreshLayout.finishRefresh(500);
                        MyToast.showText(getBaseContext(),"已刷新到当前时间！",true);
                        if (dialog != null) {
                            dialog.cancel();
                        }
                    }
                },200);
                //dialog.show();

                //dialog.cancel();
            }
        });
    }

    private void addHeaderAndFooterView() {
        //给listView添加头布局
        View header = LayoutInflater.from(this).inflate(R.layout.list_header_view, null);
        initHeaderView(header);
        lv.addHeaderView(header);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(HistoryActivity.this, HistoryInfoActivity.class);
                    HistoryEntity.ResultBean resultBean = mData.get(position-1);
                    if (resultBean != null) {
                        String info_id = resultBean.get_id();
                        intent.putExtra("info_id",info_id);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        //给listView添加尾布局
        View footer = LayoutInflater.from(this).inflate(R.layout.list_footer_view, null);
        addOnclickListener(footer);
        lv.addFooterView(footer);
    }

    private void addOnclickListener(View footer) {
        tvLoadMore = footer.findViewById(R.id.footer_tv_load_more);
        tvLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MoreHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                if (entity != null){
                    bundle.putSerializable("historyBean",entity);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                dialog.dismiss();
            }
        });
    }

    private void initHeaderView(View header) {
        tvGongli = header.findViewById(R.id.header_tv_gongli);
        tvToday = header.findViewById(R.id.header_tv_today);
        tvWeek = header.findViewById(R.id.header_tv_week);
        tvNongli = header.findViewById(R.id.header_tv_nongli);
        tvBaiji = header.findViewById(R.id.header_layout_tv_baiji);
        tvWuxing = header.findViewById(R.id.header_layout_tv_wuxing);
        tvChongsha = header.findViewById(R.id.header_layout_tv_chongsha);
        tvJishen = header.findViewById(R.id.header_layout_tv_jishen);
        tvXiongshen = header.findViewById(R.id.header_layout_tv_xiongshen);
        tvJi = header.findViewById(R.id.header_layout_tv_ji);
        tvYi = header.findViewById(R.id.header_layout_tv_yi);
        lin = header.findViewById(R.id.header_lin);
        tvHistoryToday = header.findViewById(R.id.header_tv_history_today);

        //将日期对象转换成字符串形式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        String laohuangliURL = URLContent.getLaohuangliURL(time);
        loadLaoHuangliData(laohuangliURL);
        tvTitle.setText(time);
        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog();
                dialog.dismiss();
            }
        });
    }

    private void loadLaoHuangliData(String laohuangliURL) {
        RequestParams params = new RequestParams(laohuangliURL);
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LaoHuangLiEntity huangLiEntity = new Gson().fromJson(result, LaoHuangLiEntity.class);
                LaoHuangLiEntity.ResultBean resultBean = huangLiEntity.getResult();
                if (resultBean != null) {
                    tvHistoryToday.setText("历史上的这一天");
                    tvNongli.setText("农历" + resultBean.getYinli() + "（阴历）");
                    String[] arr = resultBean.getYangli().split("-");
                    String week = getWeek(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
                    tvGongli.setText("公历" + arr[0] + "年" + arr[1] + "月" + arr[2] + "日" + week + "（阳历）");
                    tvToday.setText(arr[2]);
                    tvWeek.setText(week);
                    //Log.e("TAG", "onSuccess: "+resultBean.getYi()+resultBean.getXiongshen());
                    tvBaiji.setText("彭祖百忌：" + resultBean.getBaiji());
                    tvWuxing.setText("五行：" + resultBean.getWuxing());
                    tvChongsha.setText("冲煞：" + resultBean.getChongsha());
                    tvJishen.setText("吉神宜趋：" + resultBean.getJishen());
                    tvXiongshen.setText("凶神宜忌：" + resultBean.getXiongshen());
                    tvJi.setText("忌：" + resultBean.getJi());
                    tvYi.setText("宜：" + resultBean.getYi());
                    showOrHide(true);
                } else {
                    MyToast.showText(HistoryActivity.this, "老黄历今日访问次数上限！", false);
                    showOrHide(false);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError: "+isOnCallback);
                if (!isOnCallback){
                    showOrHide(false);
                    MyToast.showText(HistoryActivity.this,"网络暂未开启！"+isOnCallback);
                }else{
                    showOrHide(false);
                    MyToast.showText(HistoryActivity.this,"接口访问次数受限！");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                dialog.dismiss();
            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    public void showOrHide(boolean isShow){
        if (isShow) {
            lv.setVisibility(View.VISIBLE);
            linLayout.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }else{
            lv.setVisibility(View.GONE);
            linLayout.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
            tv.setText("抱歉，暂无数据！");
        }
        dialog.dismiss();
    }

    //根据年月日获取星期几
    private String getWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        String week[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (index < 0) {
            index = 0;
        }
        return week[index];
    }


    @Override
    public void onSuccess(String result) {
        try {
            mData.clear();
            entity = new Gson().fromJson(result, HistoryEntity.class);
            if (entity != null || entity.getError_code() == 0) {
                List<HistoryEntity.ResultBean> li = entity.getResult();
                for (int i = 0; i < 15; i++) {
                    mData.add(li.get(i));
                }
                adapter.notifyDataSetChanged();
                return;
            } else {
                MyToast.showText(this, "今日访问次数上限！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private void showCalendarDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String time = year + "-" + (month + 1) + "-" + dayOfMonth;
                        String laohuangliURL = URLContent.getLaohuangliURL(time);
                        tvTitle.setText(time);
                        loadLaoHuangliData(laohuangliURL);
                        String url = URLContent.getTodayHistoryURL("1.0", (month + 1), dayOfMonth);
                        loadDatas(url);
                        if (HistoryActivity.this.dialog != null){
                            HistoryActivity.this.dialog.cancel();
                        }
                    }
                },200);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}