package com.cqts.kxg.home;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MyActivity;

public class WebGoodsActivity extends MyActivity {
    private String title = "";
    private String url = "";
    private MyWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webgoods);
        getData();
        InitView();
    }

    private void getData() {
        try {
            title = getIntent().getStringExtra("title");
            url = getIntent().getStringExtra("url");
        } catch (Exception e) {

        }
    }

    private void InitView() {
        setMyTitle(title);
        webview = (MyWebView) findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("$$login")){ //需要登录
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
}
