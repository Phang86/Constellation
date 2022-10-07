package com.yyzy.constellation.tally.fragment;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.bean.GvTypeBean;
import com.yyzy.constellation.tally.db.TallyManger;

import java.util.List;

public class OutComeFragment extends TallyBaseFragment{

    @Override
    public void loadData() {
        super.loadData();
        List<GvTypeBean> bean = TallyManger.getOutOrInTypetbAll(0);
        mData.addAll(bean);
        adapter.notifyDataSetChanged();
        tvType.setText("其他");
        itemBean.setOutOrIn(0);
        imgType.setBackgroundResource(R.drawable.ic_qita_fs);
    }

    @Override
    protected void insertDataToDb() {
        TallyManger.insertData(itemBean);
    }
}
