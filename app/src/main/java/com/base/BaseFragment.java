package com.base;

import com.base.http.HttpForVolley;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BaseFragment extends Fragment {
    private Toast toast;
    private boolean isStopHttp = true;
    public HttpForVolley http;
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = new HttpForVolley(getActivity());
    }

    /**
     * 提示框
     */
    public void showToast(String str) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
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
    public void onStop() {
        super.onStop();
        if (isStopHttp) {
            BaseValue.mQueue.cancelAll(getActivity());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) this.view.getParent()).removeView(this.view);
    }

    public void onShow() {
    }

}
