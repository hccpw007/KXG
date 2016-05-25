package com.cqts.kxg.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 获取图片验证码
 * Created by Administrator on 2016/5/25.
 */
public class GetImageCode {
    public static void getImageCode(ImageView imageView,String phone) {
        String url = "http://api.kxg99.com/captcha/image?mobile_phone="+phone;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().displayImage(url,imageView,options);
    }
}
