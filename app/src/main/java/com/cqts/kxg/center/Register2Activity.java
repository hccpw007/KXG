package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.SigninInfo;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.utils.SPutils;

public class Register2Activity extends MyActivity implements View.OnClickListener {
    private TextView register2_phone_tv;
    private MyEditText register2_code_et;
    private TextView register2_count_tv;
    private MyEditText register2_invite_et;
    private Button register2_register_btn;
    private String phoneStr;
    private String pswdStr;
    private String codeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        setTransparencyBar(true);
        phoneStr = getIntent().getStringExtra("phoneStr");
        pswdStr = getIntent().getStringExtra("pswdStr");
        codeStr = getIntent().getStringExtra("codeStr");
        InitView();
    }

    private void InitView() {
        setMyTitle("添加邀请码");
        register2_phone_tv = (TextView) findViewById(R.id.register2_phone_tv);
        register2_code_et = (MyEditText) findViewById(R.id.register2_code_et);
        register2_count_tv = (TextView) findViewById(R.id.register2_count_tv);
        register2_invite_et = (MyEditText) findViewById(R.id.register2_invite_et);
        register2_register_btn = (Button) findViewById(R.id.register2_register_btn);
        MyApplication.downTimer.going();
        MyApplication.downTimer.setTextView(register2_count_tv);
        register2_phone_tv.setText("短信验证码已发送至 "
                + phoneStr.substring(0, 3) + " "
                + phoneStr.substring(3, 7) + " "
                + phoneStr.substring(7, 11));

        register2_count_tv.setOnClickListener(this);
        register2_register_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register2_count_tv: //重新发送验证码
                sendAgain();
                break;
            case R.id.register2_register_btn: //注册
                register();
                break;
            default:
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        String smsCodeStr = register2_code_et.getText().toString().trim();
        String inviteCodeStr = register2_invite_et.getText().toString().trim();

        if (smsCodeStr.length() < 6) {
            showToast("请输入6位短信验证码");
            return;
        }

        if (inviteCodeStr.length() > 0 && inviteCodeStr.length() < 6) {
            showToast("请输入完整的邀请码");
            return;
        }

        MyHttp.signup(http, null, smsCodeStr, codeStr, phoneStr, pswdStr, inviteCodeStr, new
                MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                showToast(msg);
                if (code != 0) {
                    return;
                }
                login();
            }
        });
    }

    /**
     * 再次发送验证码
     */
    private void sendAgain() {
        MyApplication.downTimer.going();
        MyHttp.sms(http, null, phoneStr, codeStr, 1, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    MyApplication.downTimer.setInit();
                }
            }
        });
    }



    /**
     * 登录
     */
    private void login() {

        MyHttp.signin(http, null, phoneStr, pswdStr, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    SPutils.setToken("");
                    showToast(msg);
                    return;
                }
                SPutils.setUserName(phoneStr);
                SigninInfo signinInfo = (SigninInfo) bean;
                MyApplication.signinInfo = signinInfo;
                MyApplication.token = signinInfo.getToken();
                getUserInfoData();
            }

        });
    }


    /**
     * 获取用户信息
     */
    private void getUserInfoData() {
        MyHttp.getUserInfo(http, null, new MyHttp.MyHttpResult() {

            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                MyApplication.userInfo = (UserInfo) bean;
                SPutils.setToken(MyApplication.token);
                LoginActivity.instance.finish();
                finish();
            }
        });
    }
}
