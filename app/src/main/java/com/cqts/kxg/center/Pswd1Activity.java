package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.utils.GetImageCode;
import com.cqts.kxg.utils.MyHttp;

/**
 * 2、登陆验证码
 * 3、找回密码验证码
 */
public class Pswd1Activity extends MyActivity implements View.OnClickListener, MyEditText
        .TextChanged {
    public static int QIUCKLOGIN = 2;
    public static int FINDPSWD = 3;
    public static int CHANGEPSWD = 1;
    private int act;

    private MyEditText pswd1_phone_et;
    private MyEditText pswd1_code_et;
    private ImageView pswd1_code_img;
    private TextView pswd1_change_tv;
    private Button pswd1_next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pswd1);
        setTransparencyBar(true);
        act = getIntent().getIntExtra("act", QIUCKLOGIN);
        InitView();
    }

    private void InitView() {
        pswd1_phone_et = (MyEditText) findViewById(R.id.pswd1_phone_et);
        pswd1_code_et = (MyEditText) findViewById(R.id.pswd1_code_et);
        pswd1_code_img = (ImageView) findViewById(R.id.pswd1_code_img);
        pswd1_change_tv = (TextView) findViewById(R.id.pswd1_change_tv);
        pswd1_next_btn = (Button) findViewById(R.id.pswd1_next_btn);

        pswd1_phone_et.addMyTextChangedListener(this);
        pswd1_change_tv.setOnClickListener(this);
        pswd1_next_btn.setOnClickListener(this);


        if (act == QIUCKLOGIN)
            setMyTitle("手机号快捷登录");
        if (act == FINDPSWD)
            setMyTitle("找回密码");

        if (act == CHANGEPSWD) {
            setMyTitle("修改登录密码");
            pswd1_phone_et.setText(getUserInfo().mobile_phone);
            pswd1_phone_et.setEnabled(false);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pswd1_change_tv: //换一张
                pswd1_change_tv.setText("换一张");
                GetImageCode.getImageCode(pswd1_code_img, pswd1_phone_et.getText()
                        .toString().trim());
                break;
            case R.id.pswd1_next_btn: //下一步
                next();
                break;
            default:
                break;
        }
    }

    /**
     * 注册 发送验证码
     */
    private void next() {
        final String phoneStr = pswd1_phone_et.getText().toString().trim();
        final String codeStr = pswd1_code_et.getText().toString().trim();
        if (phoneStr.isEmpty() || phoneStr.length() < 11) {
            showToast("请输入11位手机号码");
            return;
        }
        if (codeStr.isEmpty() || codeStr.length() < 4) {
            showToast("请输入4位验证码");
            return;
        }
        MyHttp.sms(http, null, phoneStr, codeStr, act == CHANGEPSWD ? FINDPSWD : act, null, new
                MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                Intent intent = new Intent(Pswd1Activity.this, Pswd2Activity.class);
                intent.putExtra("act", act);
                intent.putExtra("imgcaptcha", codeStr);
                intent.putExtra("phoneStr", phoneStr);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void textChanged(View view, String s, int start, int before, int count) {
        //让手机号以1开头
        if ((!s.isEmpty()) && (!s.startsWith("1"))) {
            ((MyEditText) view).setText("");
        }
        if (s.length() == 11) {
            pswd1_change_tv.setEnabled(true);
            pswd1_code_et.setEnabled(true);
        } else {
            pswd1_change_tv.setText("获取");
            pswd1_change_tv.setEnabled(false);
            pswd1_code_et.setEnabled(false);
            pswd1_code_et.setText("");
            pswd1_code_img.setImageBitmap(null);
        }

    }
}
