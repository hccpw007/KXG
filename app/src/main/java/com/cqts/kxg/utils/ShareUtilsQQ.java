package com.cqts.kxg.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.cqts.kxg.views.SharePop;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * QQ分享
 */
public class ShareUtilsQQ {
    public static void ShareQQ(Tencent mTencent, final Activity context, String title, String
            text, String url, String image_url) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, text);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "开心购991105448613");

        if (!TextUtils.isEmpty(image_url)) {//图片
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, image_url);
        }
        mTencent.shareToQQ(context, params, listener);
    }

    public static void ShareQQZone(Tencent mTencent, final Activity context, String title, String
            text, String url, String image_url) {
        Bundle params = new Bundle();

        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE,title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, text);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
        ArrayList<String> strings1 = new ArrayList<>();
        if (!TextUtils.isEmpty(image_url)) {//图片
            strings1.add(image_url);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,strings1);
        mTencent.shareToQzone(context, params, listener);
    }

    public static IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            SharePop.getInstance().setResult(SharePop.ShareResult.SUCCESS);
        }

        @Override
        public void onError(UiError uiError) {
            SharePop.getInstance().setResult(SharePop.ShareResult.FAILED);
        }

        @Override
        public void onCancel() {
            SharePop.getInstance().setResult(SharePop.ShareResult.CANCEL);
        }
    };
}
