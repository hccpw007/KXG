package com.base.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView的分割线
 * Created by Administrator on 2016/4/19.
 */
public class GridDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;

    int lineSize = 0;
    int startSize = 0; //从什么地方开始
    boolean isFrame = true;

    /**
     * newGridDecoration
     * @param startSize  开始的位置 默认为0
     * @param lineSize  分割线的宽度
     * @param lineColor  分割线的颜色
     * @param isFrame  是否有边框
     */
    public GridDecoration(int startSize, int lineSize, int lineColor, boolean isFrame) {
        this.startSize = startSize;
        this.lineSize = lineSize;
        this.isFrame = isFrame;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(lineColor);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildPosition(view);
        if (position < startSize) return;
        RecyclerView.Adapter adapter = parent.getAdapter(); //获得RecyclerView的Adapter
        int itemSize = adapter.getItemCount(); //总共有多少个item
        int childPosition = position - startSize;
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount() / layoutManager.getSpanSizeLookup()
                .getSpanSize(position);
        int i = (childPosition) % spanCount;
        float s = spanCount;
        float left = 0;
        float right = 0;
        float top = lineSize / 2;
        float bottom = lineSize / 2;
        //有边框
        if (isFrame) {
            left = (1 - i / s) * lineSize;
            right = ((1 + i) / s) * lineSize;
            if (childPosition < spanCount) {
                top = lineSize;
            }
            if (childPosition >= itemSize - spanCount - startSize) {
                bottom = lineSize;
            }
        }

        //无边框
        if (!isFrame) {
            left = (i / s) * lineSize;
            right = ((s - 1 - i) / s) * lineSize;
            if (childPosition < spanCount) {
                top = 0;
            }
            if (childPosition >= itemSize - spanCount - startSize) {
                bottom = 0;
            }
        }

        outRect.set((int) left, (int) top, (int) right, (int) bottom);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildPosition(child);
            if (position >= startSize) {
                int left = child.getLeft() - lineSize;
                int top = child.getTop() - lineSize;
                int right = child.getRight() + lineSize;
                int bottom = child.getBottom() + lineSize;
                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }


}