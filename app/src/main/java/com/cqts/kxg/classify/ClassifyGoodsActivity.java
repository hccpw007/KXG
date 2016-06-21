package com.cqts.kxg.classify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqts.kxg.R;
import com.cqts.kxg.home.GoodsFragment;
import com.cqts.kxg.main.MyActivity;

public class ClassifyGoodsActivity extends MyActivity implements View.OnClickListener {
    private TextView all_tv;
    private TextView sales_tv;
    private LinearLayout money_layout;
    private TextView money_tv;
    private ImageView money_img;

    private String keyword = "";
    private String sort = "add_time";
    private String order = "DESC";
    private GoodsFragment goodsFragment;
    String cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifygoods);
        cat_id = getIntent().getStringExtra("cat_id");
        InitView();
    }

    private void InitView() {
        setMyTitle(getIntent().getStringExtra("title"));

        all_tv = (TextView) findViewById(R.id.all_tv);
        sales_tv = (TextView) findViewById(R.id.sales_tv);
        money_layout = (LinearLayout) findViewById(R.id.money_layout);
        money_tv = (TextView) findViewById(R.id.money_tv);
        money_img = (ImageView) findViewById(R.id.money_img);
        all_tv.setOnClickListener(this);
        sales_tv.setOnClickListener(this);
        money_layout.setOnClickListener(this);

        goodsFragment = new GoodsFragment(GoodsFragment.Where.search,keyword, sort, order,cat_id);
        showFragment(goodsFragment);
    }

    @Override
    public void onClick(View v) {
        setGoodsTab(v.getId());
        goodsFragment.setSearchValue(sort, order);
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
