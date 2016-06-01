package com.cqts.kxg.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class SearchResultActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ShopFragment shopFragment = new ShopFragment();
        showFragment(shopFragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        beginTransaction.add(R.id.framelayout,fragment);
        beginTransaction.commit();

    }
}
