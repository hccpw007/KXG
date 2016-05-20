package com.cqts.kxg.classify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqts.kxg.R;
import com.cqts.kxg.classify.bean.ClassifyListInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ClassifyRecyclerViewAdapter1 extends RecyclerView.Adapter<ClassifyRecyclerViewAdapter1.MyViewHolder> {
    Context context;
    ArrayList<ClassifyListInfo> list;

    public ClassifyRecyclerViewAdapter1(Context context, ArrayList<ClassifyListInfo> list) {
        this.context = context;
        this.list = list;
        if (list.size() != 0) {
            list.get(0).ischecked = true;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classifyrv1, null);
        view.setTag(viewGroup);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int p) {
        myViewHolder.textView.setText("要四个字");
        list.get(p).viewHolder = myViewHolder;
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!list.get(p).ischecked) {
                    list.get(p).ischecked = true;
                    for (int i = 0; i < list.size(); i++) {
                        if (i != p) {
                            list.get(i).ischecked = false;
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });


        if (list.get(p).ischecked) {
            myViewHolder.item_classify_view.setVisibility(View.VISIBLE);
            myViewHolder.itemView.setBackgroundResource(R.color.white);
            myViewHolder.item_classify_view1.setVisibility(View.GONE);
            myViewHolder.item_classify_view2.setVisibility(View.VISIBLE);
            myViewHolder.item_classify_view3.setVisibility(View.VISIBLE);
            if (p != 0) {
                list.get(p - 1).viewHolder.item_classify_view1.setVisibility(View.GONE);
            }
        } else {
            myViewHolder.item_classify_view.setVisibility(View.GONE);
            myViewHolder.itemView.setBackgroundResource(R.color.myviewbg);
            myViewHolder.item_classify_view1.setVisibility(View.VISIBLE);
            myViewHolder.item_classify_view2.setVisibility(View.GONE);
            myViewHolder.item_classify_view3.setVisibility(View.GONE);
            if (p != 0) {
                list.get(p - 1).viewHolder.item_classify_view1.setVisibility(View.VISIBLE);
            }
        }

        if (p == list.size()-1) {
            myViewHolder.item_classify_view1.setVisibility(View.GONE);
            myViewHolder.item_classify_view3.setVisibility(View.GONE);
        }
        if (p == 0) {
            myViewHolder.item_classify_view2.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View item_classify_view;
        View item_classify_view1;
        View item_classify_view2;
        View item_classify_view3;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_classifyrv1_tv);
            item_classify_view = itemView.findViewById(R.id.item_classify_view);
            item_classify_view1 = itemView.findViewById(R.id.item_classify_view1);
            item_classify_view2 = itemView.findViewById(R.id.item_classify_view2);
            item_classify_view3 = itemView.findViewById(R.id.item_classify_view3);
        }
    }
}
