package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.base.views.AutoTextView;
import com.base.utils.MyGridDecoration;
import com.base.views.MyHeadImageView;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.GoodsAdapter;
import com.cqts.kxg.bean.EarnInfo;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.utils.MyHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/6.
 */
public class CenterFragment extends MyFragment implements View.OnClickListener {
    private ScrollView scrollview;
    ImageView setting_img;
    MyHeadImageView head_img;
    private RecyclerView goods_rclv;
    AutoTextView message_tv;
    TextView login_tv, name_tv, money_tv, lookall_tv, today_tv, history_tv, prentice_tv,
            prenticemoney_tv;
    LinearLayout table1, table2, table3, table4, table5, table6, table7, table8, table9, table10,
            table11, table12;
    private ArrayList<GoodsInfo> goodsInfos = new ArrayList<>();
    private GoodsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_center, null);
            InitView();
            InitRC();
        }
        return view;
    }

    private void InitView() {
        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        setting_img = (ImageView) view.findViewById(R.id.setting_img);
        head_img = (MyHeadImageView) view.findViewById(R.id.head_img);
        login_tv = (TextView) view.findViewById(R.id.login_tv);
        name_tv = (TextView) view.findViewById(R.id.name_tv);
        money_tv = (TextView) view.findViewById(R.id.money_tv);
        lookall_tv = (TextView) view.findViewById(R.id.lookall_tv);
        today_tv = (TextView) view.findViewById(R.id.today_tv);
        history_tv = (TextView) view.findViewById(R.id.history_tv);
        prentice_tv = (TextView) view.findViewById(R.id.prentice_tv);
        prenticemoney_tv = (TextView) view.findViewById(R.id.prenticemoney_tv);
        message_tv = (AutoTextView) view.findViewById(R.id.message_tv);
        goods_rclv = (RecyclerView) view.findViewById(R.id.goods_rclv);

        table1 = (LinearLayout) view.findViewById(R.id.table1);
        table2 = (LinearLayout) view.findViewById(R.id.table2);
        table3 = (LinearLayout) view.findViewById(R.id.table3);
        table4 = (LinearLayout) view.findViewById(R.id.table4);
        table5 = (LinearLayout) view.findViewById(R.id.table5);
        table6 = (LinearLayout) view.findViewById(R.id.table6);
        table7 = (LinearLayout) view.findViewById(R.id.table7);
        table8 = (LinearLayout) view.findViewById(R.id.table8);
        table9 = (LinearLayout) view.findViewById(R.id.table9);
        table10 = (LinearLayout) view.findViewById(R.id.table10);
        table11 = (LinearLayout) view.findViewById(R.id.table11);
        table12 = (LinearLayout) view.findViewById(R.id.table12);

        login_tv.setOnClickListener(this);
        name_tv.setOnClickListener(this);
        setting_img.setOnClickListener(this);
        lookall_tv.setOnClickListener(this);
        head_img.setOnClickListener(this);
        table1.setOnClickListener(this);
        table2.setOnClickListener(this);
        table3.setOnClickListener(this);
        table4.setOnClickListener(this);
        table5.setOnClickListener(this);
        table6.setOnClickListener(this);
        table7.setOnClickListener(this);
        table8.setOnClickListener(this);
        table9.setOnClickListener(this);
        table10.setOnClickListener(this);
        table11.setOnClickListener(this);
        table12.setOnClickListener(this);

        scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv://登录
                needLogin();
                break;
            case R.id.setting_img://设置
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                }
                break;
            case R.id.lookall_tv://查看全部收益
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), EarningsActivity.class));
                }
                break;
            case R.id.head_img://头像
            case R.id.name_tv://名字
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), InformationActivity.class));
                }
                break;
            case R.id.table1://收徒赚钱
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), ApprenticeActivity.class));
                }
                break;
            case R.id.table2://文章赚钱
                break;
            case R.id.table3://我要提现
                break;
            case R.id.table4://收益详情
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), EarningsActivity.class));
                }
                break;
            case R.id.table5://喜欢文章
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), MyloveActivity.class).putExtra("type",MyloveActivity.articleType));
                }
                break;
            case R.id.table6://喜欢宝贝
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), MyloveActivity.class).putExtra("type",MyloveActivity.goodsType));
                }
                break;
            case R.id.table7://喜欢店铺
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), MyloveActivity.class).putExtra("type",MyloveActivity.shopType));
                }
                break;
            case R.id.table8://常见问题
                break;
            case R.id.table9://新手任务
                break;
            case R.id.table10://牛人排行
                startActivity(new Intent(getActivity(),RankingActivity.class));
                break;
            case R.id.table11://话费充值
                break;
            case R.id.table12://我的店铺
                break;
            default:
                break;
        }
    }

    @Override
    public void onShow() {
        super.onShow();
        setMessage();//刷新消息
        if (!isLogined()) {
            showUnLogined();
        } else {
            showLogined();

        }
    }

    /**
     * 未登录状态,所有布局初始化
     */
    private void showUnLogined() {
        name_tv.setVisibility(View.GONE);
        login_tv.setVisibility(View.VISIBLE);
        money_tv.setText("0.00");
        today_tv.setText("0.00");
        history_tv.setText("0.00");
        prentice_tv.setText("0.00");
        prenticemoney_tv.setText("0.00");
        head_img.setImageResource(R.mipmap.center_head);
    }

    private void showLogined() {
        name_tv.setVisibility(View.VISIBLE);
        login_tv.setVisibility(View.GONE);
        money_tv.setText(getUserInfo().app_money);
        name_tv.setText(getUserInfo().user_name);
        ImageLoader.getInstance().displayImage(getUserInfo().headimg,head_img,BaseValue.getOptions(R.mipmap.center_head));
        MyHttp.userEarning(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                EarnInfo earnInfo = (EarnInfo) bean;
                money_tv.setText(earnInfo.history);
                today_tv.setText(earnInfo.today);
                history_tv.setText(earnInfo.history);
                prentice_tv.setText(earnInfo.receive);
                prenticemoney_tv.setText(earnInfo.kickback);
            }
        });
    }

    /**
     * 设置热门推荐商品的列表
     */
    private void InitRC() {
        goods_rclv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        goods_rclv.setLayoutManager(manager);
        MyGridDecoration myGridDecoration = new MyGridDecoration(BaseValue.dp2px(8), BaseValue
                .dp2px(8), getResources().getColor(R.color.mybg), true);
        myGridDecoration.setFrame(true);
        goods_rclv.addItemDecoration(myGridDecoration);
        adapter = new GoodsAdapter(getActivity(), goodsInfos);
        goods_rclv.setAdapter(adapter);
        MyHttp.goodsRecommend(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                goodsInfos.addAll((ArrayList<GoodsInfo>) bean);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 设置滚动消息的跑马灯
     */
    private void setMessage() {
        MyHttp.withdraw(http, null, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                if (response.optInt("code") != 0) {
                    showToast(response.optString("msg", "网络错误"));
                    return;
                }
                String data = response.optString("data");
                data = data.replace("[", "");
                data = data.replace("]", "");
                data = data.replace("\"", "");
                String[] split = data.split(",");
                message_tv.setText(split);
            }
        });
    }
}