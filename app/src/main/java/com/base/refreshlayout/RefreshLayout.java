package com.base.refreshlayout;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.base.BaseValue;
import com.base.views.MyScrollView;
import com.cqts.kxg.R;
/**
 * 使用方法:  使用之前在Activity的setContentView()方法之前初始化组件---setInit() <br>
 * 也可以在Application里面初始化后以后就无需再初始化<br>
 * 刷新接口setOnRefreshListener() <br>
 * 有ScroView调用setScrollView(ScrollView view)<br>
 * 有ListView调用setListView(ListView view)<br>
 * 设置结束状态 setResultState(ResultState resultState)<br>
 */
public class RefreshLayout extends LinearLayout {
	private View headView;
	private float y_Down;
	private float y_Move;
	private int y_Up;
	private LayoutParams params;
	public boolean canRefresh; // 是否能够刷新
	private boolean isRefreshing; // 是否正在刷新
	private static int headHeight = 0; // head的高度
	private int viewHeight = 1000; //
	private RefreshState state = RefreshState.init;
	private RotateAnimation rotateAnimation;
	private RotateAnimation refreshingAnimation;
	private View pullView;
	private View refreshingView;
	private View refreshStateImageView;
	private TextView refreshStateTextView;
	private static boolean initRefresh = false;
	public static boolean isCanRefresh = true;
	public static boolean isCanRefreshForView = true;
	private OnRefreshListener onRefreshListener = null;
	private int y_Change;
	HashMap<String, Float> location = new HashMap<String, Float>();
	ListView listview;
	ScrollView scrollView;

	public RefreshLayout(Context context) {
		super(context);
		InitView(context);
	}

