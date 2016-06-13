package com.cqts.kxg.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseValue;
import com.base.views.MyHeadImageView;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.RankingInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/12.
 */
public class RankingAdapter extends BaseAdapter {
    ArrayList<RankingInfo> rankingInfos;
    Context context;

    public RankingAdapter(ArrayList<RankingInfo> rankingInfos, Context context) {
        this.rankingInfos = rankingInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return rankingInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ranking,null);
            holder = new ViewHolder();
            holder.headImg = (MyHeadImageView) convertView.findViewById(R.id.head_img);
            holder.rankingImg = (ImageView) convertView.findViewById(R.id.ranking_img);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.moneyTv = (TextView) convertView.findViewById(R.id.money_tv);
            holder.rankingTv = (TextView) convertView.findViewById(R.id.ranking_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(rankingInfos.get(position).headimg,holder.headImg,
                BaseValue.getOptions(R.mipmap.center_head));
        holder.rankingTv.setTextColor(context.getResources().getColor(R.color.mygray));
        holder.rankingTv.setText((position+1)+"");
        holder.moneyTv.setText("￥"+rankingInfos.get(position).app_money);

        if (rankingInfos.get(position).alias.isEmpty()){
            holder.nameTv.setText(rankingInfos.get(position).user_name);
        }else {
            holder.nameTv.setText(rankingInfos.get(position).alias);
        }

        holder.rankingImg.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.headImg.getLayoutParams();
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        //第1名
        if (position == 0){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            holder.rankingImg.setVisibility(View.VISIBLE);
            holder.rankingImg.setImageResource(R.mipmap.ranking_1);
            holder.rankingTv.setTextColor(0xffff2233);
        }
        //第2名
        if (position == 1){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            holder.rankingImg.setVisibility(View.VISIBLE);
            holder.rankingImg.setImageResource(R.mipmap.ranking_2);
            holder.rankingTv.setTextColor(0xffff6022);
        }
        //第3名
        if (position == 2){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            holder.rankingImg.setVisibility(View.VISIBLE);
            holder.rankingImg.setImageResource(R.mipmap.ranking_3);
            holder.rankingTv.setTextColor(0xffffa922);
        }

        return convertView;
    }


    class ViewHolder{
        MyHeadImageView headImg;
        ImageView rankingImg;
        TextView nameTv;
        TextView moneyTv;
        TextView rankingTv;
    }
}
