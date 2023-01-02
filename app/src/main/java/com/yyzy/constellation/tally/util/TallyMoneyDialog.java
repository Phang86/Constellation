package com.yyzy.constellation.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

public class TallyMoneyDialog extends Dialog implements View.OnClickListener, TextWatcher {

    private EditText etMoney;
    private TextView btnConfirm;
    private ImageView imgClose;
    private OnClickEnSure clickEnSure;
    private TextView tvSum;
    private Float money;

    public interface OnClickEnSure{
        void onClickSure(Float money);
    }

    public void setClickEnSure(OnClickEnSure clickEnSure) {
        this.clickEnSure = clickEnSure;
    }

    public TallyMoneyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tally_dialog_money);
        initView();
    }

    private void initView() {
        etMoney = findViewById(R.id.tally_dialog_money_et);
        btnConfirm = findViewById(R.id.tally_dialog_money_btn);
        imgClose = findViewById(R.id.tally_dialog_money_img);
        tvSum = findViewById(R.id.tally_dialog_money_tv_sum);
        imgClose.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        etMoney.addTextChangedListener(this);
        btnConfirm.setEnabled(false);
    }

    public void setWindowSize(){
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
        handler.sendEmptyMessageDelayed(0,100);
    }

    public void setTvSum(String sumStr){
        tvSum.setText(sumStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tally_dialog_money_btn:
                String text = etMoney.getText().toString();
                if (TextUtils.isEmpty(text)){
                    MyToast.showText(getContext(),"预算金额不能为空！");
                    return;
                }
                money = Float.parseFloat(text);
                if (money <= 0){
                    MyToast.showText(getContext(),"预算金额不能小于等于零！");
                    return;
                }
                if (clickEnSure != null) {
                    clickEnSure.onClickSure(money);
                    cancel();
                }
                break;
            case R.id.tally_dialog_money_img:
                cancel();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.removeCallbacksAndMessages(null);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnConfirm.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String strMoney = etMoney.getText().toString();
        if (!TextUtils.isEmpty(strMoney) && strMoney.length() <= 7){
            btnConfirm.setEnabled(true);
        }else{
            btnConfirm.setEnabled(false);
        }
    }
}