	public RefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitView(context);
	}

	private void InitView(Context context) {
		if (initRefresh) {
			viewOnLayout(context);
		}
	}

	/**
	 * 初始化下拉组件
	 */
	public static void setInit() {
		initRefresh = true;
		headHeight = BaseValue.dp2px(60);
	}

	/**
	 * 刷新接口
	 */
	public interface OnRefreshListener {
		void onRefresh();
	}

	/**
	 * 设置下拉刷新
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	/**
	 * 设置背景颜色
	 */
	public void setBg(int resid) {
		headView.setBackgroundResource(resid);
	}

	/**
	 * 结束的状态
	 */
	public enum ResultState {
		success, failed, close
	}

	public void setScrollView(MyScrollView view) {
		this.scrollView = view;
		view.setOnScrollListener(new MyScrollView.OnScrollListener() {
			@Override
			public void onScroll(int scrollY) {
				if (scrollY == 0) {
					isCanRefreshForView = true;
				} else {
					isCanRefresh = false;
					isCanRefreshForView = false;
				}
			}
		});
	}

	public void setListView(ListView view) {
		this.listview = view;
		view.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				View firstView = view.getChildAt(firstVisibleItem);
				if (firstVisibleItem == 0
						&& (firstView == null || firstView.getTop() == 0)) {
					isCanRefreshForView = true;
				} else {
					isCanRefresh = false;
					isCanRefreshForView = false;
				}
			}
		});

	}

	/**
	 * 设置结束的状态
	 */
	public void setResultState(ResultState resultState) {
		if (!initRefresh) {
			return;
		}
		if (null == onRefreshListener) {
			return;
		}
		refreshingView.clearAnimation();
		refreshingView.setVisibility(View.GONE);
		switch (resultState) {
		case success:
			refreshStateImageView.setVisibility(View.VISIBLE);
			refreshStateTextView.setText(R.string.refresh_succeed);
			refreshStateImageView
					.setBackgroundResource(R.mipmap.refresh_succeed);
			closeView();
			break;
		case failed:
			refreshStateImageView.setVisibility(View.VISIBLE);
			refreshStateTextView.setText(R.string.refresh_fail);
			refreshStateImageView
					.setBackgroundResource(R.mipmap.refresh_failed);
			closeView();
			break;
		case close:
			setState(RefreshState.init);
			params.topMargin = -viewHeight;
			headView.setLayoutParams(params);
			break;
		default:
			break;
		}
	}

	private void setEnable(View view, final boolean enabled) {
		if (null != view) {
			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						isCanRefresh = isCanRefreshForView;
					}
					return !enabled;
				}
			});
		}

	}

	private void closeView() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == -1) {
					for (int i = 0; i <= headHeight / 20 + 1; i++) {
						sendEmptyMessageDelayed(i,
								(50 + (headHeight / 20 + 1 - i)
										* (headHeight / 20))
										* i);
					}
				}
				if (msg.what == -2) {
					params.topMargin = -1000;
					headView.setLayoutParams(params);
					setState(RefreshState.init);
				}
				if (msg.what == headHeight / 20 + 1) {
					sendEmptyMessageDelayed(-2, 50);
				}
				if (msg.what <= headHeight / 20 && msg.what >= 0) {
					params.topMargin = -viewHeight + headHeight
							- (msg.what * 20);
					headView.setLayoutParams(params);
				}

			}
		};
		handler.sendEmptyMessageDelayed(-1, 1000);
	}

	private void viewOnLayout(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		headView = LayoutInflater.from(context).inflate(R.layout.view_refresh_head,
				null);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, viewHeight);
		params.topMargin = -viewHeight;
		headView.setLayoutParams(params);
		addView(headView);
		// 初始化下拉布局
		pullView = headView.findViewById(R.id.pull_icon);
		refreshStateTextView = (TextView) headView.findViewById(R.id.state_tv);
		refreshingView = headView.findViewById(R.id.refreshing_icon);
		refreshStateImageView = headView.findViewById(R.id.state_iv);
		initAnim(context);
	}

	private enum RefreshState {
		init, refreshing, canrefresh
	}

	private void setState(RefreshState to) {
		state = to;
		switch (to) {
		case init:
			// 下拉布局初始状态
			isRefreshing = false;
			pullView.clearAnimation();
			refreshingView.clearAnimation();
			refreshingView.setVisibility(View.GONE);
			refreshStateImageView.setVisibility(View.GONE);
			refreshStateTextView.setText(R.string.pull_to_refresh);
			pullView.startAnimation((RotateAnimation) AnimationUtils
					.loadAnimation(getContext(), R.anim.reverse_anim2));
			pullView.setVisibility(View.VISIBLE);
			break;
		case canrefresh:
			// 释放刷新状态
			refreshStateTextView.setText(R.string.release_to_refresh);
			pullView.startAnimation(rotateAnimation);
			break;
		case refreshing:
			// 正在刷新状态
			pullView.clearAnimation();
			refreshingView.setVisibility(View.VISIBLE);
			pullView.setVisibility(View.INVISIBLE);
			refreshingView.startAnimation(refreshingAnimation);
			refreshStateTextView.setText(R.string.refreshing);
			onRefreshListener.onRefresh();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			location.put("y", ev.getY());
		}
		if (!initRefresh) {
			return super.dispatchTouchEvent(ev);
		}
		if (isRefreshing) {
			return super.dispatchTouchEvent(ev);
		}
		if (null == onRefreshListener) {
			return super.dispatchTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			y_Down = ev.getY();
			canRefresh = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isCanRefresh) {
				return super.dispatchTouchEvent(ev);
			}

			if (location.get("y") < ev.getY()) {
				// 向下
				setEnable(scrollView, false);
				setEnable(listview, false);
			}
			y_Move = ev.getY();

			y_Change = (int) (y_Move - y_Down);
			if (y_Change > headHeight * 2) {
				if (state != RefreshState.canrefresh) {
					setState(RefreshState.canrefresh);
				}
				canRefresh = true;
				params.topMargin = (int) (y_Change
						- (y_Change - headHeight * 2) / 1.3 - viewHeight);
				headView.setLayoutParams(params);
				break;
			}

			if (canRefresh && y_Change < headHeight) {
				params.topMargin = -viewHeight + headHeight;
				headView.setLayoutParams(params);
				break;
			}

			if (y_Change >= 0 && y_Change < headHeight * 2) {
				params.topMargin = y_Change - viewHeight;
			}

			if (y_Change < 0) {
				y_Down = ev.getY();
				params.topMargin = -viewHeight;
			}
			headView.setLayoutParams(params);
			y_Up = params.topMargin;
			break;
		case MotionEvent.ACTION_UP:
			setEnable(scrollView, true);
			setEnable(listview, true);
			if (!isCanRefresh) {
				return super.dispatchTouchEvent(ev);
			}
			if (canRefresh) {
				setState(RefreshState.refreshing);
				isRefreshing = true;
				params.topMargin = -viewHeight + headHeight;
			} else {
				setState(RefreshState.init);
				params.topMargin = -viewHeight;
			}
			headView.setLayoutParams(params);
			break;
		default:
			break;
		}
		super.dispatchTouchEvent(ev);
		return true;
	}

	private void initAnim(Context context) {
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.rotating);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}
}
