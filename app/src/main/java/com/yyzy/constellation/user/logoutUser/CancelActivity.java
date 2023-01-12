package com.yyzy.constellation.user.logoutUser;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.ViewUtil;

public class CancelActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ImageView imgBack;
    private TextView tvTitle;
    private TextView btnNext;
    private EditText etPhone;
    private String myPhone;

    @Override
    protected int initLayout() {
        return R.layout.activity_cancel;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        btnNext = findViewById(R.id.cancel_btn_next);
        etPhone = findViewById(R.id.cancel_et_phone);

        imgBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        etPhone.addTextChangedListener(this);

        tvTitle.setText("身份验证");
        btnNext.setEnabled(false);
        myPhone = base_phones.replace(" ","");
        Log.e("TAG", "注销用户，该用户电话为："+myPhone);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.cancel_btn_next:
                String phone = etPhone.getText().toString().trim();
                if (phone.equals(myPhone)) {//手机号一致
                    //发送验证   跳转到短信验证页面
                    intentJump(CancelidActivity.class);
                    finish();
                }else{
                    MyToast.showText(this,"手机号不正确！",false);
                }
                ViewUtil.hideOneInputMethod(CancelActivity.this,etPhone);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnNext.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String phone = etPhone.getText().toString().trim();
        if (phone.length() == 11){
            btnNext.setEnabled(true);
        }else {
            btnNext.setEnabled(false);
        }
    }
}