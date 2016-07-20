package com.cqts.kxg.center;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.CallbackContext;
import com.alibaba.sdk.android.login.LoginService;
import com.alibaba.sdk.android.login.callback.LogoutCallback;
import com.base.utils.DataCleanManager;
import com.cqts.kxg.R;
import com.cqts.kxg.main.MainActivity;
import com.cqts.kxg.main.MyActivity;
import com.cqts.kxg.main.MyApplication;
import com.cqts.kxg.main.WebActivity;
import com.cqts.kxg.utils.MyUrls;
import com.cqts.kxg.utils.SPutils;
import com.cqts.kxg.utils.ShareUtilsWB;
import com.cqts.kxg.utils.UMengUtils;
import com.cqts.kxg.views.SharePop;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QQShare;

public class SettingActivity extends MyActivity implements View.OnClickListener {
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout layout5;
    private Button exit_btn;
    private TextView cacheTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        InitView();
    }

    private void InitView() {
        setMyTitle("设置");
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout4 = (LinearLayout) findViewById(R.id.layout4);
        layout5 = (LinearLayout) findViewById(R.id.layout5);
        exit_btn = (Button) findViewById(R.id.exit_btn);
        cacheTv = (TextView) findViewById(R.id.cache_tv);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        exit_btn.setOnClickListener(this);


        try {
            cacheTv.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout1: //个人资料
                startActivity(new Intent(this, InformationActivity.class));
                break;
            case R.id.layout2: //账户与安全
                startActivity(new Intent(this, SafeActivity.class));
                finish();
                break;
            case R.id.layout3: //清除缓存
                try {
                    DataCleanManager.clearAllCache(this);
                    cacheTv.setText(DataCleanManager.getTotalCacheSize(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.layout4: //邀请好友使用开心购
                String title = (TextUtils.isEmpty(getUserInfo().alias) ? "" : ("\"" + getUserInfo
                        ().alias + "\"")) + "推荐给你“开心购久久app”，注册后有红包哦！";
                String url = getUserInfo().invite_link + getUserInfo().invite_code;
                String text = "您可以在这里浏览购买数百万商品，更有9.9包邮等特价专区！";
                SharePop.getInstance().showPop(this, layout4, title, url, text, null, null, null);
                break;
            case R.id.layout5: //关于开心购
                if (null == MyUrls.getInstance().getMyUrl(this)) {
                    return;
                }
                String versionName = getVersionName();
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "关于开心购");
                intent.putExtra("url", MyUrls.getInstance().getMyUrl(this).about + "?version=" +
                        versionName);
                startActivity(intent);
                break;
            case R.id.exit_btn: //退出当前账户
                try {
                    baiChuanLogout(); // 百川退出
                    UMengUtils.setSignOff(); //友盟退出
                } catch (Exception e) {
                }
                MyApplication.userInfo = null;
                MyApplication.token = "";
                SPutils.setToken("");
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 获得当前APP的外部版本号
     */
    public String getVersionName() {
        String versionName = "1.0";
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    //百川登录需要的回调
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        CallbackContext.onActivityResult(requestCode, resultCode, data);
//    }

    //百川登录
    public void baiChuanLogin() {
        AlibabaSDK.getService(LoginService.class).showLogin(this, new com.alibaba.sdk.android
                .login.callback.LoginCallback() {
            @Override
            public void onSuccess(com.alibaba.sdk.android.session.model.Session session) {
                System.out.println("百川登录成功");
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println("百川登录失败");
            }
        });
    }

    //百川登出
    public void baiChuanLogout() {
        LoginService loginService = AlibabaSDK.getService(LoginService.class);
        loginService.logout(this, new LogoutCallback() {
            @Override
            public void onFailure(int code, String msg) {
                System.out.println("百川登出失败===="+msg);
            }

            @Override
            public void onSuccess() {
                System.out.println("百川登出成功");
            }
        });
    }
}
