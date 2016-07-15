package com.cqts.kxg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.cqts.kxg.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * 微信分享工具
 */
public class ShareUtilsWX {
    /**
     * 微信分享<p>
     * type:分享类型, 1 = 微信分享  2 = 朋友圈分享 3 = 微信收藏
     */
    public static void wxShare(Context context, int type, String title, String url, String text,Bitmap thumb) {
        //注册微信
        String APP_ID = "wx68d4f6f1109e4f94";
        IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID, true);
        api.registerApp(APP_ID);

        boolean IsWXAppInstalledAndSupported = api
                .isWXAppInstalled() && api.isWXAppSupportAPI();
        if (!IsWXAppInstalledAndSupported) {
            Toast.makeText(context, "您的手机没有安装微信，无法分享到微信", Toast.LENGTH_SHORT).show();
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        Bitmap scaledBitmap;
        if (null!= thumb){ //如果有图片,这添加图片
            scaledBitmap = Bitmap.createScaledBitmap(thumb,80, 80, true);
        }else {
            scaledBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap,80, 80, true);
        }
        msg.thumbData = bmpToByteArray(scaledBitmap,true);
        msg.title = title;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();

        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession; //微信分享
        }
        if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline; //朋友圈分享
        }
        if (type == 3) {
            req.scene = SendMessageToWX.Req.WXSceneFavorite; //微信收藏
        }

        req.transaction = String
                .valueOf(System.currentTimeMillis());
        req.message = msg;
        api.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
