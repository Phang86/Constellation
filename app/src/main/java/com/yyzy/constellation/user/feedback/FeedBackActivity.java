package com.yyzy.constellation.user.feedback;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.entity.IpBean;
import com.yyzy.constellation.user.CallBackOkhttp;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;
import com.yyzy.constellation.utils.ViewUtil;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ImageView ivBack;
    private TextView tvTitle,tvRight;
    private TextView tvLength,tvBtn;
    private EditText etContent,etPhone;

    @Override
    protected int initLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.details_back);
        tvTitle = findViewById(R.id.details_title);
        tvRight = findViewById(R.id.details_right);
        tvLength = findViewById(R.id.feedback_tv_length);
        tvBtn = findViewById(R.id.feedback_tv_commit);
        etContent = findViewById(R.id.feedback_et_content);
        etPhone = findViewById(R.id.feedback_et_phone);
        tvBtn.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        etContent.addTextChangedListener(this);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText(getResources().getString(R.string.feedback));
        tvLength.setText(etContent.length()+"/300");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.feedback_tv_commit:
                ViewUtil.hideOneInputMethod(FeedBackActivity.this,etContent);
                commitFeedBack();
                break;
            case R.id.details_right:
                intentJump(HistoryFeedBackActivity.class);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        tvLength.setText(etContent.length()+"/300");
    }

    private void commitFeedBack() {
        String feedBackContent = etContent.getText().toString().trim();
        String feedBackPhone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(feedBackContent)){
            MyToast.showText(this,"请填写您的意见！");
        }else{
            if (!TextUtils.isEmpty(feedBackPhone)){
                if (!isPhoneNumberValid(feedBackPhone)){
                    MyToast.showText(this,"请填写正确的手机号！");
                    return;
                }
            }
            //发起网络请求
            loading();
            loadDatas(URLContent.IP_URL);
        }
    }

    private void requestNet(String content,String phone) {
        requestOkhttp(phone, base_ipaddress, content, "/user/feedback", new CallBackOkhttp() {
            @Override
            public void onSuccess(String resultStr) {
                if (resultStr.equals("success")) {
                    finish();
                    MyToast.showText(getApplicationContext(), "感谢您的反馈！");
                    //Log.e("TAG", "requestNet: 3 重新获取："+base_ipaddress);
                    return;
                }
                if (resultStr.equals("error")) {
                    MyToast.showText(getApplicationContext(), "反馈失败！");
                    return;
                }
                MyToast.showText(FeedBackActivity.this, "反馈失败！服务器连接失败！");
            }

            @Override
            public void onError(Exception e) {
                MyToast.showText(FeedBackActivity.this,"服务器连接超时！");
                Log.e("TAG", "FeedBackActivity_onError: "+e.getMessage());
            }
        });
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        IpBean bean = new Gson().fromJson(result, IpBean.class);
        String ip = null,ipaddress = null ,ipCity = null;
        if (bean.getData() != null && bean.getCode() == 1){
            ip = bean.getData().getProvince().replace("省","");
            ipaddress = bean.getData().getProvince()+" · "+bean.getData().getCity()+"（"+bean.getData().getIsp()+" · "+bean.getData().getIp()+"）";
            ipCity = bean.getData().getCity().replace("市","");
        }else{
            ip = "";
            ipaddress = "";
            ipCity = "杭州";
        }
        Log.e("TAG", "onSuccess: "+bean.toString());
        updateSpfUserInfo("ip",ip);
        updateSpfUserInfo("ipaddress",ipaddress);
        updateSpfUserInfo("ipCity",ipCity);
        getUserInfo();
        requestNet(etContent.getText().toString().trim(),etPhone.getText().toString().trim());
    }
}