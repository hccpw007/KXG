package com.cqts.kxg.utils;


import com.android.volley.Request;
import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.bean.SceneInfo;
import com.cqts.kxg.bean.SigninInfo;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.bean.ClassifyListInfo;
import com.cqts.kxg.bean.NineInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * 所有的网络请求
 * Created by Administrator on 2016/5/24.
 */
public class MyHttp {
    private static HashMap<String, String> httpMap = new HashMap<>();
    private static String url = "http://api.kxg99.com/";

    private MyHttp() {
    }

    /**
     * http请求返回的结果
     */
    public interface MyHttpResult {
        void httpResult(Integer which, int code, String msg, Object bean);
    }

    /**
     * 请求网络,并把结果返回成相应的bean
     */
    private static void toBean(int method, HttpForVolley http, Integer which, HashMap<String,
            String> httpMap
            , String url, final MyHttpResult myHttpResult, final Type bean) {
        http.goTo(method, which, httpMap, url, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                Object data = null;
                if (null != bean) {
                    data = BaseValue.gson.fromJson(response.optString("data"), bean);
                }
                myHttpResult.httpResult(which, response.optInt("code", 1), response.optString
                        ("msg", "发生错误"), data);
            }
        });
    }


//--------------------       以下是具体接口           ---------------------

    /**
     * 登录<p>
     * user_name string 用户名 此项存在手机号不用传入<br>
     * mobile_phone int 手机号码 此项存在用户名不用传入<br>
     * password string 用户密码<br>
     */
    public static void signin(HttpForVolley http, Integer which, String username, String password,
                              final MyHttpResult myHttpResult) {
        String httpUrl = url + "user/signin";
        httpMap.clear();
        httpMap.put("username", username);
        httpMap.put("password", password);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, SigninInfo.class);
    }

    /**
     * 场景菜单查询 <p>
     * 加载首页场景分类目前只有十个场景分类菜单<br>
     */
    public static void scene(HttpForVolley http, Integer which,
                             MyHttpResult myHttpResult) {
        String httpUrl = url + "home/scene";
        Type type = new TypeToken<List<SceneInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, type);
    }

    /**
     * 注册用户<p>
     * 注册用户，需要通过图形验证码获取到短信验证码，然后携带短信验证码过来注册。<br>
     * captcha 短信验证码<br>
     * imgcaptcha 图片验证码<br>
     * mobile_phone 手机号码<br>
     * password 密码<br>
     * invite_code 邀请码，可选<br>
     */
    public static void signup(HttpForVolley http, Integer which, String captcha, String
            imgcaptcha, String mobile_phone, String password, String invite_code,
                              final MyHttpResult myHttpResult) {
        String httpUrl = url + "user/signup";
        httpMap.clear();
        httpMap.put("captcha", captcha);
        httpMap.put("imgcaptcha", imgcaptcha);
        httpMap.put("mobile_phone", mobile_phone);
        httpMap.put("password", password);
        httpMap.put("invite_code", invite_code);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 短信验证码<p>
     * phone 手机号<br>
     * captcha 图形验证码<br>
     * token 这里如果是重置密码用，那么必须传入 Token ，同时图形验证码不需要传入<br>
     * act 行为：<br>
     * 1、注册验证码<br>
     * 2、登陆验证码<br>
     * 3、找回密码验证码<br>
     * 4、重置密码验证码<br>
     */
    public static void sms(HttpForVolley http, Integer which, String phone, String captcha, int
            act, String token,
                           final MyHttpResult myHttpResult) {
        httpMap.clear();
        httpMap.put("phone", phone);
        httpMap.put("captcha", captcha);
        httpMap.put("act", act + "");
        httpMap.put("token", token);
        String httpUrl = url + "captcha/sms";
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, null);
    }


    /**
     * 快捷登陆<p>
     * <p/>
     * 接口和注册一样需要先获取图形验证码，然后获取短信验证码<p>
     * mobile_phone 手机号码<br>
     * captcha 短信验证码<br>
     * imgcaptcha 图片验证码<br>
     * password 设置的密码， 可选<br>
     */
    public static void quickSignin(HttpForVolley http, Integer which, String captcha, String
            imgcaptcha, String mobile_phone, String password,
                                   final MyHttpResult myHttpResult) {
        String httpUrl = url + "user/quicksignin";
        httpMap.clear();
        httpMap.put("captcha", captcha);
        httpMap.put("imgcaptcha", imgcaptcha);
        httpMap.put("mobile_phone", mobile_phone);
        httpMap.put("password", password);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, SigninInfo.class);
    }


    /**
     * 找回密码<br>
     * 接口和注册一样需要先获取图形验证码，然后获取短信验证码<p>
     * mobile_phone 手机号码<br>
     * captcha 短信验证码<br>
     * imgcaptcha 图片验证码<br>
     * password 设置的密码<br>
     */
    public static void password(HttpForVolley http, Integer which, String captcha, String
            imgcaptcha, String mobile_phone, String password,
                                final MyHttpResult myHttpResult) {
        String httpUrl = url + "user/password";
        httpMap.clear();
        httpMap.put("captcha", captcha);
        httpMap.put("imgcaptcha", imgcaptcha);
        httpMap.put("mobile_phone", mobile_phone);
        httpMap.put("password", password);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 获取用户个人资料<br>
     * 接口用于登陆后获取用户信息， 默认读取缓存中的用户信息（缓存时间5五分钟）<p>
     */
    public static void getUserInfo(HttpForVolley http, Integer which, String token,
                                   final MyHttpResult myHttpResult) {
        httpMap.clear();
        httpMap.put("token", token);
        String httpUrl = url + "user/profile";
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, UserInfo.class);
    }

    /**
     * 获取用户个人资料<br>
     * 接口用于登陆后获取用户信息， 默认读取缓存中的用户信息（缓存时间5五分钟）<p>
     * token 登陆的时候获取到的 Token
     */
    public static void refreshToken(HttpForVolley http, Integer which, String token,
                                    final MyHttpResult myHttpResult) {
        httpMap.clear();
        httpMap.put("token", token);
        String httpUrl = url + "user/refresh";
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, SigninInfo.class);
    }


    /**
     * 查询首页推荐文章 <p>
     */
    public static void articleList(HttpForVolley http, Integer which, int article_type, int
            PerPage, int Page,
                                   MyHttpResult myHttpResult) {
        String httpUrl = url + "article/listing";
        httpMap.clear();
        httpMap.put("article_type", article_type + "");
        httpMap.put("share_sum","desc");
        httpMap.put("add_time", "desc");
        httpMap.put("perPage", PerPage + "");
        httpMap.put("page", Page + "");
        Type type = new TypeToken<List<ArticleInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 上传图片 <p>
     */
    public static void uploadImage(HttpForVolley http, Integer which, String path,
                                   final HttpForVolley.HttpTodo httpTodo) {
        httpMap.clear();
        String httpUrl = url + "user/upload_avatar";
        http.postBase64(Request.Method.POST, null, httpMap, path, httpUrl, httpTodo);
    }

    /**
     * 9.9包邮接口 <p>
     * 获取9.9包邮商品列表及banner图（注：当PageNum值为1的时候才会返回banner数据） <br>
     */
    public static void jkj(HttpForVolley http, Integer which, int PageSize, int
            PageNum, MyHttpResult myHttpResult) {
        String httpUrl = url + "cheap/jkj";
        httpMap.clear();
        httpMap.put("PageSize", PageSize + "");
        httpMap.put("PageNum", PageNum + "");
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, NineInfo.class);
    }

    /**
     * 分类集合查询 <p>
     */
    public static void category(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String httpUrl = url + "goods/category";
        Type type = new TypeToken<List<ClassifyListInfo>>() {}.getType();
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, type);
    }

    /**
     * 搜索商品 <p>
     */
    public static void searchGoods(HttpForVolley http, Integer which,int PageSize,int PageNum,String keyword,String sort, String order,MyHttpResult myHttpResult) {
        String httpUrl = url + "search/goods";
        httpMap.clear();
        httpMap.put("PageSize",PageSize+"");
        httpMap.put("PageNum",PageNum+"");
        httpMap.put("keyword",keyword);
        httpMap.put("sort",sort);
        httpMap.put("order",order);
        Type type = new TypeToken<List<GoodsInfo>>() {}.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }
    /**
     * 搜索文章 <p>
     */
    public static void searchArticle(HttpForVolley http, Integer which,int PageSize,int PageNum,String keyword,MyHttpResult myHttpResult) {
        String httpUrl = url + "search/article";
        httpMap.clear();
        httpMap.put("PageSize",PageSize+"");
        httpMap.put("PageNum",PageNum+"");
        httpMap.put("keyword",keyword);
        Type type = new TypeToken<List<ArticleInfo>>() {}.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }
}
