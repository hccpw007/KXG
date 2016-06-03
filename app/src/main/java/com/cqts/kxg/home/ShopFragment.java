package com.cqts.kxg.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.views.MyGridDecoration;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.ShopAdapter;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.bean.ShopInfo;
import com.cqts.kxg.main.MyFragment;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends MyFragment {
    private GridLayoutManager manager;
    ArrayList<ShopInfo> shopInfos = new ArrayList<>();
    private ShopAdapter adapter;
    private GotoDottom gotoDottom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_shop, null);
            InitView();
        }
        return view;
    }

    private void InitView() {
        RecyclerView fragment_rclv = (RecyclerView) view.findViewById(R.id.fragment_rclv);
        fragment_rclv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        manager = new GridLayoutManager(getActivity(), 1);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(8), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        fragment_rclv.setLayoutManager(manager);
        fragment_rclv.addItemDecoration(myGridDecoration);
        adapter = new ShopAdapter(getActivity(), shopInfos);
        fragment_rclv.setAdapter(adapter);

        fragment_rclv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager.findLastCompletelyVisibleItemPosition() == manager.getItemCount() - 1) {
                    //底部
                    if (gotoDottom != null) {
                        gotoDottom.loadMore();
                    }
                }
            }
        });
    }

    public void setNotify(List<ShopInfo> shopInfo) {
        shopInfos.clear();
        shopInfos.addAll(shopInfo);
        adapter.notifyDataSetChanged();
    }

    public void setGotoBottom(GotoDottom gotoDottom) {
        this.gotoDottom = gotoDottom;
    }

    public interface GotoDottom {
        void loadMore();
    }
}
