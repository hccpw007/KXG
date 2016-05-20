package com.cqts.kxg.center;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

/**
 * 注册 填写手机号
 */
public class Register1Activity extends MyActivity implements View.OnClickListener {

    private Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTransparencyBar(true);
        InitView();
    }

    private void InitView() {
        setMyTitle("注册", Color.WHITE);
        register_btn = (Button) findViewById(R.id.register_btn);

        register_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn: //注册btn
                startActivity(new Intent(this,Register2Activity.class));
                break;
            default:
                break;
        }
    }
}
