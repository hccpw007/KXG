package com.cqts.kxg.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.base.BaseActivity;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.utils.UMengUtils;

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

    public void setMyTitle(String title, int titleColor) {
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


    //是否已经登录
    public boolean isLogined() {
        if (MyApplication.userInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    //需要登录,未登录这跳转登录页面
    public boolean needLogin() {
        if (!isLogined()) {
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        }
        return true;
    }

    public UserInfo getUserInfo() {
        if (needLogin()) {
            return MyApplication.userInfo;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengUtils.setOnPageStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengUtils.setOnonPageEnd(this);
    }
}
