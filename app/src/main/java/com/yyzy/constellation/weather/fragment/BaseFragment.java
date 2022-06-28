package com.yyzy.constellation.weather.fragment;

import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {
    public void loadData(String path){
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }

    //请求数据获取成功时，调用的接口
    @Override
    public void onSuccess(String result) {

    }

    //请求数据获取失败时，调用的接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    //当请求取消的时候，调用的接口
    @Override
    public void onCancelled(CancelledException cex) {

    }

    //请求完成时，调用的接口
    @Override
    public void onFinished() {

    }

    public void showToast(String msg) {
        try {
            Looper.prepare();
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
