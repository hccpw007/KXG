package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class InformationActivity extends MyActivity implements View.OnClickListener {
    private LinearLayout head_layout;
    private LinearLayout name_layout;
    private LinearLayout nickname_layout;
    private LinearLayout sex_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        InitView();
    }

    private void InitView() {
        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        nickname_layout = (LinearLayout) findViewById(R.id.nickname_layout);
        sex_layout = (LinearLayout) findViewById(R.id.sex_layout);

        head_layout.setOnClickListener(this);
        name_layout.setOnClickListener(this);
        nickname_layout.setOnClickListener(this);
        sex_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_layout://修改头像

                break;
            case R.id.name_layout: //用户名

                break;
            case R.id.nickname_layout: //昵称

                break;
            case R.id.sex_layout: //性别
                break;
            default:
                break;
        }
    }
}
