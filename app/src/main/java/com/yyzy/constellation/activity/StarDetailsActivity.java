package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.adapter.StarDetailsAdapter;
import com.yyzy.constellation.entity.StarDetailsEntity;
import com.yyzy.constellation.entity.StarInfoEntity;
import com.yyzy.constellation.utils.AssetsUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StarDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private CircleImageView circleImageView;
    private TextView starName,starDate;
    private TextView footerTv;
    private ListView listView;
    private StarInfoEntity.StarinfoDTO entity;
    private Map<String,Bitmap> cirLogoImgMap;
    private List<StarDetailsEntity> mDatas;
    private StarDetailsAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_star_details;
    }

    @Override
    protected void initView() {
        //获取Intent携带过来的数据
        Intent intent = getIntent();
        entity = (StarInfoEntity.StarinfoDTO) intent.getSerializableExtra("star");

        title = findViewById(R.id.details_title);
        back = findViewById(R.id.details_back);
        circleImageView = findViewById(R.id.star_details_cv);
        starName = findViewById(R.id.star_details_name);
        starDate = findViewById(R.id.star_details_date);
        listView = findViewById(R.id.star_details_lv);
        back.setOnClickListener(this);

        //给listView添加footer布局
        View footerView = LayoutInflater.from(this).inflate(R.layout.details_footer, null);
        listView.addFooterView(footerView);
        footerTv = footerView.findViewById(R.id.footer_content);

        //为控件设置相应的数据
        starName.setText(entity.getName());
        starDate.setText(entity.getDate());
        cirLogoImgMap = AssetsUtils.getContentLogoImgMap();
        Bitmap bitmap = cirLogoImgMap.get(entity.getLogoname());
        circleImageView.setImageBitmap(bitmap);
        footerTv.setText(entity.getInfo());
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        adapter = new StarDetailsAdapter(this,mDatas);
        listView.setAdapter(adapter);

        StarDetailsEntity a = new StarDetailsEntity("性格特点：", entity.getTd(), R.color.colorAccent);
        StarDetailsEntity b = new StarDetailsEntity("掌管官位：", entity.getGw(), R.color.lightblue);
        StarDetailsEntity c = new StarDetailsEntity("显阴阳性：", entity.getYy(), R.color.orange);
        StarDetailsEntity d = new StarDetailsEntity("最大特征：", entity.getTz(), R.color.darkblue);
        StarDetailsEntity e = new StarDetailsEntity("主管星球：", entity.getZg(), R.color.lightred);
        StarDetailsEntity f = new StarDetailsEntity("幸运颜色：", entity.getYs(), R.color.darkgreen);
        StarDetailsEntity g = new StarDetailsEntity("搭配珠宝：", entity.getZb(), R.color.pink);
        StarDetailsEntity h = new StarDetailsEntity("幸运号码：", entity.getHm(), R.color.lightpink);
        StarDetailsEntity i = new StarDetailsEntity("相配属性：", entity.getJs(), R.color.grey);
        mDatas.add(a);
        mDatas.add(b);
        mDatas.add(c);
        mDatas.add(d);
        mDatas.add(e);
        mDatas.add(f);
        mDatas.add(g);
        mDatas.add(h);
        mDatas.add(i);

        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }
}