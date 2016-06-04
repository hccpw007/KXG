package com.cqts.kxg.main;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.views.MyViewPager;
import com.base.views.MyViewPager.OnMyPageChangeListener;
import com.cqts.kxg.R;
import com.cqts.kxg.center.LoginedFragment;
import com.cqts.kxg.classify.ClassifyFragment;
import com.cqts.kxg.home.HomeFragment;
import com.cqts.kxg.hot.HotFragment;
import com.cqts.kxg.nine.NineFragment;

/**
 * 主页 导航页
 */
public class NgtAty extends FragmentActivity implements OnMyPageChangeListener, OnCheckedChangeListener {
    private ArrayList<BaseFragment> list = new ArrayList<BaseFragment>();
    private MyViewPager ngt_pager;
    private RadioGroup ngt_rg;
    RadioButton[] ngt_rb = new RadioButton[5];
    int[] drawableId = new int[]{R.drawable.sl_ngt_rb1,
            R.drawable.sl_ngt_rb2, R.drawable.sl_ngt_rb3,
            R.drawable.sl_ngt_rb4, R.drawable.sl_ngt_rb5};

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
        setContentView(R.layout.activity_ngt);
        InitView();
        InitFragment();
        InitNgt();
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.get(0).onShow();
    }

    private void InitView() {
        ngt_pager = (MyViewPager) findViewById(R.id.ngt_pager);
        ngt_rg = (RadioGroup) findViewById(R.id.ngt_rg);
        ngt_rb[0] = (RadioButton) findViewById(R.id.ngt_rb1);
        ngt_rb[1] = (RadioButton) findViewById(R.id.ngt_rb2);
        ngt_rb[2] = (RadioButton) findViewById(R.id.ngt_rb3);
        ngt_rb[3] = (RadioButton) findViewById(R.id.ngt_rb4);
        ngt_rb[4] = (RadioButton) findViewById(R.id.ngt_rb5);
        
        ngt_rg.setOnCheckedChangeListener(this);
    }

    private void InitFragment() {
        list.add(new HomeFragment());
        list.add(new NineFragment());
        list.add(new ClassifyFragment());
        list.add(new HotFragment());
        list.add(new LoginedFragment());
        ngt_pager.setFragemnt(getSupportFragmentManager(), list);
        ngt_pager.setOnMyPageChangeListener(this);
    }

    @Override
    public void OnMyPageSelected(int arg0) {
        ngt_rb[arg0].setChecked(true);
        list.get(arg0).onStart();
        list.get(arg0).onShow();
    }

    @Override
    public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void OnMyPageScrollStateChanged(int arg0) {
    }

    /**
     * 设置导航栏的样式
     */
    private void InitNgt() {
        for (int i = 0; i < ngt_rb.length; i++) {
            Drawable drawable = getResources().getDrawable(drawableId[i]);
            drawable.setBounds(BaseValue.dp2px(0), BaseValue.dp2px(4),
                    BaseValue.dp2px(26), BaseValue.dp2px(30));
            ngt_rb[i].setCompoundDrawables(null, drawable, null, null);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < 5; i++) {
            if (checkedId == ngt_rb[i].getId()) {
                ngt_pager.setCurrentItem(i, false);
                break;
            }
        }
    }
}