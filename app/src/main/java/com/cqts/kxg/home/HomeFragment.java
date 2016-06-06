package com.cqts.kxg.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.refreshlayout.RefreshLayout;
import com.base.views.FullyGridLayoutManager;
import com.base.views.MyGridDecoration;
import com.base.views.MyScrollView;
import com.base.views.MyViewPager;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.bean.HomeBannerInfo;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.cqts.kxg.adapter.ArticleClassifyAdapter;
import com.cqts.kxg.adapter.ArticleListAdapter;
import com.cqts.kxg.adapter.BannerAdapter;
import com.cqts.kxg.bean.HomeTableInfo;
import com.cqts.kxg.main.NgtAty;
import com.cqts.kxg.utils.MyHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends BaseFragment implements Callback, MyViewPager
        .OnMyPageChangeListener, View.OnClickListener {
    private static final int VIEWPAGER = -1;
    private static final int urlNum = 4; //当前页面是刷新的url数量
    private Handler handler;
    private MyViewPager home_viewpager;
    private RefreshLayout home_refresh;
    private RadioButton[] rdBtn = new RadioButton[4];
    private Timer timer;
    private TimerTask timerTask;
    private ImageView home_img1;
    private ImageView home_img2;
    private ImageView home_img3;
    private RecyclerView home_rv;
    private RecyclerView home_rv2;
    private MyScrollView home_scroll;
    ArrayList<HomeSceneInfo> sceneInfos = new ArrayList<>();
    ArrayList<ArticleInfo> articleInfos = new ArrayList<>();
    private ArticleClassifyAdapter articleClassifyAdapter;
    private ArticleListAdapter articleListAdapter;
    private ImageView home_search_img;
    List<HomeBannerInfo> bannerInfos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_home, null);
            InitView();
            getData();
            InitRefresh();
            InitArticleClassify();
            InitArticleList();
        }
        return view;
    }

    private void InitView() {
        home_img1 = (ImageView) view.findViewById(R.id.home_img1);
        home_img2 = (ImageView) view.findViewById(R.id.home_img2);
        home_img3 = (ImageView) view.findViewById(R.id.home_img3);

        home_viewpager = ((MyViewPager) view.findViewById(R.id.home_viewpager));
        rdBtn[0] = ((RadioButton) view.findViewById(R.id.home_rdbtn1));
        rdBtn[1] = ((RadioButton) view.findViewById(R.id.home_rdbtn2));
        rdBtn[2] = ((RadioButton) view.findViewById(R.id.home_rdbtn3));
        rdBtn[3] = ((RadioButton) view.findViewById(R.id.home_rdbtn4));
        home_rv = (RecyclerView) view.findViewById(R.id.home_rv);
        home_rv2 = (RecyclerView) view.findViewById(R.id.home_rv2);
        home_scroll = (MyScrollView) view.findViewById(R.id.home_scroll);
        home_refresh = (RefreshLayout) view.findViewById(R.id.home_refresh);
        home_search_img = (ImageView) view.findViewById(R.id.home_search_img);
        home_scroll.setOverScrollMode(View.OVER_SCROLL_NEVER); //去掉阴影
        home_search_img.setOnClickListener(this);
        home_img1.setOnClickListener(this);
        home_img2.setOnClickListener(this);
        home_img3.setOnClickListener(this);
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

    private void getData() {
        //文章分类
        MyHttp.scene(http, 1, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                home_refresh.setUrlNum();
                if (code != 0) {
                    showToast(msg);
                    if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                        home_refresh.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                ArrayList<HomeSceneInfo> sceneInfos1 = (ArrayList<HomeSceneInfo>) bean;
                sceneInfos.addAll(sceneInfos1);
                articleClassifyAdapter.notifyDataSetChanged();

                if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                    home_refresh.setResultState(RefreshLayout.ResultState.success);
            }
        });

        //文章列表
        MyHttp.articleList(http, 2, 1, 5, 1, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                home_refresh.setUrlNum();
                if (code != 0) {
                    showToast(msg);
                    if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                        home_refresh.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                articleInfos.addAll((ArrayList<ArticleInfo>) bean);
                articleListAdapter.notifyDataSetChanged();

                if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                    home_refresh.setResultState(RefreshLayout.ResultState.success);
            }
        });

        //首页banner图
        MyHttp.homeBanner(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                home_refresh.setUrlNum();
                if (code != 0) {
                    showToast(msg);
                    if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                        home_refresh.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                InitViewPage(bean);
                if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                    home_refresh.setResultState(RefreshLayout.ResultState.success);

            }
        });

        MyHttp.homemenu(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                home_refresh.setUrlNum();
                if (code != 0) {
                    showToast(msg);
                    if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                        home_refresh.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                if (home_refresh.isRefreshing && home_refresh.getUrlNum() == urlNum)
                    home_refresh.setResultState(RefreshLayout.ResultState.success);
                InitTable((ArrayList<HomeTableInfo>) bean);
            }
        });
    }

    private void InitTable(ArrayList<HomeTableInfo> homeTableInfos) {
        if (null==homeTableInfos){
            return;
        }
        for (int i=0;i<homeTableInfos.size();i++){
            if (homeTableInfos.get(i).ad_index == 1){
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code,home_img1);
            }
            if (homeTableInfos.get(i).ad_index == 2){
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code,home_img3);
            }
            if (homeTableInfos.get(i).ad_index == 3){
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code,home_img2);
            }
        }
    }


    private void InitRefresh() {
        home_refresh.setScrollView(home_scroll, null);
        home_refresh.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sceneInfos.clear();
                articleInfos.clear();
                getData();
            }
        });
    }


    //文章分类
    private void InitArticleClassify() {
        FullyGridLayoutManager manager1 = new FullyGridLayoutManager(getActivity(), 5);
        home_rv.setLayoutManager(manager1);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(0), BaseValue
                .dp2px(0), Color.WHITE, true);
        home_rv.addItemDecoration(myGridDecoration);
        articleClassifyAdapter = new ArticleClassifyAdapter(getActivity(),
                sceneInfos);
        home_rv.setAdapter(articleClassifyAdapter);
    }

    //文章列表
    private void InitArticleList() {
        FullyGridLayoutManager manager1 = new FullyGridLayoutManager(getActivity(), 1);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(6), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), true);
        home_rv2.setLayoutManager(manager1);
        home_rv2.addItemDecoration(myGridDecoration);
        articleListAdapter = new ArticleListAdapter(getActivity(), articleInfos);
        home_rv2.setAdapter(articleListAdapter);
    }

    //广告Viewpager
    private void InitViewPage(Object bean) {
        home_viewpager.setOnMyPageChangeListener(this);
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
        handler = new Handler(this);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(VIEWPAGER);
            }
        };
        timer.schedule(timerTask, 0, 4000);
    }

    //广告Viewpager的轮播事件
    @Override
    public boolean handleMessage(Message msg) {
        home_viewpager.setCurrentItem(1 + home_viewpager.getCurrentItem(), true);
        return false;
    }

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
    @Override
    public void onStop() {
        super.onStop();
        if (home_refresh != null) {
            home_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }

    @Override
    public void onShow() {
        super.onShow();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}