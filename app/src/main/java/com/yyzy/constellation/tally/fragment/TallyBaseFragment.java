package com.yyzy.constellation.tally.fragment;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.adapter.GvTypeAdapter;
import com.yyzy.constellation.tally.bean.GvTypeBean;
import com.yyzy.constellation.tally.bean.TallyLvItemBean;
import com.yyzy.constellation.tally.db.TallyManger;
import com.yyzy.constellation.tally.util.BeiZhuDialog;
import com.yyzy.constellation.tally.util.OnClickSure;
import com.yyzy.constellation.tally.util.TallyDialogTime;
import com.yyzy.constellation.utils.KeyBoardUtils;
import com.yyzy.constellation.utils.MyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class TallyBaseFragment extends Fragment implements View.OnClickListener{

     KeyboardView keyboardView;
     GridView gv;
     ImageView imgType;
     TextView tvType;
     EditText etMoney;
     TextView tvTime,tvBeizhu;
     KeyBoardUtils keyBoardUtils;
     List<GvTypeBean> mData;
     TallyLvItemBean itemBean;
     GvTypeAdapter adapter;
     private String timeHour = "",timeMinute = "";


    public TallyBaseFragment() {

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemBean = new TallyLvItemBean();
        itemBean.setTypeName("其他");
        itemBean.setImg(R.drawable.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_tally, container, false);
        initView(view);
        setInitTime();
        initData();
        setListener();
        return view;
    }



    private void initView(View view) {
        gv = view.findViewById(R.id.record_frag_gv);
        imgType = view.findViewById(R.id.record_frag_img_title);
        tvType = view.findViewById(R.id.record_frag_tv_title);
        etMoney = view.findViewById(R.id.record_frag_et);
        tvTime = view.findViewById(R.id.record_frag_tv_time);
        tvBeizhu = view.findViewById(R.id.record_frag_tv_beizhu);
        keyboardView = view.findViewById(R.id.record_frag_keyBoard);
        tvBeizhu.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        loadData();
    }

    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        tvTime.setText(time);
        itemBean.setTime(time);
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH)+1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        itemBean.setYear(year);
        itemBean.setMonth(month);
        itemBean.setDay(day);
    }



    public void loadData() {
        mData = new ArrayList<>();
        adapter = new GvTypeAdapter(getContext(), mData);
        gv.setAdapter(adapter);

//        List<GvTypeBean> bean = TallyManger.getAll(0);
//        mData.addAll(bean);
//        adapter.notifyDataSetChanged();
    }

    private void setListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetChanged();
                GvTypeBean bean = mData.get(position);
                tvType.setText(bean.getTypeName());
                imgType.setBackgroundResource(bean.getSelectImg());
                itemBean.setTypeName(bean.getTypeName());
                itemBean.setImg(bean.getSelectImg());
            }
        });
    }

    private void initData() {
        keyBoardUtils = new KeyBoardUtils(keyboardView, etMoney);
        keyBoardUtils.showKeyboard();
        keyBoardUtils.setOnEnSureListener(new KeyBoardUtils.OnEnSureListener() {
            @Override
            public void onEnSure() {
                String money = etMoney.getText().toString();
                if (TextUtils.isEmpty(money) || money.length() < 1){
                    MyToast.showText(getContext(),"金额不能为空哦！");
                    return;
                }else if (money.equals("0") || Float.valueOf(money) == 0){
                    MyToast.showText(getContext(),"金额不能为零哦！");
                    return;
                }else if (imgType.getBackground() == null || TextUtils.isEmpty(tvType.getText())){
                    MyToast.showText(getContext(),"请选择以下列表！");
                    return;
                }else if (money.length() > 7){
                    MyToast.showText(getContext(),"金额过大，请谨慎输入！");
                    return;
                }
                itemBean.setMoney(Float.valueOf(money));
                insertDataToDb();
                getActivity().finish();
            }
        });
    }

    protected abstract void insertDataToDb();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_frag_tv_beizhu:
                String tvText = tvBeizhu.getText().toString().trim();
                showBeizhuDialog(tvText);
                break;
            case R.id.record_frag_tv_time:
                showTimeDialog();
                break;
        }
    }

    private void showTimeDialog(){
        TallyDialogTime dialogTime = new TallyDialogTime(getContext());
        dialogTime.show();
        dialogTime.setEtMinute(timeMinute);
        dialogTime.setEtHour(timeHour);
        dialogTime.setOnEnSure(new TallyDialogTime.OnEnSure() {
            @Override
            public void onSure(String time, int year, int month, int day,int hour,int minute) {
                itemBean.setTime(time);
                itemBean.setYear(year);
                itemBean.setMonth(month);
                itemBean.setDay(day);
                tvTime.setText(time);
                timeHour = ""+hour;
                timeMinute = ""+minute;
            }
        });
    }

    private void showBeizhuDialog(String str){
        BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setEtText(str);
        dialog.setDialogSize();

        dialog.setClickSure(new OnClickSure() {
            @Override
            public void onSure() {
                String text = dialog.getEtText();
                if (!TextUtils.isEmpty(text)) {
                    if (text.length() <= 50){
                        tvBeizhu.setText(text);
                        itemBean.setBeizhu(text);
                        dialog.cancel();
                    }else{
                        MyToast.showText(getContext(),"备注内容超过50字！您输入的字数长度为"+text.length());
                    }
                }else{
                    tvBeizhu.setText("");
                    itemBean.setBeizhu("");
                    dialog.cancel();
                }
            }

            @Override
            public void onCancel() {
                tvBeizhu.setText("");
                itemBean.setBeizhu("");
            }
        });
    }
}