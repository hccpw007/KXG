package com.cqts.kxg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.base.swipebacklayout.SwipeBackActivity;
import com.base.views.MyTagView;
import com.cqts.kxg.main.MyActivity;

public class TestActivity extends MyActivity implements View.OnClickListener {
    InputMethodManager m; // 软键盘管理器
    private MyTagView tag;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button button = (Button) findViewById(R.id.button);
        button.setText("按钮1");
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,TestActivity2.class));
    }
}
