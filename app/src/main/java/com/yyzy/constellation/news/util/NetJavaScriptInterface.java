package com.yyzy.constellation.news.util;

import android.content.Context;
import android.util.Log;


public class NetJavaScriptInterface {
    Context context;
    public NetJavaScriptInterface(Context context){
        this.context = context;
    }
    @android.webkit.JavascriptInterface
    public void openImage(String img, String[] imgs) {
        int size = imgs.length;
        int position = 0;
        for (int i=0; i<size; i++) {
            if (img.equals(imgs[i])) {
                position = i;
            }
        }
        Log.e("TAG", "openImage: "+position);
        Log.e("TAG", "openImage: "+imgs);
        //跳转图片界面
//        Intent intent = new Intent(context, DisplayImageActivity.class);
//        intent.putExtra("position",position);
//        intent.putExtra("imgs",imgs);
//        context.startActivity(intent);
    }
}
