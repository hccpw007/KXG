package com.cqts.kxg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.cqts.kxg.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 微博分享
 */
public class ShareUtilsWB {
    public static  String APP_KEY="2751214706";
    public static  String REDIRECT_URL="https://api.weibo.com/oauth2/default.html";
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";

    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    private static final String KEY_REFRESH_TOKEN    = "refresh_token";


    public static void wbShare(Activity context, Bitmap bitmap) {
        System.out.println("1");
        IWeiboShareAPI weiboAPI = WeiboShareSDK.createWeiboAPI(context, APP_KEY);
        weiboAPI.registerApp();
        System.out.println("2");
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text="text";
        weiboMessage.textObject=textObject;
        ImageObject imageObject = new ImageObject();
//        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
        imageObject.setImageObject(bitmap);
        weiboMessage.mediaObject = getWebpageObj(context);
        System.out.println("3");
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        System.out.println("4");
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        System.out.println("5");
        AuthInfo authInfo = new AuthInfo(context, APP_KEY, REDIRECT_URL,SCOPE);
        System.out.println("6");
        Oauth2AccessToken accessToken = readAccessToken(context.getApplicationContext());
        System.out.println("7");
        String token = "";
        if (accessToken != null) {
            System.out.println("8");
            token = accessToken.getToken();
        }
        System.out.println("9");
        weiboAPI.sendRequest(context, request, authInfo, token, new WeiboAuthListener() {
            @Override
            public void onWeiboException( WeiboException arg0 ) {
            }
            @Override
            public void onComplete( Bundle bundle ) {
            }
            @Override
            public void onCancel() {
            }
        });



    }
    private static WebpageObject getWebpageObj(Context context) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "sdaga";
        mediaObject.description = "ghdfsahs";
        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "www.baidu.com";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    public static Oauth2AccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        return token;
    }

}
