package com.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2016/7/20.
 */
public class OVBitmapTransform extends BitmapTransformation {
    int r = 0;
    private float[] radii = null;

    public OVBitmapTransform(Context context, float left, float top, float right, float bottom) {
        super(context);
        radii = new float[]{left, left, top, top, right, right, bottom, bottom};
    }

    public OVBitmapTransform(Context context, int r) {
        super(context);
        this.r = r;
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i1) {
        if (radii != null) {
            return getBitmap(bitmap,radii);
        } else {
            return getBitmap(bitmap,r);
        }
    }

    @Override
    public String getId() {
        return getClass().getName();
    }


    public Bitmap getBitmap(Bitmap source, float[] radii) {
        //获得图片的原始高度
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config
                .ARGB_8888);
        Canvas canvas = new Canvas(target);
        Path roundPath = new Path();
        RectF rectF = new RectF();
        rectF.set(0f, 0f, width, height);
        roundPath.addRoundRect(rectF, radii, Path
                .Direction.CW);
        Paint paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        canvas.drawPath(roundPath, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setAntiAlias(true);
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public Bitmap getBitmap(Bitmap source, int r) {
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
        canvas.drawRoundRect(oval, r, r, paint);//第二个参数是x半径，第三个参数是y半径
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setAntiAlias(true);
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
