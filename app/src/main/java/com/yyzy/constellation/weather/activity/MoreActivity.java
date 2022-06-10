package com.yyzy.constellation.weather.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.weather.db.DBManager;


public class MoreActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout layoutUpdate,layoutClear,layoutEnjoy,layoutVersion;
    private TextView versionTv;
    private RadioGroup radioGroup;
    private ImageView back;
    private SharedPreferences bgPref;

    @Override
    protected int initLayout() {
        return R.layout.activity_more;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.more_iv_back);
        layoutUpdate = findViewById(R.id.more_layout_updateBg);
        layoutClear = findViewById(R.id.more_layout_clear);
        layoutEnjoy = findViewById(R.id.more_layout_enjoy);
        layoutVersion = findViewById(R.id.more_layout_version);
        radioGroup = findViewById(R.id.more_rg);
        versionTv = findViewById(R.id.more_tv_version);

        back.setOnClickListener(this);
        layoutUpdate.setOnClickListener(this);
        layoutClear.setOnClickListener(this);
        layoutEnjoy.setOnClickListener(this);
        layoutVersion.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bgPref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        String versionName = getVersion();
        versionTv.setText("当前版本号："+versionName);
        setRGListener();
    }

    private void setRGListener() {
        //设置改变背景图片的单选按钮的监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int bg = bgPref.getInt("bg", 0);
                SharedPreferences.Editor editor = bgPref.edit();
                Intent intent = new Intent(MoreActivity.this, WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                switch (checkedId) {
                    case R.id.more_rg_rbt_default:
                        if (bg == 0) {
                            showToast("您选择的正为当前背景，无需改变！");
                            return;
                        }
                        editor.putInt("bg",0);
                        editor.commit();
                        break;
                    case R.id.more_rg_rbt_green:
                        if (bg == 1) {
                            showToast("您选择的正为当前背景，无需改变！");
                            return;
                        }
                        editor.putInt("bg",1);
                        editor.commit();
                        break;
                    case R.id.more_rg_rbt_pink:
                        if (bg == 2) {
                            showToast("您选择的正为当前背景，无需改变！");
                            return;
                        }
                        editor.putInt("bg",2);
                        editor.commit();
                        break;
                    case R.id.more_rg_rbt_blue:
                        if (bg == 3) {
                            showToast("您选择的正为当前背景，无需改变！");
                            return;
                        }
                        editor.putInt("bg",3);
                        editor.commit();
                        break;
                    case R.id.more_rg_rbt_star:
                        if (bg == 4) {
                            showToast("您选择的正为当前背景，无需改变！");
                            return;
                        }
                        editor.putInt("bg",4);
                        editor.commit();
                        break;
                }
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_iv_back:
                Intent intent = new Intent(MoreActivity.this, WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.more_layout_updateBg:
                if (radioGroup.getVisibility() == View.VISIBLE) {
                    radioGroup.setVisibility(View.GONE);
                }else {
                    radioGroup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.more_layout_clear:
                clearData();
                break;
            case R.id.more_layout_enjoy:
                enjoyApp("星座缘下载网址");
                break;
        }
    }

    private String getVersion() {
        //获取应用的版本名称
        PackageManager packageManager = getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private void enjoyApp(String msg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,msg);
        startActivity(Intent.createChooser(intent,"分享到"));
    }

    private void clearData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示！")
                .setMessage("您确定清理缓存数据？数据清理后不可找回哦！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAllInfo();
                        showToast("缓存数据清理完毕！");
                    }
                })
                .setNegativeButton("取消",null);
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        radioGroup.setVisibility(View.GONE);
    }
}