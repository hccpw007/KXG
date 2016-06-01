package com.cqts.kxg.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.BaseFragment;
import com.base.http.HttpForVolley;
import com.base.utils.PhotoUtil;
import com.base.utils.PhotoPopupWindow;
import com.cqts.kxg.R;
import com.cqts.kxg.utils.MyHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/28.
 */
public class LoginedFragment extends BaseFragment {
    private PhotoUtil photoUtil;
    private ImageView center_img;
    private PhotoPopupWindow mPhotoPopupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_logined, null);
        InitView(view);
        return view;
    }

    private void InitView(View view) {
        center_img = (ImageView) view.findViewById(R.id.center_img);

        center_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtil = new PhotoUtil(LoginedFragment.this, 3, 3);
                mPhotoPopupWindow = new PhotoPopupWindow(getActivity(), photoUtil);
                mPhotoPopupWindow.showpop(v);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.FromWhere.camera:
            case PhotoUtil.FromWhere.photo:
                photoUtil.onActivityResult(requestCode, resultCode, data);
                break;
            case PhotoUtil.FromWhere.forfex:
                if (resultCode == getActivity().RESULT_OK) {
                   MyHttp.uploadImage(http, null, photoUtil.getForfexPath(), new HttpForVolley
                            .HttpTodo() {
                        @Override
                        public void httpTodo(Integer which, JSONObject response) {
                            ImageLoader.getInstance().displayImage(response.optString("filename")
                                    , center_img);
                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}
