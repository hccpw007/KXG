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
import com.cqts.kxg.bean.MyApprenticeInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 */
public class ApprenticeAdapter extends RecyclerView.Adapter<ApprenticeAdapter
        .classifyViewHolder> {

    ArrayList<MyApprenticeInfo.Apprentice> apprentices;
    private Context context;

    public ApprenticeAdapter(ArrayList<MyApprenticeInfo.Apprentice> apprentices) {
        this.apprentices = apprentices;
    }

    @Override
    public classifyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        return new classifyViewHolder(LayoutInflater.from(context).inflate(R
                .layout.item_home_articleclassify, null));
    }

    @Override
    public void onBindViewHolder(classifyViewHolder classifyViewHolder, final int i) {
        if (apprentices.size() < i+1) return;

        String s = !TextUtils.isEmpty(apprentices.get(i).alias) ? apprentices.get(i).alias :
                apprentices.get(i).user_name;
        classifyViewHolder.item_tv.setText(s);
        ImageLoader.getInstance().displayImage(apprentices.get(i).headimg,
                classifyViewHolder.item_img,
                BaseValue.getOptions(R.mipmap.center_head));
    }

    @Override
    public int getItemCount() {
        return apprentices.size();
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
