package com.cqts.kxg.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.cqts.kxg.bean.ArticleInfo;
import com.cqts.kxg.bean.EaringApprenticeInfo;
import com.cqts.kxg.bean.EaringsInfo;
import com.cqts.kxg.bean.EarnInfo;
import com.cqts.kxg.bean.HomeBannerInfo;
import com.cqts.kxg.bean.GoodsInfo;
import com.cqts.kxg.bean.HomeSceneInfo;
import com.cqts.kxg.bean.HomeTableInfo;
import com.cqts.kxg.bean.MyApprenticeInfo;
import com.cqts.kxg.bean.MyUrlInfo;
import com.cqts.kxg.bean.RankingInfo;
import com.cqts.kxg.bean.ShopInfo;
import com.cqts.kxg.bean.SigninInfo;
import com.cqts.kxg.bean.UpdateInfo;
import com.cqts.kxg.bean.UserInfo;
import com.cqts.kxg.bean.ClassifyListInfo;
import com.cqts.kxg.bean.NineInfo;
import com.cqts.kxg.center.LoginActivity;
import com.cqts.kxg.main.MainActivity;
import com.cqts.kxg.main.MyApplication;
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
    private static String url = "https://api.kxg99.com/";

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
    private static void toBean(int method, final HttpForVolley http, Integer which, HashMap<String,
            String> httpMap
            , String url, final MyHttpResult myHttpResult, final Type bean) {
        http.goTo(method, which, httpMap, url, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                int code = response.optInt("code", 1);
                //登录失效
                if (code == 2 && (http.getContext().getClass() != MainActivity.class)) {
                    Context context = http.getContext();
                    Toast.makeText(context, "登录失效,请重新登录!", Toast.LENGTH_SHORT).show();
                    MyApplication.userInfo = null;
                    MyApplication.token = "";
                    SPutils.setToken("");
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
                }

                Object data = null;
                if (null != bean && code == 0) {
                    data = BaseValue.gson.fromJson(response.optString("data"), bean);
                }
                if (null!=myHttpResult){
                    myHttpResult.httpResult(which, code, response.optString("msg", "发生错误"), data);
                }
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
        Type type = new TypeToken<List<HomeSceneInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, type);
    }

    /**
     * 首页banner图 <p>
     */
    public static void homeBanner(HttpForVolley http, Integer which,
                                  MyHttpResult myHttpResult) {
        String httpUrl = url + "home/banner";
        Type type = new TypeToken<List<HomeBannerInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, type);
    }

    /**
     * 首页活动按钮(分类,店铺街,9.9) <p>
     */
    public static void homemenu(HttpForVolley http, Integer which,
                                MyHttpResult myHttpResult) {
        String httpUrl = url + "home/menu";
        Type type = new TypeToken<List<HomeTableInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, type);
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
        httpMap.put("share_sum", "desc");
        httpMap.put("add_time", "desc");
        httpMap.put("perPage", PerPage + "");
        httpMap.put("page", Page + "");
        httpMap.put("token", MyApplication.token);
        Type type = new TypeToken<List<ArticleInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
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
     * 5、绑定手机号<br>
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
     * 此接口用于找回密码和绑定手机号的短信验证码发送
     * 必须发送token 不需要图片验证码
     */
    public static void sms2(HttpForVolley http, Integer which, String phone, int
            act, MyHttpResult myHttpResult) {
        httpMap.clear();
        httpMap.put("phone", phone);
        httpMap.put("act", act + "");
        httpMap.put("token", MyApplication.token);
        String httpUrl = url + "user/appsms";
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 快捷登陆<p>
     * <p>
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
        String httpUrl = url + "user/forgot";
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
    public static void getUserInfo(HttpForVolley http, Integer which,
                                   final MyHttpResult myHttpResult) {
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        String httpUrl = url + "user/profile";
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, UserInfo.class);
    }

    /**
     * 刷新token <br>
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
     * 上传图片 <p>
     */
    public static void uploadImage(HttpForVolley http, Integer which, String path,
                                   final HttpForVolley.HttpTodo httpTodo) {
        httpMap.clear();
        String httpUrl = url + "user/upload_avatar";
//        String httpUrl ="http://caiji.kxg99.com/test.php";
        httpMap.put("token", MyApplication.token);
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
        Type type = new TypeToken<List<ClassifyListInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, type);
    }

    /**
     * 热门搜索关键字 <p>
     */
    public static void hotKeyword(HttpForVolley http, Integer which, HttpForVolley.HttpTodo
            httpTodo) {
        String httpUrl = url + "search/hotKeyword";
        http.goTo(Request.Method.GET, which, null, httpUrl, httpTodo);
    }

    /**
     * 搜索商品 <p>
     */
    public static void searchGoods(HttpForVolley http, Integer which, int PageSize, int PageNum,
                                   String keyword, String sort, String order, String cat_id,
                                   MyHttpResult
                                           myHttpResult) {
        String httpUrl = url + "search/goods";
        httpMap.clear();
        httpMap.put("PageSize", PageSize + "");
        httpMap.put("PageNum", PageNum + "");
        httpMap.put("keyword", keyword);
        httpMap.put("sort", sort);
        httpMap.put("order", order);
        httpMap.put("cat_id", cat_id);
        Type type = new TypeToken<List<GoodsInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 搜索文章 <p>
     */
    public static void searchArticle(HttpForVolley http, Integer which, int PageSize, int
            PageNum, String keyword, MyHttpResult myHttpResult) {
        String httpUrl = url + "search/article";
        httpMap.clear();
        httpMap.put("PageSize", PageSize + "");
        httpMap.put("PageNum", PageNum + "");
        httpMap.put("keyword", keyword);
        httpMap.put("token", MyApplication.token);
        Type type = new TypeToken<List<ArticleInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 搜索店铺 <p>
     */
    public static void searchShop(HttpForVolley http, Integer which, int PageSize, int PageNum,
                                  String keyword, String sort, MyHttpResult myHttpResult) {
        String httpUrl = url + "search/shops";
        httpMap.clear();
        httpMap.put("PageSize", PageSize + "");
        httpMap.put("PageNum", PageNum + "");
        httpMap.put("keyword", keyword);
        httpMap.put("sort", sort);
        Type type = new TypeToken<List<ShopInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 热门文章 <p>
     */
    public static void articleHot(HttpForVolley http, Integer which, int perPage, int
            page, int hot_type, MyHttpResult myHttpResult) {
        String httpUrl = url + "article/hot";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("perPage", perPage + "");
        httpMap.put("page", page + "");
        httpMap.put("hot_type", hot_type + "");
        Type type = new TypeToken<List<ArticleInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 首页文章分类查询列表 <p>
     */
    public static void articleListing(HttpForVolley http, Integer which, int PageSize, int
            PageNum, String cat_id,String sort, MyHttpResult myHttpResult) {
        String httpUrl = url + "article/listing";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("cat_id", cat_id);
        httpMap.put("perPage", PageSize + "");
        httpMap.put("page", PageNum + "");
        httpMap.put("sort", sort);
        Type type = new TypeToken<List<ArticleInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 获取个人中心收益<br>
     */
    public static void userEarning(HttpForVolley http, Integer which,
                                   final MyHttpResult myHttpResult) {
        String httpUrl = url + "user/earning";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, EarnInfo.class);
    }

    /**
     * 获取个人中心热门商品 <p>
     */
    public static void goodsRecommend(HttpForVolley http, Integer which, MyHttpResult
            myHttpResult) {
        String httpUrl = url + "goods/recommend";
        httpMap.clear();
        httpMap.put("PageSize",30+"");
        Type type = new TypeToken<List<GoodsInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 修改用户性别<br>
     */
    public static void userSex(HttpForVolley http, Integer which, int sex, MyHttpResult
            myHttpResult) {
        String httpUrl = url + "user/sex";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("sex", sex + "");
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 修改用户昵称<br>
     */
    public static void userAlias(HttpForVolley http, Integer which, String alias, MyHttpResult
            myHttpResult) {
        String httpUrl = url + "user/alias";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("alias", alias);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 获取个人中心滚动消息 <p>
     */
    public static void withdraw(HttpForVolley http, Integer which, HttpForVolley.HttpTodo
            httpTodo) {
        String httpUrl = url + "user/withdraw/listing";
        http.goTo(Request.Method.GET, which, null, httpUrl, httpTodo);
    }

    /**
     * 收益 & 提现 <p>
     * type 1、提现 2、收益
     */
    public static void userDetails(HttpForVolley http, Integer which, int type, int pageNum, int
            pageSize, MyHttpResult myHttpResult) {
        String httpUrl = url + "user/details";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("type", type + "");
        httpMap.put("page", pageNum + "");
        httpMap.put("perPage", pageSize + "");
        Type type1 = new TypeToken<List<EaringsInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type1);
    }

    /**
     * 牛人排行 <p>
     * type 1、提现 2、收益
     */
    public static void userRanking(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String httpUrl = url + "user/ranking";
        Type type = new TypeToken<List<RankingInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, type);
    }

    /**
     * 我的喜欢-商品 <p>
     * article:文章,good商品,supplier:商品
     */
    public static void loveGoods(HttpForVolley http, Integer which, int pageNum, int pageSize,
                                 MyHttpResult myHttpResult) {
        String httpUrl = url + "user/collect";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("type", "good");
        httpMap.put("page", pageNum + "");
        httpMap.put("perPage", pageSize + "");
        Type type = new TypeToken<List<GoodsInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 我的喜欢-文章 <p>
     * article:文章,good商品,supplier:商品
     */
    public static void loveArticle(HttpForVolley http, Integer which, int pageNum, int pageSize,
                                   MyHttpResult myHttpResult) {
        String httpUrl = url + "user/collect";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("type", "article");
        httpMap.put("page", pageNum + "");
        httpMap.put("perPage", pageSize + "");
        Type type = new TypeToken<List<ArticleInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 我的喜欢-店铺 <p>
     * article:文章,good商品,supplier:商品
     */
    public static void loveShop(HttpForVolley http, Integer which, int pageNum, int pageSize,
                                MyHttpResult myHttpResult) {
        String httpUrl = url + "user/collect";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("type", "supplier");
        httpMap.put("page", pageNum + "");
        httpMap.put("perPage", pageSize + "");
        Type type = new TypeToken<List<ShopInfo>>() {
        }.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }

    /**
     * 查询文章是否喜欢<p>
     */
    public static void articleCollect(HttpForVolley http, Integer which, String article_id,
                                      HttpForVolley.HttpTodo httpTodo) {
        String httpUrl = url + "article/collect";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("article_id", article_id);
        http.goTo(Request.Method.GET, which, httpMap, httpUrl, httpTodo);
    }

    /**
     * 查询商品是否喜欢<p>
     */
    public static void goodsCollect(HttpForVolley http, Integer which, String goods_id,
                                    HttpForVolley.HttpTodo httpTodo) {
        String httpUrl = url + "goods/collect";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("goods_id", goods_id);
        http.goTo(Request.Method.GET, which, httpMap, httpUrl, httpTodo);
    }

    /**
     * 收藏文章<p>
     * behavior 0 : 关注 1 : 取关
     */
    public static void articleLove(HttpForVolley http, Integer which, String article_id, int
            behavior,
                                   MyHttpResult myHttpResult) {
        String httpUrl = url + "article/love";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("article_id", article_id);
        httpMap.put("behavior", behavior + "");
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, ArticleInfo.class);
    }


    /**
     * 收藏商品<p>
     * is_attention 0 : 关注 1 : 取关
     */
    public static void userLoveGoods(HttpForVolley http, Integer which, String goods_id, int
            is_attention, MyHttpResult myHttpResult) {
        String httpUrl = url + "user/lovegoods";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("goods_id", goods_id);
        httpMap.put("is_attention", is_attention + "");
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 最近完成新手任务的徒弟
     * 这里接口返回一个多层级的 JSON 其中 task 代表最近完成新手任务的徒弟， signup 是最新注册的徒弟。<p>
     */
    public static void apprenticeListing(HttpForVolley http, Integer which,
                                         MyHttpResult myHttpResult) {
        String httpUrl = url + "user/apprentice/listing";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, MyApprenticeInfo
                .class);
    }

    /**
     * 收徒信息
     */
    public static void userApprentice(HttpForVolley http, Integer which,
                                      MyHttpResult myHttpResult) {
        String httpUrl = url + "user/apprentice";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, EaringApprenticeInfo
                .class);
    }

    /**
     * 修改邀请码<p>
     */
    public static void userInvitecode(HttpForVolley http, Integer which, String invite_code,
                                      MyHttpResult myHttpResult) {
        String httpUrl = url + "user/invitecode";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("invite_code", invite_code);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 绑定手机号<p>
     */
    public static void bindPhone(HttpForVolley http, Integer which, String mobile_phone, String
            captcha, MyHttpResult myHttpResult) {
        String httpUrl = url + "user/bind";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("mobile_phone", mobile_phone);
        httpMap.put("captcha", captcha);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 分享添加收益回调的接口<p>
     * 分享文章或者商品
     */
    public static void articleShare(HttpForVolley http, Integer which, String article_id, String
            goods_id, MyHttpResult myHttpResult) {
        String httpUrl = url + "user/articleShare";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("article_id", article_id);
        httpMap.put("goods_id", goods_id);
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 阅读添加收益<p>
     */
    public static void userRead(HttpForVolley http, Integer which, String article_id,MyHttpResult myHttpResult) {
        String httpUrl = url + "user/read";
        httpMap.clear();
        httpMap.put("token", MyApplication.token);
        httpMap.put("article_id", article_id);
        if (TextUtils.isEmpty(MyApplication.token)){
            return;
        }
        toBean(Request.Method.POST, http, which, httpMap, httpUrl, myHttpResult, null);
    }

    /**
     * 阅读添加收益<p>
     */
    public static void update(HttpForVolley http, Integer which,MyHttpResult myHttpResult) {
        String httpUrl = url + "system/android";
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, UpdateInfo.class);
    }

    /**
     * 获取URL<p>
     */
    public static void getLinkIndex(HttpForVolley http, Integer which,MyHttpResult myHttpResult) {
        String httpUrl = url + "getLink/index";
        toBean(Request.Method.GET, http, which, null, httpUrl, myHttpResult, MyUrlInfo.class);
    }

    /**
     * 获取文章详情<p>
     */
    public static void articleDetail(HttpForVolley http, Integer which,String article_id,MyHttpResult myHttpResult) {
        String httpUrl = url + "article/detail";
        httpMap.clear();
        httpMap.put("article_id", article_id);
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, ArticleInfo.class);
    }
}