package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.SPutils;

public class SettingActivity extends MyActivity implements View.OnClickListener {
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout layout5;
    private Button exit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        InitView();
    }

    private void InitView() {
        setMyTitle("设置");
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout4 = (LinearLayout) findViewById(R.id.layout4);
        layout5 = (LinearLayout) findViewById(R.id.layout5);
        exit_btn = (Button) findViewById(R.id.exit_btn);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        exit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout1: //个人资料
                startActivity(new Intent(this,InformationActivity.class));
                break;
            case R.id.layout2: //账户与安全
                break;
            case R.id.layout3: //清楚缓存
                break;
            case R.id.layout4: //邀请好友使用开心购
                break;
            case R.id.layout5: //关于开心购
                break;
            case R.id.exit_btn: //退出当前账户
                MyApplication.userInfo = null;
                MyApplication.token = null;
                SPutils.setToken("");
                finish();
                break;
            default:
                break;
        }
    }
}
