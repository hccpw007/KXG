package com.base.views;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.base.BaseFragment;

public class MyViewPager extends ViewPager {
	private boolean isCanScroll = true;
	private boolean isHaveFragment = false;
	HashMap<String, Fragment> stopMap = new HashMap<String, Fragment>();
	List<BaseFragment> list;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOverScrollMode(View.OVER_SCROLL_NEVER); // 设置滑动到顶部的阴影
	}

	public MyViewPager(Context context) {
		super(context);
		setOverScrollMode(View.OVER_SCROLL_NEVER);// 设置滑动到顶部的阴影
	}

	/**
	 * 设置是否可以滑动
	 */
	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	/**
	 * 设置fragment
	 */
	public void setFragemnt(FragmentManager fragmentManager,
			final List<BaseFragment> list) {
		this.list = list;
		isHaveFragment = true;
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
				fragmentManager, list);
		setOffscreenPageLimit(0);
		stopMap.put("Fragment", list.get(0));
		setAdapter(adapter);
		setOnMyPageChangeListener(null);
	}

	public void setOnMyPageChangeListener(final OnMyPageChangeListener listener) {
		setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if (null != listener) {
					listener.OnMyPageSelected(arg0);
				}
				if (isHaveFragment) {
					stopMap.get("Fragment").onStop();
					stopMap.put("Fragment", list.get(arg0));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public interface OnMyPageChangeListener {
		public void OnMyPageSelected(int arg0);
	}

	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
	}
}
