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
import com.cqts.kxg.main.MyApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebShopActivity extends MyActivity {
    private String title = "";
    private String url = "";
    private MyWebView webview;
    int type = 0; //用来判定是否是从商品页面返回的type,如果是2表示是是,这不刷新页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        InitView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type == 1) {
            webview.loadUrl(url + "&token=" + MyApplication.token);
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
                try {
                    url = URLDecoder.decode(url, "utf-8");
                    if (url.contains("$$login")) { //需要登录
                        startActivityForResult(new Intent(WebShopActivity.this, LoginActivity
                                .class), 1);
                        return true;
                    }
                    if (url.contains("$$push_goods")) {//跳转到商品
                        Intent intent = new Intent(WebShopActivity.this, WebGoodsActivity.class);
                        int start = url.indexOf("goods_name=");
                        int end = url.indexOf("$$push_goods");
                        String goods_name = url.substring(start + 11, end);
                        intent.putExtra("url", url);
                        intent.putExtra("title", URLDecoder.decode(goods_name, "utf-8"));
                        intent.putExtra("id", url.substring(url.indexOf("id=") + 3, url.indexOf
                                ("&goods_name=")));
                        startActivityForResult(intent, 2);
                        return true;
                    }
                    view.loadUrl(url);
                    return true;
                } catch (UnsupportedEncodingException e) {
                }
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }
        });
        webview.loadUrl(url + "&token=" + MyApplication.token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        type = requestCode;
    }
}
