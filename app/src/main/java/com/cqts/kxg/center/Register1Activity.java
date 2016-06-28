package com.cqts.kxg.center;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.views.MyEditText;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.utils.GetImageCode;
import com.cqts.kxg.utils.MyHttp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 注册 填写手机号
 */
public class Register1Activity extends MyActivity implements View.OnClickListener, MyEditText
        .TextChanged {
    private ImageView register1_code_img;
    private MyEditText register1_phone_et;
    private MyEditText register1_pswd_et;
    private MyEditText register1_code_et;
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
        register1_phone_et = (MyEditText) findViewById(R.id.register1_phone_et);
        register1_pswd_et = (MyEditText) findViewById(R.id.register1_pswd_et);
        register1_code_et = (MyEditText) findViewById(R.id.register1_code_et);
        register1_change_tv = (TextView) findViewById(R.id.register1_change_tv);
        register1_register_btn = (Button) findViewById(R.id.register1_register_btn);
        register1_login_tv = (TextView) findViewById(R.id.register1_login_tv);
        register1_code_img = (ImageView) findViewById(R.id.register1_code_img);


        register1_change_tv.setOnClickListener(this);
        register1_register_btn.setOnClickListener(this);
        register1_login_tv.setOnClickListener(this);
        register1_phone_et.addMyTextChangedListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register1_change_tv: //换一张
                register1_change_tv.setText("换一张");
                GetImageCode.getImageCode(register1_code_img, register1_phone_et.getText()
                        .toString().trim());
                break;
            case R.id.register1_register_btn: //下一步
                next();
                break;
            case R.id.register1_login_tv: //已有帐号登录
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 注册 发送验证码
     */
    private void next() {
        final String phoneStr = register1_phone_et.getText().toString().trim();
        final String pswdStr = register1_pswd_et.getText().toString().trim();
        final String codeStr = register1_code_et.getText().toString().trim();
        if (phoneStr.isEmpty() || phoneStr.length() < 11) {
            showToast("请输入11位手机号码");
            return;
        }
        if (pswdStr.isEmpty() || pswdStr.length() < 6) {
            showToast("请输入至少6位登录密码");
            return;
        }
        if (codeStr.isEmpty() || codeStr.length() < 4) {
            showToast("请输入4位验证码");
            return;
        }
       MyHttp.sms(http, null, phoneStr, codeStr, 1,null,new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                Intent intent = new Intent(Register1Activity.this , Register2Activity.class);
                intent.putExtra("phoneStr", phoneStr);
                intent.putExtra("pswdStr", pswdStr);
                intent.putExtra("codeStr", codeStr);
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
            register1_change_tv.setEnabled(true);
            register1_code_et.setEnabled(true);
            register1_change_tv.setText("换一张");
            GetImageCode.getImageCode(register1_code_img, register1_phone_et.getText()
                    .toString().trim());
        } else {
            register1_change_tv.setText("获取");
            register1_change_tv.setEnabled(false);
            register1_code_et.setEnabled(false);
            register1_code_et.setText("");
            register1_code_img.setImageBitmap(null);
        }
    }
}
