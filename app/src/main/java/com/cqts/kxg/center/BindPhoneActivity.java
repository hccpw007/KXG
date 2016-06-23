package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;

public class BindPhoneActivity extends MyActivity implements View.OnClickListener, MyEditText
        .TextChanged {
    private MyEditText phone_et;
    private MyEditText smscode_et;
    private TextView smscount_tv;
    private Button bind_btn;
    private TextView username_tv;
    private String userName;
    private String phoneNum;
    private String smsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindphone);
        userName = getIntent().getStringExtra("userName");
        InitView();
    }

    private void InitView() {
        setMyTitle("绑定手机号");
        username_tv = (TextView) findViewById(R.id.username_tv);
        phone_et = (MyEditText) findViewById(R.id.phone_et);
        smscode_et = (MyEditText) findViewById(R.id.smscode_et);
        smscount_tv = (TextView) findViewById(R.id.smscount_tv);
        bind_btn = (Button) findViewById(R.id.bind_btn);

        username_tv.setText(userName);
        smscount_tv.setOnClickListener(this);
        bind_btn.setOnClickListener(this);
        MyApplication.downTimer.setTextView(smscount_tv);

        phone_et.addMyTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.smscount_tv: //获取短信验证码
                getSmsCode();
                break;
            case R.id.bind_btn: //绑定手机
                bindPhoneNum();
                break;
        }
    }

    //获取短信验证码
    private void getSmsCode() {
        phoneNum = phone_et.getText().toString().trim();
        if (phoneNum.length() < 11) {
            showToast("请输入11位手机号码!");
            return;
        }
        MyHttp.sms2(http, null, phoneNum, 5, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                MyApplication.downTimer.going();
            }
        });
    }

    //绑定手机
    private void bindPhoneNum() {
        phoneNum = phone_et.getText().toString().trim();
        smsCode = smscode_et.getText().toString().trim();
        if (smsCode.length() < 6) {
            showToast("请输入6位手机验证码!");
            return;
        }
        if (phoneNum.length() < 11) {
            showToast("请输入11位手机号码!");
            return;
        }
        bind_btn.setEnabled(false);
        phone_et.setEnabled(false);
        MyHttp.bindPhone(http, null, phoneNum, smsCode, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                phone_et.setEnabled(true);
                bind_btn.setEnabled(true);
                 System.out.println(code+"===code");

                showToast(msg);
                if (code != 0) {
                    return;
                }
                System.out.println(code+"===code");
                getUserInfo().mobile_phone = phoneNum;
                System.out.println(code+"===code");
                setResult(RESULT_OK);
                System.out.println(code+"===code");
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind_btn.setEnabled(true);
        phone_et.setEnabled(true);
    }

    @Override
    public void textChanged(View view, String s, int start, int before, int count) {
        if (s.length() < 11) {
            smsCode = "";
            smscode_et.setText("");
        }
    }
}
