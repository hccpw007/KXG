package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cqts.kxg.R;
import com.cqts.kxg.adapter.EarningsAdapter;
import com.cqts.kxg.bean.EaringsInfo;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;

public class EarningsActivity extends MyActivity implements RadioGroup.OnCheckedChangeListener,
        AbsListView.OnScrollListener {
    private RadioGroup radiogroup;
    private ListView listview;
    static final int out = 1;
    static final int in = 2;
    ArrayList<EaringsInfo> outData = new ArrayList<>();
    ArrayList<EaringsInfo> inData = new ArrayList<>();
    ArrayList<EaringsInfo> adapterData = new ArrayList<>();
    int pageSize = 50;
    int outPageNum = 1;
    int inPageNum = 1;
    int checked = in;
    private EarningsAdapter adapter;
    boolean canGetData = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        InitView();
        InitList();
        getData(checked);
    }

    private void InitList() {
        listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new EarningsAdapter(adapterData, this);
        listview.setAdapter(adapter);
        listview.setOnScrollListener(this);
        listview.setFooterDividersEnabled(false);
    }

    private void InitView() {
        setMyTitle("收益详情");
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        listview = (ListView) findViewById(R.id.listview);
        radiogroup.setOnCheckedChangeListener(this);
    }

    void getData(final int type) {
        if (!canGetData){
            return;
        }
        MyHttp.userDetails(http, type, type, (type == out ? outPageNum : inPageNum), pageSize,
                new MyHttp.MyHttpResult() {
                    @Override
                    public void httpResult(Integer which, int code, String msg, Object bean) {
                        canGetData = true;

                        if (code != 0) {
                            showToast(msg);
                            return;
                        }

                        ArrayList<EaringsInfo> infos = (ArrayList<EaringsInfo>) bean;
                        if (infos == null || infos.size() == 0) {
                            return;
                        }

                        adapterData.clear();
                        switch (type) {
                            case out:
                                outData.addAll(infos);
                                adapterData.addAll(outData);
                                adapter.notifyDataSetChanged();
                                if (infos.size() == pageSize) {
                                    outPageNum++;
                                }
                                break;
                            case in:
                                inData.addAll(infos);
                                adapterData.addAll(inData);
                                adapter.notifyDataSetChanged();
                                if (infos.size() == pageSize) {
                                    inPageNum++;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        adapterData.clear();
        switch (checkedId) {
            case R.id.rediobtn1://收益
                checked = in;
                if (inData.size() == 0) {
                    getData(checked);
                }
                adapterData.addAll(inData);
                break;
            case R.id.rediobtn2://提现
                checked = out;
                if (outData.size() == 0) {
                    getData(checked);
                }
                adapterData.addAll(outData);
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                // 加载更多功能的代码
                if (adapterData.size() >= pageSize) {
                    getData(checked);
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {
    }
}