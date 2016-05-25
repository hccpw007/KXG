package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

/**
 * 注册 填写手机号
 */
public class Register1Activity extends MyActivity implements View.OnClickListener {

    private ImageView title_left_img;
    private TextView title_middle_text;
    private TextView title_right_text;
    private MyEditText register1_phone_et;
    private MyEditText register1_pswd_et;
    private MyEditText register1_code_et;
    private TextView register1_code_tv;
    private TextView register1_change_tv;
    private Button register1_register_btn;
    private TextView register1_login_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        setTransparencyBar(true);
        InitView();
    }

    private void InitView() {
        setMyTitle("手机注册");
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_middle_text = (TextView) findViewById(R.id.title_middle_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);
        register1_phone_et = (MyEditText) findViewById(R.id.register1_phone_et);
        register1_pswd_et = (MyEditText) findViewById(R.id.register1_pswd_et);
        register1_code_et = (MyEditText) findViewById(R.id.register1_code_et);
        register1_code_tv = (TextView) findViewById(R.id.register1_code_tv);
        register1_change_tv = (TextView) findViewById(R.id.register1_change_tv);
        register1_register_btn = (Button) findViewById(R.id.register1_register_btn);
        register1_login_tv = (TextView) findViewById(R.id.register1_login_tv);

        register1_change_tv.setOnClickListener(this);
        register1_register_btn.setOnClickListener(this);
        register1_login_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register1_change_tv: //换一张
                break;
            case R.id.register1_register_btn: //注册btn
                register();
                break;
            case R.id.register1_login_tv: //已有帐号登录
                break;
            default:
                break;
        }
    }

    /**
     * 注册 发送验证码
     */
    private void register() {

    }
}
