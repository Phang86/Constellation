package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.entity.ChengyuInfoEntity;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.MyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChengYuActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private ImageView backImg;
    private EditText searchEt;
    private GridView gv;
    private ArrayList<String> mData = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Intent intent;
    private RelativeLayout relativeLayout;
    private TextView tvClear;

    @Override
    protected int initLayout() {
        return R.layout.activity_cheng_yu;
    }

    @Override
    protected void initView() {
        backImg = findViewById(R.id.chengyu_iv_back);
        searchEt = findViewById(R.id.chengyu_et);
        gv = findViewById(R.id.chengyu_gv);
        relativeLayout = findViewById(R.id.chengyu_layout);
        tvClear = findViewById(R.id.chengyu_tv_clear);
        backImg.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        searchEt.setOnEditorActionListener(this);
        relativeLayout.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        adapter = new ArrayAdapter<>(this, R.layout.item_chengyu_gv, R.id.item_chengyu_tv, mData);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = mData.get(position);
                searchEt.setText(s);
                startPage(s);
            }
        });

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mData.get(position);
                AlertDialogUtils instance = AlertDialogUtils.getInstance();
                AlertDialogUtils.showConfirmDialog(ChengYuActivity.this,"温馨提示","确定删除（"+name+"）这条记录吗？","确定","算了");
                instance.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog dialog) {
                        int del = DBmanager.delWhereCyFromCyutb(name);
                        if (del > 0){
                            MyToast.showText(getBaseContext(),"删除成功！");
                            mData.clear();
//                            List<String> list = DBmanager.queryAllCyFromCyutb();
//                            if (list.size() > 0){
//                                relativeLayout.setVisibility(View.VISIBLE);
//                            }else{
//                                relativeLayout.setVisibility(View.GONE);
//                            }
//                            mData.addAll(list);
                            loadData();
                            adapter.notifyDataSetChanged();
                        }else{
                            MyToast.showText(getBaseContext(),"删除失败！");
                        }
                        dialog.cancel();
                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog dialog) {
                        dialog.cancel();
                    }
                });

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchEt.setText("");
        loadData();
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        mData.clear();
        List<String> list = DBmanager.queryAllCyFromCyutb();
        mData.addAll(list);
        if (mData.size() > 0){
            relativeLayout.setVisibility(View.VISIBLE);
        }else{
            relativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chengyu_iv_back:
                finish();
                //overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
            case R.id.chengyu_tv_clear:
                AlertDialogUtils instance = AlertDialogUtils.getInstance();
                AlertDialogUtils.showConfirmDialog(this,"温馨提示","确定清空这（"+mData.size()+"条）查找记录吗？","确定","算了");
                instance.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog dialog) {
                        DBmanager.delAllCyFromCyutb();
                        mData.clear();
                        List<String> list = DBmanager.queryAllCyFromCyutb();
                        mData.addAll(list);
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                        if (mData.size() > 0){
                            relativeLayout.setVisibility(View.VISIBLE);
                            MyToast.showText(getBaseContext(),"记录清空失败！",false);
                        }else{
                            relativeLayout.setVisibility(View.GONE);
                            MyToast.showText(getBaseContext(),"记录已全部清空！",true);
                        }
                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog dialog) {
                        dialog.cancel();
                    }
                });
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        downOption(actionId);
        return false;
    }

    private void downOption(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                String text = searchEt.getText().toString().trim();
                if (text.isEmpty()){
                    MyToast.showText(context, "搜索框不能为空哦！");
                    return;
                }else if (!checkHanZi(text)){
                    MyToast.showText(context, "请输入中文汉字！");
                    return;
                }else if (text.length() != 4){
                    MyToast.showText(context, "请输入四字成语！");
                    return;
                }else{
                    //把文本输入的信息添加到集合
                    //跳转页面
                    startPage(text);
                    //清空文本框
                    searchEt.setText("");
                }
                break;
        }
    }

    private void startPage(String name){
        //跳转到成语详情界面
        intent = new Intent(this, ChengYuInfoActivity.class);
        intent.putExtra("chengyu",name);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
    }
}