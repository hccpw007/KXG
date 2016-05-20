package com.base;

import com.base.http.SupportHttps;
import com.base.refreshlayout.RefreshLayout;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

public class BaseApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		BaseValue.setInit(this); //初始化全局常量
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
	private void creatUIL() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
				.build();
		ImageLoaderConfiguration defaultconfig = new ImageLoaderConfiguration.Builder(getBaseContext())
				.defaultDisplayImageOptions(defaultOptions).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(defaultconfig);
	}
}
