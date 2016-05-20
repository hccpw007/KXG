package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.NgtAty;

/**
 * 登录
 */
public class LoginActivity extends MyActivity implements View.OnClickListener {

    private MyEditText login_user_et;
    private MyEditText login_pswd_et;
    private Button login_login_btn;
    private TextView login_regist_tv;
    private TextView lgoin_forget_tv;

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


        login_login_btn.setOnClickListener(this);
        login_regist_tv.setOnClickListener(this);
        lgoin_forget_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_regist_tv: //注册
                startActivity(new Intent(this,Register1Activity.class));
                break;
            case R.id.lgoin_forget_tv: //忘记密码
                break;
            case R.id.login_login_btn: //登录
                startActivity(new Intent(this,NgtAty.class));
                break;
            default:
                break;
        }
    }
}
