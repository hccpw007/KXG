package com.cqts.kxg.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class SafeActivity extends MyActivity implements View.OnClickListener {
    private LinearLayout nameLayout;
    private TextView nameTv;
    private LinearLayout changenameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        InitView();
    }

    private void InitView() {
        setMyTitle("账户与安全");
        nameTv = (TextView) findViewById(R.id.name_tv);
        nameLayout = (LinearLayout) findViewById(R.id.name_layout);
        changenameLayout = (LinearLayout) findViewById(R.id.changename_layout);

        nameTv.setText(getUserInfo().user_name);
        nameLayout.setOnClickListener(this);
        changenameLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_layout: //会员名
                startActivity(new Intent(this, InformationActivity.class));
                break;
            case R.id.changename_layout: // 修改密码
                startActivity(new Intent(this, Pswd1Activity.class).putExtra("act", Pswd1Activity
                        .CHANGEPSWD));
                finish();
                break;
        }
    }
}