package com.cqts.kxg.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.cqts.kxg.R;
import com.cqts.kxg.bean.BannerInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class BannerAdapter extends PagerAdapter {
    private ImageView[] imageViews;
    Context context;
    RadioButton[] rdBtn;
    private List<BannerInfo> imgUrls  = new ArrayList<>();
    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(R.mipmap.solid_banner)
            .showImageOnFail(R.mipmap.solid_banner).build();

    public BannerAdapter(Context context, RadioButton[] rdBtn, List<BannerInfo> imgUrl) {
        this.context = context;
        this.rdBtn = rdBtn;
        this.imgUrls .addAll(imgUrl);

        if (imgUrls.size() > 0) {
            imgUrls.add(imgUrls.get(0));
            imgUrls.add(0, imgUrls.get(imgUrls.size() - 2));
            imageViews = new ImageView[imgUrls.size()];
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imgUrls.size(); i++) {
            if (i < imgUrls.size() - 2) {
                rdBtn[i].setVisibility(View.VISIBLE);
            }
            imageViews[i] = new ImageView(context);
            imageViews[i].setLayoutParams(params);
            imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
        }
        }
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(imageViews[position]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(imageViews[position]);
        ImageLoader.getInstance().displayImage(imgUrls.get(position).ad_code, imageViews[position],defaultOptions);
        return imageViews[position];
    }
}
