package com.cqts.kxg.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.base.http.HttpForVolley;
import com.cqts.kxg.R;
import com.cqts.kxg.bean.UpdateInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 自动更新
 */
public class UpdateUtils implements MyHttp.MyHttpResult, View.OnClickListener, Handler.Callback,
        DialogInterface.OnKeyListener {
    public AlertDialog alertDialog;
    private Activity context;
    private UpdateInfo updateInfo;
    private View goingView;
    private ProgressBar progress;
    private TextView sizeTv;
    private TextView planTv;
    private boolean isCanUpdate = true;
    private File apkFile;
    private Handler handler = new Handler(this);


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
        if (getVersion() == updateInfo.version) {
            //版本号相同,不需要更新
            return;
        }
        showForceDialog();
        if (updateInfo.forcibly) { //需要强制更新
            alertDialog.setContentView(goingView);
            setStartUpdate();
        }
    }

    private void showForceDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        TextView msg_tv = (TextView) view.findViewById(R.id.msg_tv);
        TextView no_tv = (TextView) view.findViewById(R.id.no_tv);
        TextView yes_tv = (TextView) view.findViewById(R.id.yes_tv);

        goingView = LayoutInflater.from(context).inflate(R.layout.dialog_updategoing, null);
        progress = (ProgressBar) goingView.findViewById(R.id.progress);
        sizeTv = (TextView) goingView.findViewById(R.id.size_tv);
        planTv = (TextView) goingView.findViewById(R.id.plan_tv);
        TextView cancel_tv = (TextView) goingView.findViewById(R.id.cancel);

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setContentView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        msg_tv.setText(updateInfo.msg);
        no_tv.setOnClickListener(this);
        yes_tv.setOnClickListener(this);
        cancel_tv.setOnClickListener(this);
        alertDialog.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_tv:
                alertDialog.dismiss();
                break;
            case R.id.yes_tv:
                alertDialog.setContentView(goingView);
                setStartUpdate();
                break;
            case R.id.cancel:
                setClose();
                break;
        }
    }

    private void setStartUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sdpath;
                    boolean sdCardExist = Environment.getExternalStorageState().equals
                            (Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
                    if (sdCardExist) {
                        sdpath = Environment.getExternalStorageDirectory().getPath() + "/";
                    } else {
                        sdpath = Environment.getRootDirectory().getParentFile().getPath() + "/";
                    }
                    String mSavePath = sdpath + "download";
                    String apkFilePath = mSavePath + "/开心购99_" + updateInfo.version + ".apk";
                    File file = new File(mSavePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    apkFile = new File(apkFilePath);
                    URL url = new URL(updateInfo.url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    float length = conn.getContentLength();
                    String size = String.format("%.2f", length / 1024 / 1024);
                    Message message = handler.obtainMessage();
                    message.what = -1;
                    message.obj = size;
                    handler.sendMessage(message);
                    InputStream is = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    float haveSize = 0;
                    while ((len = is.read(buffer)) != -1 && isCanUpdate) {
                        fos.write(buffer, 0, len);
                        haveSize += len;
                        //已经下载的百分比
                        int v = (int) (Double.valueOf(String.format("%.2f", haveSize / length)) *
                                100);
                        handler.sendEmptyMessage(v);
                    }
                    Message message2 = handler.obtainMessage();
                    message2.what = -2;
                    message2.obj = apkFilePath;
                    handler.sendMessage(message2);
                    fos.flush();
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    if (null != apkFile && apkFile.exists()) {
                        apkFile.delete();
                    }
                    if (null != alertDialog&&alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            }
        }).start();
    }

    /**
     * 获得当前APP的版本号
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

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == -1) {//下载开始
            sizeTv.setText("总共:" + (String) msg.obj + "M");
            return false;
        }
        if (msg.what == -2 && isCanUpdate) {//下载完成
            alertDialog.dismiss();
            installAPK((String) msg.obj);
            return false;
        }
        if (msg.what >= 0) {
            progress.setProgress(msg.what);
            planTv.setText(msg.what + "");
        }
        return false;
    }

    private void installAPK(String path) {
        String type = "application/vnd.android.package-archive";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)), type);
        context.startActivity(intent);
    }

    public void setClose() {
        isCanUpdate = false;
        if (null!=apkFile&&apkFile.exists()) {
            apkFile.delete();
        }
        if (null != alertDialog&&alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
