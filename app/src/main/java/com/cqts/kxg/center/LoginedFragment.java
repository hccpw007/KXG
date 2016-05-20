package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseFragment;
import com.cqts.kxg.R;

/**
 * Created by Administrator on 2016/4/28.
 */
public class LoginedFragment extends BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_logined,null);
        return view;
    }
}
