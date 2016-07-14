package com.cqts.kxg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.login.LoginService;
import com.alibaba.sdk.android.login.callback.LoginCallback;
import com.alibaba.sdk.android.login.callback.LogoutCallback;
import com.alibaba.sdk.android.session.model.Session;
import com.taobao.tae.sdk.callback.CallbackContext;

public class TestActivity extends Activity implements View.OnClickListener {
    private Button showLogin;
    private Button logout;
    private Button showQrLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_img);
        showLogin = (Button) findViewById(R.id.show_login);
        logout = (Button) findViewById(R.id.logout);
        showQrLogin = (Button) findViewById(R.id.showQrLogin);

        showLogin.setOnClickListener(this);
        logout.setOnClickListener(this);
        showQrLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_login:
                showLogin();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.showQrLogin:
                break;
            default:
                break;
        }
    }

    //登录
    public void showLogin() {
        AlibabaSDK.getService(LoginService.class).showLogin(this, new InternalLoginCallback());
    }
    public void logout() {
        AlibabaSDK.getService(LoginService.class).logout(this, new LogoutCallback() {
            @Override
            public void onFailure(int code, String msg) {
                System.out.println("登出失败");
            }

            @Override
            public void onSuccess() {
                System.out.println("登出成功");

            }
        });
    }
    private class InternalLoginCallback implements LoginCallback {

        @Override
        public void onSuccess(Session session) {
            System.out.println("成功");
        }

        @Override
        public void onFailure(int code, String message) {
            System.out.println("失败");
        }
    }
}
