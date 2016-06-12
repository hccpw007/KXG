package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cqts.kxg.R;
import com.cqts.kxg.adapter.RankingAdapter;
import com.cqts.kxg.bean.RankingInfo;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.utils.MyHttp;

import java.util.ArrayList;

/**
 * 牛人排行
 */
public class RankingActivity extends MyActivity {

    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        InitView();
    }

    private void InitView() {
        setMyTitle("牛人排行");
        listview = (ListView) findViewById(R.id.listview);
        listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        MyHttp.userRanking(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code!=0){
                    showToast(msg);
                    return;
                }
                ArrayList<RankingInfo> rankingInfos = (ArrayList<RankingInfo>) bean;
                if (rankingInfos == null||rankingInfos.size() ==0){
                    return;
                }
                RankingAdapter adapter = new RankingAdapter(rankingInfos,RankingActivity.this);
                listview.setAdapter(adapter);
            }
        });
    }
}
