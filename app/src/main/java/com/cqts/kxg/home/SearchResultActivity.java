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
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.bean.ShopInfo;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchResultActivity extends MyActivity implements View.OnClickListener{

    private int PageSize = 50;
    private int PageNum = 1;
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
    private List<GoodsInfo> goodsInfos = new ArrayList<GoodsInfo>();
    private List<ArticleInfo> articleInfos = new ArrayList<ArticleInfo>();
    private List<ShopInfo> shopInfos = new ArrayList<>();

    private ArticleFragment articleFragment;
    private GoodsFragment goodsFragment;
    private ShopFragment shopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        type = getIntent().getIntExtra("type", 1);
        keyword = getIntent().getStringExtra("keyword");

        InitView();
        setFragment();
        getData();
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
                break;
            default:
                break;
        }
    }

    /**
     * 设置tab栏状态的改变,请求数据
     */
    void setGoodsTab(int which) {
        //数据清空
        goodsInfos.clear();
        PageNum = 1;
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
        getGoodsData();
    }


    private void setFragment() {
        switch (type) {
            case SearchActivity.type_goods:
                goodsFragment = new GoodsFragment();
                showFragment(goodsFragment);
                type_tv.setText("商品");
                break;
            case SearchActivity.type_article:
                goods_layout.setVisibility(View.GONE);
                articleFragment = new ArticleFragment();
                showFragment(articleFragment);
                type_tv.setText("文章");
                break;
            case SearchActivity.type_shop:
                goods_layout.setVisibility(View.GONE);
                shopFragment = new ShopFragment();
                showFragment(shopFragment);
                type_tv.setText("店铺");
            default:
                break;
        }
    }

    private void getData() {
        switch (type) {
            case SearchActivity.type_goods:
                goodsFragment.setGotoBottom(new GoodsFragment.GotoDottom() {
                    @Override
                    public void loadMore() {
                        getGoodsData();
                    }
                });
                getGoodsData();
                break;
            case SearchActivity.type_article:
                articleFragment.setGotoBottom(new ArticleFragment.GotoDottom() {
                    @Override
                    public void loadMore() {
                        getArticleData();
                    }
                });
                getArticleData();
                break;
            case SearchActivity.type_shop:
                shopFragment.setGotoBottom(new ShopFragment.GotoDottom() {
                    @Override
                    public void loadMore() {
                        getShopData();
                    }
                });
                getShopData();
                break;
            default:
                break;
        }
    }

    /**
     * 获得店铺数据
     */
    private void getShopData() {
        MyHttp.searchShop(http, type, PageSize, PageNum, keyword, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code == 404){
                    shopFragment.setHttpFail(new MyFragment.HttpFail() {
                        @Override
                        public void toHttpAgain() {
                            getShopData();
                        }
                    });
                    return;
                }
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                shopInfos.addAll((Collection<? extends ShopInfo>) bean);
                if (shopInfos.size() == 0) {
                    shopFragment.setHttpNotData(new MyFragment.HttpFail() {
                        @Override
                        public void toHttpAgain() {
                            getShopData();
                        }
                    });
                    return;
                }
                shopFragment.setHttpSuccess();
                shopFragment.setNotify(shopInfos);
                PageNum++;
            }
        });
    }

    private void getGoodsData() {
        MyHttp.searchGoods(http, type, PageSize, PageNum, keyword, sort, order, new MyHttp
                .MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code == 404){
                    goodsFragment.setHttpFail(new MyFragment.HttpFail() {
                        @Override
                        public void toHttpAgain() {
                            getGoodsData();
                        }
                    });
                    return;
                }
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                goodsInfos.addAll((ArrayList<GoodsInfo>) bean);
                if (goodsInfos.size() == 0) {
                    goodsFragment.setHttpNotData(new MyFragment.HttpFail() {
                        @Override
                        public void toHttpAgain() {
                            getGoodsData();
                        }
                    });
                    return;
                }
                goodsFragment.setHttpSuccess();
                goodsFragment.setNotify(goodsInfos);
                PageNum++;
            }
        });
    }

    private void getArticleData() {
        MyHttp.searchArticle(http, type, PageSize, PageNum, keyword, new MyHttp
                .MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code == 404){
                    articleFragment.setHttpFail(new MyFragment.HttpFail() {
                        @Override
                        public void toHttpAgain() {
                            getArticleData();
                        }
                    });
                    return;
                }
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                articleInfos.addAll((ArrayList<ArticleInfo>) bean);
                if (articleInfos.size() == 0) {
                    articleFragment.setHttpNotData(new MyFragment.HttpFail() {
                        @Override
                        public void toHttpAgain() {
                            getArticleData();
                        }
                    });
                    return;
                }
                articleFragment.setHttpSuccess();
                articleFragment.setNotify(articleInfos);
                PageNum++;
            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        beginTransaction.add(R.id.framelayout, fragment);
        beginTransaction.commit();
    }
}
