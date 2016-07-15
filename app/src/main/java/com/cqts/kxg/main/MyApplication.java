package com.cqts.kxg.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
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
    public static boolean isAliSDKInit = false;
    protected static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        downTimer = new CodeCountDownTimer(60000, 100);
        userSp = getSharedPreferences("usersp", Context
                .MODE_PRIVATE);
        UMengUtils.setUMeng(this);
        aliSDKInit(this);
        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
    }

    //阿里百川初始化
    public static void aliSDKInit(Context context) {
        AlibabaSDK.asyncInit(context, new InitResultCallback() {
            @Override
            public void onSuccess() {
                isAliSDKInit = true;
            }
            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

    // 创建服务用于捕获崩溃异常
    // 发生崩溃异常时,重启应用
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Intent intent = new Intent(instance,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            instance.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
        }
    };
}
