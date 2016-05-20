package com.base.views;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.base.BaseFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	List<BaseFragment> list;
	public MyFragmentPagerAdapter(FragmentManager fragmentManager,List<BaseFragment> list) {
		super(fragmentManager);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}