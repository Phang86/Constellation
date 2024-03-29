package com.yyzy.constellation.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.StarDetailsActivity;
import com.yyzy.constellation.adapter.StarItemAdapter;
import com.yyzy.constellation.adapter.StarPagerAdapter;
import com.yyzy.constellation.entity.NotableBean;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.StringUtils;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.weather.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarFragment extends BaseFragment implements View.OnClickListener{
    private GridView gridView;
    private LinearLayout linearLayout;
    private ViewPager viewPager;
    private StarItemAdapter adapter;
    private List<StarInfoEntity.StarinfoDTO> dtoList;
    private StarPagerAdapter starPagerAdapter;
    private TextView tvNotable;
    private ImageView iv;
    //声明图片数组
    private int[] imgIds = {R.mipmap.zero,R.mipmap.one,R.mipmap.two,R.mipmap.pic_star};
    //声明Viewpager的数据源
    private List<ImageView> ivList;
    //声明指示器小圆点
    private List<ImageView> pointList;

    //完成定时装置，实现自动切换图片的效果
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                //获取当前viewpager显示的页面
                int currentItem = viewPager.getCurrentItem();
                //判断是否最后一张，是就切换到第一张，否就递增
                if (currentItem == ivList.size()-1) {
                    viewPager.setCurrentItem(0);
                }else {
                    currentItem++;
                    viewPager.setCurrentItem(currentItem);
                }
                //形成循环发送，接收消息的效果，在接收消息的同时也要进行消息的发送
                handler.sendEmptyMessageDelayed(1,2000);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_star, container, false);
        getUserInfo(getContext());
        initView(view);
        volleyLoadData(URLContent.NOTABLE);
        getStarData();
        initPager();
        setVpListener();
        setGvListener();
        //handler.sendEmptyMessageDelayed(1,2000);
        return view;
    }

    //设置GridView事件监听函数
    private void setGvListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StarInfoEntity.StarinfoDTO entity = dtoList.get(position);
                Intent intent = new Intent(getContext(), StarDetailsActivity.class);
                intent.putExtra("star",entity);
                startActivity(intent);
            }
        });
    }

    //设置viewpager显示的界面
    private void setVpListener() {
        //为viewpager添加监听
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pointList.size(); i++) {
                    pointList.get(i).setImageResource(R.mipmap.point_normal);
                }
                pointList.get(position).setImageResource(R.mipmap.point_focus);
            }
        });
    }

    //设置viewpager显示的页面
    private void initPager() {
        ivList = new ArrayList<>();
        pointList = new ArrayList<>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(imgIds[i]);
            int finalI = i;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToast.showText(getContext(),"dianjiale"+imgIds[finalI]);
                }
            });
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //设置图片的宽高
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            //将图片view设置到集合中
            ivList.add(iv);

            //创建图片对应的指示器小圆点
            ImageView pointIv = new ImageView(getContext());
            pointIv.setImageResource(R.mipmap.point_normal);
            //设置小圆点的宽高
            LinearLayout.LayoutParams plp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            plp.setMargins(20,0,0,0);
            pointIv.setLayoutParams(plp);
            //将小圆点添加到布局中
            linearLayout.addView(pointIv);
            //将小圆点view设置到集合中
            pointList.add(pointIv);

        }
        //默认第一个小圆点是获取焦点的状态
        pointList.get(0).setImageResource(R.mipmap.point_focus);
        starPagerAdapter = new StarPagerAdapter(getContext(), ivList);
        viewPager.setAdapter(starPagerAdapter);
    }

    //获取星座信息
    private void getStarData() {
        Bundle bundle = getArguments();
        StarInfoEntity info = (StarInfoEntity) bundle.getSerializable("info");
        dtoList = info.getStarinfo();
        adapter = new StarItemAdapter(getContext(),dtoList);
        gridView.setAdapter(adapter);
    }

    //初始化控件信息
    private void initView(View view) {
        gridView = view.findViewById(R.id.starFrag_gv);
        linearLayout = view.findViewById(R.id.starFrag_layout);
        viewPager = view.findViewById(R.id.starFrag_vp);
        tvNotable = view.findViewById(R.id.starFrag_tv_notable);
        iv = view.findViewById(R.id.starFrag_iv_notification);
        iv.setOnClickListener(this);
        tvNotable.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvNotable.setSelected(true);
        tvNotable.setFocusable(true);
        tvNotable.setFocusableInTouchMode(true);
        setTvShowContent();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(1,2000);
    }


    @Override
    public void onResponse(String response) {
        super.onResponse(response);
        NotableBean bean = new Gson().fromJson(response, NotableBean.class);
        Log.e("TAG", "onResponse: "+bean.toString());
        if (bean.getCode() == 1 && bean.getData() != null) {
            tvNotable.setText("\t\t\t\t欢迎"+base_user_names+"来到星缘应用软件，下面为您带来每日搞笑段子。  ------  "+bean.getData().get((int) (Math.random() * bean.getData().size())).getContent());
            return;
        }
        setTvShowContent();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        setTvShowContent();
    }

    @Override
    public void onClick(View v) {
        volleyLoadData(URLContent.NOTABLE);
    }

    private void setTvShowContent(){
        tvNotable.setText("\t\t\t\t欢迎"+base_user_names+"来到星缘应用软件！"+StringUtils.setContent());
    }
}