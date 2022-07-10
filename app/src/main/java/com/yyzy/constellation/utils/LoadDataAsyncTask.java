package com.yyzy.constellation.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadDataAsyncTask extends AsyncTask<String,Void,String> {

    private Context context;
    private OnGetNetDataListener listener;
    private DiyProgressDialog dialog;
    private boolean isShowDialog = false;

    public void initDialog(){
        dialog = new DiyProgressDialog(context,"正在加载中...");
        //dialog.setTitle("提示信息");
    }

    public LoadDataAsyncTask(Context context, OnGetNetDataListener listener,boolean isShowDialog) {
        this.context = context;
        this.listener = listener;
        this.isShowDialog = isShowDialog;
        initDialog();
    }

    public interface OnGetNetDataListener{
        void onSuccess(String json);
    }

    //运行再主线程当中，通常在此方法中进行控件的初始化
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowDialog){
            dialog.show();
        }
    }

    //运行在子线程中，可以在此处耗时操作等逻辑 例如：获取网络数据
    @Override
    protected String doInBackground(String... params) {
        String jsonFromNet = HttpUtils.getJSONFromNet(params[0]);
        return jsonFromNet;
    }

    //运行在主线程中，可以再次得到doInBackground返回的数据，在此处通常进行控件的更新
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (isShowDialog) {
            dialog.dismiss();
        }
        listener.onSuccess(s);
    }
}
