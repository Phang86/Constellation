package com.yyzy.constellation.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.yyzy.constellation.R;

import org.jetbrains.annotations.NotNull;

public class BeiZhuDialog extends Dialog implements View.OnClickListener, TextWatcher {
    private EditText et;
    private Button btnCancel,btnConfirm;
    private OnClickSure clickSure;
    private TextView tv;

    public void setClickSure(OnClickSure clickSure) {
        this.clickSure = clickSure;
    }

    public BeiZhuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tally_dialog_bottom);
        et = findViewById(R.id.tally_dialog_et);
        btnCancel = findViewById(R.id.tally_dialog_btn_cancel);
        btnConfirm = findViewById(R.id.tally_dialog_btn_confirm);
        tv = findViewById(R.id.tally_dialog_tv);
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
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnConfirm.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = et.getText().toString().trim();
        if (!TextUtils.isEmpty(text) && et.length() <= 50) {
            btnConfirm.setEnabled(true);
        }else{
            btnConfirm.setEnabled(false);
        }
    }
}
