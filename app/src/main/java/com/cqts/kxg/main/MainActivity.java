package com.cqts.kxg.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.base.BaseApplication;
import com.base.BaseValue;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.SigninInfo;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.utils.SPutils;
import com.cqts.kxg.utils.UMengUtils;

public class MainActivity extends MyActivity implements MyHttp.MyHttpResult, Handler.Callback {
    public static final int REFRESHTOKEN = 1;
    public static final int GETUSERINFO = 2;
    public static MainActivity instance;
    Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        setSwipeBackEnable(false);
        InitView();
    }



    /**
     * 自动登录
     */
    private void AutoLogin() {
        handler.sendEmptyMessageDelayed(1, 2000);
        String token = SPutils.getToken();
        if (token.isEmpty() || token.length() < 2) {
            return;
        }
        MyHttp.refreshToken(http, REFRESHTOKEN, token, this);
    }

    private void InitView() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!MyApplication.isMyInit) {
                }
                AutoLogin();
            }
        }.start();
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if (code != 0) {
            SPutils.setToken("");
            return;
        }

        switch (which) {
            case REFRESHTOKEN: //刷新token
                SigninInfo signinInfo = (SigninInfo) bean;
                MyApplication.signinInfo = signinInfo;
                MyApplication.token = signinInfo.getToken();
                SPutils.setToken(signinInfo.getToken());
                MyHttp.getUserInfo(http, GETUSERINFO, this);
                break;
            case GETUSERINFO: //获得userinfo
                UMengUtils.setSignIn();
                MyApplication.userInfo = (UserInfo) bean;
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (SPutils.getFirst()) {
            startActivity(new Intent(this, IndexActivity.class));
        } else {
            startActivity(new Intent(this, NgtAty.class));
        }
        finish();
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
