package com.yyzy.constellation.tally.fragment;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.bean.GvTypeBean;
import com.yyzy.constellation.tally.db.TallyManger;

import java.util.List;

public class InComeFragment extends TallyBaseFragment{

    @Override
    public void loadData() {
        super.loadData();
        List<GvTypeBean> bean = TallyManger.getOutOrInTypetbAll(1);
        mData.addAll(bean);
        adapter.notifyDataSetChanged();
        tvType.setText("其他");
        imgType.setBackgroundResource(R.drawable.icon_qita_fs);
        itemBean.setImg(R.drawable.icon_qita_fs);
        itemBean.setOutOrIn(1);
    }

    @Override
    protected void insertDataToDb(){
        TallyManger.insertData(itemBean);
    }


}