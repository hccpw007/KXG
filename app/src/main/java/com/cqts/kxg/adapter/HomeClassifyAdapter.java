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

import com.cqts.kxg.R;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.cqts.kxg.classify.ClassifyGoodsActivity;
import com.cqts.kxg.home.ArticleActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.utils.LoginUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * 首页文章分类
 */
public class HomeClassifyAdapter extends RecyclerView.Adapter<HomeClassifyAdapter
        .classifyViewHolder> {

    ArrayList<HomeSceneInfo> sceneInfos;
    private Context context;
    private DisplayImageOptions options;

    public HomeClassifyAdapter(ArrayList<HomeSceneInfo> sceneInfos) {
        this.sceneInfos = sceneInfos;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).showImageOnFail(R.mipmap.home_articleclassify)
                .showImageOnLoading(R.color.transparency).displayer(new RoundedBitmapDisplayer(200))
                .showImageForEmptyUri(R.mipmap.home_articleclassify).build();
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
                classifyViewHolder.item_img, options);
        classifyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneInfos.get(i).cat_id == -1) {//携程
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("title", sceneInfos.get(i).cat_name);
                    String url = sceneInfos.get(i).url;
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                    return;
                }
                if (sceneInfos.get(i).cat_id == -2) {//生活圈
                    if (!LoginUtils.isLogin()) {
                        LoginUtils.login(context);
                        return;
                    }
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("title", sceneInfos.get(i).cat_name);
                    String url = sceneInfos.get(i).url;
                    if (url.contains("?")) {
                        url = url + "&";
                    } else {
                        url = url + "?";
                    }
                    intent.putExtra("url", url + "token=" + MyApplication.token);
                    context.startActivity(intent);
                    return;
                }

                Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                intent.putExtra("title", sceneInfos.get(i).cat_name);
                intent.putExtra("cat_id", sceneInfos.get(i).cat_id + "");
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
