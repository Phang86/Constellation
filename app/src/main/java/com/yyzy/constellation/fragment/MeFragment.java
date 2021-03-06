package com.yyzy.constellation.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.AppInfoActivity;
import com.yyzy.constellation.activity.LoginActivity;
import com.yyzy.constellation.adapter.LuckItemAdapter;
import com.yyzy.constellation.dict.activity.DictActivity;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.DialogUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.StringUtils;
import com.yyzy.constellation.weather.activity.WeatherActivity;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import skin.support.SkinCompatManager;

import static android.content.Context.MODE_PRIVATE;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //????????????????????????????????????
        Bundle bundle = getArguments();
        starInfoEntity = (StarInfoEntity) bundle.getSerializable("info");
        mDatas = starInfoEntity.getStarinfo();
        //??????????????????????????????
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

        //???????????????sp???????????????  ?????????
        String name = spf.getString("name", "?????????");
        String logoname = spf.getString("logoname", "baiyang");
        imgMap = AssetsUtils.getContentLogoImgMap();
        Bitmap bitmap = imgMap.get(logoname);
        cv.setImageBitmap(bitmap);
        tvName.setText(name);
        //Log.e("TAG", "????????????"+findByKey("name")+"\t"+"???????????????"+findByKey("createTime"));
    }

    protected String findByKey(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    protected void insertVal(String key, String val) {
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
                showDialogSure(getContext(),"????????????",StringUtils.setContent());
//                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//                alertDialog.setTitle("????????????");
//                alertDialog.setMessage(StringUtils.setContent());
//                alertDialog.setCancelable(true);
//                alertDialog.setCanceledOnTouchOutside(true);
//                alertDialog.show();
                break;
            case R.id.meFrag_tv_huanfu:
                String skin = findByKey("skin");
                if (skin.equals("night")){
                    // ????????????????????????
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    insertVal("skin","defualt");
                }else {
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // ????????????
                    insertVal("skin","night");
                }
                break;
            case R.id.meFrag_tv_tuichu:
                dialogShow();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
//                        .setTitle("???????????????")
//                        .setMessage("?????????????????????")
//                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //???????????????sp??????????????????????????????????????????????????????
//                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("busApp", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                //????????????
//                                editor.clear();
//                                //editor.remove("username");
//                                //editor.remove("password");
//                                //??????
//                                editor.commit();
//                                DiyProgressDialog dialog1 = new DiyProgressDialog(getContext(),"???????????????...");
//                                dialog1.setCancelable(false);//?????????????????????????????????
//                                dialog1.show();
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Intent intent = new Intent(getContext(), LoginActivity.class);
//                                        intent.setFlags(flag);
//                                        startActivity(intent);
//                                        dialog1.cancel();
//                                        Toast.makeText(getContext(), "?????????????????????", Toast.LENGTH_SHORT).show();
//                                    }
//                                },3000);
//
//                            }
//                        })
//                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //????????????
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

    public static void showDialogSure(Context context,String titMsg,String contentMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.diy_alert_dialog_sure, null);
        TextView content = (TextView) view.findViewById(R.id.dialog_two_content);
        TextView title = (TextView) view.findViewById(R.id.dialog_two_title);
        Button btn_sure = (Button) view.findViewById(R.id.dialog_two_btn_sure);
        //builder.setView(v);//??????????????????builer.setView(v)??????????????????????????????title???button??????????????????
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(view);//?????????????????????????????????????????????dialog.show()?????????
        dialog.getWindow().setGravity(Gravity.CENTER);//???????????????????????????
        title.setText(titMsg);
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
        dialog.setTitle("?????????????????????:");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        GridView gv = view.findViewById(R.id.meFrag_dialog_gv);
        //???GridView???Item??????????????????
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StarInfoEntity.StarinfoDTO dto = mDatas.get(position);
                String name = dto.getName();
                String logoname = dto.getLogoname();
                Bitmap bitmap = imgMap.get(logoname);
                tvName.setText(name);
                cv.setImageBitmap(bitmap);
                selectPos = position;   //?????????????????????????????????
                dialog.cancel();
            }
        });
        //???????????????
        LuckItemAdapter adapter = new LuckItemAdapter(getContext(), mDatas);
        gv.setAdapter(adapter);
        dialog.show();
        //?????????alertDialog??????
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        //lp.width = 1100;//????????????
        lp.height = 1600;//????????????
        dialog.getWindow().setAttributes(lp);
    }

    //???????????????   ???????????????????????????
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

    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.diy_alert_dialog, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        //builder.setView(v);//??????????????????builer.setView(v)??????????????????????????????title???button??????????????????
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackground(null);
        dialog.getWindow().setContentView(v);//?????????????????????????????????????????????dialog.show()?????????
        //????????????dialog???????????????
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(Gravity.CENTER);//???????????????????????????
        content.setText("??????????????????");
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //???????????????sp??????????????????????????????????????????????????????
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("busApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //????????????
                editor.clear();
                //editor.remove("username");
                //editor.remove("password");
                //??????
                editor.commit();
                DiyProgressDialog dialog1 = new DiyProgressDialog(getContext(),"???????????????...");
                dialog1.setCancelable(false);//?????????????????????????????????
                dialog1.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(flag);
                        startActivity(intent);
                        dialog1.cancel();
                        Toast.makeText(getContext(), "?????????????????????", Toast.LENGTH_SHORT).show();
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