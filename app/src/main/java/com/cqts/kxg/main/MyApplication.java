package com.cqts.kxg.main;

import android.content.Context;
import android.content.SharedPreferences;

import com.base.BaseApplication;
import com.cqts.kxg.bean.SigninInfo;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.utils.UMengUtils;
import com.cqts.kxg.views.CodeCountDownTimer;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyApplication extends BaseApplication {
    public static SharedPreferences userSp;//缓存文件
    public static CodeCountDownTimer downTimer;
    public static UserInfo userInfo; //用户信息
    public static String token = "";
    public static SigninInfo signinInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        downTimer = new CodeCountDownTimer(60000, 100);
        userSp = getSharedPreferences("usersp", Context
                .MODE_PRIVATE);
        UMengUtils.setUMeng(this);
    }
}
