package com.yyzy.constellation.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.yyzy.constellation.R;

import org.jetbrains.annotations.NotNull;

public class Mydialog extends Dialog {
    private ClickSure clickSure;
    private TextView tvTitle,tvContent;
    private Button mBtnSure;
    private Context mContext;
    private String title,content;

    public void setClickSure(ClickSure clickSure) {
        this.clickSure = clickSure;
    }

    public Mydialog(Context context, String title, String content) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diy_alert_dialog_sure);
        mBtnSure = (Button) findViewById(R.id.dialog_two_btn_sure);
        tvTitle = findViewById(R.id.dialog_two_title);
        tvContent = findViewById(R.id.dialog_two_content);
        tvTitle.setText(title);
        tvContent.setText(content);
        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSure.onClickSure();
            }
        });
    }

    public void setDialogSize(){
        //获取窗口对象
        Window window = getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
        wlp.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }


    public interface ClickSure{
        void onClickSure();
    }
}
