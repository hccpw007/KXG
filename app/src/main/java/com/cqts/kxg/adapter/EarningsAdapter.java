package com.cqts.kxg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqts.kxg.R;
import com.cqts.kxg.bean.EaringsInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/12.
 */
public class EarningsAdapter extends BaseAdapter {
    ArrayList<EaringsInfo> adapterData;
    Context context;

    public EarningsAdapter(ArrayList<EaringsInfo> adapterData, Context context) {
        this.adapterData = adapterData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return adapterData.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_earnings,null);
        }
        TextView typeTv = (TextView) convertView.findViewById(R.id.type_tv);
        TextView amountTv = (TextView) convertView.findViewById(R.id.amount_tv);
        TextView statusTv = (TextView) convertView.findViewById(R.id.status_tv);
        TextView timeTv = (TextView) convertView.findViewById(R.id.time_tv);

        typeTv.setText(adapterData.get(position).type);
        amountTv.setText(adapterData.get(position).amount);
        statusTv.setText(adapterData.get(position).status);
        timeTv.setText(adapterData.get(position).created_at);
        return convertView;
    }

}
