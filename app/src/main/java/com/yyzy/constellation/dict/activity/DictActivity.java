package com.yyzy.constellation.dict.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.google.gson.Gson;
import com.yyzy.constellation.BuildConfig;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.activity.MainActivity;
import com.yyzy.constellation.dict.entity.TuWenEntity;
import com.yyzy.constellation.utils.FileUtil;
import com.yyzy.constellation.utils.PatternUtils;
import com.yyzy.constellation.utils.RecognizeService;
import com.yyzy.constellation.utils.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DictActivity extends BaseActivity implements View.OnClickListener,TextView.OnEditorActionListener {

    private ImageView imgBack,imgSet;
    private TextView tvPinYin,tvBuShou,tvChengYu,tvXiangJi,tvString;
    private EditText editText;
    //private ImageView imgSearch;
    private String text;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private AlertDialog.Builder alertDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_dict;
    }

    @Override
    protected void initView() {
        alertDialog = new AlertDialog.Builder(this);
        imgBack = findViewById(R.id.dict_iv_back);
        imgSet = findViewById(R.id.dict_iv_set);
        tvPinYin = findViewById(R.id.dict_tv_pinyin);
        tvBuShou = findViewById(R.id.dict_tv_bushou);
        tvChengYu = findViewById(R.id.dict_tv_chengyu);
        tvXiangJi = findViewById(R.id.dict_tv_xiangji);
        editText = findViewById(R.id.dict_ed);
        //imgSearch = findViewById(R.id.dict_iv_search);
        tvString = findViewById(R.id.dict_tv_everyDay);

        imgBack.setOnClickListener(this);
        imgSet.setOnClickListener(this);
        tvPinYin.setOnClickListener(this);
        tvBuShou.setOnClickListener(this);
        tvChengYu.setOnClickListener(this);
        tvXiangJi.setOnClickListener(this);
        //imgSearch.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
        tvString.setText(StringUtils.setString());
    }



    @Override
    protected void initData() {
        initAccessTokenWithAkSk();
    }

    @Override
    public void onClick(View v) {
        text = editText.getText().toString().trim();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dict_iv_back:
                finish();
                break;
            case R.id.dict_iv_set:
                intent.setClass(this, SetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.dict_tv_pinyin:
                intent.setClass(this, SearchPinyinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.dict_tv_bushou:
                intent.setClass(this, SearchBushouActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.dict_tv_chengyu:
                intent.setClass(this,ChengYuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.dict_tv_xiangji:
                if (!checkTokenStatus()) {
                    return;
                }
                intent.setClass(DictActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
                break;
            //case R.id.dict_iv_search:
//                if (TextUtils.isEmpty(text)) {
//                    showToast("输入框不能空！");
//                    return;
//                }else if (!TextUtils.isEmpty(text)){
//                    intent.setClass(this, WordInfoActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("zi",text);
//                    startActivity(intent);
//                    editText.setText("");
//                }
                //break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralBasic(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            //result是识别出的字符串，可以将字符串传递给下一个界面
                            TuWenEntity wenBean = new Gson().fromJson(result, TuWenEntity.class);
                            List<TuWenEntity.WordsResultBean> wordResult = wenBean.getWords_result();
                            //将识别到的文字存放到list集合当中
                            ArrayList<String> list = new ArrayList<>();
                            if (wordResult != null && wordResult.size() != 0) {
                                for (int i = 0; i < wordResult.size(); i++) {
                                    TuWenEntity.WordsResultBean bean = wordResult.get(i);
                                    String words = bean.getWords();
                                    String res = PatternUtils.removeAll(words);
                                    //将字符串当中每一个字符串都添加到集合当中
                                    for (int j = 0; j < res.length(); j++) {
                                        String s = String.valueOf(res.charAt(j));
                                        //添加集合之前，先判断一下，集合是否包括这个汉字
                                        if (!list.contains(s)) {
                                            list.add(s);
                                        }
                                    }
                                }
                                if (list.size() == 0) {
                                    Toast.makeText(context, "无法识别图片中的文字！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(DictActivity.this, IdentifyImgActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("wordList", list);
                                    intent.putExtras(bundle);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
        }

    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }
            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(),  "oqRRbIasDIm0c2xtQspwssQi", "RXtWO0HnfFrBmFGEkmVE0nLOInZs5Fy7");
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(this).release();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        downOptions(actionId);
        return true;
    }

    private void downOptions(int actionId) {
        text = editText.getText().toString().trim();
        switch (actionId){
            case EditorInfo.IME_ACTION_SEARCH:
                if (TextUtils.isEmpty(text)) {
                    showToast("输入框不能空！");
                    return;
                }else if (!TextUtils.isEmpty(text)){
                    Intent intent = new Intent();
                    intent.setClass(this, WordInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("zi",text);
                    startActivity(intent);
                    editText.setText("");
                }
                break;
        }
    }
}