package com.cqts.kxg.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class SearchResultActivity extends MyActivity implements View.OnClickListener {

    private int type;
    private String keyword = "";
    private String sort = "add_time";
    private String order = "DESC";
    private ImageView search_finish_iv;
    private TextView type_tv;
    private TextView keywords_tv;
    private LinearLayout goods_layout;
    private TextView all_tv;
    private TextView sales_tv;
    private LinearLayout money_layout;
    private TextView money_tv;
    private ImageView money_img;
    private ArticleFragment articleFragment;
    private GoodsFragment goodsFragment;
    private ShopFragment shopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        type = getIntent().getIntExtra("type", 1);
        keyword = getIntent().getStringExtra("keyword");
        InitView();
        setFragment();
    }

    private void InitView() {
        search_finish_iv = (ImageView) findViewById(R.id.search_finish_iv);
        type_tv = (TextView) findViewById(R.id.type_tv);
        keywords_tv = (TextView) findViewById(R.id.keywords_tv);
        goods_layout = (LinearLayout) findViewById(R.id.goods_layout);
        all_tv = (TextView) findViewById(R.id.all_tv);
        sales_tv = (TextView) findViewById(R.id.sales_tv);
        money_layout = (LinearLayout) findViewById(R.id.money_layout);
        money_tv = (TextView) findViewById(R.id.money_tv);
        money_img = (ImageView) findViewById(R.id.money_img);
        keywords_tv.setText(keyword);

        search_finish_iv.setOnClickListener(this);
        all_tv.setOnClickListener(this);
        sales_tv.setOnClickListener(this);
        money_layout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_finish_iv:
                finish();
                break;
            case R.id.all_tv: //全部
            case R.id.sales_tv: //销量
            case R.id.money_layout: //价格
                setGoodsTab(v.getId());
                goodsFragment.setSearchValue(sort, order);
                try {
                    if (goodsFragment.goods_rclv.getChildCount()!=0){
                        goodsFragment.goods_rclv.scrollToPosition(0);
                    }
                }catch (Exception e){
                }
                break;
            default:
                break;
        }
    }


    private void setFragment() {
        switch (type) {
            case SearchActivity.type_goods:
                type_tv.setText("商品");
                goodsFragment = new GoodsFragment(GoodsFragment.Where.search,keyword, sort, order);
                showFragment(goodsFragment);
                break;
            case SearchActivity.type_article:
                goods_layout.setVisibility(View.GONE);
                type_tv.setText("文章");
                articleFragment = new ArticleFragment(ArticleFragment.Where.search,keyword);
                showFragment(articleFragment);
                break;
            case SearchActivity.type_shop:
                goods_layout.setVisibility(View.GONE);
                type_tv.setText("店铺");
                shopFragment = new ShopFragment(ShopFragment.Where.search, keyword);
                showFragment(shopFragment);
            default:
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        beginTransaction.add(R.id.framelayout, fragment);
        beginTransaction.commit();
    }

    /**
     * 设置tab栏状态的改变,请求数据
     */
    void setGoodsTab(int which) {
        //数据清空
        all_tv.setTextColor(getResources().getColor(R.color.black));
        sales_tv.setTextColor(getResources().getColor(R.color.black));
        money_tv.setTextColor(getResources().getColor(R.color.black));
        money_img.setImageResource(R.mipmap.sort_default);
        switch (which) {
            case R.id.all_tv://全部
                all_tv.setTextColor(getResources().getColor(R.color.myred));
                sort = "add_time";
                order = "DESC";
                break;
            case R.id.sales_tv: //销量
                sales_tv.setTextColor(getResources().getColor(R.color.myred));
                sort = "volume";
                order = "DESC";
                break;
            case R.id.money_layout: //价格
                money_tv.setTextColor(getResources().getColor(R.color.myred));
                sort = "shop_price";
                if (order.equals("DESC")) {
                    order = "ASC";
                    money_img.setImageResource(R.mipmap.sort_asc);
                } else {
                    order = "DESC";
                    money_img.setImageResource(R.mipmap.sort_desc);
                }
                break;
            default:
                break;
        }
    }
}
