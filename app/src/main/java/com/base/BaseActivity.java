package com.base;

import com.base.http.HttpForVolley;
import com.base.swipebacklayout.SwipeBackActivity;
import com.cqts.kxg.R;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends SwipeBackActivity {
    private Toast toast;
    public HttpForVolley http;
    private boolean isStopHttp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        http = new HttpForVolley(this);
    }


    /**
     * 提示框
     */
    public void showToast(String str) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Log.e("error", "error:" + e.getMessage());
        }
    }

    /**
     * stop页面的时候是否执行取消网络请求
     */
    public void setStopHttp(boolean isStopHttp) {
        this.isStopHttp = isStopHttp;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isStopHttp) {
            BaseValue.mQueue.cancelAll(this);
        }
    }

    /**
     * 是否透明化状态栏
     */
    public void setTransparencyBar(boolean transparency) {
        if (transparency) {
            //通知栏和虚拟按键透明(xml需要设置属性)

            // android:clipToPadding="true"
            // android:fitsSystemWindows="true"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //通知栏透明
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //虚拟按键透明
                // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }
}
