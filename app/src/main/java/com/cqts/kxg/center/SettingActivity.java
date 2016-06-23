package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.DataCleanManager;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.utils.MyURL;
import com.cqts.kxg.utils.SPutils;
import com.cqts.kxg.views.SharePop;

public class SettingActivity extends MyActivity implements View.OnClickListener {
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout layout5;
    private Button exit_btn;
    private TextView cacheTv;

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
        cacheTv = (TextView) findViewById(R.id.cache_tv);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        exit_btn.setOnClickListener(this);


        try {
            cacheTv.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout1: //个人资料
                startActivity(new Intent(this, InformationActivity.class));
                break;
            case R.id.layout2: //账户与安全
                startActivity(new Intent(this, SafeActivity.class));
                finish();
                break;
            case R.id.layout3: //清除缓存
                try {
                    DataCleanManager.clearAllCache(this);
                    cacheTv.setText(DataCleanManager.getTotalCacheSize(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.layout4: //邀请好友使用开心购
                SharePop.getInstance().showPop(this, layout4, "我是一个大逗逼!", "www.baidu.com",
                        "分享测试", null);
                break;
            case R.id.layout5: //关于开心购
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "关于开心购");
                intent.putExtra("url", MyURL.ABOUT);
                startActivity(intent);
                break;
            case R.id.exit_btn: //退出当前账户
                MyApplication.userInfo = null;
                MyApplication.token = "";
                SPutils.setToken("");
                finish();
                break;
            default:
                break;
        }
    }
}
