package com.cqts.kxg.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqts.kxg.R;
import com.cqts.kxg.bean.SceneInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ArticleClassifyAdapter extends RecyclerView.Adapter<ArticleClassifyAdapter
        .MyViewHolder> {
    Context context;
    List<SceneInfo> sceneInfos;

    public ArticleClassifyAdapter(Context context, List<SceneInfo> sceneInfos) {
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

    }
    @Override
    public int getItemCount() {
        return 10;
//        return sceneInfos.size();
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
