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
import com.cqts.kxg.adapter.ShopAdapter;
import com.cqts.kxg.bean.ShopInfo;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/6/1.
 */
public class ShopFragment extends MyFragment implements RefreshLayout.OnRefreshListener,
        MyHttp.MyHttpResult, MyFragment.HttpFail, RefreshLayout.TopOrBottom {
    private ShopAdapter adapter;
    private List<ShopInfo> shopInfos = new ArrayList<>();
    private GridLayoutManager manager;
    private RefreshLayout shop_refresh;
    private RecyclerView shop_rclv;
    int where;
    private int PageSize = 20;
    private int PageNum = 1;
    String str = ""; //搜索或者店铺街传的参数

    /**
     * 来自搜索或者热门店铺 <p>
     * 搜索的时候 传的参数是keyword<br>
     * 店铺街传的参数就是排序 sort<br>
     */
    public static ShopFragment getInstanceForSeach(String str) {
        ShopFragment fragment = new ShopFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", WhereS.search);
        bundle.putString("str", str);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ShopFragment getInstanceForStreet(String str) {
        ShopFragment fragment = new ShopFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", WhereS.street);
        bundle.putString("str", str);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 来自我的喜欢
     */
    public static ShopFragment getInstanceForLove() {
        ShopFragment fragment = new ShopFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", WhereS.love);
        fragment.setArguments(bundle);
        return fragment;
    }

    void getBundleData(Bundle bundle) {
        this.where = bundle.getInt("where");
        switch (this.where) {
            case WhereS.search:
            case WhereS.street:
                this.str = bundle.getString("str");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null == view) {
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_shop, null);
            InitView();
            getData();
        }
        return view;
    }

    private void InitView() {
        shop_refresh = (RefreshLayout) view.findViewById(R.id.shop_refresh);
        shop_rclv = (RecyclerView) view.findViewById(R.id.shop_rclv);
        shop_refresh.setOnRefreshListener(this);
        InteRV();
    }

    /**
     * 初始化文章列表
     */
    private void InteRV() {
        shop_rclv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        manager = new GridLayoutManager(getActivity(), 1);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(8), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        shop_rclv.setLayoutManager(manager);
        shop_rclv.addItemDecoration(myGridDecoration);
        adapter = new ShopAdapter(getActivity(), shopInfos);
        shop_rclv.setAdapter(adapter);
        shop_refresh.setRC(shop_rclv, this);

    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        PageNum = 1;
        shopInfos.clear();
        getData();
    }

    private void getData() {
        switch (where) {
            case WhereS.search: //来自搜索
                shop_refresh.setRefreshble(false);
                MyHttp.searchShop(http, 1, PageSize, PageNum, str, "", this);
                break;
            case WhereS.love: //来自喜欢
                shop_refresh.setRefreshble(false);
                MyHttp.loveShop(http, 2, PageNum, PageSize, this);
                break;
            case WhereS.street: //来自店铺街
                shop_refresh.setRefreshble(false);
                MyHttp.searchShop(http, 3, PageSize, PageNum, "", str, this);
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
            shop_refresh.setResultState(RefreshLayout.ResultState.failed);
            return;
        }
        if (code != 0) {
            showToast(msg);
            shop_refresh.setResultState(RefreshLayout.ResultState.failed);
            return;
        }
        shop_refresh.setResultState(RefreshLayout.ResultState.success);
        shopInfos.addAll((ArrayList<ShopInfo>) bean);
        if (shopInfos.size() == 0) {
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
        if (shopInfos.size() >= PageSize) {
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
        if (shop_refresh != null && shop_refresh.isRefreshing) {
            shop_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }
}
