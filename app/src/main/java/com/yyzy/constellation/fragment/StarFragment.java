package com.yyzy.constellation.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.MainActivity;
import com.yyzy.constellation.activity.StarDetailsActivity;
import com.yyzy.constellation.adapter.StarItemAdapter;
import com.yyzy.constellation.adapter.StarPagerAdapter;
import com.yyzy.constellation.entity.StarInfoEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StarFragment extends Fragment {
    private GridView gridView;
    private LinearLayout linearLayout;
    private ViewPager viewPager;
    private StarItemAdapter adapter;
    private List<StarInfoEntity.StarinfoDTO> dtoList;
    private StarPagerAdapter starPagerAdapter;
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
        initView(view);
        getStarData();
        initPager();
        setVpListener();
        setGvListener();
        handler.sendEmptyMessageDelayed(1,2000);
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
    }
}