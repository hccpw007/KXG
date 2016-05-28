package com.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cqts.kxg.R;


/**
 * 圆角矩形
 * 自定义钝角
 * Created by Administrator on 2016/4/27.
 */
public class MyOvalImageView extends ImageView {
    Context context;
    AttributeSet attrs;
    private int anInt;

    public MyOvalImageView(Context context) {
        super(context);
        this.context = context;
    }

    public MyOvalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        getAttrs();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap img = getImg();
        canvas.drawBitmap(img, 0, 0, null);
    }

    /**
     * 获取头像图片
     *
     * @return
     */
    public Bitmap getImg() {
        //获得图片
        Drawable drawable = getDrawable();
        Bitmap source = ((BitmapDrawable) drawable).getBitmap();
        //获得图片的原始高度
        int width = source.getWidth();
        int height = source.getHeight();
        //创建一个画布,大小是最终需要的大小(正方形)
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config
                .ARGB_8888);
        //创建画布
        Canvas canvas = new Canvas(target);
        //画笔
        Paint paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        //画圆角矩形
        paint.setStyle(Paint.Style.FILL);//充满
        paint.setColor(Color.BLUE);
        RectF oval = new RectF(0, 0, width, height);// 设置个新的长方形
        canvas.drawRoundRect(oval, anInt, anInt, paint);//第二个参数是x半径，第三个参数是y半径
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setAntiAlias(true);
        canvas.drawBitmap(source, 0, 0, paint);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(target, getWidth(), getHeight(), true);
        return scaledBitmap;
    }

    //获得参数
    void getAttrs() {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyOvalImageView);
        anInt = typedArray.getDimensionPixelSize(R.styleable.MyOvalImageView_myradius, 0);
        typedArray.recycle();
    }
}
