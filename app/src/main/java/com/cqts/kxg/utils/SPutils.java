package com.cqts.kxg.utils;

import com.cqts.kxg.main.MyApplication;

/**
 * 管理 SharedPreferences<p>
 * token ----------用户登录token <br>
 * classifyTime ----------分类的存储时间<br>
 * classifyData ----------分类的存储数据<br>
 * First 是否是第一次使用APP 如果是则显示引导页<br>
 * userName 用户名 用于登录<br>
 */
public class SPutils {

    public static String getUserName() {
        return MyApplication.userSp.getString("userName", "");
    }

    public static void setUserName(String userName) {
        MyApplication.userSp.edit().putString("userName", userName).commit();
    }

    public static boolean getFirst() {
        return MyApplication.userSp.getBoolean("isFirst", true);
    }

    public static void setFitst(boolean isFirst) {
        MyApplication.userSp.edit().putBoolean("isFirst", isFirst).commit();
    }

    public static String getToken() {
        return MyApplication.userSp.getString("token", "");
    }

    public static void setToken(String token) {
        MyApplication.userSp.edit().putString("token", token).commit();
    }

    public static Long getClassifyTime() {
        return MyApplication.userSp.getLong("classifyTime", 0);
    }

    public static void setClassifyTime(Long classifyTime) {
        MyApplication.userSp.edit().putLong("classifyTime", classifyTime).commit();
    }

    public static String getClassifyData() {
        return MyApplication.userSp.getString("classifyData", "");
    }

    public static void setClassifyData(String classifyData) {
        MyApplication.userSp.edit().putString("classifyData", classifyData).commit();
    }
}
