package com.base;

import com.base.http.SupportHttps;
import com.base.views.RefreshLayout;
import com.cqts.kxg.main.MyApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

public class BaseApplication extends Application {
    protected static BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static void init() {
        BaseValue.setInit(instance); //初始化全局常量
        SupportHttps.setInit();// 初始化https
        RefreshLayout.setInit();// 初始化下拉刷新
        creatUIL();
    }

    /**
     * 设置字体不随系统改变而改变
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    /**
     * 注册UIL
     */
    private static void creatUIL() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory
                (true).cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration defaultconfig = new ImageLoaderConfiguration.Builder(instance
                .getBaseContext())
                .defaultDisplayImageOptions(defaultOptions).threadPriority(Thread.NORM_PRIORITY -
                        2).writeDebugLogs()
                .denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new
                        Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(defaultconfig);
    }
}
