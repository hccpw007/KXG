package com.cqts.kxg.utils;

import android.app.Activity;
import android.widget.Toast;

import com.base.http.HttpForVolley;
import com.cqts.kxg.bean.MyUrlInfo;
import com.cqts.kxg.views.SharePop;

/**
 * Created by Administrator on 2016/6/27.
 */
public class MyUrls {
    static MyUrls instance;
    MyUrlInfo urlInfo;

    public static MyUrls getInstance() {
        if (instance == null) {
            synchronized (SharePop.class) {
                if (instance == null) {
                    instance = new MyUrls();
                }
            }
        }
        return instance;
    }

    public MyUrlInfo getMyUrl(final Activity context) {
        if (null != urlInfo) {
            return urlInfo;
        }
        HttpForVolley httpForVolley = new HttpForVolley(context);
        MyHttp.getLinkIndex(httpForVolley, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                urlInfo = (MyUrlInfo) bean;
            }
        });
        return urlInfo;
    }
}
