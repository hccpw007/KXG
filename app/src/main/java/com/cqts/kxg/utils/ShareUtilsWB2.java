package com.cqts.kxg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.cqts.kxg.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 微博分享
 */
public class ShareUtilsWB2 {
    public static String APP_KEY = "2751214706";
    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    public IWeiboShareAPI mWeiboShareAPI;
    private static ShareUtilsWB2 instance;

    public static  ShareUtilsWB2 getInstance() {
        if (null == instance) {
            instance = new ShareUtilsWB2();
        }
            return instance;
    }

    private ShareUtilsWB2() {
    }

    public void wbShare(Activity context, Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.center_table8);
        }
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        TextObject textObject = new TextObject();
        textObject.text = "http://www.baidu.com";
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, APP_KEY);
        mWeiboShareAPI.registerApp();
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = textObject;
        weiboMessage.imageObject = imageObject;
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        AuthInfo authInfo = new AuthInfo(context, APP_KEY, "http://www.sina.com",
                SCOPE);
        Oauth2AccessToken accessToken = readAccessToken(context.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(context, request, authInfo, token,
                new WeiboAuthListener() {
                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        System.out.println("失败");
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        System.out.println("成功");
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("取消");
                    }
                });
    }

    public Oauth2AccessToken readAccessToken(Context context) {
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        return token;
    }
}
