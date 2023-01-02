package com.yyzy.constellation.weather.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.utils.ViewUtil;
import com.yyzy.constellation.weather.db.DBManager;
import com.yyzy.constellation.weather.entity.WeatherEntity;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private ImageView imgBack;
    private TextView imgSearch;
    private EditText etSearch;
    private GridView gv;
    String hotCity[] = {"北京","上海","杭州","重庆","长沙","苏州","厦门","广州","深圳","成都"};
    private ArrayAdapter<String> adapter;
    private String city;


    @Override
    protected int initLayout() {
        return R.layout.activity_search_city;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.search_iv_back);
        imgSearch = findViewById(R.id.search_iv_confirm);
        etSearch = findViewById(R.id.search_et);
        gv = findViewById(R.id.search_gv);

        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        etSearch.setOnEditorActionListener(this);

        //设置适配器
        adapter = new ArrayAdapter<>(this, R.layout.item_hotcity, R.id.item_hotcity_tv,hotCity);
        gv.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = hotCity[position];
                checkCityHaveOrNot(city);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_confirm:
                city = etSearch.getText().toString();
                checkCityHaveOrNot(city);
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        WeatherEntity entity = new Gson().fromJson(result, WeatherEntity.class);
        if (entity.getError_code() == 0) {
            Intent intent = new Intent();
            intent.setClass(this,WeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("city",city);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
        }else if (entity.getError_code() == 207301){
            MyToast.showText(this,"暂时未收入此城市信息！");
        }else {
            MyToast.showText(this,"今日接口访问次数已上限！");
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        downKey(actionId);
        return false;
    }

    private void downKey(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                city = etSearch.getText().toString();
                checkCityHaveOrNot(city);
                break;
        }
    }

    private void checkCityHaveOrNot(String city){
        ViewUtil.hideOneInputMethod(SearchCityActivity.this,etSearch);
        if (!TextUtils.isEmpty(city)) {
            if (DBManager.findCity(city).equals(city)){
                MyToast.showText(this,"（"+city+"）相关天气信息已存在！无需重复添加！");
            }else{
                String url = URLContent.getTemp_url(city);
                loadDatas(url);
            }
        }else {
            MyToast.showText(this,"请输入关键字！");
        }
    }
}