package com.yyzy.constellation.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.LocalMusicActivity;
import com.yyzy.constellation.activity.PlayGameActivity;
import com.yyzy.constellation.activity.StarStartActivity;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.utils.AssetsUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartnershipFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ImageView imgWoman, imgMan;
    private Spinner snWoman, snMan;
    private Button btnLucky, btnStart, btnGame;
    private StarInfoEntity info;
    private List<StarInfoEntity.StarinfoDTO> dtoList;
    private Map<String, Bitmap> logoImgMap;
    private List<String> mDatas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Map<String, Bitmap> imgMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partnership, container, false);

        initView(view);
        getActivityData();

        return view;
    }


    //获取Activity传来的数据
    private void getActivityData() {
        //拿到相同键值对匹配的数据
        Bundle bundle = getArguments();
        info = (StarInfoEntity) bundle.getSerializable("info");
        dtoList = info.getStarinfo();
        //获取适配器所需要的数据源
        for (int i = 0; i < dtoList.size(); i++) {
            String name = dtoList.get(i).getName();
            mDatas.add(name);
        }
        adapter = new ArrayAdapter<>(getContext(), R.layout.item_partnership_sn, R.id.item_partnership_sn_tv, mDatas);
        snWoman.setAdapter(adapter);
        snMan.setAdapter(adapter);
        //默认显示第一张星座图片
        String logoname = dtoList.get(0).getLogoname();
        logoImgMap = AssetsUtils.getContentLogoImgMap();
        Bitmap bitmap = logoImgMap.get(logoname);
        imgWoman.setImageBitmap(bitmap);
        imgMan.setImageBitmap(bitmap);


    }

    //初始化控件
    private void initView(View view) {
        imgWoman = view.findViewById(R.id.partnershipFrag_iv_woman);
        imgMan = view.findViewById(R.id.partnershipFrag_iv_man);
        snWoman = view.findViewById(R.id.partnershipFrag_sn_woman);
        snMan = view.findViewById(R.id.partnershipFrag_sn_man);
        btnLucky = view.findViewById(R.id.partnershipFrag_btn_lucky);
        btnStart = view.findViewById(R.id.partnershipFrag_btn_start);
        btnGame = view.findViewById(R.id.partnershipFrag_btn_game);

        btnLucky.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        snWoman.setOnItemSelectedListener(this);
        snMan.setOnItemSelectedListener(this);
        btnGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.partnershipFrag_btn_lucky:
                startActivity(new Intent(getContext(), LocalMusicActivity.class));
                break;
            case R.id.partnershipFrag_btn_start:
                //获取当前图片位置
                int womanPos = snWoman.getSelectedItemPosition();
                int manPos = snMan.getSelectedItemPosition();
                Intent intent = new Intent(getContext(), StarStartActivity.class);
                intent.putExtra("woman_name", dtoList.get(womanPos).getName());
                intent.putExtra("woman_logo", dtoList.get(womanPos).getLogoname());
                intent.putExtra("man_name", dtoList.get(manPos).getName());
                intent.putExtra("man_logo", dtoList.get(manPos).getLogoname());
                startActivity(intent);
                break;
            case R.id.partnershipFrag_btn_game:
                startActivity(new Intent(getContext(), PlayGameActivity.class));
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String logoname = dtoList.get(position).getLogoname();
        switch (parent.getId()) {
            case R.id.partnershipFrag_sn_woman:
                imgMap = AssetsUtils.getContentLogoImgMap();
                Bitmap bitmap = imgMap.get(logoname);
                imgWoman.setImageBitmap(bitmap);
                break;
            case R.id.partnershipFrag_sn_man:
                imgMap = AssetsUtils.getContentLogoImgMap();
                bitmap = imgMap.get(logoname);
                imgMan.setImageBitmap(bitmap);
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}