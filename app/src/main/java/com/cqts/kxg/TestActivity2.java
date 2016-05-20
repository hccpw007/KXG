package com.cqts.kxg;

import android.app.PendingIntent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.base.swipebacklayout.SwipeBackActivity;
import com.base.views.MyTagView;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class TestActivity2 extends SwipeBackActivity implements View.OnClickListener {
    InputMethodManager m; // 软键盘管理器
    private MyTagView tag;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button button = (Button) findViewById(R.id.button);
        button.setText("按钮2");
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
