package com.yyzy.constellation.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;


import java.util.List;

public class TallyInfoDialog extends BottomSheetDialog implements View.OnClickListener{
    private TallyLvItemBean bean;
    private Button btnOk;
    private TextView tvTitle,tvType,tvBeiZhu,tvTime,tvMoney;

    public TallyInfoDialog(@NonNull Context context,TallyLvItemBean mData) {
        super(context);
        this.bean = mData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_and_in_info_layout);
        setWindowSize();
        initView();
        initData();
    }

    private void setWindowSize() {
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display dp = window.getWindowManager().getDefaultDisplay();
        wlp.width = dp.getWidth();
//        wlp.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }

    private void initView() {
        btnOk = findViewById(R.id.outInInfo_btn_ok);
        tvTitle = findViewById(R.id.outInInfo_tv_title);
        tvType = findViewById(R.id.outInInfo_tv_type);
        tvBeiZhu = findViewById(R.id.outInInfo_tv_beizhuInfo);
        tvTime = findViewById(R.id.outInInfo_tv_time);
        tvMoney = findViewById(R.id.outInInfo_tv_money);
        btnOk.setOnClickListener(this);
    }

    private void initData() {
        int outOrIn = bean.getOutOrIn();
        if (outOrIn == 0){
            tvTitle.setText("支出记录");
        }else{
            tvTitle.setText("收入记录");
        }
        if (outOrIn == 0){
            tvType.setText("支出类型："+bean.getTypeName());
        }else{
            tvType.setText("收入类型："+bean.getTypeName());
        }
        if (outOrIn == 0){
            tvMoney.setText("支出金额：￥"+bean.getMoney());
        }else{
            tvMoney.setText("收入金额：￥"+bean.getMoney());
        }
        String time = bean.getTime();
        String repTime = time.replace("年", "/").replace("月", "/").replace("日", "");
        if(outOrIn == 0){
            tvTime.setText("支出具体时间："+repTime);
        }else{
            tvTime.setText("收入具体时间："+repTime);
        }
        String beizhu = bean.getBeizhu();
        if (TextUtils.isEmpty(beizhu)){
            tvBeiZhu.setText("备注详情：无备注！");
        }else{
            tvBeiZhu.setText("备注详情："+bean.getBeizhu());
        }

    }

    @Override
    public void onClick(View v) {
        cancel();
    }
}
