package com.cqts.kxg.views;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cqts.kxg.R;
import com.cqts.kxg.utils.WXShareUtils;

/**
 * 分享POP
 */
public class SharePop implements View.OnClickListener {
    public final static int TAG_SETTING = 1, TAG_APPRENTICE = 2, TAG_ARTICLE = 3;
    private Context context;
    private String url;
    private String title = "";
    private String text;
    private ShareResult shareResult;
    private PopupWindow window;
    private static SharePop instance;
    private Bitmap image;

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

    public SharePop showPop(Context context, View view, String title, String url, String text,
                            Bitmap image,
                            ShareResult payResult) {
        this.image = image;
        this.shareResult = payResult;
        this.title = title;
        this.context = context;
        this.url = url;
        this.text = text;
        View inflate = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        ImageView wx_img = (ImageView) inflate.findViewById(R.id.wx_img);
        ImageView friend_img = (ImageView) inflate.findViewById(R.id.friend_img);
        ImageView copy_img = (ImageView) inflate.findViewById(R.id.copy_img);
        TextView cancel_tv = (TextView) inflate.findViewById(R.id.cancel_tv);

        wx_img.setOnClickListener(this);
        cancel_tv.setOnClickListener(this);
        friend_img.setOnClickListener(this);
        copy_img.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.wx_img: //微信分享
                WXShareUtils.wxShare(context, 1, title, url, text, image);
                break;
            case R.id.friend_img: //朋友圈分享
                WXShareUtils.wxShare(context, 2, title, url, text, image);
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
        if (shareResult != null) {
            shareResult.shareResult(result);
        }
    }

    /**
     * 支付结果接口,在支付的Activity实现方法
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
}