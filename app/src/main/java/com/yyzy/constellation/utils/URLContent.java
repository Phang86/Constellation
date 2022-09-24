package com.yyzy.constellation.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLContent {
    //登录、注册  本地服务器接口
    public static final String BASE_URL = "http://192.168.43.208:8080/myapp";
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
        //String path = "http://apis.juhe.cn/xzpd/query?men="+man+"&women="+woman+"&key=0b2e6cb43593654c90614ef441b8b2f3";
        String path = "http://apis.juhe.cn/xzpd/query?men="+man+"&women="+woman+"&key=aab7f23b9a6149ef03e1b8136e38b640";
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
    private static final String KEY = "dc5aa3c9d420eca9d567ff9220f3d8f1";
//    private static final String KEY = "864bdd5bfbaa5474078aa98ec94ed947";
//    private static final String KEY = "d9bc43174536fa905488b4fd93165efe";


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
//    public static final String DICTKEY = "3022583457067131a719f84d10efd275";
    public static final String DICTKEY = "7af407700fe10eddc0c28ea381a83ebe";

    public static String wordurl = "http://v.juhe.cn/xhzd/query?key=";

    //成语API接口
    public static final String CHENGYUKEY = "e8a46192a557700f9a8c9b21eab233e5";
    //http://apis.juhe.cn/idioms/query?
    public static String chengyuurl = "http://apis.juhe.cn/idioms/query?key=";

    public static String getChengyuurl(String word){
        String url = chengyuurl+CHENGYUKEY+"&wd="+word;
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

    //    获取指定日期对应的历史上的今天的网址
    public static String getTodayHistoryURL(String version,int month,int day){
        String url = "http://api.juheapi.com/japi/toh?v="+version+"&month="+month+"&day="+day+"&key=6a877b186ccd134296d131183b74c9f4";
        return url;
    }

    //    获取指定日期的老黄历的网址
    public static String getLaohuangliURL(String time){
        String url = "http://v.juhe.cn/laohuangli/d?date="+time+"&key=c7c6d7da1062f007a33609571cdb17f2";
        return url;
    }

//    http://api.juheapi.com/japi/tohdet?key=6a877b186ccd134296d131183b74c9f4&v=1.0&id=18401114

    //    根据指定事件id获取指定事件详情信息
    public static String getHistoryDescURL(String version,String id){
        String url = "http://api.juheapi.com/japi/tohdet?key=6a877b186ccd134296d131183b74c9f4&v="+version+"&id="+id;
        return url;
    }

    //    公共的key   http://v.juhe.cn/toutiao/index?key=0af60a86bfa3575d53c1491d1be310ca&type=top
    public static String key = "0af60a86bfa3575d53c1491d1be310ca";
    public static String info_url = "http://v.juhe.cn/toutiao/index?key="+key+"&type=";
    //     头条
    public static String headline_url = info_url +"top";
    //    社会
    public static String society_url = info_url +"shehui";
    //    国内
    public static String home_url = info_url +"guonei";
    //    国际
    public static String internation_url = info_url+"guoji";
    //    娱乐
    public static String entertainment_url = info_url+"yule";
    //    体育
    public static String sport_url = info_url+"tiyu";
    //    军事
    public static String military_url = info_url+"junshi";
    //    科技
    public static String science_url = info_url+"keji";
    //    财经
    public static String finance_url = info_url+"caijing";
    //    时尚
    public static String fashion_url = info_url+"shishang";
}
