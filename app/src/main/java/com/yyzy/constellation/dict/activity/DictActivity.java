package com.yyzy.constellation.dict.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.activity.MainActivity;
import com.yyzy.constellation.utils.StringUtils;

public class DictActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack,imgSet;
    private TextView tvPinYin,tvBuShou,tvChengYu,tvXiangJi,tvString;
    private EditText editText;
    private ImageView imgSearch;
    private String text;

    @Override
    protected int initLayout() {
        return R.layout.activity_dict;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.dict_iv_back);
        imgSet = findViewById(R.id.dict_iv_set);
        tvPinYin = findViewById(R.id.dict_tv_pinyin);
        tvBuShou = findViewById(R.id.dict_tv_bushou);
        tvChengYu = findViewById(R.id.dict_tv_chengyu);
        tvXiangJi = findViewById(R.id.dict_tv_xiangji);
        editText = findViewById(R.id.dict_ed);
        imgSearch = findViewById(R.id.dict_iv_search);
        tvString = findViewById(R.id.dict_tv_everyDay);

        imgBack.setOnClickListener(this);
        imgSet.setOnClickListener(this);
        tvPinYin.setOnClickListener(this);
        tvBuShou.setOnClickListener(this);
        tvChengYu.setOnClickListener(this);
        tvXiangJi.setOnClickListener(this);
        imgSearch.setOnClickListener(this);

        tvString.setText(StringUtils.setString());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        text = editText.getText().toString().trim();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dict_iv_back:
                intentJump(MainActivity.class);
                finish();
                break;
            case R.id.dict_iv_set:

                break;
            case R.id.dict_tv_pinyin:
                intent.setClass(this, SearchPinyinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.dict_tv_bushou:
                intent.setClass(this, SearchBushouActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.dict_tv_chengyu:

                break;
            case R.id.dict_tv_xiangji:

                break;
            case R.id.dict_iv_search:
                if (TextUtils.isEmpty(text)) {
                    showToast("输入框不能空！");
                    return;
                }
                break;
        }
    }
}