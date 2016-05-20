package com.base.swipebacklayout;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup.LayoutParams;

import com.cqts.kxg.R;

/**
 * 侧滑的父Activity
 */
public class SwipeBackActivity extends FragmentActivity {
	public SwipeBackLayout swipeBackLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_ani_enter, 0);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().getDecorView().setBackgroundDrawable(null);
//		swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
//				R.layout.base, null);
		swipeBackLayout = new SwipeBackLayout(this, null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		swipeBackLayout.setLayoutParams(layoutParams);
		swipeBackLayout.attachToActivity(this);
	}

	public void setSwipeBackEnable(boolean enable) {
		swipeBackLayout.setEnableGesture(enable);
	}

	/**
	 * 返回键调成此方法
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.activity_ani_exist);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.activity_ani_exist);
	}
}
