package com.yyzy.constellation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String setContent(){
        String content = "此应用是一款星座配对多功能软件，功能包括：星座介绍、星配分析、星座配对、星座运势、出行天气、姓氏查找等功能。";
        return content;
    }

    /*
    * 注册：严格使用正则表达式
    **/
    // 验证用户名是否匹配指定格式的方法
    public static boolean checkUsername(String user) {
        //用户名只能大小写字母，长度不低于6不大于12。
        String regexp = "^[a-zA-Z]{6,12}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(user);
        return matcher.matches();
    }

    // 验证密码是否匹配指定格式的方法
    public static boolean checkPassword(String pwd) {
        //密码只能用大小写字母、数字组合，长度不低于8不大于16。
        String regexp = "^[0-9a-zA-Z]{8,16}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }
}
