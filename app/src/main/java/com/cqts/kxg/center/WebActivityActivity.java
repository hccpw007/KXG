package com.cqts.kxg.center;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MyActivity;

public class WebActivityActivity extends MyActivity {
    public final static int REQUESTCODE = 8;
    public final static int RESULTCODE = 9;
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
                if (url.contains("$$push_hot")) { //跳转到热门文章
                    setResult(RESULTCODE);
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
