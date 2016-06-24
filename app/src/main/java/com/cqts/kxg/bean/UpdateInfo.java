package com.cqts.kxg.bean;

/**
 * 检测是否升级
 */
public class UpdateInfo {
    public int version; //版本号
    public String url; //下载连接
    public boolean forcibly; //是否强制升级
    public String msg; //升级信息
}
