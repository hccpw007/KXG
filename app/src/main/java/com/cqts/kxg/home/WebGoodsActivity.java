package com.cqts.kxg.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.base.http.HttpForVolley;
import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.utils.MyURL;
import com.cqts.kxg.views.FavoriteAnimation;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webgoods);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
        InitView();
        InitWebView();
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

        collectLayout.setOnClickListener(this);
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
                    if (url.contains("$$push_shop")) {//跳转到商品
                        Intent intent = new Intent(WebGoodsActivity.this, WebShopActivity.class);
                        int start = url.indexOf("supplier_name=");
                        int end = url.indexOf("$$push_shop");
                        String shop_name = url.substring(start + 14, end);
                        intent.putExtra("url", url);
                        intent.putExtra("title", URLDecoder.decode(shop_name, "utf-8"));
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
        if (!needLogin()) {
            return;
        }
        switch (v.getId()) {
            case R.id.collect_layout://收藏
                setLove();
                break;
            case R.id.tobuy_tv://去淘宝购买
                String urlStr = MyURL.JUMP+"?id="+id+"&token="+MyApplication.token;
                Intent intent =new Intent(this, WebBuyActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("url",urlStr);
                startActivity(intent);
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
            collectTv.setText("收藏");
        } else {//已收藏
            collectImg.setImageResource(R.mipmap.home_taoxin_hover);
            collectTv.setText("已收藏");
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
            collectTv.setText("已收藏");
        } else { //取消收藏成功
            animation = new FavoriteAnimation(collectImg, false);
            collectTv.setText("收藏");
        }
        collectImg.startAnimation(animation);
    }
}
