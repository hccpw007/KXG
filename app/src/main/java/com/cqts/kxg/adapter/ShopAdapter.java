package com.cqts.kxg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.views.MyOvalImageView;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.ShopInfo;
import com.cqts.kxg.home.WebShopActivity;
import com.cqts.kxg.main.WebActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 店铺
 * Created by Administrator on 2016/6/3.
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyviewHolder>{
    Context context;
    List<ShopInfo> shopInfos;
    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(R.mipmap.home_articleclassify)
            .build();

    public ShopAdapter(Context context, List<ShopInfo> shopInfos) {
        this.context = context;
        this.shopInfos = shopInfos;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop,null);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyviewHolder myviewHolder, final int i) {
        myviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebShopActivity.class);
                intent.putExtra("title", shopInfos.get(i).supplier_name);
                intent.putExtra("url", shopInfos.get(i).url);
                context.startActivity(intent);
            }
        });
        myviewHolder.item_shopname_tv.setText(shopInfos.get(i).supplier_name);
        myviewHolder.item_name_tv.setText("店主: "+shopInfos.get(i).user_name);
        myviewHolder.item_views_tv.setText(shopInfos.get(i).views+"");
        myviewHolder.item_num_tv.setText(shopInfos.get(i).goods_num+"");
        ImageLoader.getInstance().displayImage(shopInfos.get(i).logo,myviewHolder.item_img,defaultOptions);
    }

    @Override
    public int getItemCount() {
        return shopInfos.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        MyOvalImageView item_img;
        TextView item_shopname_tv;
        TextView item_name_tv;
        TextView item_views_tv;
        TextView item_num_tv;
        TextView item_into_tv;
        public MyviewHolder(View itemView) {
            super(itemView);
            item_img = (MyOvalImageView) itemView.findViewById(R.id.item_img);
            item_shopname_tv = (TextView) itemView.findViewById(R.id.item_shopname_tv);
            item_name_tv = (TextView) itemView.findViewById(R.id.item_name_tv);
            item_views_tv = (TextView) itemView.findViewById(R.id.item_views_tv);
            item_num_tv = (TextView) itemView.findViewById(R.id.item_num_tv);
            item_into_tv = (TextView) itemView.findViewById(R.id.item_into_tv);
        }
    }
}
