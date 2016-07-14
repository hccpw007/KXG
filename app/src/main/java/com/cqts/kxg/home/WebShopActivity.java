package com.cqts.kxg.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.base.BaseValue;
import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.ShopInfo;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.views.SharePop;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebShopActivity extends MyActivity implements View.OnClickListener {
    private String title = "";
    private String url = "";
    private String shop_id = "";
    private MyWebView webview;
    int type = 0; //用来判定是否是从商品页面返回的type,如果是2表示是是,这不刷新页面
    private ImageView share_img;
    private ShopInfo shopInfo;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        shop_id = getIntent().getStringExtra("shop_id");
        getDetailData();
        InitView();
        setSwipeBackEnable(false);
    }

    //获取店铺详情
    private void getDetailData() {
        MyHttp.shopDetail(http, null, shop_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    return;
                }
                shopInfo = (ShopInfo) bean;
                getBitmap();
            }
        });
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
        share_img = (ImageView) findViewById(R.id.share_img);
        share_img.setOnClickListener(this);
        webview = (MyWebView) findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    url = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
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
                        intent.putExtra("title", goods_name);
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
    public void onClick(View v) {
        //分享
        if (shopInfo==null|| TextUtils.isEmpty(shopInfo.share_url)){
            getDetailData();
            return;
        }
        String shareTitle = (isLogined() ? getUserInfo().alias : "我") + " 向你推荐一个店铺";
        SharePop.getInstance().showPop(this,share_img,shareTitle,shopInfo.share_url,shopInfo.supplier_name,bitmap,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        type = requestCode;
    }

    void getBitmap() {
        try {
            ImageRequest imageRequest = new ImageRequest(shopInfo.logo,
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
}
