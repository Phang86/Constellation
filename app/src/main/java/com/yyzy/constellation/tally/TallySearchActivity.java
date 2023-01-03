package com.yyzy.constellation.tally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.tally.adapter.TallyLVAdapter;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.tally.util.TallyInfoDialog;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyEditText;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.ViewUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TallySearchActivity extends BaseActivity implements View.OnClickListener,TextView.OnEditorActionListener{

    private ImageView imgBack;
    private ListView lv;
    private EditText et;
    private List<TallyLvItemBean> mData = new ArrayList<>();
    private TallyLVAdapter adapter;
    private LinearLayout lin;
    private TextView tv,search;
    private DiyProgressDialog dialog;
    private TextView tvResult;

    @Override
    protected int initLayout() {
        return R.layout.activity_tally_search;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.details_back);
        et = findViewById(R.id.tally_search_et);
        lv = findViewById(R.id.tally_search_lv);
        lin = findViewById(R.id.tally_search_lin);
        tv = findViewById(R.id.local_music_tv);
        search = findViewById(R.id.search_iv_confirm);
        tvResult = findViewById(R.id.tv_result);
        imgBack.setOnClickListener(this);
        et.setOnEditorActionListener(this);
        search.setOnClickListener(this);
        //lin.setVisibility(View.GONE);
        setLvListener();
    }

    @Override
    protected void initData() {

    }

    private void setLvListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TallyLvItemBean itemBean = mData.get(position);
                TallyInfoDialog dialog = new TallyInfoDialog(TallySearchActivity.this, itemBean);
                dialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.search_iv_confirm:
                String text = et.getText().toString().trim();
                searchRecord(text);
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        downOptions(actionId);
        return true;
    }

    private void downOptions(int actionId) {
        String text = et.getText().toString().trim();
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                searchRecord(text);
                break;
        }
    }

    private void searchRecord(String text) {
        ViewUtil.hideOneInputMethod(TallySearchActivity.this,et);
        lin.setVisibility(View.GONE);
        if (TextUtils.isEmpty(text)) {
            MyToast.showText(getBaseContext(),"请输入关键字！");
            return;
        }
        mData.clear();
        loadProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mData = TallyManger.findBeizhuList(text);
                String textContent = et.getText().toString();
                if (mData.size() > 0 && mData != null) {
                    tvResult.setVisibility(View.VISIBLE);
                    tvResult.setText("共查询到 "+mData.size()+" 条记录...");
                    lin.setVisibility(View.GONE);
                    adapter = new TallyLVAdapter(getBaseContext(), mData);
                    lv.setAdapter(adapter);
                    dialog.cancel();
                    return;
                }
                dialog.cancel();
                lin.setVisibility(View.VISIBLE);
                tvResult.setVisibility(View.GONE);
                tv.setText("未查询到备注内容包含（"+textContent+"）的相关记录！");

            }
        },300);
    }

    private void loadProgress(){
        dialog = new DiyProgressDialog(this, "加载中...");
        dialog.show();
        tvResult.setVisibility(View.GONE);
    }
}