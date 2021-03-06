package com.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cqts.kxg.R;
import com.cqts.kxg.views.SharePop;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BaseValue {

    public final static boolean isDebug = true;// 调试开关
    public static int screenwidth;// 屏幕宽度
    public static int screenHeight;// 屏幕高度
    public static float density;// 屏幕密度
    public static float scaledDensity;// 屏幕放大密度
    public static int densityDPI;// 屏幕密度
    public static Gson gson;
    public static RequestQueue mQueue;
    public static InputMethodManager imm; //输入法管理器
    public static SharePop sharePop; //分享

    public static void setInit(Application application) {
        getDisplayValue(application);
        gson = new Gson();
        mQueue = Volley.newRequestQueue(application);
        imm = (InputMethodManager) application.getBaseContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        sharePop = new SharePop();
    }

    /**
     * 获取屏幕参数
     */
    private static void getDisplayValue(Application application) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = application.getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenwidth = displayMetrics.widthPixels;
        density = displayMetrics.density;
        scaledDensity = displayMetrics.scaledDensity;
        densityDPI = displayMetrics.densityDpi;
    }

    /**
     * dp转px
     */
    public static int dp2px(float dipValue) {
        return (int) (dipValue * density + 0.5f);
    }
    /**
     * sp转px
     */
//    public static int sp2px(float dipValue) {
//        return (int) (dipValue * scaledDensity + 0.5f);
//    }

    public static DisplayImageOptions getOptions(int defaultimg){
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageOnFail(defaultimg)
                .showImageOnLoading(R.color.transparency)
               .showImageForEmptyUri(defaultimg).build();
    }
}
