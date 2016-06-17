package com.base.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.base.BaseValue;

/**
 * 组件在屏幕自由移动
 */
public class ViewMove {

    public ViewMove(View v, final int width, final int height, final Context context,
                    final OnClickListener click_Down) {
        v.setOnTouchListener(new OnTouchListener() {
            private float DOWN_X;
            private float DOWN_Y;
            private float UP_X;
            private float UP_Y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int mx;
                int my;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DOWN_X = event.getRawX();
                        DOWN_Y = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        UP_X = event.getRawX();
                        UP_Y = event.getRawY();
                        if (DOWN_X == UP_X && DOWN_Y == UP_Y) {
                            click_Down.onClick(v);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int w = BaseValue.dp2px(width) / 2;
                        int h = BaseValue.dp2px(height) / 2;
                        mx = (int) (event.getRawX());
                        my = (int) (event.getRawY() - 50);
                        int l = mx - w;
                        int t = my - h;
                        int r = mx + w;
                        int b = my + h;
                        if (l < 0) {
                            l = 0;
                            r = w * 2;
                        }
                        if (t < 0) {
                            t = 0;
                            b = h * 2;
                        }
                        if (r > BaseValue.screenwidth) {
                            r = BaseValue.screenwidth;
                            l = BaseValue.screenwidth - w * 2;
                        }
                        if (b > BaseValue.screenHeight - 50) {
                            b = BaseValue.screenHeight - 50;
                            t = BaseValue.screenHeight - h * 2 - 50;
                        }
                        v.layout(l, t, r, b);
                        break;
                }
                return true;
            }
        });
    }

    // 获得通知栏的高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
