package com.cqts.kxg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseValue;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.cqts.kxg.home.ArticleActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.utils.LoginUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 首页文章分类
 */
public class ArticleClassifyAdapter extends RecyclerView.Adapter<ArticleClassifyAdapter
        .classifyViewHolder> {

    ArrayList<HomeSceneInfo> sceneInfos;
    private Context context;

    public ArticleClassifyAdapter(ArrayList<HomeSceneInfo> sceneInfos) {
        this.sceneInfos = sceneInfos;
    }

    @Override
    public classifyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (context == null) {
            context = viewGroup.getContext();
        }
        return new classifyViewHolder(LayoutInflater.from(context).inflate(R
                .layout.item_home_articleclassify, null));
    }

    @Override
    public void onBindViewHolder(classifyViewHolder classifyViewHolder, final int i) {
        if (sceneInfos.size() < i + 1) return;

        classifyViewHolder.item_tv.setText(sceneInfos.get(i).cat_name);
        ImageLoader.getInstance().displayImage(sceneInfos.get(i).cover_img,
                classifyViewHolder.item_img,
                BaseValue.getOptions(R.mipmap.home_articleclassify));
        classifyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(sceneInfos.get(i).url)) { //充话费和携程旅游
                    if (!LoginUtils.isLogin()){
                        LoginUtils.login(context);
                        return;
                    }
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("title", sceneInfos.get(i).cat_name);
                    intent.putExtra("url", sceneInfos.get(i).url+"&token="+ MyApplication.token);
                    context.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("title", sceneInfos.get(i).cat_name);
                intent.putExtra("cat_id", sceneInfos.get(i).cat_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sceneInfos.size();
    }

    class classifyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_tv;

        public classifyViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}
