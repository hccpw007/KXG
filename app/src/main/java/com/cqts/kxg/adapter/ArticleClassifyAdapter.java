package com.cqts.kxg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseValue;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ArticleClassifyAdapter extends RecyclerView.Adapter<ArticleClassifyAdapter
        .MyViewHolder> {
    Context context;
    List<HomeSceneInfo> sceneInfos;

    public ArticleClassifyAdapter(Context context, List<HomeSceneInfo> sceneInfos) {
        this.context = context;
        this.sceneInfos = sceneInfos;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_articleclassify, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        if (sceneInfos.size() == 0) return;
        myViewHolder.item_tv.setText(sceneInfos.get(i).cat_name);
        ImageLoader.getInstance().displayImage(sceneInfos.get(i).cover_img,myViewHolder.item_img,
                BaseValue.getOptions(R.mipmap.home_articleclassify));
    }

    @Override
    public int getItemCount() {
        if (sceneInfos.size() == 0) {
            return 10;
        } else {
            return sceneInfos.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}
