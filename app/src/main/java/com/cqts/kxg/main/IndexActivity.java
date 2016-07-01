package com.cqts.kxg.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import com.base.views.MyViewPager;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.IndexAdapter;
import com.cqts.kxg.utils.SPutils;
import com.cqts.kxg.utils.UMengUtils;

/**
 * 引导页
 */
public class IndexActivity extends Activity implements MyViewPager.OnMyPageChangeListener, View
        .OnClickListener {
    private MyViewPager viewpager;
    private RadioButton[] rdbtns = new RadioButton[3];
    private Button into_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //通知栏和虚拟按键透明(xml需要设置属性)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //通知栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟按键透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_index);
        InitView();
    }

    private void InitView() {
        viewpager = (MyViewPager) findViewById(R.id.viewpager);
        into_btn = (Button) findViewById(R.id.into_btn);
        rdbtns[0] = (RadioButton) findViewById(R.id.rdbtn1);
        rdbtns[1] = (RadioButton) findViewById(R.id.rdbtn2);
        rdbtns[2] = (RadioButton) findViewById(R.id.rdbtn3);

        into_btn.setOnClickListener(this);

        viewpager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewpager.setAdapter(new IndexAdapter(this));
        viewpager.setOnMyPageChangeListener(this);
    }

    @Override
    public void OnMyPageSelected(int arg0) {
        rdbtns[arg0].setChecked(true);
        if (arg0 == 2) { //显示按钮
            into_btn.setVisibility(View.VISIBLE);
        } else {
            into_btn.setVisibility(View.GONE);
        }
    }
    @Override
    public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void OnMyPageScrollStateChanged(int arg0) {
    }
    @Override
    public void onClick(View v) {
        //立即体验
        SPutils.setFitst(false);
        startActivity(new Intent(this,NgtAty.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
        }
        return super.onKeyDown(keyCode, event);
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
