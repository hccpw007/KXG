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
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.adapter.GoodsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
public class GoodsFragment extends BaseFragment {
    private RecyclerView fragment_rclv;
    private GoodsAdapter adapter;
    private  List<GoodsInfo> shopInfos = new ArrayList<GoodsInfo>();
    private GridLayoutManager manager;
    private GotoDottom gotoDottom;
    public GoodsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_goods, null);
            InitView();
        }
        return view;
    }

    private void InitView() {
        fragment_rclv = (RecyclerView) view.findViewById(R.id.fragment_rclv);
        fragment_rclv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        manager = new GridLayoutManager(getActivity(), 2);
        fragment_rclv.setLayoutManager(manager);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(8), BaseValue
                .dp2px(8), getResources().getColor(R.color.mybg), false);
        myGridDecoration.setImageView(R.id.item_nine_img, 1);
        fragment_rclv.addItemDecoration(myGridDecoration);
        adapter = new GoodsAdapter(getActivity(), shopInfos);
        fragment_rclv.setAdapter(adapter);

        fragment_rclv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager.findLastCompletelyVisibleItemPosition() == manager.getItemCount() -
                        1) {
                    //底部
                    if (gotoDottom!=null){
                        gotoDottom.loadMore();
                    }
                }
            }
        });
    }

    public void setNotify(List<GoodsInfo> shopInfo) {
        shopInfos.clear();
        shopInfos.addAll(shopInfo);
        adapter.notifyDataSetChanged();
    }

    public void setGotoBottom(GotoDottom gotoDottom) {
            this.gotoDottom =gotoDottom;
    }

    public interface GotoDottom {
        void loadMore();
    }
}
