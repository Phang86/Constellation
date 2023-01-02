package com.yyzy.constellation.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
    public static String getJSONFromNet(String path){
        String json = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //将路径转换为url对象
        try {
            URL url = new URL(path);
            //获取网络连接对象
            URLConnection ucon = url.openConnection();
            //开始连接
            ucon.connect();
            //读取输入流里面的信息
            InputStream is = ucon.getInputStream();
            //读取流
            int hasRead = 0;
            byte[] buf = new byte[1024];
            //循环读取
            while (true){
                hasRead = is.read(buf);
                if (hasRead == -1){
                    break;
                }
                baos.write(buf,0,hasRead);
            }
            is.close();
            json = baos.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
