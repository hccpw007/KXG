package com.cqts.kxg.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseValue;
import com.base.utils.GridDecoration;
import com.base.views.RefreshLayout;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.ArticleAdapter;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ArticleFragment extends MyFragment implements RefreshLayout.OnRefreshListener,
        MyHttp.MyHttpResult, MyFragment.HttpFail, RefreshLayout.TopOrBottom {
    private ArticleAdapter adapter;
    private List<ArticleInfo> articleInfos = new ArrayList<ArticleInfo>();
    private GridLayoutManager manager;
    private RefreshLayout article_refresh;
    private RecyclerView article_rclv;
    int where;
    private int PageSize = 20;
    private int PageNum = 1;
    int hotType = 1; //热门模块的分类
    String keyword = ""; //搜索文章的关键字
    String cat_id; //来自首页分类文章查询
    String sort; //首页文章分类排序

    /**
     * 热门的分类hotType <p>
     */
    public static ArticleFragment getInstanceForHOt(int hotType) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", Where.hot);
        bundle.putInt("hotType", hotType);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 来自搜索
     */
    public static ArticleFragment getInstanceForSearch(String keyword) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", Where.search);
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 来自首页的分类文章查询传 cat_id
     */
    public static ArticleFragment getInstanceForHome(String cat_id, String sort) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", Where.home);
        bundle.putString("cat_id", cat_id);
        bundle.putString("sort", sort);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setSort(String sort) {
        this.sort = sort;
        PageNum = 1;
        articleInfos.clear();
        getData();
    }

    /**
     * 来自我的喜欢
     */
    public static ArticleFragment getInstanceForLove() {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", Where.love);
        fragment.setArguments(bundle);
        return fragment;
    }

    void getBundleData(Bundle bundle) {
        this.where = bundle.getInt("where");
        switch (this.where) {
            case Where.hot:
                this.hotType = bundle.getInt("hotType");
                break;
            case Where.home:
                this.cat_id = bundle.getString("cat_id");
                this.sort = bundle.getString("sort");
                break;
            case Where.search:
                this.keyword = bundle.getString("keyword");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null == view) {
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_article, null);
            InitView();
            getData();
        }
        return view;
    }

    private void InitView() {
        article_refresh = (RefreshLayout) view.findViewById(R.id.article_refresh);
        article_rclv = (RecyclerView) view.findViewById(R.id.article_rclv);
        article_refresh.setOnRefreshListener(this);
        InteRV();
    }

    /**
     * 初始化文章列表
     */
    private void InteRV() {
        article_rclv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        manager = new GridLayoutManager(getActivity(), 1);
        GridDecoration newGridDecoration = new GridDecoration(0, BaseValue.dp2px(6),
                getResources().getColor(R.color.mybg), true);
        article_rclv.setLayoutManager(manager);
        article_rclv.addItemDecoration(newGridDecoration);
        adapter = new ArticleAdapter(this, articleInfos);
        article_rclv.setAdapter(adapter);
        article_refresh.setRC(article_rclv, this);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        PageNum = 1;
        articleInfos.clear();
        getData();
    }

    private void getData() {
        switch (where) {
            case Where.hot: //来自热门
                MyHttp.articleHot(http, 1, PageSize, PageNum, hotType, this);
                break;
            case Where.search: //来自搜索
                article_refresh.setRefreshble(false);
                MyHttp.searchArticle(http, 2, PageSize, PageNum, keyword, this);
                break;
            case Where.love: //来自喜欢
                article_refresh.setRefreshble(false);
                MyHttp.loveArticle(http, 3, PageNum, PageSize, this);
                break;
            case Where.home: //来住首页
                article_refresh.setRefreshble(false);
                MyHttp.articleListing(http, 4, PageSize, PageNum, cat_id, sort, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void toHttpAgain() {
        getData();
    }

    /**
     * 网络数据返回
     */
    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {

        if (code == 404) {
            setHttpFail(this);
            article_refresh.setResultState(RefreshLayout.ResultState.failed);
            return;
        }
        if (code != 0) {
            showToast(msg);
            article_refresh.setResultState(RefreshLayout.ResultState.failed);
            return;
        }
        article_refresh.setResultState(RefreshLayout.ResultState.success);
        articleInfos.addAll((ArrayList<ArticleInfo>) bean);
        if (articleInfos.size() == 0) {
            setHttpNotData(this);
            return;
        }
        setHttpSuccess();
        adapter.notifyDataSetChanged();
        PageNum++;
    }

    //到达底部  加载更多
    @Override
    public void gotoBottom() {
        if (articleInfos.size() >= PageSize) {
            getData();
        }
    }

    @Override
    public void move() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void gotoTop() {
    }
    @Override
    public void onStop() {
        super.onStop();
        if (article_refresh != null && article_refresh.isRefreshing) {
            article_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }
}
