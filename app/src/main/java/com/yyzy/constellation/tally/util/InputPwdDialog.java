package com.yyzy.constellation.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

public class InputPwdDialog extends Dialog implements View.OnClickListener {

    private Button btnConfirm;
    private EditText etPwd;
    private String password;
    private OnClickSure onClickSure;

    public void setOnClickSure(OnClickSure onClickSure) {
        this.onClickSure = onClickSure;
    }

    public InputPwdDialog(@NonNull Context context, String pwd) {
        super(context);
        this.password = pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tally_dialog_pwd);
        initView();
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
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            handler.sendEmptyMessageDelayed(0,10);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
//            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

            etPwd.setFocusable(true);
            etPwd.setFocusableInTouchMode(true);
            etPwd.requestFocus();
            InputMethodManager inputManager = (InputMethodManager)etPwd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etPwd, 0);
        }
    };

    private void initView() {
        btnConfirm = findViewById(R.id.dialog_pwd_btn_sure);
        etPwd = findViewById(R.id.dialog_pwd_et_content);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_pwd_btn_sure:
                String pwd = etPwd.getText().toString();
                if (!TextUtils.isEmpty(pwd)){
                    if (pwd.equals(password)){
                        onClickSure.onSure();
                        return;
                    }
                    MyToast.showText(getContext(),"密码错误！");
                    dismiss();
                    return;
                }
                //MyToast.showText(getContext(),"请输入密码！");
                etPwd.setHint("请输入密码");
                break;
        }
    }
}
