package com.cqts.kxg.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.base.views.MyViewPager;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.HomeBannerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/16.
 */
public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.Viewholder> {

    private Context context;
    List<HomeBannerInfo> bannerInfos;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;

    public HomeBannerAdapter(List<HomeBannerInfo> bannerInfos) {
        this.bannerInfos = bannerInfos;
    }

    @Override
    public HomeBannerAdapter.Viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (context == null) {
            context = viewGroup.getContext();
        }
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.item_homebanner, null));
    }

    @Override
    public void onBindViewHolder(final Viewholder viewholder, int i) {

        if (bannerInfos.size() == 0) return;

        viewholder.home_viewpager.setOnMyPageChangeListener(new MyViewPager
                .OnMyPageChangeListener() {
            //广告Viewpager保证可以循环滑动
            @Override
            public void OnMyPageSelected(int arg0) {
                if (arg0 == 0) {
                    viewholder.home_viewpager.setCurrentItem(bannerInfos.size(), false);
                } else if (arg0 == bannerInfos.size() + 1) {
                    viewholder.home_viewpager.setCurrentItem(1, false);
                } else {
                    viewholder.rdBtn[(arg0 - 1)].setChecked(true);
                }
            }

            @Override
            public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void OnMyPageScrollStateChanged(int arg0) {
            }
        });

        ArrayList<HomeBannerInfo> bannerBeans = (ArrayList<HomeBannerInfo>) bannerInfos;

        if (bannerBeans.size() > 4) {
            bannerInfos = bannerBeans.subList(0, 3);
        } else {
            bannerInfos = bannerBeans;
        }

        viewholder.home_viewpager.setAdapter(new HomeViewPagerAdapter(context, viewholder.rdBtn,
                bannerInfos));
        viewholder.home_viewpager.setOffscreenPageLimit(3);
        viewholder.home_viewpager.setCurrentItem(1, false);


        if (handler != null) {
            return;
        }

        //设置循环播放
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                viewholder.home_viewpager.setCurrentItem(1 + viewholder.home_viewpager
                        .getCurrentItem(), true);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 4000);
    }

    @Override
    public int getItemCount() {

        return 1;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        MyViewPager home_viewpager;
        RadioButton[] rdBtn = new RadioButton[4];

        public Viewholder(View itemView) {
            super(itemView);
            home_viewpager = ((MyViewPager) itemView.findViewById(R.id.home_viewpager));
            rdBtn[0] = ((RadioButton) itemView.findViewById(R.id.home_rdbtn1));
            rdBtn[1] = ((RadioButton) itemView.findViewById(R.id.home_rdbtn2));
            rdBtn[2] = ((RadioButton) itemView.findViewById(R.id.home_rdbtn3));
            rdBtn[3] = ((RadioButton) itemView.findViewById(R.id.home_rdbtn4));
        }
    }
}
