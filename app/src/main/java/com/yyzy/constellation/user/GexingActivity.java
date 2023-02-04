package com.yyzy.constellation.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.utils.MyEditText;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.ViewUtil;

public class GexingActivity extends BaseActivity implements View.OnClickListener , TextWatcher {

    private TextView tvTitle,tvLength,btnCommit;
    private MyEditText et;
    private ImageView ivBack;

    @Override
    protected int initLayout() {
        return R.layout.activity_gexing;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        tvLength = findViewById(R.id.gexing_tv_length);
        btnCommit = findViewById(R.id.gexing_tv_commit);
        et = findViewById(R.id.gexing_et_content);

        btnCommit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        et.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("个性签名");
        tvLength.setText("0/50");
        if (!TextUtils.isEmpty(base_signature)){
            et.setText(base_signature);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                ViewUtil.hideOneInputMethod(this,et);
                finish();
                break;
            case R.id.gexing_tv_commit:
                ViewUtil.hideOneInputMethod(this,et);
                //保存个性签名
                String text = et.getText().toString().trim();
                saveSignature(text);
//                MyToast.showText(GexingActivity.this,"请输入个性签名");
                break;
        }
    }

    private void saveSignature(String signature) {
        loading();
        requestOkhttpLoadFeedback("", signature,"user", base_user_names, "/user/inset/signature", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                if (resultStr.equals("success")){
                    MyToast.showText(GexingActivity.this,"保存成功");
                    updateSpfUserInfo("signature",signature);
                    getUserInfo();
                    finish();
                    return;
                }
                MyToast.showText(GexingActivity.this,"保存失败");
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(GexingActivity.this,"保存失败，服务器连接超时！");
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = et.getText().toString().trim();
        tvLength.setText(text.length()+"/50");
    }
}