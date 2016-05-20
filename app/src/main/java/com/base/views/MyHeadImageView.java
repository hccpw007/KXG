package com.base.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 圆形头像图片
 * Created by Administrator on 2016/4/27.
 */
public class MyHeadImageView extends ImageView {

    public MyHeadImageView(Context context) {
        super(context);
    }

    public MyHeadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap img = getImg();
        canvas.drawBitmap(img, 0, 0, null);
    }

    /**
     * 获取头像图片
     * @return
     */
    public Bitmap getImg() {
        //获得图片
        Drawable drawable = getDrawable();
        Bitmap source = ((BitmapDrawable) drawable).getBitmap();

        //获得图片的原始高度
        int width = source.getWidth();
        int height = source.getHeight();

        int length = width < height ? width : height;
        //白色圆环的宽度
        int frame = 10;

        //创建一个画布,大小是最终需要的大小(正方形)
        Bitmap target = Bitmap.createBitmap(length + frame * 2, length + frame * 2, Bitmap.Config.ARGB_8888);

        //创建画布
        Canvas canvas = new Canvas(target);

        //画笔
        Paint paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);

        //画一个圆形(无圆环的图片大小)
        canvas.drawCircle(length / 2 + frame, length / 2 + frame, length / 2, paint);

        //设置模式  详细说明:http://blog.csdn.net/lmj623565791/article/details/24555655
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //图片的长宽比较  取短
        if (width > height) {
            canvas.drawBitmap(source, (height - width) / 2 + frame, frame, paint);
        } else {
            canvas.drawBitmap(source, frame, (width - height) / 2 + frame, paint);
        }

        //更改paint的显示模式  改变上一个mode的模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);//设置空心
        paint.setStrokeWidth(frame);
        //画圆环
        canvas.drawCircle(length / 2 + frame, length / 2 + frame, (length + frame) / 2 - 1, paint);

        //把图片压缩成合适imageview的大小
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(target, getWidth(), getHeight(), true);
        return scaledBitmap;
    }
}
