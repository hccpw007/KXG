package com.cqts.kxg.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class HomeAdapter extends RecyclerView.Adapter {
    public static final int bannerType = 1;
    public static final int classifyType = 2;
    public static final int tableType = 3;
    public static final int listType = 4;

    ArticleAdapter articleListAdapter;
    HomeClassifyAdapter homeArticleClassifyAdapter;
    HomeBannerAdapter homeBannerAdapter;
    HomeTableAdapter homeTableAdapter;

    public HomeAdapter(HomeBannerAdapter homeBannerAdapter, HomeTableAdapter homeTableAdapter,
                       HomeClassifyAdapter homeArticleClassifyAdapter, ArticleAdapter
                               articleListAdapter) {
        this.homeBannerAdapter = homeBannerAdapter;
        this.homeTableAdapter = homeTableAdapter;
        this.homeArticleClassifyAdapter = homeArticleClassifyAdapter;
        this.articleListAdapter = articleListAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == bannerType)
            return homeBannerAdapter.onCreateViewHolder(viewGroup, viewType);
        if (viewType == classifyType)
            return homeArticleClassifyAdapter.onCreateViewHolder(viewGroup, viewType);
        if (viewType == tableType)
            return homeTableAdapter.onCreateViewHolder(viewGroup, viewType);
        if (viewType == listType)
            return articleListAdapter.onCreateViewHolder(viewGroup, viewType);
        return null;
    }

    @Override
    public int getItemCount() {
        return articleListAdapter.getItemCount() + 12;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return bannerType;
        if (position > 0 && position <= 10) return classifyType;
        if (position == 11) return tableType;
        return listType;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case tableType:
                homeTableAdapter.onBindViewHolder((HomeTableAdapter.ViewHolder) viewHolder,
                        position);
                break;
            case bannerType:
                homeBannerAdapter.onBindViewHolder((HomeBannerAdapter.Viewholder) viewHolder,
                        position);
                break;
            case classifyType:
                homeArticleClassifyAdapter.onBindViewHolder((HomeClassifyAdapter
                        .classifyViewHolder) viewHolder, position - 1);
                break;
            case listType:
                articleListAdapter.onBindViewHolder((ArticleAdapter.MyViewHolder) viewHolder,
                        position - 12);
                break;
        }
    }
}