package com.yyzy.constellation.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLContent {
    //登录、注册  本地服务器接口
    public static final String BASE_URL = "http://192.168.1.100:8080/myapp";
    //注册  本地服务器接口
    //星座配对网络接口
    public static String getPartnerURL(String man,String woman){
        man = man.replace("座","");
        woman = woman.replace("座","");
        try {
            man = URLEncoder.encode(man,"UTF-8");
            woman = URLEncoder.encode(woman,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String path = "http://apis.juhe.cn/xzpd/query?men="+man+"&women="+woman+"&key=0b2e6cb43593654c90614ef441b8b2f3";
        //String path = "http://apis.juhe.cn/xzpd/query?men="+man+"&women="+woman+"&key=aab7f23b9a6149ef03e1b8136e38b640";
        return path;
    }

    //星座运势网络接口
    public static String getLuckURL(String name){
        try {
            name = URLEncoder.encode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //String path = "http://web.juhe.cn/constellation/getAll?consName="+name+"&type=year&key=37654c72ae968cedc1f7f173181eb019";
        String path = "http://web.juhe.cn/constellation/getAll?consName="+name+"&type=year&key=7610d57786604caf00b00db70ebdc31f";
        return path;
    }
}
