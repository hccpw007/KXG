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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.refreshlayout.RefreshLayout;
import com.base.views.FullyGridLayoutManager;
import com.base.views.MyGridDecoration;
import com.base.views.MyScrollView;
import com.base.views.MyViewPager;
import com.cqts.kxg.R;
import com.cqts.kxg.home.adapter.HomeRecyclerViewAdapter;
import com.cqts.kxg.home.adapter.HomeViewpagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends BaseFragment implements Callback, MyViewPager
        .OnMyPageChangeListener, View.OnClickListener {
    private static final String ViewGroup = null;
    private static final int VIEWPAGER = -1;
    private Handler handler;
    private MyViewPager home_viewpager;
    private RefreshLayout home_refresh;
    private RadioButton[] rdBtn = new RadioButton[4];
    private Timer timer;
    private TimerTask timerTask;
    private RecyclerView home_rv;
    private RecyclerView home_rv2;
    private MyScrollView home_scroll;
    private LinearLayout home_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_home, null);
            InitView();
        }
        return view;
    }

    private void InitView() {
        home_viewpager = ((MyViewPager) view.findViewById(R.id.home_viewpager));
        rdBtn[0] = ((RadioButton) view.findViewById(R.id.home_rdbtn1));
        rdBtn[1] = ((RadioButton) view.findViewById(R.id.home_rdbtn2));
        rdBtn[2] = ((RadioButton) view.findViewById(R.id.home_rdbtn3));
        rdBtn[3] = ((RadioButton) view.findViewById(R.id.home_rdbtn4));
        home_rv = (RecyclerView) view.findViewById(R.id.home_rv);
        home_rv2 = (RecyclerView) view.findViewById(R.id.home_rv2);
        home_scroll = (MyScrollView) view.findViewById(R.id.home_scroll);
        home_refresh = (RefreshLayout) view.findViewById(R.id.home_refresh);
        home_search = (LinearLayout) view.findViewById(R.id.home_search);

        home_search.setOnClickListener(this);
        InitRefresh();
        InitRecyclerView1();
        InitRecyclerView2();
        InitViewPage();
    }

    private void InitRefresh() {
        home_refresh.setScrollView(home_scroll);

        home_refresh.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        home_scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search: //搜索
                startActivity(new Intent(getActivity(),SearchActivity.class));
                break;
            default:
                break;
        }
    }

    //第一个recyclerview(今日推荐)
    private void InitRecyclerView1() {
        FullyGridLayoutManager manager1 = new FullyGridLayoutManager(getActivity(), 2);
        home_rv.setLayoutManager(manager1);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(4), BaseValue
                .dp2px(4), Color.WHITE, true);
        myGridDecoration.setImageView(R.id.item_homerv_img, 1);
        myGridDecoration.setFrame(true);
        home_rv.addItemDecoration(myGridDecoration);
        home_rv.setAdapter(new HomeRecyclerViewAdapter(getActivity()));
    }

    private void InitRecyclerView2() {
        FullyGridLayoutManager manager1 = new FullyGridLayoutManager(getActivity(), 2);
        home_rv2.setLayoutManager(manager1);
        MyGridDecoration myGridDecoration = new MyGridDecoration(10, 10, Color.WHITE, true);
        myGridDecoration.setImageView(R.id.item_homerv_img, 1);
        home_rv2.addItemDecoration(myGridDecoration);
        home_rv2.setAdapter(new HomeRecyclerViewAdapter(getActivity()));
    }

    //广告Viewpager
    private void InitViewPage() {
        home_viewpager.setOnMyPageChangeListener(this);
        home_viewpager.setAdapter(new HomeViewpagerAdapter(getActivity(), rdBtn));
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
            home_viewpager.setCurrentItem(3, false);
        } else if (arg0 == 4) {
            home_viewpager.setCurrentItem(1, false);
        } else {
            rdBtn[(arg0 - 1)].setChecked(true);
        }
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (home_refresh != null) {
            home_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
