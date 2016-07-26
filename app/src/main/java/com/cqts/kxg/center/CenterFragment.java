package com.cqts.kxg.center;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.base.utils.GridDecoration;
import com.base.views.AutoTextView;
import com.base.utils.MyGridDecoration;
import com.base.views.MyHeadImageView;
import com.base.zxing.PlanarYUVLuminanceSource;
import com.cqts.kxg.R;
import com.cqts.kxg.adapter.GoodsAdapter;
import com.cqts.kxg.bean.EarnInfo;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.bean.MyUrlInfo;
import com.cqts.kxg.classify.ClassifyFragment;
import com.cqts.kxg.home.WebShopActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.main.MyFragment;
import com.cqts.kxg.main.NgtAty;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.utils.MyUrls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
    private LinearLayout money_layout;

    public static CenterFragment fragment;
    private LinearLayout money_table1;
    private LinearLayout money_table2;
    private LinearLayout money_table3;
    private LinearLayout money_table4;

    public static CenterFragment getInstance() {
        if (fragment == null) {
            fragment = new CenterFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

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
        money_layout = (LinearLayout) view.findViewById(R.id.money_layout);
        money_table1 = (LinearLayout) view.findViewById(R.id.money_table1);
        money_table2 = (LinearLayout) view.findViewById(R.id.money_table2);
        money_table3 = (LinearLayout) view.findViewById(R.id.money_table3);
        money_table4 = (LinearLayout) view.findViewById(R.id.money_table4);

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

        money_table1.setOnClickListener(this);
        money_table2.setOnClickListener(this);
        money_table3.setOnClickListener(this);
        money_table4.setOnClickListener(this);
        login_tv.setOnClickListener(this);
        money_layout.setOnClickListener(this);
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
            case R.id.money_table3://收徒赚钱
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), ApprenticeActivity.class));
                }
                break;
            case R.id.table2://文章赚钱
                if (needLogin() && null != MyUrls.getInstance().getMyUrl(getActivity())) {
                    Intent intent = new Intent(getActivity(), WebActivityActivity.class);
                    intent.putExtra("title", "文章赚钱");
                    intent.putExtra("url", MyUrls.getInstance().getMyUrl(getActivity()).activity);
                    startActivityForResult(intent, WebActivityActivity.REQUESTCODE);
                }
                break;
            case R.id.table3://我要提现
                if (needLogin() && null != MyUrls.getInstance().getMyUrl(getActivity())) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title", "提现");
                    intent.putExtra("url", MyUrls.getInstance().getMyUrl(getActivity()).withdraw +
                            "?token=" + MyApplication.token);
                    startActivity(intent);
                }
                break;
            case R.id.table4://收益详情
            case R.id.money_layout://账户余额
            case R.id.money_table1://收徒金额信息
            case R.id.money_table2://收徒金额信息
            case R.id.money_table4://收徒金额信息
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), EarningsActivity.class));
                }
                break;
            case R.id.table5://喜欢文章
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), MyloveActivity.class).putExtra
                            ("type", MyloveActivity.articleType));
                }
                break;
            case R.id.table6://喜欢宝贝
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), MyloveActivity.class).putExtra
                            ("type", MyloveActivity.goodsType));
                }
                break;
            case R.id.table7://喜欢店铺
                if (needLogin()) {
                    startActivity(new Intent(getActivity(), MyloveActivity.class).putExtra
                            ("type", MyloveActivity.shopType));
                }
                break;
            case R.id.table8://常见问题
                if (null == MyUrls.getInstance().getMyUrl(getActivity())) {
                    return;
                }
                Intent intent1 = new Intent(getActivity(), WebActivity.class);
                intent1.putExtra("title", "常见问题");
                intent1.putExtra("url", MyUrls.getInstance().getMyUrl(getActivity()).problem);
                startActivity(intent1);
                break;
            case R.id.table9://新手任务
                if (needLogin() && null != MyUrls.getInstance().getMyUrl(getActivity())) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title", "新手任务");
                    intent.putExtra("url", MyUrls.getInstance().getMyUrl(getActivity()).noviceTask
                            + "?token=" + MyApplication.token);
                    startActivity(intent);
                }
                break;
            case R.id.table10://牛人排行
                startActivity(new Intent(getActivity(), RankingActivity.class));
                break;
            case R.id.table11://话费充值
                if (null == MyUrls.getInstance().getMyUrl(getActivity())) {
                    return;
                }
                if (needLogin()) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title", "话费充值");
                    intent.putExtra("url", MyUrls.getInstance().getMyUrl(getActivity()).recharge
                            + "&token=" + MyApplication.token);
                    startActivity(intent);
                }
                break;
            case R.id.table12://我的店铺
                if (needLogin() && null != MyUrls.getInstance().getMyUrl(getActivity())) {
                    if (!TextUtils.isEmpty(getUserInfo().store)) {

                        int id_start = getUserInfo().store.indexOf("?id=");
                        String shop_id = getUserInfo().store.substring(id_start + 4, getUserInfo
                                ().store.length());
                        startActivity(new Intent(getActivity(), WebShopActivity.class).putExtra
                                ("title", "我的店铺").putExtra("url", getUserInfo().store).putExtra
                                ("shop_id", shop_id));
                    } else {
                        showToast("您还没有店铺!");
                        Intent intent2 = new Intent(getActivity(), WebActivity.class);
                        intent2.putExtra("title", "我要开店");
                        intent2.putExtra("url", MyUrls.getInstance().getMyUrl(getActivity())
                                .openShop);
                        startActivity(intent2);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onShow() {
        System.out.println("view == null? ---"+(null == view));
        System.out.println("center_onshow");
        System.out.println("isLogined() == "+isLogined());
        System.out.println("UserInfo == "+(MyApplication.userInfo == null));
        super.onShow();
        try {
            if (isLogined()) {
                System.out.println("showLogined==start");
                showLogined();
                System.out.println("showLogined==end");
            } else {
                System.out.println("showUnLogined==start");
                showUnLogined();
                System.out.println("showUnLogined==end");
            }
            setMessage();//刷新消息
        } catch (Exception e) {
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
        prentice_tv.setText("0");
        prenticemoney_tv.setText("0.00");
        head_img.setImageResource(R.mipmap.center_head);
    }

    private void showLogined() {
        System.out.println("1");
        name_tv.setVisibility(View.VISIBLE);
        System.out.println("2");
        login_tv.setVisibility(View.GONE);
        System.out.println("3");
        money_tv.setText(String.format("%.2f", getUserInfo().app_money));
        System.out.println("4");
        name_tv.setText(TextUtils.isEmpty(getUserInfo().alias) ? getUserInfo().user_name :
                getUserInfo().alias);
        System.out.println("5");
        DisplayImageOptions build = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).showImageOnFail(R.mipmap.center_head)
                .showImageForEmptyUri(R.mipmap.center_head).build();
        System.out.println("6");
        ImageLoader.getInstance().displayImage(getUserInfo().headimg, head_img, build);
        System.out.println("7");
        MyHttp.userEarning(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                System.out.println("8");
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                EarnInfo earnInfo = (EarnInfo) bean;
                getUserInfo().app_money = Double.valueOf(String.format("%.2f", earnInfo.balance));
                money_tv.setText(String.format("%.2f", earnInfo.balance));
                history_tv.setText(String.format("%.2f", earnInfo.history));
                today_tv.setText(String.format("%.2f", earnInfo.today));
                prentice_tv.setText(earnInfo.receive);
                prenticemoney_tv.setText(String.format("%.2f", earnInfo.kickback));
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
                if (TextUtils.isEmpty(data) || data.length() < 3) {
                    return;
                }
                data = data.replace("[", "");
                data = data.replace("]", "");
                data = data.replace("\"", "");
                String[] split = data.split(",");
                message_tv.setText(split);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //文章赚钱的返回,跳转到热门文章
        if (requestCode == WebActivityActivity.REQUESTCODE && resultCode == WebActivityActivity
                .RESULTCODE) {
            ((NgtAty) getActivity()).ngt_pager.setCurrentItem(2, false);
        }
    }
}