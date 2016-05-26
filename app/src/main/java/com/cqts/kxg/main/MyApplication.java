package com.cqts.kxg.main;

import com.base.BaseApplication;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.views.CodeCountDownTimer;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyApplication extends BaseApplication{

    public static CodeCountDownTimer downTimer;
    public static UserInfo userInfo; //用户信息
    public static String token = null;

    @Override
    public void onCreate() {
        super.onCreate();
        downTimer = new CodeCountDownTimer(10000,100);
        userInfo = new UserInfo();
    }
}
