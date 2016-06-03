package com.cqts.kxg.main;

import android.os.Bundle;
import android.view.View;

import com.base.BaseFragment;
import com.cqts.kxg.R;

/**
 * Created by Administrator on 2016/6/3.
 */
public class MyFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setHttpFail(final HttpFail httpFail) {
        view.findViewById(R.id.include_framelayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.include_faillayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.include_nodatalayout).setVisibility(View.GONE);
        view.findViewById(R.id.include_fail_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpFail.toHttpAgain();
            }
        });

    }

    public void setHttpNotData(final HttpFail httpFail) {
        view.findViewById(R.id.include_framelayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.include_faillayout).setVisibility(View.GONE);
        view.findViewById(R.id.include_nodatalayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.include_nodata_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpFail.toHttpAgain();
            }
        });
    }
    public void setHttpSuccess() {
        view.findViewById(R.id.include_framelayout).setVisibility(View.GONE);
    }
    public interface HttpFail{
       void toHttpAgain();
    }
}
