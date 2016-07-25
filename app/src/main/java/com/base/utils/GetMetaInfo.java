package com.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 获取META-INF文件夹下文件名称(空文件)
 * 用于邀请码,渠道号等操作
 * http://www.cnblogs.com/ct2011/p/4152323.html
 */
public class GetMetaInfo {
    private static String channel = null;
    public static String getChannel(Context context) {
        if (channel != null) {
            return channel;
        }
        final String start_flag = "META-INF/invite_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (channel == null || channel.length() <= 0) {
            channel = "kxg";//读不到渠道号就默认官方渠道
        }
        return channel;
    }
}
