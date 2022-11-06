package com.yyzy.constellation.weather.fragment;

import android.media.MediaSync;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yyzy.constellation.utils.Constellation;
import com.yyzy.constellation.utils.DiyProgressDialog;
import com.yyzy.constellation.utils.MyToast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BaseFragment extends Fragment implements Callback.CommonCallback<String>, Response.Listener<String>, Response.ErrorListener {


    public void loadData(String path){
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }

    public void volleyLoadData(String url){
        //创建Volley网络请求框架
        StringRequest request = new StringRequest(url, this, this);
        //添加到网络请求队列当中
        Constellation.getHttpQueue().add(request);

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
            MyToast.showText(getContext(), msg);
            Looper.loop();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //dialog.dismiss();
        //获取网络请求失败的方法
    }

    @Override
    public void onResponse(String response) {
        //获取网络请求成功的方法
        //dialog.dismiss();

    }

}
