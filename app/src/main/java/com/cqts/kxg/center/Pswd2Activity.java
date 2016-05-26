package com.cqts.kxg.center;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;

public class Pswd2Activity extends MyActivity implements View.OnClickListener {
    private TextView pswd2_phone_tv;
    private MyEditText pswd2_code_et;
    private TextView pswd2_count_tv;
    private MyEditText pswd2_pswd_et;
    private Button pswd2_login_btn;

    private int act;
    private String imgcaptcha;
    private String phoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pswd2);
        setTransparencyBar(true);
        act = getIntent().getIntExtra("act", Pswd1Activity.QIUCKLOGIN);
        imgcaptcha = getIntent().getStringExtra("imgcaptcha");
        phoneStr = getIntent().getStringExtra("phoneStr");
        InitView();
    }

    private void InitView() {
        pswd2_phone_tv = (TextView) findViewById(R.id.pswd2_phone_tv);
        pswd2_code_et = (MyEditText) findViewById(R.id.pswd2_code_et);
        pswd2_count_tv = (TextView) findViewById(R.id.pswd2_count_tv);
        pswd2_pswd_et = (MyEditText) findViewById(R.id.pswd2_pswd_et);
        pswd2_login_btn = (Button) findViewById(R.id.pswd2_login_btn);
        settitle();

        MyApplication.downTimer.going();
        MyApplication.downTimer.setTextView(pswd2_count_tv);

        pswd2_phone_tv.setText("短信验证码已发送至 "
                + phoneStr.substring(0, 3) + " "
                + phoneStr.substring(3, 7) + " "
                + phoneStr.substring(7, 11));

        pswd2_count_tv.setOnClickListener(this);
        pswd2_login_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pswd2_count_tv: //重新发送验证码
                sendAgain();
                break;
            case R.id.pswd2_login_btn: //登录
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        String smsCodeStr = pswd2_code_et.getText().toString().trim();
        String pswdStr = pswd2_pswd_et.getText().toString().trim();

        if (smsCodeStr.length()<6){
            showToast("请输入6位短信验证码");
            return;
        }

        if (pswdStr.length()>0&&pswdStr.length()<6){
            showToast("请输入至少6位登录密码");
            return;
        }


        MyHttp.MyHttpResult myHttpResult = new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code!=0){
                    showToast(msg);
                    return;
                }

            }
        };


        if (act == Pswd1Activity.QIUCKLOGIN) {
            MyHttp.quickSignin(http,act,smsCodeStr,imgcaptcha,phoneStr,pswdStr,myHttpResult);
        }
        if (act == Pswd1Activity.FINDPSWD) {
            if (pswdStr.length()<6){
                showToast("请输入至少6位登录密码");
                return;
            }
            MyHttp.password(http,act,smsCodeStr,imgcaptcha,phoneStr,pswdStr,myHttpResult);
        }
    }

    /**
     * 再次发送验证码
     */
    private void sendAgain() {
        MyApplication.downTimer.going();
        MyHttp.sms(http, null, phoneStr, imgcaptcha, act, null, new MyHttp.MyHttpResult() {
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
     * 设置标题和hint
     */
    private void settitle() {
        if (act == Pswd1Activity.QIUCKLOGIN) {
            setMyTitle("手机号快捷登录");
            pswd2_pswd_et.setHint(Html.fromHtml("<font color=\"red\">(选填) </font>" + "新登录密码" +
                    "(6-24位字符)"));

        }
        if (act == Pswd1Activity.FINDPSWD) {
            setMyTitle("重置登录密码");
            pswd2_login_btn.setText("找回密码并登录");
        }
    }
}
