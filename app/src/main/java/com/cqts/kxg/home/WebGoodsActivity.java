package com.cqts.kxg.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.http.HttpForVolley;
import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;

import org.json.JSONObject;

public class WebGoodsActivity extends MyActivity implements View.OnClickListener {
    private String title = "";
    private String url = "";
    private String id = "";
    private MyWebView webview;
    private LinearLayout collectLayout;
    private ImageView collectImg;
    private TextView collectTv;
    private TextView tobuyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webgoods);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
        InitView();
        InitWebView();
        getIsLove();
    }

    /**
     * 刷新是否已经收藏
     */
    private void getIsLove() {
        if (!isLogined()) {
            return;
        }
        MyHttp.goodsCollect(http, null, id, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {

            }
        });
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
                if (url.contains("$$login")) { //需要登录
                    startActivity(new Intent(WebGoodsActivity.this, LoginActivity.class));
                    finish();
                    return true;
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

    }
}
