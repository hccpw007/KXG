/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.cqts.kxg.wxapi;

import com.base.BaseActivity;
import com.cqts.kxg.views.SharePop;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, "wx9d268d263aef3fd4");
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq arg0) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			SharePop.getInstance().setResult(SharePop.ShareResult.SUCCESS);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			SharePop.getInstance().setResult(SharePop.ShareResult.CANCEL);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			SharePop.getInstance().setResult(SharePop.ShareResult.FAILED);
			break;
		default:
			SharePop.getInstance().setResult(SharePop.ShareResult.FAILED);
			break;
		}
		finish();
	}
}
