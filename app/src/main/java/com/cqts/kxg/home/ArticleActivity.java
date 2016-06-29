package com.cqts.kxg.home;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

/**
 * 根据首页的文章分类的文章列表
 */
public class ArticleActivity extends MyActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radiogroup;
    private RadioButton rediobtn1;
    private RadioButton rediobtn2;
    String sort = "share_sum";
    private ArticleFragment articleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        InitView();
    }

    private void InitView() {
        setMyTitle(getIntent().getStringExtra("title"));
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        articleFragment = new ArticleFragment(ArticleFragment.Where.home, getIntent()
                .getStringExtra("cat_id"), sort);
        beginTransaction.add(R.id.framelayout, articleFragment);
        beginTransaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rediobtn1:
                sort = "share_sum";
                break;
            case R.id.rediobtn2:
                sort = "add_time";
                break;
        }
        articleFragment.setSort(sort);
    }
}
