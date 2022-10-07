package com.yyzy.constellation.weather.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.activity.LoginActivity;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.weather.db.DBManager;


public class MoreActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout layoutUpdate,layoutClear,layoutEnjoy;
    private RadioGroup radioGroup;
    private ImageView back;
    private SharedPreferences bgPref;
    private String versionName;

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
        radioGroup = findViewById(R.id.more_rg);

        back.setOnClickListener(this);
        layoutUpdate.setOnClickListener(this);
        layoutClear.setOnClickListener(this);
        layoutEnjoy.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bgPref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        versionName = getVersion();
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                enjoyApp(getResources().getString(R.string.app_name)+"下载网址");
                break;
//            case R.id.more_layout_version:
//                //showAlertDialog();
//                break;
            default:
                break;

        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.diy_alert_dialog_no, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_no_content);
        TextView title = (TextView) v.findViewById(R.id.dialog_no_title);
        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //设置隐藏dialog默认的背景
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        title.setText("应用版本");
        content.setText("当前应用版本为\t"+getVersion());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }


    private void enjoyApp(String msg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,msg);
        startActivity(Intent.createChooser(intent,"分享到"));
    }

    private void clearData() {
        AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();
        AlertDialogUtils.showConfirmDialog(this,"温馨提示","您确定清理缓存数据？数据清理后不可找回哦！","确定","取消");
        alertDialogUtils.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(androidx.appcompat.app.AlertDialog dialog) {
                //确定
                showLoadingDialog();
                dialog.dismiss();
            }

            @Override
            public void onNegativeButtonClick(androidx.appcompat.app.AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    private void showLoadingDialog() {
        DiyProgressDialog dialog1 = new DiyProgressDialog(this,"数据清理中...");
        dialog1.setCancelable(false);//设置不能通过后退键取消
        dialog1.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog1.cancel();
                DBManager.deleteAllInfo();
                showToast("缓存数据清理完毕！");

            }
        },1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        radioGroup.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MoreActivity.this, WeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}