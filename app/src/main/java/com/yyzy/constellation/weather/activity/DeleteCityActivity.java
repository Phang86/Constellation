package com.yyzy.constellation.weather.activity;


import android.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.weather.adapter.DeleteCityAdapter;
import com.yyzy.constellation.weather.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends BaseActivity implements View.OnClickListener {
    private TextView imgError,imgRight;
    private ListView listView;
    private DeleteCityAdapter adapter;
    private List<String> mData = DBManager.queryAllCityName();    //数据源
    private List<String> deleteCity = new ArrayList<>();
    private AlertDialog dialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_delete_city;
    }

    @Override
    protected void initView() {
        imgError = findViewById(R.id.delete_iv_error);
        imgRight = findViewById(R.id.delete_iv_right);
        listView = findViewById(R.id.delete_lv);

        imgError.setOnClickListener(this);
        imgRight.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new DeleteCityAdapter(this,mData,deleteCity);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_iv_error:
                showDialog();
                break;
            case R.id.delete_iv_right:
                for (int i = 0; i < deleteCity.size(); i++) {
                    String city = deleteCity.get(i);
                    DBManager.deleteInfoCity(city);
                    adapter.notifyDataSetChanged();
                }
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.diy_alert_dialog, null);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) view.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        //设置隐藏dialog默认的背景
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //获取窗口对象
        Window window = dialog.getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
        wlp.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        content.setText("确定退出删除界面吗？");
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            showDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}