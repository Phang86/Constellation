package com.yyzy.constellation.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yyzy.constellation.R;
import com.yyzy.constellation.utils.MyToast;

import org.jetbrains.annotations.NotNull;

public class TallyDialogTime extends Dialog implements View.OnClickListener, TextWatcher {
    private DatePicker datePicker;
    private EditText etHour,etMinute;
    private TextView btnCancel,btnConfirm;
    private OnEnSure onEnSure;

    public void setOnEnSure(OnEnSure onEnSure) {
        this.onEnSure = onEnSure;
    }



    public interface OnEnSure{
        void onSure(String time,int year,int month,int day,int hour,int minute);
    }


    public TallyDialogTime(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tally_dialog_time);
        datePicker = findViewById(R.id.tally_dialog_time_datePicker);
        etHour = findViewById(R.id.tally_dialog_time_etHour);
        etMinute = findViewById(R.id.tally_dialog_time_etMinute);
        btnCancel = findViewById(R.id.tally_dialog_time_btnCancel);
        btnConfirm = findViewById(R.id.tally_dialog_time_btnConfirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        etHour.addTextChangedListener(this);
        etMinute.addTextChangedListener(this);
        btnConfirm.setEnabled(false);
        hideHeader();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        handler.sendEmptyMessageDelayed(0,100);
    }

    public void setEtHour(String hour){
        etHour.setText(hour);
    }

    public void setEtMinute(String minute){
        etMinute.setText(minute);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tally_dialog_time_btnCancel:
                cancel();
                break;
            case R.id.tally_dialog_time_btnConfirm:
                String hour = etHour.getText().toString();
                String minute = etMinute.getText().toString();
                String hours = "",minutes = "";
                if (!TextUtils.isEmpty(hour)){
                    if(hour.length() == 1 && Integer.parseInt(hour) < 10 && Integer.parseInt(hour) >= 0){
                        hours = "0"+hour;
                        //return;
                    }else if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0){
                        MyToast.showText(getContext(),"小时输入格式不正确！");
                        return;
                    }else{
                        hours = hour;
                    }
                }else{
                    //MyToast.showText(getContext(),"请填写具体小时！");
                    return;
                }
                if (!TextUtils.isEmpty(minute)){
                    if (minute.length() == 1 && Integer.parseInt(minute) < 10 && Integer.parseInt(minute) >= 0){
                        minutes = "0"+minute;
                        //return;
                    }else if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0){
                        MyToast.showText(getContext(),"分钟输入格式不正确！");
                        return;
                    }else{
                        minutes = minute;
                    }
                }else{
                    //MyToast.showText(getContext(),"内容格式不正确（分钟）！");
                    return;
                }
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                String monthStr = "",dayStr = "";
                if (month < 10){
                    monthStr = "0"+month;
                }else{
                    monthStr = ""+month;
                }
                if (day < 10){
                    dayStr = "0"+day;
                }else{
                    dayStr = ""+day;
                }
                String timeSdf = year+"年"+monthStr+"月"+dayStr+"日 "+hours+":"+minutes;
//                if (timeSdf.length() > 20){
//                    String repTime = timeSdf.replace("年", "/").replace("月", "/").replace("日", "\t\t");
//                }
                onEnSure.onSure(timeSdf,year,month,day,Integer.parseInt(hours),Integer.parseInt(minutes));
                cancel();
                break;
        }
    }

    //隐藏日历header布局
    private void hideHeader(){
        ViewGroup viewGroup = (ViewGroup) datePicker.getChildAt(0);
        if (viewGroup == null) {
            return;
        }
        View headerView = viewGroup.getChildAt(0);
        if (headerView == null) {
            return;
        }
        //int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        int headerId = getContext().getResources().getIdentifier("date_picker_header","id","android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnConfirm.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String minute = etMinute.getText().toString();
        String hour = etHour.getText().toString();
        if (!TextUtils.isEmpty(minute) && !TextUtils.isEmpty(hour)) {
            btnConfirm.setEnabled(true);
            return;
        }
        btnConfirm.setEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(0);
    }

}
