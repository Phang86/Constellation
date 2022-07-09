package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.fragment.LuckFragment;
import com.yyzy.constellation.fragment.MeFragment;
import com.yyzy.constellation.fragment.PartnershipFragment;
import com.yyzy.constellation.fragment.StarFragment;
import com.yyzy.constellation.utils.AssetsUtils;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private FragmentManager manager;
    private Bundle bundle;
    private Fragment starFragment,partnershipFragment,luckFragment,meFragment;
    private TextView title;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        StarInfoEntity starInfoEntity = loadData();
        bundle = new Bundle();
        bundle.putSerializable("info",starInfoEntity);

        radioGroup = findViewById(R.id.main_rb);
        //为RadioGroup控件设置监听，判断点击哪个
        radioGroup.setOnCheckedChangeListener(this);
        //分别实例化对应的Fragment
        starFragment = new StarFragment();
        starFragment.setArguments(bundle);
        partnershipFragment = new PartnershipFragment();
        partnershipFragment.setArguments(bundle);
        luckFragment = new LuckFragment();
        luckFragment.setArguments(bundle);
        meFragment = new MeFragment();
        meFragment.setArguments(bundle);

        title = findViewById(R.id.main_tv_title);
    }

    @Override
    protected void initData() {
        //为布局管理器添加相应的Fragment
        //创建碎片管理者对象
        manager = getSupportFragmentManager();
        //创建碎片处理事务的对象
        FragmentTransaction transaction = manager.beginTransaction();
        //将四个Fragment添加到布局中
        transaction.add(R.id.main_layout_linear, starFragment);
        transaction.add(R.id.main_layout_linear, partnershipFragment);
        transaction.add(R.id.main_layout_linear, luckFragment);
        transaction.add(R.id.main_layout_linear, meFragment);
        transaction.hide(partnershipFragment);
        transaction.hide(luckFragment);
        transaction.hide(meFragment);
        //提交事务
        transaction.commit();
    }

    //加载数据  读取Assets文件夹下的xzcontent.json文件
    private StarInfoEntity loadData() {
        String json = AssetsUtils.getJsonFromAssets(this, "xzcontent/xzcontent.json");
        Gson gson = new Gson();
        StarInfoEntity infoEntity = gson.fromJson(json, StarInfoEntity.class);
        AssetsUtils.saveBitmapFromAssets(this,infoEntity);
        return infoEntity;
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (checkedId){
            case R.id.main_rb_star:
                transaction.hide(partnershipFragment);
                transaction.hide(luckFragment);
                transaction.hide(meFragment);
                transaction.show(starFragment);
                title.setText("星座缘");
                break;
            case R.id.main_rb_partnership:
                transaction.hide(starFragment);
                transaction.hide(luckFragment);
                transaction.hide(meFragment);
                transaction.show(partnershipFragment);
                title.setText("星座配对");
                break;
            case R.id.main_rb_luck:
                transaction.hide(partnershipFragment);
                transaction.hide(starFragment);
                transaction.hide(meFragment);
                transaction.show(luckFragment);
                title.setText("星座运势");
                break;
            case R.id.main_rb_me:
                transaction.hide(partnershipFragment);
                transaction.hide(luckFragment);
                transaction.hide(starFragment);
                transaction.show(meFragment);
                title.setText("我的");
                break;
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}