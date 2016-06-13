package com.cqts.kxg.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseValue;
import com.base.refreshlayout.RefreshLayout;
import com.base.views.MyGridDecoration;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.GoodsAdapter;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/6/1.
 */
@SuppressLint("ValidFragment")
public class GoodsFragment extends MyFragment implements RefreshLayout.OnRefreshListener,
        MyHttp.MyHttpResult, MyFragment.HttpFail, RefreshLayout.TopOrBottom {
    private GoodsAdapter adapter;
    private List<GoodsInfo> goodsInfos = new ArrayList<>();
    private GridLayoutManager manager;
    private RefreshLayout goods_refresh;
    private RecyclerView goods_rclv;
    Where where;
    private int PageSize = 50;
    private int PageNum = 1;
    String keyword = ""; //搜索文章的关键字
    String sort = "";
    String order = "";

    /**
     * 搜索商品
     */
    public GoodsFragment(Where where, String keyword, String sort, String order) {
        this.keyword = keyword;
        this.sort = sort;
        this.order = order;
        this.where = where;
    }

    /**
     * 喜欢
     */
    public GoodsFragment(Where where) {
        this.where = where;
    }

    //设置搜索的排序参数
    public void setSearchValue(String sort, String order) {
        this.sort = sort;
        this.order = order;
        PageNum = 1;
        goodsInfos.clear();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_goods, null);
            InitView();
            getData();
        }
        return view;
    }

    private void InitView() {
        goods_refresh = (RefreshLayout) view.findViewById(R.id.goods_refresh);
        goods_rclv = (RecyclerView) view.findViewById(R.id.goods_rclv);
        goods_refresh.setOnRefreshListener(this);
        InteRV();
    }

    /**
     * 初始化文章列表
     */
    private void InteRV() {
        goods_rclv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        manager = new GridLayoutManager(getActivity(), 2);
        goods_rclv.setLayoutManager(manager);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(8), BaseValue
                .dp2px(8), getResources().getColor(R.color.mybg), false);
        myGridDecoration.setImageView(R.id.item_nine_img, 1);
        goods_rclv.addItemDecoration(myGridDecoration);
        adapter = new GoodsAdapter(getActivity(), goodsInfos);
        goods_rclv.setAdapter(adapter);
        goods_refresh.setRC(goods_rclv, this);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        PageNum = 1;
        goodsInfos.clear();
        getData();
    }

    private void getData() {
        switch (where) {
            case search: //来自搜索
                goods_refresh.setRefreshble(false);
                MyHttp.searchGoods(http, null, PageSize, PageNum, keyword, sort, order, this);
                break;
            case love: //来自喜欢
                goods_refresh.setRefreshble(false);
                MyHttp.loveGoods(http,null,PageNum,PageSize,this);
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
            goods_refresh.setResultState(RefreshLayout.ResultState.failed);
            return;
        }
        if (code != 0) {
            showToast(msg);
            goods_refresh.setResultState(RefreshLayout.ResultState.failed);
            return;
        }
        goods_refresh.setResultState(RefreshLayout.ResultState.success);

        goodsInfos.addAll((ArrayList<GoodsInfo>) bean);
        if (goodsInfos.size() == 0) {
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
        if (goodsInfos.size() >= PageSize) {
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
     * 商品的ArticleFragment出现在什么地方<p>
     * search ------- 搜索<br>
     * love --------- 喜欢(收藏)<br>
     */
    public enum Where {
        search, love
    }

    @Override
    public void onStop() {
        super.onStop();
        if (goods_refresh!=null&&goods_refresh.isRefreshing) {
            goods_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }
}
