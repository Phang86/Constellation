package com.yyzy.constellation.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yyzy.constellation.R;

import org.jetbrains.annotations.NotNull;


public class BeiZhuDialog extends Dialog implements View.OnClickListener, TextWatcher {
    private EditText et;
    private TextView btnCancel,btnConfirm;
    private OnClickSure clickSure;
    private TextView tvLength;
    private Context context;

    public void setClickSure(OnClickSure clickSure) {
        this.clickSure = clickSure;
    }

    public BeiZhuDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tally_dialog_bottom);
        et = findViewById(R.id.tally_dialog_et);
        btnCancel = findViewById(R.id.tally_dialog_btn_cancel);
        btnConfirm = findViewById(R.id.tally_dialog_btn_confirm);
        tvLength = findViewById(R.id.tally_dialog_tv_length);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        et.addTextChangedListener(this);
        btnConfirm.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tally_dialog_btn_cancel:
                if (clickSure != null){
                    clickSure.onCancel();
                    cancel();
                }
                break;
            case R.id.tally_dialog_btn_confirm:
                if (clickSure != null) {
                    clickSure.onSure();
                }
                break;
        }
    }

    public String getEtText(){
        return et.getText().toString().trim();
    }

    public void setEtText(String etText){
        et.setText(etText);
    }

    public void setDialogSize(){
        //获取窗口对象
        Window window = getWindow();
        if (window != null) {
            //获取窗口对象参数
            WindowManager.LayoutParams wlp = window.getAttributes();
            //获取屏幕尺寸
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.dimAmount = 0.2f;
            wlp.gravity = Gravity.BOTTOM;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(wlp);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        tvLength.setText("0/50");
        btnConfirm.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = et.getText().toString().trim();
        tvLength.setText(text.length()+"/50");
        if (text.length() > 50){
            tvLength.setTextColor(context.getResources().getColor(R.color.red));
            tvLength.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tvLength.getPaint().setAntiAlias(true);//抗锯齿
        }else{
            tvLength.setTextColor(context.getResources().getColor(R.color.grey));
            tvLength.getPaint().setFlags(Paint.HINTING_OFF);
            tvLength.getPaint().setAntiAlias(true);
        }
        if (!TextUtils.isEmpty(text) && et.length() <= 50) {
            btnConfirm.setEnabled(true);
        }else{
            btnConfirm.setEnabled(false);
        }
    }
}
