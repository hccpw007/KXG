package com.cqts.kxg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqts.kxg.R;
import com.cqts.kxg.bean.ClassifyListInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ClassifyRVAdapter extends RecyclerView.Adapter<ClassifyRVAdapter.MyViewHolder> {
    Context context;
    ArrayList<ClassifyListInfo.ClassifyChildInfo> list;

    public ClassifyRVAdapter(Context context, ArrayList<ClassifyListInfo.ClassifyChildInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classifyrv, null);
        view.setTag(viewGroup);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int p) {
        myViewHolder.textView.setText(list.get(p).cat_name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_classifyrv2_tv);
            imageView = (ImageView) itemView.findViewById(R.id.item_classifyrv2_img);
        }
    }
}
