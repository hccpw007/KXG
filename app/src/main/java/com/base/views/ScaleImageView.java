package com.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqts.kxg.R;

/**
 * 按比例显示图片
 */
public class ScaleImageView extends ImageView {

    private float myScale = 0;

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (myScale == 0) {
            getAttrs(context, attrs);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getMeasuredHeight()!= getMeasuredWidth()*myScale){
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = (int) (getMeasuredWidth()*myScale);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //获得参数
    void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ScaleImageView);
        myScale = typedArray.getFloat(R.styleable.ScaleImageView_myScale, 0);
        typedArray.recycle();
    }
}
