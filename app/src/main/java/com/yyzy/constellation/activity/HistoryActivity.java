package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.adapter.HistoryAdapter;
import com.yyzy.constellation.entity.HistoryEntity;
import com.yyzy.constellation.entity.LaoHuangLiEntity;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv;
    private ImageButton imgBtn;
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

    @Override
    protected int initLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected void initView() {
        lv = findViewById(R.id.history_lv);
        imgBtn = findViewById(R.id.history_imgBtn);
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        imgBtn.setOnClickListener(this);
        imgBack.setOnClickListener(this);
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
                Intent intent = new Intent(HistoryActivity.this,MoreHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                if (entity != null){
                    bundle.putSerializable("historyBean",entity);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
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
    }

    private void loadLaoHuangliData(String laohuangliURL) {
        RequestParams params = new RequestParams(laohuangliURL);
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LaoHuangLiEntity huangLiEntity = new Gson().fromJson(result, LaoHuangLiEntity.class);
                LaoHuangLiEntity.ResultBean resultBean = huangLiEntity.getResult();
                if (resultBean != null) {
                    lin.setVisibility(View.VISIBLE);
                    //lv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                    tvHistoryToday.setText("历史上的这一天");
                    tvNongli.setText("农历" + resultBean.getYinli() + "（阴历）");
                    String[] arr = resultBean.getYangli().split("-");
                    String week = getWeek(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
                    tvGongli.setText("公历" + arr[0] + "年" + arr[1] + "月" + arr[2] + "日" + week + "（阳历）");
                    tvToday.setText(arr[2]);
                    tvWeek.setText(week);
                    tvBaiji.setText("彭祖百忌：" + resultBean.getBaiji());
                    tvWuxing.setText("五行：" + resultBean.getWuxing());
                    tvChongsha.setText("冲煞：" + resultBean.getChongsha());
                    tvJishen.setText("吉神宜趋：" + resultBean.getJishen());
                    tvXiongshen.setText("凶神宜忌：" + resultBean.getXiongshen());
                    tvJi.setText("忌：" + resultBean.getJi());
                    tvYi.setText("宜：" + resultBean.getYi());
                    return;
                } else {
                    lin.setVisibility(View.GONE);
                    //lv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    MyToast.showText(HistoryActivity.this, "老黄历今日访问次数上限！", false);
                    tvWeek.setText("暂无数据！");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
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
            List<HistoryEntity.ResultBean> li = entity.getResult();
            if (entity != null || entity.getError_code() == 0) {
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
        switch (v.getId()) {
            case R.id.history_imgBtn:
                showCalendarDialog();
                break;
            case R.id.details_back:
                finish();
                break;
        }
    }

    private void showCalendarDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String time = year + "-" + (month + 1) + "-" + dayOfMonth;
                String laohuangliURL = URLContent.getLaohuangliURL(time);
                tvTitle.setText(time);
                loadLaoHuangliData(laohuangliURL);

                String url = URLContent.getTodayHistoryURL("1.0", (month + 1), dayOfMonth);
                loadDatas(url);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}