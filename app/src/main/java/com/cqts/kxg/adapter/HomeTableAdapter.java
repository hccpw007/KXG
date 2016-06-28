package com.cqts.kxg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqts.kxg.R;
import com.cqts.kxg.bean.HomeTableInfo;
import com.cqts.kxg.home.ShopStreetActivity;
import com.cqts.kxg.main.NgtAty;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/16.
 */
public class HomeTableAdapter extends RecyclerView.Adapter<HomeTableAdapter.ViewHolder>
        implements View.OnClickListener {
    Context context;
    ArrayList<HomeTableInfo> homeTableInfos;

    public HomeTableAdapter(ArrayList<HomeTableInfo> homeTableInfos) {
        this.homeTableInfos = homeTableInfos;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (context == null) {
            context = viewGroup.getContext();
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hometable, null));
    }
    @Override
    public void onBindViewHolder(ViewHolder viewholder, int a) {
        viewholder.home_img1.setOnClickListener(this);
        viewholder.home_img2.setOnClickListener(this);
        viewholder.home_img3.setOnClickListener(this);
        if (null == homeTableInfos) {
            return;
        }
        for (int i = 0; i < homeTableInfos.size(); i++) {
            if (homeTableInfos.get(i).ad_index == 1) {
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code, viewholder.home_img1);
            }
            if (homeTableInfos.get(i).ad_index == 2) {
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code, viewholder.home_img3);
            }

            if (homeTableInfos.get(i).ad_index == 3) {
                ImageLoader.getInstance().displayImage(homeTableInfos.get(i).ad_code, viewholder.home_img2);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_img1: //商品分类
                ((NgtAty) context).ngt_pager.setCurrentItem(3, false);
                break;
            case R.id.home_img2:  //9.9包邮
                ((NgtAty) context).ngt_pager.setCurrentItem(1, false);
                break;
            case R.id.home_img3: //店铺街
                context.startActivity(new Intent(context, ShopStreetActivity.class));
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView home_img1, home_img2, home_img3;
        public ViewHolder(View itemView) {
            super(itemView);
            home_img1 = (ImageView) itemView.findViewById(R.id.home_img1);
            home_img2 = (ImageView) itemView.findViewById(R.id.home_img2);
            home_img3 = (ImageView) itemView.findViewById(R.id.home_img3);
        }
    }
}
