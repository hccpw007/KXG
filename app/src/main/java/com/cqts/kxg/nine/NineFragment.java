package com.cqts.kxg.nine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.base.BaseFragment;
import com.base.BaseValue;
import com.base.views.RefreshLayout;
import com.base.utils.MyGridDecoration;
import com.base.utils.GridDecoration;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.NineInfo;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.adapter.GoodsAdapter;
import com.cqts.kxg.home.HomeFragment;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NineFragment extends MyFragment {
    private ImageView nine_img;
    private RecyclerView nine_rv;
    List<GoodsInfo> goods_list = new ArrayList<GoodsInfo>();
    private GoodsAdapter adapter;
    private LinearLayout.LayoutParams params;
    private int width;
    private int headHeight;
    boolean isClosed = false; //banner是否关闭
    private GridLayoutManager manager;
    private RefreshLayout nine_refresh;
    int pageNum = 1;
    int pageSize = 50;
    boolean isCanLoadMore = true; //是否可以加载更多

    public static NineFragment fragment;

    public static NineFragment getInstance() {
        if (fragment == null) {
            fragment = new NineFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_nine, null);
            InitView();
            getData();
        }
        return view;
    }

    private void InitView() {
        nine_img = (ImageView) view.findViewById(R.id.nine_img);
        nine_rv = (RecyclerView) view.findViewById(R.id.nine_rv);
        nine_refresh = (RefreshLayout) view.findViewById(R.id.nine_refresh);
        InitRecyclerView();

        nine_refresh.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goods_list.clear();
                pageNum = 1;
                getData();
            }
        });


        params = (LinearLayout.LayoutParams) nine_img.getLayoutParams();
        width = params.width;
        headHeight = params.height;

    }

    private void InitRecyclerView() {
        nine_rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        manager = new GridLayoutManager(getActivity(), 2);
        nine_rv.setLayoutManager(manager);
        GridDecoration newGridDecoration = new GridDecoration(0, BaseValue.dp2px(8),
                getResources().getColor(R.color.mybg),false);
        nine_rv.addItemDecoration(newGridDecoration);
        adapter = new GoodsAdapter(getActivity(), goods_list);
        nine_rv.setAdapter(adapter);
        nine_refresh.setRC(nine_rv, new RefreshLayout.TopOrBottom() {
            @Override
            public void gotoTop() {
                if (isClosed) {
                    openView();
                }
            }

            @Override
            public void stop() {
            }

            @Override
            public void gotoBottom() {
                loadMore();
            }

            @Override
            public void move() {
                if (!isClosed) {
                    closeView();
                }
            }
        });
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        if (isCanLoadMore) {
            getData();
            isCanLoadMore = false;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        MyHttp.jkj(http, null, pageSize, pageNum, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    if (nine_refresh.isRefreshing) {
                        nine_refresh.setResultState(RefreshLayout.ResultState.failed);
                    }
                    return;
                }
                if (nine_refresh.isRefreshing) {
                    nine_refresh.setResultState(RefreshLayout.ResultState.success);
                }
                NineInfo nineInfo = (NineInfo) bean;
                List<GoodsInfo> ninelist = nineInfo.goods_list;
                goods_list.addAll(ninelist);
                adapter.notifyDataSetChanged();
                if (nineInfo.banner != null) {
                    ImageLoader.getInstance().displayImage(nineInfo.banner.ad_code, nine_img);
                }
                pageNum++;
                isCanLoadMore = true;
            }
        });
    }


    private void closeView() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == -1) {
                    if (params.height > headHeight / 20) {
                        params.height = params.height - (headHeight / 20);
                        params.width = width;
                        nine_img.setLayoutParams(params);
                        sendEmptyMessageDelayed(-1, 20);
                    } else {
                        params.height = 0;
                        params.width = width;
                        nine_img.setLayoutParams(params);
                        isClosed = true;
                    }
                }
            }
        };
        handler.sendEmptyMessageDelayed(-1, 100);
    }

    private void openView() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == -1) {
                    if (params.height < headHeight) {
                        params.height = params.height + (headHeight / 10);
                        params.width = width;
                        nine_img.setLayoutParams(params);
                        sendEmptyMessageDelayed(-1, 20);
                    } else {
                        params.height = headHeight;
                        params.width = width;
                        nine_img.setLayoutParams(params);
                        isClosed = false;
                    }
                }
            }
        };
        handler.sendEmptyMessageDelayed(-1, 100);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (nine_refresh != null) {
            nine_refresh.setResultState(RefreshLayout.ResultState.close);
        }
    }
}
