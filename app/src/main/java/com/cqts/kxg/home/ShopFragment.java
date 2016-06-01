package com.cqts.kxg.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseFragment;
import com.cqts.kxg.R;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ShopFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null==view){
            view = inflater.inflate(R.layout.fragment_shop,null);
            InitView();
        }
        return view;
    }

    private void InitView() {

    }
}
