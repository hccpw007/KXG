package com.cqts.kxg.bean;

/**
 * 登录的返回数据
 * Created by Administrator on 2016/5/24.
 */
public class SigninInfo {
    String expired_at;
    String created_at;
    String token;
    boolean bind_mobile;

    public boolean isBind_mobile() {
        return bind_mobile;
    }

    public void setBind_mobile(boolean bind_mobile) {
        this.bind_mobile = bind_mobile;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}