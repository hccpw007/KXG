package com.cqts.kxg.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MyActivity;

import java.util.ArrayList;

public class WebBuyActivity extends MyActivity {
    private String title = "";
    private String url = "";
    private MyWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
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
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("tmall://page.tm/itemDetail?itemId="))
                {
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webview.loadUrl(url);
        }
        return true;
    }
}
