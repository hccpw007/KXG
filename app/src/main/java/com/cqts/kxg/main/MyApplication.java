package com.cqts.kxg.main;

import com.base.BaseApplication;
import com.cqts.kxg.views.CodeCountDownTimer;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyApplication extends BaseApplication{

    public static CodeCountDownTimer downTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        downTimer = new CodeCountDownTimer(10000,100);
    }
}
