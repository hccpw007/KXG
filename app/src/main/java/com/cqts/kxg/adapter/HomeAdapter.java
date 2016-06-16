package com.cqts.kxg.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/6/15.
 */
public class HomeAdapter extends RecyclerView.Adapter {
    public static final int bannerType = 1;
    public static final int classifyType = 2;
    public static final int tableType = 3;
    public static final int listType = 4;

    View item_homebanner;
    View item_hometable;
    ArticleListAdapter articleListAdapter;
    ArticleClassifyAdapter homeArticleClassifyAdapter;


    public HomeAdapter(View item_homebanner, View item_hometable, ArticleListAdapter
            articleListAdapter, ArticleClassifyAdapter homeArticleClassifyAdapter) {
        this.articleListAdapter = articleListAdapter;
        this.homeArticleClassifyAdapter = homeArticleClassifyAdapter;
        this.item_homebanner = item_homebanner;
        this.item_hometable = item_hometable;
    }

    class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == bannerType) return new Viewholder(item_homebanner);
        if (viewType == classifyType)
            return homeArticleClassifyAdapter.onCreateViewHolder(viewGroup, viewType);
        if (viewType == tableType) return new Viewholder(item_hometable);
        if (viewType == listType)
            return articleListAdapter.onCreateViewHolder(viewGroup, viewType);
        return null;
    }

    @Override
    public int getItemCount() {
        articleListAdapter.notifyDataSetChanged();
        homeArticleClassifyAdapter.notifyDataSetChanged();

        return  articleListAdapter.getItemCount() + 12;
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

            case classifyType:
                if (homeArticleClassifyAdapter.getItemCount() == 0) return;
                homeArticleClassifyAdapter.onBindViewHolder((ArticleClassifyAdapter
                        .classifyViewHolder) viewHolder, position - 1);
                break;

            case listType:
                if (articleListAdapter.getItemCount() == 0)return;
                articleListAdapter.onBindViewHolder((ArticleListAdapter.MyViewHolder) viewHolder,
                        position - 12);
                break;
        }
    }
}