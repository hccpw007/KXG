package com.cqts.kxg.nine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.refreshlayout.RefreshLayout;
import com.base.views.MyViewPager;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.BannerAdapter;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.bean.HomeBannerInfo;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.cqts.kxg.bean.HomeTableInfo;
import com.cqts.kxg.home.SearchActivity;
import com.cqts.kxg.home.ShopStreetActivity;
import com.cqts.kxg.main.NgtAty;
import com.cqts.kxg.utils.MyHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewHomeFragment extends BaseFragment implements View.OnClickListener, RefreshLayout
        .OnRefreshListener, MyHttp.MyHttpResult {
    private View item_homebanner, item_hometable;
    private ImageView home_img1, home_img2, home_img3;
    private MyViewPager home_viewpager;
    private RadioButton[] rdBtn = new RadioButton[4];
    private RefreshLayout home_refresh;

    List<HomeBannerInfo> bannerInfos;
    ArrayList<HomeSceneInfo> sceneInfos = new ArrayList<>();
    ArrayList<ArticleInfo> articleInfos = new ArrayList<>();

    private RecyclerView recyclerview;
    private HomeAdapter adapter;

    int pageSize = 20;
    int pageNum = 1;
    private static final int urlNum = 4; //当前页面是刷新的url数量

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_newhome, null);
            initView();
            initRecyclerView();
            getData();
        }
        return view;
    }

    private void initView() {
        ImageView homeSearchImg = (ImageView) view.findViewById(R.id.home_search_img);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        home_refresh = (RefreshLayout) view.findViewById(R.id.refreshing);

        item_homebanner = LayoutInflater.from(getActivity()).inflate(R.layout.item_homebanner,
                null);
        item_hometable = LayoutInflater.from(getActivity()).inflate(R.layout.item_hometable, null);

        home_img1 = (ImageView) item_hometable.findViewById(R.id.home_img1);
        home_img2 = (ImageView) item_hometable.findViewById(R.id.home_img2);
        home_img3 = (ImageView) item_hometable.findViewById(R.id.home_img3);

        home_viewpager = ((MyViewPager) item_homebanner.findViewById(R.id.home_viewpager));

        rdBtn[0] = ((RadioButton) item_homebanner.findViewById(R.id.home_rdbtn1));
        rdBtn[1] = ((RadioButton) item_homebanner.findViewById(R.id.home_rdbtn2));
        rdBtn[2] = ((RadioButton) item_homebanner.findViewById(R.id.home_rdbtn3));
        rdBtn[3] = ((RadioButton) item_homebanner.findViewById(R.id.home_rdbtn4));

        homeSearchImg.setOnClickListener(this);
        home_img1.setOnClickListener(this);
        home_img2.setOnClickListener(this);
        home_img3.setOnClickListener(this);
        home_refresh.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER); //去掉阴影
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 10);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 10;
                if (position > 0 && position <= 10) return 2;
                if (position == 11) return 10;
                return 10;
            }
        });
        NewGridDecoration newGridDecoration = new NewGridDecoration(12, BaseValue.dp2px(6),
                getResources().getColor(R.color.mybg));
        adapter = new HomeAdapter(item_homebanner, item_hometable, sceneInfos, articleInfos);
        recyclerview.addItemDecoration(newGridDecoration);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        home_refresh.setRC(recyclerview, new RefreshLayout.TopOrBottom() {
            @Override
            public void gotoTop() {
            }
            @Override
            public void gotoBottom() {
                if (articleInfos.size() >= pageSize){
                    MyHttp.articleList(http, HomeAdapter.listType, 1, pageSize, pageNum, NewHomeFragment.this);
                }
            }

            @Override
            public void move() {
            }

            @Override
            public void stop() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_img:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.home_img1: //商品分类
                ((NgtAty) getActivity()).ngt_pager.setCurrentItem(2, false);
                break;
            case R.id.home_img2:  //9.9包邮
                ((NgtAty) getActivity()).ngt_pager.setCurrentItem(1, false);
                break;
            case R.id.home_img3: //店铺街
                startActivity(new Intent(getActivity(), ShopStreetActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        home_refresh.setUrlNum();
        if (code != 0) {
            showToast(msg);
            if (home_refresh.getUrlNum() == urlNum)
                home_refresh.setResultState(RefreshLayout.ResultState.failed);
            return;
        }

        switch (which) {
            case HomeAdapter.bannerType:
                InitViewPage(bean);
                break;
            case HomeAdapter.tableType:
                InitTable((ArrayList<HomeTableInfo>) bean);
                break;
            case HomeAdapter.classifyType:
                ArrayList<HomeSceneInfo> sceneInfos1 = (ArrayList<HomeSceneInfo>) bean;
                sceneInfos.addAll(sceneInfos1);
                break;
            case HomeAdapter.listType:
                articleInfos.addAll((ArrayList<ArticleInfo>) bean);
                break;
            default:
                break;
        }

        if (home_refresh.getUrlNum() == urlNum) {
            adapter.notifyDataSetChanged();
            home_refresh.setResultState(RefreshLayout.ResultState.success);
        }
    }

    private void getData() {
        MyHttp.articleList(http, HomeAdapter.listType, 1, pageSize, pageNum, this);
        MyHttp.scene(http, HomeAdapter.classifyType, this);
        MyHttp.homemenu(http, HomeAdapter.tableType, this);
        MyHttp.homeBanner(http, HomeAdapter.bannerType, this);
    }

    //首页3个图标按钮
    private void InitTable(ArrayList<HomeTableInfo> homeTableInfos) {
        if (null == homeTableInfos) {
            return;
        }
        for (int i = 0; i < homeTableInfos.size(); i++) {
            if (homeTableInfos.get(i).ad_index == 1) {
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code, home_img1);
            }
            if (homeTableInfos.get(i).ad_index == 2) {
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code, home_img3);
            }
            if (homeTableInfos.get(i).ad_index == 3) {
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code, home_img2);
            }
        }
    }

    //广告Viewpager
    private void InitViewPage(Object bean) {
        home_viewpager.setOnMyPageChangeListener(new MyViewPager.OnMyPageChangeListener() {
            //广告Viewpager保证可以循环滑动
            @Override
            public void OnMyPageSelected(int arg0) {
                if (arg0 == 0) {
                    home_viewpager.setCurrentItem(bannerInfos.size(), false);
                } else if (arg0 == bannerInfos.size() + 1) {
                    home_viewpager.setCurrentItem(1, false);
                } else {
                    rdBtn[(arg0 - 1)].setChecked(true);
                }
            }

            @Override
            public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void OnMyPageScrollStateChanged(int arg0) {
            }
        });

        ArrayList<HomeBannerInfo> bannerBeans = (ArrayList<HomeBannerInfo>) bean;

        if (bannerBeans.size() > 4) {
            bannerInfos = bannerBeans.subList(0, 3);
        } else {
            bannerInfos = bannerBeans;
        }
        home_viewpager.setAdapter(new BannerAdapter(getActivity(), rdBtn, bannerInfos));
        home_viewpager.setOffscreenPageLimit(3);
        home_viewpager.setCurrentItem(1, false);

        //设置循环播放
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                home_viewpager.setCurrentItem(1 + home_viewpager.getCurrentItem(), true);
            }
        };
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 4000);
    }

    @Override
    public void onRefresh() {
//        pageNum = 1;
//        sceneInfos.clear();
//        articleInfos.clear();
        getData();
    }
}
