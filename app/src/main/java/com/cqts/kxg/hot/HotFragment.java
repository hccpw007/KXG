package com.cqts.kxg.hot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.views.MyViewPager;
import com.cqts.kxg.R;
import com.cqts.kxg.home.ArticleFragment;
import com.cqts.kxg.home.SearchActivity;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.nine.NineFragment;

import java.util.ArrayList;

public class HotFragment extends MyFragment implements MyViewPager.OnMyPageChangeListener, View
        .OnClickListener {
    private ArrayList<BaseFragment> list = new ArrayList<>();
    private MyViewPager hot_viewpager;
    private ImageView hot_view;
    private TextView hot_tv0;
    private TextView hot_tv1;
    private TextView hot_tv2;
    private TextView hot_tv3;

    public static HotFragment fragment;

    public static HotFragment getInstance() {
        if (fragment == null) {
            fragment = new HotFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_hot, null);
            InitView();
        }
        return view;
    }

    private void InitView() {
        ImageView search_img = (ImageView) view.findViewById(R.id.search_img);
        hot_tv0 = (TextView)view. findViewById(R.id.hot_tv0);
        hot_tv1 = (TextView) view.findViewById(R.id.hot_tv1);
        hot_tv2 = (TextView) view.findViewById(R.id.hot_tv2);
        hot_tv3 = (TextView) view.findViewById(R.id.hot_tv3);

        search_img.setOnClickListener(this);
        hot_tv0.setOnClickListener(this);
        hot_tv1.setOnClickListener(this);
        hot_tv2.setOnClickListener(this);
        hot_tv3.setOnClickListener(this);

        hot_viewpager = (MyViewPager)view.findViewById(R.id.hot_viewpager);
        hot_view = (ImageView)view. findViewById(R.id.hot_view);


        ViewGroup.LayoutParams layoutParams = hot_view.getLayoutParams();
        layoutParams.width = BaseValue.screenwidth/4;

        InitViewPager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_img: //搜索按钮
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.hot_tv0:
                hot_viewpager.setCurrentItem(0,false);
                break;
            case R.id.hot_tv1:
                hot_viewpager.setCurrentItem(1,false);
                break;
            case R.id.hot_tv2:
                hot_viewpager.setCurrentItem(2,false);
                break;
            case R.id.hot_tv3:
                hot_viewpager.setCurrentItem(3,false);
                break;
            default:
                break;
        }
    }

    private void InitViewPager() {
        list.add(new ArticleFragment(ArticleFragment.Where.hot,1));
        list.add(new ArticleFragment(ArticleFragment.Where.hot,2));
        list.add(new ArticleFragment(ArticleFragment.Where.hot,3));
        list.add(new ArticleFragment(ArticleFragment.Where.hot,4));
        hot_viewpager.setFragemnt(getActivity().getSupportFragmentManager(),list);
        hot_viewpager.setOnMyPageChangeListener(this);
    }

    @Override
    public void OnMyPageSelected(int arg0) {
    }

    /**
     * 根据滑动改变红标的位置
     */
    @Override
    public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {
        hot_view.setX(arg0*BaseValue.screenwidth/4+arg2/4);
    }

    @Override
    public void OnMyPageScrollStateChanged(int arg0) {
    }
}
