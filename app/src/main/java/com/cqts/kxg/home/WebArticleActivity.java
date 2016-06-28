package com.cqts.kxg.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.base.views.MyWebView;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.utils.MyHttp;
import com.cqts.kxg.views.FavoriteAnimation;
import com.cqts.kxg.views.SharePop;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebArticleActivity extends MyActivity implements View.OnClickListener {
    private String title = "";
    private String url = "";
    private ArticleInfo articleInfo;
    private MyWebView webview;
    private LinearLayout collectLayout;
    private ImageView collectImg;
    private TextView collectTv;
    private LinearLayout shareLayout;
    private TextView shareTv;
    boolean canClick = true;
    private int is_love;
    private FavoriteAnimation animation;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webarticle);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        articleInfo = (ArticleInfo) getIntent().getSerializableExtra("articleInfo");
        InitView();
        InitWebView();
        MyHttp.userRead(http, null, articleInfo.article_id, null); //阅读回调
        getBitmap();
    }

    private void InitView() {
        setMyTitle(title);
        webview = (MyWebView) findViewById(R.id.webview);
        collectLayout = (LinearLayout) findViewById(R.id.collect_layout);
        collectImg = (ImageView) findViewById(R.id.collect_img);
        collectTv = (TextView) findViewById(R.id.collect_tv);
        shareLayout = (LinearLayout) findViewById(R.id.share_layout);
        shareTv = (TextView) findViewById(R.id.share_tv);

        collectLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);

        collectTv.setText(articleInfo.love);
        shareTv.setText(articleInfo.share_sum);

        is_love = articleInfo.is_love;
        if (is_love != 1) { //未收藏
            collectImg.setImageResource(R.mipmap.home_taoxin);
        } else {//已收藏
            collectImg.setImageResource(R.mipmap.home_taoxin_hover);
        }
    }

    private void InitWebView() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    url = URLDecoder.decode(url, "utf-8");
                    System.out.println(url);
                    if (url.contains("$$push_article")) { //跳转到文章
                        ArticleInfo articleInfo = new ArticleInfo();

                        int start = url.indexOf("id=");
                        int end = url.indexOf("&title");
                        String id = url.substring(start + 3, end);
                        articleInfo.article_id = id;

                        start = url.indexOf("&title=");
                        end = url.indexOf("&share_sum=");
                        String title = url.substring(start + 7, end);
                        articleInfo.title = title;

                        start = url.indexOf("&share_sum=");
                        end = url.indexOf("&love=");
                        String share_sum = url.substring(start + 11, end);
                        articleInfo.share_sum = share_sum;

                        start = url.indexOf("&love=");
                        end = url.indexOf("&share_content=");
                        String love = url.substring(start + 6, end);
                        articleInfo.love = love;

                        start = url.indexOf("&share_content=");
                        end = url.indexOf("&share_img=");
                        String share_content = url.substring(start + 15, end);
                        articleInfo.share_content = share_content;

                        start = url.indexOf("&share_img=");
                        end = url.indexOf("$$push_article");
                        String share_img = url.substring(start + 11, end);
                        articleInfo.share_img = share_img;
                        articleInfo.is_love = 0;
                        articleInfo.article_url = url;

                        Intent intent = new Intent(WebArticleActivity.this,
                                WebArticleActivity.class);
                        intent.putExtra("title", articleInfo.title);
                        intent.putExtra("url", url);
                        intent.putExtra("articleInfo", articleInfo);
                        startActivity(intent);
                        return true;
                    }

                    if (url.contains("$$push_goods")) {//跳转到商品
                        Intent intent = new Intent(WebArticleActivity.this, WebGoodsActivity.class);
                        int start = url.indexOf("goods_name=");
                        int end = url.indexOf("$$push_goods");
                        String goods_name = url.substring(start + 11, end);
                        intent.putExtra("url", url);
                        intent.putExtra("title", URLDecoder.decode(goods_name, "utf-8"));
                        intent.putExtra("id", url.substring(url.indexOf("id=") + 3, url.indexOf
                                ("&goods_name=")));
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
            case R.id.collect_layout://收藏  取消收藏
                setLove();
                break;

            case R.id.share_layout: //分享
                setShare();
                break;
        }
    }

    /**
     * 分享文章并请求接口调用收益
     */
    private void setShare() {
        SharePop.getInstance().showPop(this, shareLayout, articleInfo.title, articleInfo
                        .article_url+"&share=1",
                articleInfo.share_content, bitmap, new SharePop.ShareResult() {
                    @Override
                    public void shareResult(int result) {
                        if (result == SharePop.ShareResult.SUCCESS) { //分享成功
                            if (getUserInfo() != null) {
                                MyHttp.articleShare(http, null, articleInfo.article_id,
                                        "", null);
                            }
                        }
                    }
                });
    }

    private void setLove() {
        if (!canClick) {
            return;
        }

        if (!needLogin()) {
            return;
        }

        final MyHttp.MyHttpResult myHttpResult = new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                canClick = true;
                showToast(msg);

                if (code != 0) {
                    return;
                }

                if (is_love == 0) { //喜欢成功
                    animation = new FavoriteAnimation(collectImg, true);

                } else { //取消收藏成功
                    animation = new FavoriteAnimation(collectImg, false);
                }
                collectImg.startAnimation(animation);

                ArticleInfo articleInfo = (ArticleInfo) bean;
                collectTv.setText(articleInfo.love);
            }
        };

        HttpForVolley.HttpTodo httpTodo = new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                if (response.optInt("code") != 0) {
                    canClick = true;
                    showToast(response.optString("msg", "发生错误"));
                    return;
                }

                is_love = response.optJSONObject("data").optInt("is_love");

                if (is_love != 1) { //未收藏
                    collectImg.setImageResource(R.mipmap.home_taoxin);
                } else {//已收藏
                    collectImg.setImageResource(R.mipmap.home_taoxin_hover);
                }

                MyHttp.articleLove(http, 2, articleInfo.article_id, is_love,
                        myHttpResult);
            }
        };

        MyHttp.articleCollect(http, 1, articleInfo.article_id, httpTodo);
        canClick = false;
    }

    void getBitmap() {
        ImageRequest imageRequest = new ImageRequest(articleInfo.share_img,
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
    }
}

