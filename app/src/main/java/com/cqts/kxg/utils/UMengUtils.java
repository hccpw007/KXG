package com.cqts.kxg.utils;

import android.content.Context;
import android.text.TextUtils;
import com.cqts.kxg.main.MyApplication;
import com.umeng.analytics.MobclickAgent;

/**
 * 友盟数据统计方法
 */
public class UMengUtils {
    /**
     * 注册友盟数据统计
     */
    public static void setUMeng(Context context) {
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL); //注册友盟
        MobclickAgent.enableEncrypt(true);//友盟加密
        MobclickAgent.openActivityDurationTrack(false); //不使用自带的统计,使用自定义统计页面
    }

    /**
     * 开启页面
     */
    public static void setOnPageStart(Context context) {
        try {
            MobclickAgent.onResume(context);
            MobclickAgent.onPageStart(context.getClass().getName());
        } catch (Exception e) {
        }
    }

    /**
     * 关闭页面
     */
    public static void setOnonPageEnd(Context context) {
        try {
            MobclickAgent.onPause(context);
            MobclickAgent.onPageEnd(context.getClass().getName());
        } catch (Exception e) {
        }
    }

    /**
     * 统计帐号登入
     */
    public static void setSignIn() {
        try {
            if (null != MyApplication.userInfo && !TextUtils.isEmpty(MyApplication.userInfo
                    .user_name)) {
                MobclickAgent.onProfileSignIn(MyApplication.userInfo.user_name);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 统计帐号登出
     */
    public static void setSignOff() {
        try {
            if (null != MyApplication.userInfo && !TextUtils.isEmpty(MyApplication.userInfo
                    .user_name)) {
                MobclickAgent.onProfileSignOff();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 保存友盟的数据
     */
    public static void setKillProcess(Context context) {
        try {
            MobclickAgent.onKillProcess(context);//保存友盟的数据
        } catch (Exception e) {
        }
    }
}
