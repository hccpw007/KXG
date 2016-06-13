package com.cqts.kxg.home;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

/**
 * 根据首页的文章分类的文章列表
 */
public class ArticleActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        InitView();
    }

    private void InitView() {
        setMyTitle(getIntent().getStringExtra("title"));
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        beginTransaction.add(R.id.framelayout, new ArticleFragment(ArticleFragment.Where.home,getIntent().getStringExtra("cat_id")));
        beginTransaction.commit();
    }
}
