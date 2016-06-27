package com.cqts.kxg.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqts.kxg.R;

/**
 * Created by Administrator on 2016/6/27.
 */
public class IndexAdapter extends PagerAdapter {
    ImageView[] imageViews = new ImageView[3];

    public IndexAdapter(Context context) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < 3; i++) {
            imageViews[i] = new ImageView(context);
            imageViews[i].setLayoutParams(params);
            imageViews[i].setBackgroundResource(R.color.white);
        }
        imageViews[0] .setImageResource(R.mipmap.bg_index1);
        imageViews[1] .setImageResource(R.mipmap.bg_index2);
        imageViews[2] .setImageResource(R.mipmap.bg_index3);
    }

    @Override
    public int getCount() {
        return 3;
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
        return imageViews[position];
    }
}
