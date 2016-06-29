package com.cqts.kxg.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseValue;
import com.base.views.RefreshLayout;
import com.base.utils.MyGridDecoration;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.ArticleAdapter;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.List;

import static com.cqts.kxg.home.ArticleFragment.Where.*;

/**
 * Created by Administrator on 2016/6/1.
 */
@SuppressLint("ValidFragment")
public class ArticleFragment extends MyFragment implements RefreshLayout.OnRefreshListener,
        MyHttp.MyHttpResult, MyFragment.HttpFail, RefreshLayout.TopOrBottom {
    private ArticleAdapter adapter;
    private List<ArticleInfo> articleInfos = new ArrayList<ArticleInfo>();
    private GridLayoutManager manager;
    private RefreshLayout article_refresh;
    private RecyclerView article_rclv;
    Where where;
    private int PageSize = 20;
    private int PageNum = 1;
    int hotType = 1; //热门模块的分类
    String keyword = ""; //搜索文章的关键字
    String cat_id; //来自首页分类文章查询
    String sort; //首页文章分类排序

    /**
     * 热门的分类hotType <p>
     */
    public ArticleFragment(Where where, int hotType) {
        this.where = where;
        //来自热门
        this.hotType = hotType;
    }

    /**
     * 来自搜索传keyword<p>
     */
    public ArticleFragment(Where where, String keyword) {
        this.where = where;
        this.keyword = keyword;
    }

    /**
     * 来自首页的分类文章查询传 cat_id
     */
    public ArticleFragment(Where where, String cat_id, String sort) {
        this.where = where;

        //来自首页分类查询
        this.cat_id = cat_id;
        this.sort = sort;
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
    public ArticleFragment(Where where) {
        this.where = where;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null == view) {
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
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(6), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        article_rclv.setLayoutManager(manager);
        article_rclv.addItemDecoration(myGridDecoration);
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
            case hot: //来自热门
                MyHttp.articleHot(http, 1, PageSize, PageNum, hotType, this);
                break;
            case search: //来自搜索
                article_refresh.setRefreshble(false);
                MyHttp.searchArticle(http, 2, PageSize, PageNum, keyword, this);
                break;
            case love: //来自喜欢
                article_refresh.setRefreshble(false);
                MyHttp.loveArticle(http, 3, PageNum, PageSize, this);
                break;
            case home: //来住首页
                article_refresh.setRefreshble(false);
                MyHttp.articleListing(http, 4, PageSize, PageNum, cat_id, sort,this);
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

    /**
     * 文章的ArticleFragment出现在什么地方<p>
     * hot ---------- 热门<br>
     * search ------- 搜索<br>
     * love --------- 喜欢(收藏)<br>
     */
    public enum Where {
        hot, search, love, home
    }

    @Override
    public void onStop() {
        super.onStop();
        if (article_refresh != null && article_refresh.isRefreshing) {
            article_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }
}
