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
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.views.FavoriteAnimation;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/28.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder>{
     Context context;
     List<ArticleInfo> articleInfos;

    public ArticleListAdapter(Context context, List<ArticleInfo> articleInfos) {
        this.context = context;
        this.articleInfos = articleInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        myViewHolder.item_collect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteAnimation animation = new FavoriteAnimation( myViewHolder.item_collect_img,true);
                myViewHolder.item_collect_img.startAnimation(animation);
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("title", articleInfos.get(i).title);
                intent.putExtra("url", articleInfos.get(i).article_url);
                context.startActivity(intent);
            }
        });

        myViewHolder.item_collect_tv.setText(articleInfos.get(i).love);
        myViewHolder.item_tv.setText(articleInfos.get(i).title);
        ImageLoader.getInstance().displayImage(articleInfos.get(i).cover_img,myViewHolder.item_img, BaseValue.getOptions(R.mipmap.solid_article));
    }

    @Override
    public int getItemCount() {
        return articleInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView item_img;
        TextView item_tv;
        LinearLayout item_collect_layout;
        ImageView item_collect_img;
        TextView item_collect_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
            item_collect_layout = (LinearLayout) itemView.findViewById(R.id.item_collect_layout);
            item_collect_img = (ImageView) itemView.findViewById(R.id.item_collect_img);
            item_collect_tv = (TextView) itemView.findViewById(R.id.item_collect_tv);
        }
    }


}
