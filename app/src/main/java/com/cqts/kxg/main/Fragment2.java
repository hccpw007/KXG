package com.cqts.kxg.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseFragment;
import com.cqts.kxg.R;

public class Fragment2 extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.item_homerv, null);
            InitView();
        }
        return view;
    }

    private void InitView() {
    }
}
