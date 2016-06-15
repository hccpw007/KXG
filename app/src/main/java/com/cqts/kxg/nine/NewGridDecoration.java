package com.cqts.kxg.nine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * RecyclerView的分割线
 * Created by Administrator on 2016/4/19.
 */
public class NewGridDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;
    int lineSize = 0;
    int whichStart = 0;

    public NewGridDecoration(int whichStart, int lineSize, int lineColor) {
        this.whichStart = whichStart;
        this.lineSize = lineSize;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(lineColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildPosition(view);
        if (position < whichStart) return;
        RecyclerView.Adapter adapter = parent.getAdapter(); //获得RecyclerView的Adapter
        int itemSize = adapter.getItemCount(); //总共有多少个item
        int childPosition = position - whichStart;
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount() / layoutManager.getSpanSizeLookup()
                .getSpanSize(position);
        int i = (childPosition) % spanCount;
        float a = spanCount;
        float left = (1 - i / a) * lineSize;
        float right = ((1 + i) / a) * lineSize;
        float top = lineSize / 2;
        float bottom = lineSize / 2;
        outRect.set((int) left, (int) top, (int) right, (int) bottom);
        if (childPosition < spanCount) {
            top = lineSize;
        }
        if (childPosition >= itemSize - spanCount - whichStart) {
            bottom = lineSize;
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
            if (position >= whichStart) {
                int left = child.getLeft() - lineSize;
                int top = child.getTop() - lineSize;
                int right = child.getRight() + lineSize;
                int bottom = child.getBottom() + lineSize;
                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }
}
