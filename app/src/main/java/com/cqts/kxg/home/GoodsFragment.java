package com.cqts.kxg.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseValue;
import com.base.utils.MyGridDecoration;
import com.base.views.RefreshLayout;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.GoodsAdapter;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.List;

public class GoodsFragment extends MyFragment implements RefreshLayout.OnRefreshListener,
        MyHttp.MyHttpResult, MyFragment.HttpFail, RefreshLayout.TopOrBottom {
    private GoodsAdapter adapter;
    private List<GoodsInfo> goodsInfos = new ArrayList<>();
    private GridLayoutManager manager;
    private RefreshLayout goods_refresh;
    public RecyclerView goods_rclv;
    int where;
    private int PageSize = 50;
    private int PageNum = 1;
    String keyword = ""; //搜索文章的关键字
    String sort = "";
    String order = "";
    String cat_id = "0";

    public GoodsFragment() {
    }

    /**
     * 喜欢
     */
    public static GoodsFragment getInstanceForLove() {
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", Where.love);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 搜索和分类商品
     */
    public static GoodsFragment getInstanceForSearch(String keyword, String sort, String order,
                                                       String cat_id) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", Where.search);
        bundle.putString("keyword", keyword);
        bundle.putString("sort", sort);
        bundle.putString("order", order);
        bundle.putString("cat_id", cat_id);
        fragment.setArguments(bundle);
        return fragment;
    }


    //设置搜索的排序参数
    public void setSearchValue(String sort, String order) {
        this.sort = sort;
        this.order = order;
        PageNum = 1;
        goodsInfos.clear();
        getData();
    }

    void getBundleData(Bundle bundle) {
        this.where = bundle.getInt("where");
        switch (this.where) {
            case Where.search:
                this.keyword = bundle.getString("keyword");
                this.sort = bundle.getString("sort");
                this.order = bundle.getString("order");
                this.cat_id = bundle.getString("cat_id");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null == view) {
            getBundleData(getArguments());
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
//        myGridDecoration.setImageView(R.id.item_nine_img, 1);
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
            case Where.search: //来自搜索
                goods_refresh.setRefreshble(false);
                MyHttp.searchGoods(http, null, PageSize, PageNum, keyword, sort, order, cat_id,
                        this);
                break;
            case Where.love: //来自喜欢
                goods_refresh.setRefreshble(false);
                MyHttp.loveGoods(http, null, PageNum, PageSize, this);
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

    @Override
    public void onStop() {
        super.onStop();
        if (goods_refresh != null && goods_refresh.isRefreshing) {
            goods_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }
}
