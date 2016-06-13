package com.cqts.kxg.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cqts.kxg.R;
import com.cqts.kxg.main.MyApplication;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 分享POP
 */
public class SharePop implements View.OnClickListener {

    private ImageView wx_img;
    private ImageView friend_img;
    private ImageView weibo_img;
    private ImageView qq_img;
    Context context;
    String url;
    String title = "开心购久久商城";
    String description;

    public SharePop(Context context, View view, String url, String description) {
        this.context = context;
        this.url = url;
        this.description = description;

        View inflate = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        wx_img = (ImageView) inflate.findViewById(R.id.wx_img);
        friend_img = (ImageView) inflate.findViewById(R.id.friend_img);
        weibo_img = (ImageView) inflate.findViewById(R.id.weibo_img);
        qq_img = (ImageView) inflate.findViewById(R.id.qq_img);

        wx_img.setOnClickListener(this);
        friend_img.setOnClickListener(this);
        weibo_img.setOnClickListener(this);
        qq_img.setOnClickListener(this);

        PopupWindow window = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_img: //微信分享
                wxShare(1);
                break;
            case R.id.friend_img: //朋友圈分享
                wxShare(2);
                break;
            case R.id.weibo_img: //微博分享
                break;
            case R.id.qq_img: //QQ分享
                break;
            default:
                break;
        }
    }

    /**
     * 微信分享<p>
     *     type:分享类型, 1 = 微信分享  2 = 朋友圈分享 3 = 微信收藏
     */
    private void wxShare(int type) {
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
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap
// .center_table9);
//        msg.thumbData = WXShareUtil.bmpToByteArray(thumb, true);
        msg.title = title;
        msg.description = description;
        SendMessageToWX.Req req = new SendMessageToWX.Req();

        if (type == 1){
            req.scene = SendMessageToWX.Req.WXSceneSession; //微信分享
        }
        if (type == 2){
            req.scene = SendMessageToWX.Req.WXSceneTimeline; //朋友圈分享
        }
        if (type == 3){
            req.scene = SendMessageToWX.Req.WXSceneFavorite; //微信收藏
        }

        req.transaction = String
                .valueOf(System.currentTimeMillis());
        req.message = msg;
        api.sendReq(req);
    }
}