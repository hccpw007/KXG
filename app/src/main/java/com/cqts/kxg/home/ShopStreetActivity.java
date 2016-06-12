package com.cqts.kxg.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import android.widget.RadioGroup;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class ShopStreetActivity extends MyActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {
    private ImageView search_img;
    private RadioGroup radiogroup;
    private ShopFragment shopFragment1;
    private ShopFragment shopFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopstreet);
        InitView();
        InitFragment();
        showFragment(shopFragment1);
    }

    private void InitView() {
        setMyTitle("店铺街");
        search_img = (ImageView) findViewById(R.id.search_img);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        search_img.setVisibility(View.VISIBLE);

        radiogroup.setOnCheckedChangeListener(this);
        search_img.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rediobtn1:
                showFragment(shopFragment1);
                break;
            case R.id.rediobtn2:
                showFragment(shopFragment2);
                break;
            default:
                break;
        }
    }

    private void InitFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        shopFragment1 = new ShopFragment(ShopFragment.Where.street, "");
        shopFragment2 = new ShopFragment(ShopFragment.Where.street, "views");
        beginTransaction.add(R.id.framelayout, shopFragment1);
        beginTransaction.add(R.id.framelayout, shopFragment2);
        beginTransaction.commit();
    }

    /**
     * 显示指定fragment
     */
    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        beginTransaction.hide(shopFragment1);
        beginTransaction.hide(shopFragment2);
        beginTransaction.show(fragment);
        beginTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,SearchActivity.class));
    }
}