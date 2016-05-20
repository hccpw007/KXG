package com.cqts.kxg.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/18.
 */
public class HomeViewpagerAdapter extends PagerAdapter {
    String[] imgUrl = {"http://pic25.nipic.com/20121108/9252150_160744284000_2.jpg",
            "http://pic20.nipic.com/20120428/5455122_162725484388_2.jpg",
            "http://img3.redocn.com/tupian/20150411/shouhuixiantiaopingguoshiliang_4042458.jpg"
    };
    private ImageView[] imageViews;
    Context context;
    RadioButton[] rdBtn;
    private ArrayList<String> imgUrls;

    public HomeViewpagerAdapter(Context context, RadioButton[] rdBtn) {
        this.context = context;
        this.rdBtn = rdBtn;

        imgUrls = new ArrayList<String>();
        imgUrls.add(imgUrl[0]);
        imgUrls.add(imgUrl[1]);
        imgUrls.add(imgUrl[2]);

        imgUrls.add(0, imgUrl[2]);
        imgUrls.add(imgUrl[0]);

        imageViews = new ImageView[imgUrls.size()];
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imgUrls.size(); i++) {
            if (i < imgUrls.size() - 2) {
                rdBtn[i].setVisibility(View.VISIBLE);
            }
            imageViews[i] = new ImageView(context);
            imageViews[i].setLayoutParams(params);
            imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
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
        ImageLoader.getInstance().displayImage(imgUrls.get(position), imageViews[position]);
        return imageViews[position];
    }
}
