package com.yyzy.constellation.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.LuckDetailsActivity;
import com.yyzy.constellation.adapter.LuckItemAdapter;
import com.yyzy.constellation.entity.StarInfoEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LuckFragment extends Fragment {

    private StarInfoEntity entity;
    private List<StarInfoEntity.StarinfoDTO> mDatas;
    private GridView gridView;
    private CircleImageView cv;
    private TextView tv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_luck, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gridView = view.findViewById(R.id.luck_gv);
        Bundle bundle = getArguments();
        entity = (StarInfoEntity) bundle.getSerializable("info");
        mDatas = entity.getStarinfo();
        LuckItemAdapter adapter = new LuckItemAdapter(getContext(), mDatas);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击GridView的item跳转到二级页面
                Intent intent = new Intent(getContext(),LuckDetailsActivity.class);
                intent.putExtra("starName",mDatas.get(position).getName());
                intent.putExtra("starLogoName",mDatas.get(position).getLogoname());
                startActivity(intent);
            }
        });
    }
}