package com.cqts.kxg.center;

import android.graphics.Color;
import android.os.Bundle;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class Register2Activity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        setTransparencyBar(true);
        InitView();
    }

    private void InitView() {
        setMyTitle("验证手机号", Color.WHITE);
    }
}
