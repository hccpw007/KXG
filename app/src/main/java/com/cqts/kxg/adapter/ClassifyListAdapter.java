package com.cqts.kxg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.cqts.kxg.R;
import com.cqts.kxg.bean.ClassifyListInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 */
public class ClassifyListAdapter extends BaseAdapter {
    Context context;
    List<ClassifyListInfo> classifyListInfos;

    public ClassifyListAdapter(Context context, List<ClassifyListInfo> classifyListInfos) {
        this.context = context;
        this.classifyListInfos = classifyListInfos;
    }

    @Override
    public int getCount() {
        return classifyListInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_classifylist,
                null);
        CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
        check.setText(classifyListInfos.get(position).cat_name);
        clickView(check, position);
        check.setChecked(classifyListInfos.get(position).ischecked);
        return convertView;
    }

    private void clickView(final CheckBox view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classifyListInfos.get(position).ischecked) {
                    for (ClassifyListInfo classifyListInfo : classifyListInfos) {
                        classifyListInfo.ischecked = false;
                    }
                    classifyListInfos.get(position).ischecked = true;
                    notifyDataSetChanged();
                    childAdapter.ChildAdapterChange(classifyListInfos.get(position).son);
                } else {
                    view.setChecked(true);
                }
            }
        });
    }

    public  interface ChildAdapterChange{
      void  ChildAdapterChange(ArrayList<ClassifyListInfo.ClassifyChildInfo> classifyChildInfos);
    }
    ChildAdapterChange childAdapter;
    public void setChildAdapter(ChildAdapterChange childAdapter) {
        this.childAdapter = childAdapter;
    }
}
