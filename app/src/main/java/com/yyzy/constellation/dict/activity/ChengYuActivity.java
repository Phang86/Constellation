package com.yyzy.constellation.dict.activity;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.db.RecordsDao;
import com.yyzy.constellation.tally.util.OnClickSure;
import com.yyzy.constellation.utils.AlertDialogUtils;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.ViewUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChengYuActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private TextView tvSearch;
    private ImageView backImg;
    private EditText searchEt;
    private List<String> mData = new ArrayList<>();
    private Intent intent;
    private RelativeLayout relativeLayout;
    private TextView tvClear;
    private TagAdapter mRecordsAdapter;
    private TagFlowLayout tagFlowLayout;
    private ImageView moreArrow;
    private RecordsDao mRecordsDao;
    //默然展示词条个数
    private final int DEFAULT_RECORD_NUMBER = 25;

    @Override
    protected int initLayout() {
        return R.layout.activity_cheng_yu;
    }

    @Override
    protected void initView() {
        backImg = findViewById(R.id.chengyu_iv_back);
        searchEt = findViewById(R.id.chengyu_et);
        tagFlowLayout = findViewById(R.id.fl_search_records);
        moreArrow = findViewById(R.id.iv_arrow);
//        gv = findViewById(R.id.chengyu_gv);
        relativeLayout = findViewById(R.id.chengyu_layout);
        tvClear = findViewById(R.id.chengyu_tv_clear);
        tvSearch = findViewById(R.id.search_iv_confirm);
        backImg.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        searchEt.setOnEditorActionListener(this);
        relativeLayout.setVisibility(View.GONE);

        SharedPreferences app = getSharedPreferences("busApp", MODE_PRIVATE);
        String username = app.getString("username", "");
        //初始化数据库
        mRecordsDao = new RecordsDao(this, username);
    }

    @Override
    protected void initData() {
        //为历史记录布局设置适配器
        mRecordsAdapter = new TagAdapter<String>(mData) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(ChengYuActivity.this).inflate(R.layout.tv_history,
                        tagFlowLayout, false);
                //为标签设置对应的内容
                tv.setText(s);
                return tv;
            }
        };

        tagFlowLayout.setAdapter(mRecordsAdapter);
        //点击某个记录、实现跳转
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public void onTagClick(View view, int position, FlowLayout parent) {
                //清空editText之前的数据
                searchEt.setText("");
                //将获取到的字符串传到搜索结果界面,点击后搜索对应条目内容
                searchEt.setText(mData.get(position));
                searchEt.setSelection(searchEt.length());
                startPage(mData.get(position));
            }
        });

        //长按删除某个条目
        tagFlowLayout.setOnLongClickListener(new TagFlowLayout.OnLongClickListener() {
            @Override
            public void onLongClick(View view, final int position) {
                String name = mData.get(position);
                delRecordAlertDialog("确定删除（" + name + "）这条记录吗？", "确定", new OnClickSure() {
                    @Override
                    public void onSure() {
                        mRecordsDao.deleteRecord(mData.get(position));
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        //view加载完成时回调
        tagFlowLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOverFlow = tagFlowLayout.isOverFlow();
                boolean isLimit = tagFlowLayout.isLimit();
                if (isLimit && isOverFlow) {
                    moreArrow.setVisibility(View.VISIBLE);
                } else {
                    moreArrow.setVisibility(View.GONE);
                }
            }
        });

        //点击查看更多记录
        moreArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagFlowLayout.setLimit(false);
                mRecordsAdapter.notifyDataChanged();
            }
        });

        mRecordsDao.setNotifyDataChanged(new RecordsDao.NotifyDataChanged() {
            @Override
            public void notifyDataChanged() {
                loadData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchEt.setText("");
        loadData();
        mRecordsAdapter.notifyDataChanged();
    }

    //从数据库查找记录
    private void loadData() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                emitter.onNext(mRecordsDao.getRecordsByNumber(DEFAULT_RECORD_NUMBER));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> s) throws Exception {
                        mData.clear();
                        mData = s;
                        if (null == mData || mData.size() == 0) {
                            relativeLayout.setVisibility(View.GONE);
                        } else {
                            relativeLayout.setVisibility(View.VISIBLE);
                        }
                        if (mRecordsAdapter != null) {
                            mRecordsAdapter.setData(mData);
                            mRecordsAdapter.notifyDataChanged();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chengyu_iv_back:
                finish();
                break;
            case R.id.chengyu_tv_clear:
                delRecordAlertDialog( "确定清空这（"+mData.size()+"条）查找记录吗？", "确定",new OnClickSure() {
                    @Override
                    public void onSure() {
                        mRecordsDao.deleteUsernameAllRecords();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.search_iv_confirm:
                searchIdiom(searchEt.getText().toString().trim());
                break;
        }
    }

    //删除记录提示框
    private void delRecordAlertDialog(String msg,String config,OnClickSure click) {
        AlertDialogUtils instance = AlertDialogUtils.getInstance();
        AlertDialogUtils.showConfirmDialog(this,  msg, config,"取消");
        instance.setMonDialogButtonClickListener(new AlertDialogUtils.OnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(AlertDialog dialog) {
                click.onSure();
                dialog.cancel();
            }

            @Override
            public void onNegativeButtonClick(AlertDialog dialog) {
                click.onCancel();
                dialog.cancel();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        downOption(actionId);
        return false;
    }

    private void downOption(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                searchIdiom(searchEt.getText().toString().trim());
                break;
        }
    }

    private void searchIdiom(String text) {
        ViewUtil.hideOneInputMethod(ChengYuActivity.this,searchEt);
        if (text.isEmpty()) {
            MyToast.showText(context, "请输入关键字！");
            return;
        }
        if (!checkHanZi(text)) {
            MyToast.showText(context, "请输入汉字！");
            return;
        }
        if (text.length() != 4) {
            MyToast.showText(context, "请输入四字成语！");
            return;
        }
        mRecordsDao.addRecords(text);
        //把文本输入的信息添加到集合
        //跳转页面
        startPage(text);
        //清空文本框
        searchEt.setText("");
    }


    private void startPage(String name) {
        //跳转到成语详情界面
        intent = new Intent(this, ChengYuInfoActivity.class);
        intent.putExtra("chengyu", name);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    protected void onDestroy() {
        mRecordsDao.closeDatabase();
        mRecordsDao.removeNotifyDataChanged();
        super.onDestroy();
    }
}