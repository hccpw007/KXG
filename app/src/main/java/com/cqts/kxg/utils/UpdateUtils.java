package com.cqts.kxg.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.base.http.HttpForVolley;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.UpdateInfo;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 自动更新
 */
public class UpdateUtils implements MyHttp.MyHttpResult, View.OnClickListener {
    Activity context;
    private UpdateInfo updateInfo;
    private AlertDialog alertDialog;
    private View goingView;

    public UpdateUtils(Activity context) {
        this.context = context;
        HttpForVolley http = new HttpForVolley(context);
        //请求网络接口,检测是否需要更新
        MyHttp.update(http, null, this);
    }


    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if (code != 0) {
            return;
        }
        updateInfo = (UpdateInfo) bean;
        showForceDialog();
        if (getVersion() == updateInfo.version) {
            //版本号相同,不需要更新
            return;
        }

        if (updateInfo.forcibly) { //需要强制更新

        } else { //只提示不强制

        }
    }

    private void showForceDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        TextView msg_tv = (TextView) view.findViewById(R.id.msg_tv);
        TextView no_tv = (TextView) view.findViewById(R.id.no_tv);
        TextView yes_tv = (TextView) view.findViewById(R.id.yes_tv);

        goingView = LayoutInflater.from(context).inflate(R.layout.dialog_updategoing, null);

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setContentView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        msg_tv.setText(updateInfo.msg);
        no_tv.setOnClickListener(this);
        yes_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_tv:
                alertDialog.dismiss();
                break;
            case R.id.yes_tv:
                alertDialog.setContentView(goingView);
                break;
        }
    }

    void setStartUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(updateInfo.url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();

                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 获得当前APP的版本号
     *
     * @return
     */
    private int getVersion() {
        int versionCode = 0;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
