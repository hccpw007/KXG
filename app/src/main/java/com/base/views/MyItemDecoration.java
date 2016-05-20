package com.base.views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/4/19.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;
    private int size;


    public MyItemDecoration(int size, int color) {
        this.size = size;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int orientation = layoutManager.getOrientation();

        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            final int left = child.getLeft();
            final int right = child.getRight();
            final int bottom = child.getBottom();
            final int top = child.getTop();

            if (orientation== OrientationHelper.VERTICAL) {
                c.drawRect(left, bottom, right , bottom+size, paint);
            } else {
                c.drawRect(right, top, right +size, bottom, paint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int orientation = layoutManager.getOrientation();
        RecyclerView.Adapter adapter = parent.getAdapter(); //获得RecyclerView的Adapter
        int itemSize = adapter.getItemCount(); //总共有多少个item
        int childPosition = parent.getChildPosition(view); //当前View的Position
        if (childPosition != itemSize - 1) {
            if (orientation== OrientationHelper.VERTICAL) {
                outRect.set(0, 0, 0, size);
            } else {
                outRect.set(0, 0, size, 0);
            }
        }

    }
}
