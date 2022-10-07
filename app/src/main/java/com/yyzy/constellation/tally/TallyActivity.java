package com.yyzy.constellation.tally;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yyzy.constellation.tally.util.BeiZhuDialog;
import com.yyzy.constellation.tally.util.OnClickSure;
import com.yyzy.constellation.tally.util.TallyInfoDialog;
import com.yyzy.constellation.tally.util.TallyMoneyDialog;
import com.yyzy.constellation.tally.util.TallyMoreDialog;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TallyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack,imgAdd,imgMore;
    private TextView tvTitle;
    private ListView lv;
    private List<TallyLvItemBean> mData;
    private TallyLVAdapter adapter;
    private SmartRefreshLayout refreshLayout;

    private int year,month,day;

    private TextView tvOut,tvIn,tvYusuan,tvLook,tvSum;
    private ImageView imgLookMoney;
    private float outSumMoneyOneDay;
    private float inSumMoneyOneDay;
    private SharedPreferences spf;

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
        spf = getSharedPreferences("jilu", Context.MODE_PRIVATE);
    }

    @Override
    protected void initData() {
        Calendar ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH)+1;
        day = ca.get(Calendar.DAY_OF_MONTH);

        mData = TallyManger.getItemAll(year,month,day);
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
        initLvHeaderView(view);
        //给listView  item添加长按事件
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    //MyToast.showText(context,"此纪录不可删除哦！");
                    return false;
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

        //给listView  item添加单击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    return;
                }else{
                    TallyLvItemBean itemBean = mData.get(position-1);
                    TallyInfoDialog dialog = new TallyInfoDialog(TallyActivity.this, itemBean);
                    dialog.show();
//                    dialog.setWindowSize();
                }
            }
        });
    }

    private void initLvHeaderView(View view){
        tvOut = view.findViewById(R.id.tally_lv_header_tv_Out);
        tvIn = view.findViewById(R.id.tally_lv_header_tv_In);
        tvYusuan = view.findViewById(R.id.tally_lv_header_tv_yusuan);
        tvLook = view.findViewById(R.id.tally_lv_header_tv_look);
        tvSum = view.findViewById(R.id.tally_lv_header_tv_sum);
        imgLookMoney = view.findViewById(R.id.tally_lv_header_img_eyes);
        imgLookMoney.setOnClickListener(this);
        tvLook.setOnClickListener(this);
        tvYusuan.setOnClickListener(this);
        initHeaderViewData();
    }

    private void initHeaderViewData() {
        outSumMoneyOneDay = TallyManger.getSumMoneyOneDay(year, month, day, 0);
        inSumMoneyOneDay = TallyManger.getSumMoneyOneDay(year, month, day, 1);
        String str = "今日支出：￥"+outSumMoneyOneDay+"\t\t\t今日收入：￥"+inSumMoneyOneDay;
        tvSum.setText(str);
        float outMoneyOneMonth = TallyManger.getSumMoneyOneMonth(year, month, 0);
        float inMoneyOneMonth = TallyManger.getSumMoneyOneMonth(year, month, 1);
        tvOut.setText("￥"+outMoneyOneMonth);
        tvIn.setText("￥"+inMoneyOneMonth);
        float money = spf.getFloat("money", 0);
        if (money == 0){
            tvYusuan.setText("￥0.0");
        }else{
            float m = money - outMoneyOneMonth;
            tvYusuan.setText("￥"+m);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tally_img_back:
                finish();
                break;
            case R.id.tally_img_add:
                startIntent(RecordActivity.class);
                break;
            case R.id.tally_img_more:
                TallyMoreDialog dialog = new TallyMoreDialog(this);
                dialog.show();
                dialog.setCancelable(false);
                dialog.setDialogSize();
                dialog.setRefresh(new TallyMoreDialog.RefreshListener() {
                    @Override
                    public void refreshData() {
                        onRefresh();
                        MyToast.showText(getBaseContext(),"记录清理完毕！");
                    }
                });
                break;
            case R.id.tally_lv_header_img_eyes:
                toggleShow();
                break;
            case R.id.tally_lv_header_tv_look:
                startIntent(ChartActivity.class);
                break;
            case R.id.tally_lv_header_tv_yusuan:
                float money = spf.getFloat("money", 0);
                float sheng = TallyManger.getSumMoneyOneMonth(year, month, 0);
                float yu;
                if (String.valueOf(money).equals("￥0.0")){
                    yu = 0;
                }else{
                    yu = money - sheng;
                }
                showDialogBottom(""+money,yu);
                break;
        }
    }

    boolean isShow = true;
    private void toggleShow() {
        if (isShow){
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            tvOut.setTransformationMethod(passwordMethod);
            tvIn.setTransformationMethod(passwordMethod);
            tvYusuan.setTransformationMethod(passwordMethod);
            imgLookMoney.setImageResource(R.drawable.icon_eyes_hint);
            isShow = false;
        }else{
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            tvOut.setTransformationMethod(hideMethod);
            tvIn.setTransformationMethod(hideMethod);
            tvYusuan.setTransformationMethod(hideMethod);
            imgLookMoney.setImageResource(R.drawable.icon_eyes);
            isShow = true;
        }
    }

    private void showDialogBottom(String money, float yu) {
        TallyMoneyDialog dialog = new TallyMoneyDialog(this);
        dialog.show();
        dialog.setTvSum("总预算金额"+"（￥"+money+"）"+"\n"+"预算余额（￥"+yu+"）");
        dialog.setWindowSize();
        dialog.setClickEnSure(new TallyMoneyDialog.OnClickEnSure() {
            @Override
            public void onClickSure(Float money) {
                SharedPreferences.Editor edit = spf.edit();
                float sumMoneyOneMonth = TallyManger.getSumMoneyOneMonth(year, month, 0);
                float m = money - sumMoneyOneMonth;
                edit.putFloat("money",money);
                edit.commit();
                tvYusuan.setText("￥"+m);

            }
        });
    }

    private void startIntent(Class clc) {
        Intent intent = new Intent(this,clc);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRefresh();
    }

    private void onRefresh(){
        mData.clear();
        mData = TallyManger.getItemAll(year,month,day);
//        mData = TallyManger.getAll();
        adapter = new TallyLVAdapter(this,mData);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initHeaderViewData();
    }
}