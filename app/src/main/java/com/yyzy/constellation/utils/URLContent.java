package com.yyzy.constellation.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLContent {
    //登录、注册  本地服务器接口
    public static final String BASE_URL = "http://192.168.1.104:8080/myapp";
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


    //天气预报网络接口
    private static final String temp_url = "http://apis.juhe.cn/simpleWeather/query?";
    //天气预报网络接口键值
    //private static final String KEY = "dc5aa3c9d420eca9d567ff9220f3d8f1";
    //private static final String KEY = "864bdd5bfbaa5474078aa98ec94ed947";
    private static final String KEY = "d9bc43174536fa905488b4fd93165efe";


    public static String getTemp_url(String city){
        try {
            //转UTF-8格式
            city = URLEncoder.encode(city,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //拼接网络地址   path = temp_url + city + key;
        String path = temp_url+"city="+city+"&key="+KEY;
        return path;
    }

    //天气  空气指数接口
    private static final String index_url = "http://apis.juhe.cn/simpleWeather/life?";
    public static String getIndex_url(String city){
        try {
            //转UTF-8格式
            city = URLEncoder.encode(city,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String path = index_url+"city="+city+"&key="+KEY;
        return path;
    }

    //字典接口
    public static String pinyinurl = "http://v.juhe.cn/xhzd/querypy?key=";

    public static String bushourul = "http://v.juhe.cn/xhzd/querybs?key=";

    //字典key
    public static final String DICTKEY = "3022583457067131a719f84d10efd275";
    //public static final String DICTKEY = "7af407700fe10eddc0c28ea381a83ebe";

    public static String wordurl = "http://v.juhe.cn/xhzd/query?key=";

    public static final String CHENGYUKEY = "e8a46192a557700f9a8c9b21eab233e5";
    public static String chengyuurl = "http://v.juhe.cn/chengyu/query?key=";

    public static String getChengyuurl(String word){
        String url = chengyuurl+CHENGYUKEY+"&word="+word;
        return url;
    }
    public static String getWordurl(String word){
        String url = wordurl+DICTKEY+"&word="+word;
        return url;
    }

    public static String getPinyinurl(String word,int page,int pagesize){
        String url = pinyinurl+DICTKEY+"&word="+word+"&page="+page+"&pagesize="+pagesize;
        return url;
    }

    public static String getBushouurl(String bs,int page,int pagesize){
        String url = bushourul+DICTKEY+"&word="+bs+"&page="+page+"&pagesize="+pagesize;
        return url;
    }
}
