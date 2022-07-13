package com.yyzy.constellation.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.yyzy.constellation.R;

import org.jetbrains.annotations.NotNull;

public class Mydialog extends AlertDialog implements View.OnClickListener{
    TextView text_read;
    Button mBtnCancel, mBtnConnect;
    Context mContext;
    Activity mActivity;

    protected Mydialog(Context context, Activity activity) {
        super(context);
        mContext = context;
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diy_alert_dialog);
        //mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        //mBtnCancel.setOnClickListener(this);
        ///mBtnConnect = (Button) findViewById(R.id.btn_connect);
        //mBtnConnect.setOnClickListener(this);
        //text_read = (TextView) findViewById(R.id.read_text);

        final SpannableStringBuilder style = new SpannableStringBuilder();

        //设置文字
        style.append("请您在使用前点击阅读《XXXX隐私政策》，如果同意，请点击下方“同意”按钮开始使用软件。");

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(mContext, "触发点击事件!", Toast.LENGTH_SHORT).show();
            }
        };
        style.setSpan(clickableSpan, 11, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_read.setText(style);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        style.setSpan(foregroundColorSpan, 11, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //配置给TextView
        text_read.setMovementMethod(LinkMovementMethod.getInstance());
        text_read.setText(style);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
