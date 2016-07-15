package com.cqts.kxg.views;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cqts.kxg.R;
import com.cqts.kxg.utils.ShareUtilsQQ;
import com.cqts.kxg.utils.ShareUtilsWB;
import com.cqts.kxg.utils.ShareUtilsWB2;
import com.cqts.kxg.utils.ShareUtilsWX;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.tauth.Tencent;

/**
 * 分享POP
 */
public class SharePop implements View.OnClickListener {
    public final static int TAG_SETTING = 1, TAG_APPRENTICE = 2, TAG_ARTICLE = 3;
    private Activity context;
    private String url;
    private String title = "";
    private String text;
    private String image_url;
    private ShareResult shareResult;
    private PopupWindow window;
    private static SharePop instance;
    private Bitmap image;
    private Tencent tencent;
    private int clickId = 0;
    private ImageView qq_img;
    private ImageView qqzone_img;

    public static SharePop getInstance() {
        if (instance == null) {
            synchronized (SharePop.class) {
                if (instance == null) {
                    instance = new SharePop();
                }
            }
        }
        return instance;
    }

    public SharePop showPop(Activity context, View view, String title, String url, String text,
                            Bitmap image, String image_url,
                            ShareResult shareResult) {
        this.image = image;
        this.image_url = image_url;
        this.shareResult = shareResult;
        this.title = title;
        this.context = context;
        this.url = url;
        this.text = text;
        tencent = Tencent.createInstance("1105448613", context.getApplicationContext());
        View inflate = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        ImageView wx_img = (ImageView) inflate.findViewById(R.id.wx_img);
        ImageView friend_img = (ImageView) inflate.findViewById(R.id.friend_img);
        ImageView copy_img = (ImageView) inflate.findViewById(R.id.copy_img);
        ImageView xl_img = (ImageView) inflate.findViewById(R.id.xl_img);
        qq_img = (ImageView) inflate.findViewById(R.id.qq_img);
        qqzone_img = (ImageView) inflate.findViewById(R.id.qqzone_img);

        wx_img.setOnClickListener(this);
        qqzone_img.setOnClickListener(this);
        friend_img.setOnClickListener(this);
        copy_img.setOnClickListener(this);
        xl_img.setOnClickListener(this);
        qq_img.setOnClickListener(this);

        window = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
        return instance;
    }

    @Override
    public void onClick(View v) {
        clickId = v.getId();
        switch (v.getId()) {
            case R.id.wx_img: //微信分享
                ShareUtilsWX.wxShare(context, 1, title, url, text, image);
                break;
            case R.id.friend_img: //朋友圈分享
                ShareUtilsWX.wxShare(context, 2, title, url, text, image);
                break;
            case R.id.xl_img: //新浪微博
                ShareUtilsWB2.getInstance().wbShare(context, image);
                break;
            case R.id.qq_img: //QQ分享
                ShareUtilsQQ.ShareQQ(tencent, context, title, text, url, image_url);
                break;
            case R.id.qqzone_img: //QQ空间分享
                ShareUtilsQQ.ShareQQZone(tencent, context, title, text, url, image_url);
                break;
            case R.id.copy_img: //复制到剪切板
                ClipboardManager cmb = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(url);
                Toast.makeText(context, "已经复制到粘贴板!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        window.dismiss();
    }

    public void setResult(int result) {
        switch (result) {
            case ShareResult.SUCCESS:
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case ShareResult.CANCEL:
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
                break;
            case ShareResult.FAILED:
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                break;
        }

        if (shareResult != null) {
            shareResult.shareResult(result);
        }
    }

    /**
     * 分享结果接口,在支付的Activity实现方法
     */
    public interface ShareResult {
        public static int SUCCESS = -1;
        public static int DOING = -2;
        public static int CANCEL = -3;
        public static int FAILED = -4;

        /**
         * @param result (int) <br>
         *               SUCCESS -1 支付成功<br>
         *               DOING -2 支付处理中<br>
         *               CANCEL -3 支付取消<br>
         *               FAILED -4 支付失败<br>
         */
        public void shareResult(int result);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (null != window && (clickId == qq_img.getId() || clickId ==
                    qqzone_img.getId())) {
                tencent.onActivityResultData(requestCode, resultCode, data, ShareUtilsQQ.listener);
                clickId = 0;
            }
        } catch (Exception e) {
        }
    }
}