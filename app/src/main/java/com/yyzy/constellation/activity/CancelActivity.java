package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.MyEditText;
import com.yyzy.constellation.utils.MyToast;

public class CancelActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ImageView imgBack;
    private TextView tvTitle;
    private Button btnNext;
    private EditText etPhone;
    private String myPhone;
    private String name;

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

        tvTitle.setText("手机验证");
        btnNext.setEnabled(false);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("MyPhone");
        myPhone = phone.replace(" ","");
        name = intent.getStringExtra("name");
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
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Myphone",myPhone);
                    intent.putExtra("name",name);
                    intent.setClass(this,CancelidActivity.class);
                    //MyToast.showText(this,"手机号正确",true);
                    startActivity(intent);
                    finish();
                }else{
                    MyToast.showText(this,"手机号不正确！",false);
                    return;
                }
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