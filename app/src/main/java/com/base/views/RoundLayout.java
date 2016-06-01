package com.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.cqts.kxg.R;

/**
 * 圆角矩形布局<br>
 * 所有view/布局-都可以用
 */
public class RoundLayout extends LinearLayout {
    private float roundLayoutRadius = 20f;
    private Path roundPath;
    private RectF rectF;
    private int bgColor;

    public RoundLayout(Context context) {
        this(context, null);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public RoundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        getAttrs(context, attrs);
        init();
    }

    private void init() {
        setWillNotDraw(false);//如果你继承的是ViewGroup,注意此行,否则draw方法是不会回调的;
        roundPath = new Path();
        rectF = new RectF();
    }

    private void setRoundPath() {
        //添加一个圆角矩形到path中, 如果要实现任意形状的View, 只需要手动添加path就行
        roundPath.addRoundRect(rectF, roundLayoutRadius, roundLayoutRadius, Path.Direction.CW);
//        roundPath.addRoundRect(rectF,new float[]{0,0,0,0,20,20,20,20},Path.Direction.CW);
    }


    public void setRoundLayoutRadius(float roundLayoutRadius) {
        this.roundLayoutRadius = roundLayoutRadius;
        setRoundPath();
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        rectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        setRoundPath();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(bgColor);
        if (roundLayoutRadius > 0f) {
            canvas.clipPath(roundPath);
        }
        super.draw(canvas);
    }

    //获得参数
    void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundLayout);
        //圆角弧度
        roundLayoutRadius = typedArray.getDimensionPixelSize(R.styleable
                .RoundLayout_mylayoutradius, 0);
        // 背景参数
        bgColor = typedArray.getColor(R.styleable.RoundLayout_mylayoutbg, Color.WHITE);
        typedArray.recycle();
    }
}