package com.yyzy.constellation.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyzy.constellation.R;
import com.yyzy.constellation.user.AppInfoActivity;
import com.yyzy.constellation.adapter.LuckItemAdapter;
import com.yyzy.constellation.dict.activity.DictActivity;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.StringUtils;
import com.yyzy.constellation.weather.activity.WeatherActivity;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class MeFragment extends Fragment implements View.OnClickListener {
    private int flag = Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK;
    private StarInfoEntity starInfoEntity;
    private Button btnConfirm,btnFinsh;
    private List<StarInfoEntity.StarinfoDTO> mDatas;
    private Map<String, Bitmap> imgMap;
    private CircleImageView cv;
    private TextView tvName,tvJie,tvTui,tvHuan,tvWeather,tvNameLookup,tvInfo;
    private SharedPreferences spf;
    private int selectPos = 0;
    private NotificationManager manager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取上个界面传过来的数据
        Bundle bundle = getArguments();
        starInfoEntity = (StarInfoEntity) bundle.getSerializable("info");
        mDatas = starInfoEntity.getStarinfo();
        //把星座图片和名称保存
        spf = getContext().getSharedPreferences("star_spf", MODE_PRIVATE);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        cv = view.findViewById(R.id.meFrag_cv);
        tvName = view.findViewById(R.id.meFrag_tv_name);
        tvJie = view.findViewById(R.id.meFrag_tv_jieshao);
        tvHuan = view.findViewById(R.id.meFrag_tv_huanfu);
        tvTui = view.findViewById(R.id.meFrag_tv_tuichu);
        tvWeather = view.findViewById(R.id.meFrag_tv_weather);
        tvNameLookup = view.findViewById(R.id.meFrag_tv_nameLookup);
        tvInfo = view.findViewById(R.id.meFrag_tv_info);
        cv.setOnClickListener(this);
        tvJie.setOnClickListener(this);
        tvHuan.setOnClickListener(this);
        tvTui.setOnClickListener(this);
        tvWeather.setOnClickListener(this);
        tvNameLookup.setOnClickListener(this);
        tvInfo.setOnClickListener(this);

        //获取保存在sp里面的状态  并设置
        String name = spf.getString("name", "白羊座");
        String logoname = spf.getString("logoname", "baiyang");
        imgMap = AssetsUtils.getContentLogoImgMap();
        Bitmap bitmap = imgMap.get(logoname);
        cv.setImageBitmap(bitmap);
        tvName.setText(name);
        //Log.e("TAG", "用户名："+findByKey("name")+"\t"+"创建时间："+findByKey("createTime"));
    }

    protected String findByKey(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    protected void insertVal( String key, String val) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.meFrag_cv:
                showDialog();
                break;
            case R.id.meFrag_tv_jieshao:
                showDialogSure(getContext(),StringUtils.setContent());
                break;
//            case R.id.meFrag_tv_huanfu:
//                String skin = findByKey("skin");
//                if (skin.equals("night")){
//                    // 恢复应用默认皮肤
//                    SkinCompatManager.getInstance().restoreDefaultTheme();
//                    insertVal("skin","defualt");
//                }else {
//                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
//                    insertVal("skin","night");
//                }
//                break;
            case R.id.meFrag_tv_tuichu:
                dialogShow();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
//                        .setTitle("温馨提示！")
//                        .setMessage("你确定退出吗？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //获取存储在sp里面的用户名和密码以及两个复选框状态
//                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("busApp", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                //清空所有
//                                editor.clear();
//                                //editor.remove("username");
//                                //editor.remove("password");
//                                //提交
//                                editor.commit();
//                                DiyProgressDialog dialog1 = new DiyProgressDialog(getContext(),"退出登录中...");
//                                dialog1.setCancelable(false);//设置不能通过后退键取消
//                                dialog1.show();
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Intent intent = new Intent(getContext(), LoginActivity.class);
//                                        intent.setFlags(flag);
//                                        startActivity(intent);
//                                        dialog1.cancel();
//                                        Toast.makeText(getContext(), "您已成功退出！", Toast.LENGTH_SHORT).show();
//                                    }
//                                },3000);
//
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //取消按钮
//                            }
//                        });
//                dialog.create().show();

                break;
            case R.id.meFrag_tv_weather:
                intent.setClass(getContext(),WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.meFrag_tv_nameLookup:
                intent.setClass(getContext(),DictActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.meFrag_tv_info:
                intent.setClass(getContext(), AppInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    public static void showDialogSure(Context context,String contentMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.diy_alert_dialog_sure, null);
        TextView content = (TextView) view.findViewById(R.id.dialog_two_content);
        Button btn_sure = (Button) view.findViewById(R.id.dialog_two_btn_sure);
        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().getDecorView().setBackground(null);
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
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位
        content.setText(contentMsg);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.mefrag_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        dialog.setTitle("请选择您的星座:");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        GridView gv = view.findViewById(R.id.meFrag_dialog_gv);
        //为GridView的Item设置点击事件
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StarInfoEntity.StarinfoDTO dto = mDatas.get(position);
                String name = dto.getName();
                String logoname = dto.getLogoname();
                Bitmap bitmap = imgMap.get(logoname);
                tvName.setText(name);
                cv.setImageBitmap(bitmap);
                selectPos = position;   //保存最后一次选中的位置
                dialog.cancel();
            }
        });
        //设置适配器
        LuckItemAdapter adapter = new LuckItemAdapter(getContext(), mDatas);
        gv.setAdapter(adapter);
        dialog.show();
        //自定义alertDialog大小
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        //lp.width = 1100;//定义宽度
        lp.height = 1600;//定义高度
        dialog.getWindow().setAttributes(lp);
    }

    //失去焦点时   保存图片和星座名称
    @Override
    public void onPause() {
        super.onPause();
        StarInfoEntity.StarinfoDTO starinfoDTO = mDatas.get(selectPos);
        String name = starinfoDTO.getName();
        String logoname = starinfoDTO.getLogoname();
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("name",name);
        edit.putString("logoname",logoname);
        edit.commit();
    }

    public void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.diy_alert_dialog, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        //builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //设置隐藏dialog默认的背景
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        content.setText("确定退出吗？");
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //获取存储在sp里面的用户名和密码以及两个复选框状态
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("busApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                SharedPreferences sp = getContext().getSharedPreferences("sp_ttit", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                //清空所有
                //editor.remove("username");
                //editor.remove("password");
                //提交
                DiyProgressDialog dialog1 = new DiyProgressDialog(getContext());
                dialog1.setCancelable(false);//设置不能通过后退键取消
                dialog1.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Intent intent = new Intent(getContext(), LoginActivity.class);
//                        intent.setFlags(flag);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_MAIN);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        editor.clear();
                        ed.clear();
                        editor.commit();
                        ed.commit();
                        getActivity().finish();
                        startActivity(intent);
                        //取消登录通知
                        manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        manager.cancel(1);
                        //取消加载
                        dialog1.cancel();
                        Toast.makeText(getContext(), "您已成功退出！", Toast.LENGTH_SHORT).show();
                    }
                },3000);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }
}