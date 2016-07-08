package com.cqts.kxg.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.base.BaseFragment;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.utils.UMengUtils;

/**
 * Created by Administrator on 2016/6/3.
 */
public class MyFragment extends BaseFragment {
    private RelativeLayout include_framelayout;
    private LinearLayout include_faillayout;
    private Button include_fail_btn;
    private LinearLayout include_nodatalayout;
    private Button include_nodata_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void getIncludeView() {
        if (null == include_framelayout) {
            include_framelayout = (RelativeLayout) view.findViewById(R.id.include_framelayout);
            include_faillayout = (LinearLayout) view.findViewById(R.id.include_faillayout);
            include_fail_btn = (Button) view.findViewById(R.id.include_fail_btn);
            include_nodatalayout = (LinearLayout) view.findViewById(R.id.include_nodatalayout);
            include_nodata_btn = (Button) view.findViewById(R.id.include_nodata_btn);
        }
    }

    public void setHttpFail(final HttpFail httpFail) {
        getIncludeView();
        include_framelayout.setVisibility(View.VISIBLE);
        include_faillayout.setVisibility(View.VISIBLE);
        include_nodatalayout.setVisibility(View.GONE);
        include_fail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpFail.toHttpAgain();
            }
        });
    }

    public void setHttpNotData(final HttpFail httpFail) {
        getIncludeView();
        include_framelayout.setVisibility(View.VISIBLE);
        include_faillayout.setVisibility(View.GONE);
        include_nodatalayout.setVisibility(View.VISIBLE);
        include_nodata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpFail.toHttpAgain();
            }
        });
    }

    public void setHttpSuccess() {
        getIncludeView();
        include_framelayout.setVisibility(View.GONE);
    }

    public interface HttpFail {
        void toHttpAgain();
    }


    //是否已经登录
    public boolean isLogined() {
        if (MyApplication.userInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    //需要登录,未登录这跳转登录页面
    public boolean needLogin() {
        if (!isLogined()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return false;
        }
        return true;
    }

    public UserInfo getUserInfo() {
        if (needLogin()) {
            return MyApplication.userInfo;
        }
        return null;
    }

    @Override
    public void onShow() {
        super.onShow();
        UMengUtils.setOnPageStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        UMengUtils.setOnonPageEnd(this);
    }
}
