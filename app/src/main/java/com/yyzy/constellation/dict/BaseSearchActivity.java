package com.yyzy.constellation.dict;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.yyzy.constellation.R;
import com.yyzy.constellation.activity.BaseActivity;
import com.yyzy.constellation.dict.activity.WordInfoActivity;
import com.yyzy.constellation.dict.adapter.SearchLeftAdapter;
import com.yyzy.constellation.dict.adapter.SearchRightAdapter;
import com.yyzy.constellation.dict.db.DBmanager;
import com.yyzy.constellation.dict.entity.PinBuEntity;
import com.yyzy.constellation.dict.entity.PinBuWordEntity;
import com.yyzy.constellation.utils.AssetsUtils;
import com.yyzy.constellation.utils.CommonUtils;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.HttpUtils;
import com.yyzy.constellation.utils.MyToast;
import com.yyzy.constellation.utils.URLContent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class BaseSearchActivity extends BaseActivity implements Callback.CommonCallback<String> {
    private ImageView imgBack;
    public TextView tv;
    public ExpandableListView listView;
    private PullToRefreshGridView gridView;
    private List<String> groupDatas = new ArrayList<>();       //表示分组列表
    private List<List<PinBuEntity.ResultDTO>> childDatas = new ArrayList<>();       //将每组对应的子类列表存放到这个集合
    public List<PinBuWordEntity.ResultBean.ListBean> gridDatas; //右边GridView数据源
    public SearchLeftAdapter adapter;
    private int selGroupPos = 0;   //表示被点击组的位置
    private int selChildPos = 0;   //表示被点击组里面item的位置
    public SearchRightAdapter gridViewAdapter;

    public int totalPage;    //总页数
    public int page = 1;     //当前获取的页面
    public int pageSize = 48;  //默认一页获取48条数据
    public String word = "";   //点击了左侧的哪个拼音或部首
    public String url = "";
    private DiyProgressDialog dialog;



    @Override
    protected int initLayout() {
        return R.layout.activity_search_pinyin;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.search_pinyin_iv_back);
        listView = findViewById(R.id.search_pinyin_epdlistView);
        gridView = findViewById(R.id.search_pinyin_gv);
        tv = findViewById(R.id.search_pinyin_tv);
    }

    @Override
    protected void initData() {
        gridDatas = new ArrayList<>();
        //设置适配器
        gridViewAdapter = new SearchRightAdapter(this, gridDatas);
        gridView.setAdapter(gridViewAdapter);
    }

    public void setExlvListener(int type) {
        //给ExpandableListView中的group组设置监听
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                adapter.setSelectGroupPos(groupPosition);

                //获取被点击位置的内容
                selGroupPos = groupPosition;
                int groupSize = childDatas.get(selGroupPos).size();
                if (groupSize <= selChildPos) {
                    selChildPos = groupSize-1;
                    adapter.setSelectChildPos(selChildPos);
                }
                adapter.notifyDataSetInvalidated();
                //获取数据信息
                getDataAlertWord(type);
                return false;
            }
        });
        //给ExpandableListView中的group组中的item设置监听
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                adapter.setSelectGroupPos(groupPosition);
                adapter.setSelectChildPos(childPosition);
                adapter.notifyDataSetInvalidated();
                selGroupPos = groupPosition;
                selChildPos = childPosition;
                //网络加载右边显示的内容
                getDataAlertWord(type);
                return false;
            }
        });
    }

    //改变左边的选中，从网上获取对应选中的结果，显示在右边
    public void getDataAlertWord(int type) {
        //获取选中组位置
        List<PinBuEntity.ResultDTO> groupList = childDatas.get(selGroupPos);
        page = 1;
        PinBuEntity.ResultDTO entity = groupList.get(selChildPos);
        if (type == CommonUtils.TYPE_PINYIN) {
            word = entity.getPinyin();
            url = URLContent.getPinyinurl(word,page,pageSize);
        }else if (type == CommonUtils.TYPE_BUSHOU) {
            word = entity.getBushou();
            url = URLContent.getBushouurl(word,page,pageSize);
        }
        //加载数据
        loadData(url);
    }




    public void setGVListener(int type) {
        //下拉加载的监听器
        gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                //判断当前页数是否小于总页数
                if (page < totalPage){
                    page++;
                    if (type == CommonUtils.TYPE_PINYIN) {
                        url = URLContent.getPinyinurl(word,page,pageSize);
                    }else if (type == CommonUtils.TYPE_BUSHOU) {
                        url = URLContent.getBushouurl(word,page,pageSize);
                    }
                    loadData(url);
                }else {
                    //否则不用加载数据   提示用户
                    gridView.onRefreshComplete();
                    MyToast.showText(BaseSearchActivity.this, "没有更多数据了哦！");
                }
            }
        });

        //给GridView的item添加点击事件,点击则跳转到详情界面
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PinBuWordEntity.ResultBean.ListBean bean = gridDatas.get(position);
                String zi = bean.getZi();
                //跳转到对应文字详情界面
                Intent intent = new Intent();
                intent.putExtra("zi",zi);
                intent.setClass(getBaseContext(), WordInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
            }
        });

    }

    //assetName:文件名称；type:文件类型(pinyin——0，bushou——1)
    public void initData(String assetName, int type) {
        //读取Assets文件夹下的数据，使用Gson解析，将数据分组，存储到二维列表中
        String json = AssetsUtils.getJsonFromAssets(this, assetName);
        if (!TextUtils.isEmpty(json)) {
            PinBuEntity entity = new Gson().fromJson(json, PinBuEntity.class);
            List<PinBuEntity.ResultDTO> list = entity.getResult();
            //整理数据
            List<PinBuEntity.ResultDTO> groupList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                PinBuEntity.ResultDTO resultDTO = list.get(i);
                if (type == CommonUtils.TYPE_PINYIN) {
                    String pinyin_key = resultDTO.getPinyin_key();
                    if (!groupDatas.contains(pinyin_key)) {   //判断是否存在分组的列表中
                        groupDatas.add(pinyin_key);
                        //说明上一个拼音已经全部添加到grouplist当中了，可以将上一个拼音集合添加到childDatas中
                        if (groupList.size() > 0) {
                            childDatas.add(groupList);
                        }
                        //既然是新的一组，就要创建一个对应的新子列表
                        groupList = new ArrayList<>();
                        groupList.add(resultDTO);
                    } else {
                        groupList.add(resultDTO);
                    }
                } else if (type == CommonUtils.TYPE_BUSHOU) {
                    String bihua = resultDTO.getBihua();
                    if (!groupDatas.contains(bihua)) {
                        groupDatas.add(bihua);
                        //新的一组，把上一组进行添加、
                        if (groupList.size() > 0) {
                            childDatas.add(groupList);
                        }
                        //新的一组，新创建子列表
                        groupList = new ArrayList<>();
                        groupList.add(resultDTO);
                    } else {
                        groupList.add(resultDTO);   //当前笔划已存在，不用再次想组当中添加
                    }
                }
            }
            //循环结束之后，最后一组并没有添加进行，所有需要手动添加
            childDatas.add(groupList);
            adapter = new SearchLeftAdapter(this, groupDatas, childDatas, type);
            listView.setAdapter(adapter);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_pinyin_iv_back:
                finish();
                break;
        }
    }

    public void loadData(String path) {
        //创建网络请求体
        RequestParams params = new RequestParams(path);
        x.http().get(params, this);
    }

    @Override
    public void onSuccess(String result) {
        PinBuWordEntity entity = new Gson().fromJson(result, PinBuWordEntity.class);
        //获取成功时
        PinBuWordEntity.ResultBean resultBean = entity.getResult();
        totalPage = resultBean.getTotalpage();
        List<PinBuWordEntity.ResultBean.ListBean> list = resultBean.getList();
        //将数据进行加载
        refreshDataByGV(list);
        //将加载到的网络数据写入到数据库中
        writeDBByThread(list);
    }

    //将网络数据保存到数据库中  使用子线程加载和保存网络数据
    private void writeDBByThread(List<PinBuWordEntity.ResultBean.ListBean> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBmanager.insertListToPywordtb(list);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //设置GridView当中的数据，提示适配器重新加载
    public void refreshDataByGV(List<PinBuWordEntity.ResultBean.ListBean> list) {
        if (page == 1) {
            gridDatas.clear();
            gridDatas.addAll(list);
            gridViewAdapter.notifyDataSetChanged();
        } else {  //进行上拉加载新的一页，获取到的数据，保留原来的数据
            gridDatas.addAll(list);
            gridViewAdapter.notifyDataSetChanged();
            //停止显示加载条
            gridView.onRefreshComplete();
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //获取失败时
        Log.e("获取数据失败", "onError: " + isOnCallback);
    }

    @Override
    public void onCancelled(CancelledException cex) {
        //当获取网络数据取消时
    }

    @Override
    public void onFinished() {
        //当获取网络数据完成时
    }

    //请求网络数据时，判断返回结果
    public static void loadNetDataResult(Context context, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtils.getJSONFromNet(url);
                PinBuWordEntity entity = new Gson().fromJson(json, PinBuWordEntity.class);
                try {
                    if (entity.getReason().equals("超过每日可允许请求次数!") || entity.getError_code() == 10012) {
                        Looper.prepare();
                        MyToast.showText(context, "请求接口次数今日已上限！");
                        Looper.loop();
                        return;
                    }else {
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
