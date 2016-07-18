package com.cqts.kxg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cqts.kxg.utils.ShareUtilsWB;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

public class TestActivity extends Activity implements View.OnClickListener{
    private ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate================");
        setContentView(R.layout.activity_main);
        imageview = (ImageView) findViewById(R.id.imageview);
        imageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        ShareUtilsWB.getInstance().wbShare(this,null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop=======================");
    }
}
