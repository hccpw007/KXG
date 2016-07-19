package com.cqts.kxg.utils;

import android.content.Context;
import android.content.Intent;

import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MyApplication;

/**
 * Created by Administrator on 2016/7/19.
 */
public class LoginUtils {
    //是否登录
    public static boolean isLogin() {
        return MyApplication.userInfo != null;
    }

    //登录
    public static void login(Context context) {
        if (!isLogin()) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }
}
