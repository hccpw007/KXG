package com.cqts.kxg.home;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseValue;

public class MyManager extends RecyclerView.ItemDecoration {
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        View childAt = parent.getChildAt(0);
        RecyclerView.LayoutParams layoutParams1 = (RecyclerView.LayoutParams) childAt.getLayoutParams();
        System.out.println(layoutParams1.topMargin+"===");
        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        layoutParams.height = childAt.getMeasuredHeight()*2;
    }
}