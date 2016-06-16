package com.cqts.kxg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseValue;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.cqts.kxg.home.ArticleActivity;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.views.FavoriteAnimation;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/15.
 */
public class HomeAdapter extends RecyclerView.Adapter {
    public static final int bannerType = 1;
    public static final int classifyType = 2;
    public static final int tableType = 3;
    public static final int listType = 4;

    ArrayList<HomeSceneInfo> sceneInfos = new ArrayList<>();
    ArrayList<ArticleInfo> articleInfos = new ArrayList<>();
    View item_homebanner;
    View item_hometable;

    private Context context;

    public HomeAdapter(View item_homebanner, View item_hometable, ArrayList<HomeSceneInfo>
            sceneInfos, ArrayList<ArticleInfo> articleInfos) {
        this.item_homebanner = item_homebanner;
        this.item_hometable = item_hometable;
        this.sceneInfos = sceneInfos;
        this.articleInfos = articleInfos;
    }

    class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(View itemView) {
            super(itemView);
        }
    }

    class classifyViewHolder extends Viewholder {
        ImageView item_img;
        TextView item_tv;

        public classifyViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_tv;
        LinearLayout item_collect_layout;
        ImageView item_collect_img;
        TextView item_collect_tv;

        public ListViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
            item_collect_layout = (LinearLayout) itemView.findViewById(R.id.item_collect_layout);
            item_collect_img = (ImageView) itemView.findViewById(R.id.item_collect_img);
            item_collect_tv = (TextView) itemView.findViewById(R.id.item_collect_tv);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == bannerType) return new Viewholder(item_homebanner);
        if (viewType == classifyType)
            return new classifyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R
                    .layout.item_home_articleclassify, null));
        if (viewType == tableType) return new Viewholder(item_hometable);
        if (viewType == listType)
            return new ListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R
                    .layout.item_article, null));
        return null;
    }

    @Override
    public int getItemCount() {
        return articleInfos.size() + 12;
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
        context = viewHolder.itemView.getContext();
        switch (getItemViewType(position)) {
            case classifyType:
                bindClassifyHolder(viewHolder, position - 1);
                break;
            case listType:
                bindListHolder(viewHolder, position - 12);
                break;
        }
    }

    void bindClassifyHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (sceneInfos.size() == 0) return;

        final classifyViewHolder classifyViewHolder = (classifyViewHolder) viewHolder;
        classifyViewHolder.item_tv.setText(sceneInfos.get(i).cat_name);
        ImageLoader.getInstance().displayImage(sceneInfos.get(i).cover_img,
                classifyViewHolder.item_img,
                BaseValue.getOptions(R.mipmap.home_articleclassify));
        classifyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("title", sceneInfos.get(i).cat_name);
                intent.putExtra("cat_id", sceneInfos.get(i).cat_id);
                context.startActivity(intent);
            }
        });
    }

    void bindListHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (articleInfos.size() == 0) return;
        final ListViewHolder listViewHolder = (ListViewHolder) viewHolder;


        listViewHolder.item_collect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteAnimation animation = new FavoriteAnimation(listViewHolder
                        .item_collect_img, true);
                listViewHolder.item_collect_img.startAnimation(animation);
            }
        });

        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("title", articleInfos.get(i).title);
                intent.putExtra("url", articleInfos.get(i).article_url);
                context.startActivity(intent);
            }
        });

        listViewHolder.item_collect_tv.setText(articleInfos.get(i).love);
        listViewHolder.item_tv.setText(articleInfos.get(i).title);
        ImageLoader.getInstance().displayImage(articleInfos.get(i).cover_img, listViewHolder
                .item_img, BaseValue.getOptions(R.mipmap.solid_article));
    }
}
