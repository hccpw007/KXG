package com.cqts.kxg.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.login.LoginService;
import com.alibaba.sdk.android.trade.TradeService;
import com.alibaba.sdk.android.trade.page.ItemDetailPage;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.main.MainActivity;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.utils.MyUrls;
import com.cqts.kxg.views.FavoriteAnimation;
import com.cqts.kxg.views.SharePop;
import com.taobao.tae.sdk.callback.CallbackContext;
import com.taobao.tae.sdk.callback.LoginCallback;
import com.taobao.tae.sdk.callback.LogoutCallback;
import com.taobao.tae.sdk.callback.TradeProcessCallback;
import com.taobao.tae.sdk.model.Session;
import com.taobao.tae.sdk.model.TaokeParams;
import com.taobao.tae.sdk.model.TradeResult;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebGoodsActivity extends MyActivity implements View.OnClickListener, HttpForVolley
        .HttpTodo, MyHttp.MyHttpResult {
    private String title = "";
    private String url = "";
    private String id = "";
    private MyWebView webview;
    private LinearLayout collectLayout;
    private ImageView collectImg;
    private TextView collectTv;
    private TextView tobuyTv;
    boolean canClick = true;
    private int is_love;
    private FavoriteAnimation animation;
    private GoodsInfo goodsInfo;
    private ImageView shareImg;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webgoods);
        setSwipeBackEnable(false);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
        getDetailData();
        InitView();
        InitWebView();
    }

    private void getDetailData() {
        MyHttp.goodsDetail(http, null, id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    return;
                }
                goodsInfo = (GoodsInfo) bean;
                getBitmap();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIsLove();
    }

    /**
     * 刷新是否已经收藏
     */
    private void getIsLove() {
        if (!isLogined()) {
            return;
        }
        MyHttp.goodsCollect(http, 1, id, this);
    }

    private void InitView() {
        setMyTitle(title);
        webview = (MyWebView) findViewById(R.id.webview);
        collectLayout = (LinearLayout) findViewById(R.id.collect_layout);
        collectImg = (ImageView) findViewById(R.id.collect_img);
        collectTv = (TextView) findViewById(R.id.collect_tv);
        tobuyTv = (TextView) findViewById(R.id.tobuy_tv);
        shareImg = (ImageView) findViewById(R.id.share_img);

        collectLayout.setOnClickListener(this);
        shareImg.setOnClickListener(this);
        tobuyTv.setOnClickListener(this);
    }

    private void InitWebView() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    url = URLDecoder.decode(url, "utf-8");
                    if (url.contains("$$push_shop")) {//跳转到商店
                        Intent intent = new Intent(WebGoodsActivity.this, WebShopActivity.class);
                        int start = url.indexOf("supplier_name=");
                        int end = url.indexOf("$$push_shop");
                        String shop_name = url.substring(start + 14, end);

                        int id_start = url.indexOf("?id=");
                        int id_end = url.indexOf("&supplier_name=");
                        String shop_id = url.substring(id_start + 4, id_end);

                        intent.putExtra("url", url);
                        intent.putExtra("title", URLDecoder.decode(shop_name, "utf-8"));
                        intent.putExtra("shop_id", URLDecoder.decode(shop_id, "utf-8"));
                        startActivity(intent);
                        return true;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_layout://收藏
                if (!needLogin()) {
                    return;
                }
                setLove();
                break;
            case R.id.share_img://分享
                if (goodsInfo == null) {
                    getDetailData();
                    return;
                }
                String shareTitle = (isLogined() ? getUserInfo().alias : "我") + " 向你推荐一个商品";
                SharePop.getInstance().showPop(this, v, shareTitle, goodsInfo.share_url,
                        goodsInfo.goods_name, bitmap,goodsInfo.goods_thumb, null);
                break;
            case R.id.tobuy_tv://去购买
                goShoping();
//                if (!needLogin()) {
//                    return;
//                }
//                if (null == MyUrls.getInstance().getMyUrl(this)) {
//                    return;
//                }
//                String urlStr = MyUrls.getInstance().getMyUrl(this).jump + "?id=" + id +
//                        "&token=" + MyApplication.token;
//                Intent intent = new Intent(this, WebBuyActivity.class);
//                intent.putExtra("title", title);
//                intent.putExtra("url", urlStr);
//                startActivity(intent);
                break;
        }
    }

    private void setLove() {
        if (!canClick) {
            return;
        }

        MyHttp.goodsCollect(http, 2, id, this);
        canClick = false;
    }

    @Override
    public void httpTodo(Integer which, JSONObject response) {

        int code = response.optInt("code", 1);
        if (code != 0) {
            canClick = true;
            showToast(response.optString("msg", "发生错误!"));
            return;
        }

        is_love = response.optJSONObject("data").optInt("is_love", 0);

        if (is_love != 1) { //未收藏
            collectImg.setImageResource(R.mipmap.home_taoxin);
            collectTv.setText("喜欢");
        } else {//已收藏
            collectImg.setImageResource(R.mipmap.home_taoxin_hover);
            collectTv.setText("已喜欢");
        }

        if (which == 2) { //收藏
            MyHttp.userLoveGoods(http, null, id, is_love, this);
        }
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        canClick = true;
        showToast(msg);
        if (code != 0) {
            return;
        }

        if (is_love == 0) { //喜欢成功
            animation = new FavoriteAnimation(collectImg, true);
            collectTv.setText("已喜欢");
        } else { //取消收藏成功
            animation = new FavoriteAnimation(collectImg, false);
            collectTv.setText("喜欢");
        }
        collectImg.startAnimation(animation);
    }

    void getBitmap() {
        try {
            ImageRequest imageRequest = new ImageRequest(goodsInfo.goods_thumb,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            bitmap = response;
                        }
                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            BaseValue.mQueue.add(imageRequest);
        } catch (Exception e) {

        }
    }

    //登录
    public void showLogin() {
        AlibabaSDK.getService(LoginService.class).showLogin(this, new com.alibaba.sdk.android
                .login.callback.LoginCallback() {
            @Override
            public void onSuccess(com.alibaba.sdk.android.session.model.Session session) {
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);
        SharePop.getInstance().onActivityResult(requestCode,resultCode,data);
    }


    private void goShoping() {
        if (goodsInfo == null) {
            getDetailData();
            return;
        }

        if (goodsInfo.goods_type == 0) {  //自营产品
            if (!needLogin()) {
                return;
            }
            String urlStr = goodsInfo.jump + "&token=" + MyApplication.token;
            Intent intent = new Intent(this, WebBuyActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("url", urlStr);
            startActivity(intent);
        }

        if (goodsInfo.goods_type == 1 || goodsInfo.goods_type == 2) {
            showItemDetailPage();
        }
    }

    //天猫淘宝详情页
    public void showItemDetailPage() {
        if (!MyApplication.isAliSDKInit) {
            MyApplication.aliSDKInit(this);
            return;
        }
        TradeService tradeService = AlibabaSDK.getService(TradeService.class);
        ItemDetailPage itemDetailPage = new ItemDetailPage(goodsInfo.goods_sn, null);
        TaokeParams taokeParams = new TaokeParams(); //若非淘客taokeParams设置为null即可
        taokeParams.pid = "mm_114951381_0_0";
        tradeService.show(itemDetailPage, null, this, null, new com.alibaba.sdk.android.trade
                .callback.TradeProcessCallback() {
            @Override
            public void onPaySuccess(com.alibaba.sdk.android.trade.model.TradeResult tradeResult) {
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

}
