package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.SceneInfo;
import com.cqts.kxg.bean.SigninInfo;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;

/**
 * 登录
 */
public class LoginActivity extends MyActivity implements View.OnClickListener {

    private MyEditText login_user_et;
    private MyEditText login_pswd_et;
    private Button login_login_btn;
    private TextView login_regist_tv;
    private TextView lgoin_forget_tv;
    private TextView login_quick_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparencyBar(true);
        setContentView(R.layout.activity_login);
        setSwipeBackEnable(true);
        InitView();

    }

    private void InitView() {
        login_user_et = (MyEditText) findViewById(R.id.login_user_et);
        login_pswd_et = (MyEditText) findViewById(R.id.login_pswd_et);
        login_login_btn = (Button) findViewById(R.id.login_login_btn);
        login_regist_tv = (TextView) findViewById(R.id.login_regist_tv);
        lgoin_forget_tv = (TextView) findViewById(R.id.lgoin_forget_tv);
        login_quick_tv = (TextView) findViewById(R.id.login_quick_tv);

        login_login_btn.setOnClickListener(this);
        login_regist_tv.setOnClickListener(this);
        lgoin_forget_tv.setOnClickListener(this);
        login_quick_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login_regist_tv: //注册
                intent.setClass(this,Register1Activity.class);
                startActivity(intent);
                break;
            case R.id.lgoin_forget_tv: //忘记密码
                intent.setClass(this,Pswd1Activity.class);
                intent.putExtra("act",Pswd1Activity.FINDPSWD);
                startActivity(intent);
                break;
            case R.id.login_login_btn: //登录
                login();
                break;
            case R.id.login_quick_tv: //快捷登录
                intent.setClass(this,Pswd1Activity.class);
                intent.putExtra("act",Pswd1Activity.QIUCKLOGIN);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        String userName = login_user_et.getText().toString().trim();
        String pswd = login_pswd_et.getText().toString().trim();

        if (userName.isEmpty()){
            showToast("请输入手机号或用户名");
            return;
        }
        if (pswd.isEmpty()||pswd.length()<6){
            showToast("请输入登录密码");
            return;
        }

        MyHttp.signin(http, null, userName, pswd, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                SigninInfo signinInfo = (SigninInfo) bean;
                MyApplication.token = signinInfo.getToken();
                getUserInfo();
            }

        });
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        MyHttp.getUserInfo(http, null, MyApplication.token, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code!=0){
                    showToast(msg);
                    return;
                }
                MyApplication.userInfo = (UserInfo) bean;
            }
        });
    }

}
