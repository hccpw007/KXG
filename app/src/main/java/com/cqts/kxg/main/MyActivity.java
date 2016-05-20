package com.cqts.kxg.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.base.BaseActivity;
import com.cqts.kxg.R;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置标题和返回按钮的点击
     */
    public void setMyTitle(String title) {
        View title_left_img = findViewById(R.id.title_left_img);
        TextView title_middle_text = (TextView) findViewById(R.id.title_middle_text);

        if (null != title_left_img) {
            title_middle_text.setText(title);
        }
        //返回按钮
        title_left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setMyTitle(String title,int titleColor) {
        View title_left_img = findViewById(R.id.title_left_img);
        TextView title_middle_text = (TextView) findViewById(R.id.title_middle_text);

        if (null != title_left_img) {
            title_middle_text.setText(title);
            title_middle_text.setTextColor(titleColor);
        }
        //返回按钮
        title_left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
