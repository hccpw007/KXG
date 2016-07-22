package com.cqts.kxg.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.BaseValue;
import com.base.views.RefreshLayout;
import com.base.utils.GridDecoration;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.ArticleAdapter;
import com.cqts.kxg.adapter.HomeAdapter;
import com.cqts.kxg.adapter.HomeClassifyAdapter;
import com.cqts.kxg.adapter.HomeBannerAdapter;
import com.cqts.kxg.adapter.HomeTableAdapter;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.bean.HomeBannerInfo;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.cqts.kxg.bean.HomeTableInfo;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends MyFragment implements View.OnClickListener, RefreshLayout
        .OnRefreshListener, MyHttp.MyHttpResult {
    List<HomeBannerInfo> bannerInfos = new ArrayList<>();
    ArrayList<HomeSceneInfo> sceneInfos = new ArrayList<>();
    ArrayList<ArticleInfo> articleInfos = new ArrayList<>();
    ArrayList<HomeTableInfo> homeTableInfos = new ArrayList<>();

    private RefreshLayout home_refresh;
    private RecyclerView recyclerview;
    private HomeAdapter adapter;

    int pageSize = 20;
    int pageNum = 1;

    private static final int urlNum = 4; //当前页面是刷新的url数量

    public static HomeFragment fragment;

    public static HomeFragment getInstance() {
        if (fragment == null) {
            fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_home, null);
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

        homeSearchImg.setOnClickListener(this);
        home_refresh.setOnRefreshListener(this);
    }

    private void getData() {
        MyHttp.articleList(http, HomeAdapter.listType, 1, pageSize, pageNum, this);
        MyHttp.scene(http, HomeAdapter.classifyType, this);
        MyHttp.homemenu(http, HomeAdapter.tableType, this);
        MyHttp.homeBanner(http, HomeAdapter.bannerType, this);
    }

    private void initRecyclerView() {
        ArticleAdapter articleListAdapter = new ArticleAdapter(this, articleInfos);
        HomeClassifyAdapter homeArticleClassifyAdapter = new HomeClassifyAdapter(sceneInfos);
        HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(bannerInfos);
        HomeTableAdapter homeTableAdapter = new HomeTableAdapter(homeTableInfos);

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

        GridDecoration newGridDecoration = new GridDecoration(12, BaseValue.dp2px(6),
                getResources().getColor(R.color.mybg), true);
        adapter = new HomeAdapter(homeBannerAdapter, homeTableAdapter,
                homeArticleClassifyAdapter, articleListAdapter);
        recyclerview.addItemDecoration(newGridDecoration);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        home_refresh.setRC(recyclerview, new RefreshLayout.TopOrBottom() {
            @Override
            public void gotoTop() {
            }

            @Override
            public void gotoBottom() {
                if (articleInfos.size() >= pageSize) {
                    MyHttp.articleList(http, HomeAdapter.listType, 1, pageSize, pageNum,
                            HomeFragment.this);
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
        //搜索
        startActivity(new Intent(getActivity(), SearchActivity.class));
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
                bannerInfos.clear();
                bannerInfos.addAll((List<HomeBannerInfo>) bean);
                break;
            case HomeAdapter.tableType:
                homeTableInfos.clear();
                homeTableInfos.addAll((ArrayList<HomeTableInfo>) bean);
                break;
            case HomeAdapter.classifyType:
                sceneInfos.clear();
                sceneInfos.addAll((ArrayList<HomeSceneInfo>) bean);
                break;
            case HomeAdapter.listType:
                articleInfos.addAll((ArrayList<ArticleInfo>) bean);
                pageNum++;
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
        if (home_refresh.getUrlNum() == urlNum) {
            home_refresh.setResultState(RefreshLayout.ResultState.success);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        sceneInfos.clear();
        articleInfos.clear();
        getData();
    }
}
