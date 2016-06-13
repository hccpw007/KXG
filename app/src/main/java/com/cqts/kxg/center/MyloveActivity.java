package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.views.MyViewPager;
import com.cqts.kxg.R;
import com.cqts.kxg.home.ArticleFragment;
import com.cqts.kxg.home.GoodsFragment;
import com.cqts.kxg.home.ShopFragment;
import com.cqts.kxg.main.MyActivity;

import java.util.ArrayList;

/**
 * 我喜欢(收藏)的文章,商品,店铺
 */
public class MyloveActivity extends MyActivity implements View.OnClickListener, MyViewPager.OnMyPageChangeListener {
    public static int goodsType = 0; //商品
    public static int articleType = 1;//文章
    public static int shopType = 2;//店铺
    private ArrayList<BaseFragment> list = new ArrayList<>();

    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private ImageView view;
    private MyViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylove);
        InitView();
        InitViewPager();
        viewpager.setCurrentItem(getIntent().getIntExtra("type",0),false);
    }

    private void InitView() {
        setMyTitle("我的喜欢");
        tv0 = (TextView) findViewById(R.id.tv0);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        view = (ImageView) findViewById(R.id.view);
        viewpager = (MyViewPager) findViewById(R.id.viewpager);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = BaseValue.screenwidth / 3;

        tv0.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
    }

    private void InitViewPager() {
        list.add(new GoodsFragment(GoodsFragment.Where.love));
        list.add(new ArticleFragment(ArticleFragment.Where.love));
        list.add(new ShopFragment(ShopFragment.Where.love));
        viewpager.setFragemnt(getSupportFragmentManager(),list);
        viewpager.setOnMyPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv0: //商品
                viewpager.setCurrentItem(goodsType,false);
                break;
            case R.id.tv1: //文章
                viewpager.setCurrentItem(articleType,false);
                break;
            case R.id.tv2: //店铺
                viewpager.setCurrentItem(shopType,false);
                break;
            default:
                break;
        }
    }

    @Override
    public void OnMyPageSelected(int arg0) {

    }
    /**
     * 根据滑动改变红标的位置
     */
    @Override
    public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {
        view.setX(arg0*BaseValue.screenwidth/3+arg2/3);
    }

    @Override
    public void OnMyPageScrollStateChanged(int arg0) {

    }
}
