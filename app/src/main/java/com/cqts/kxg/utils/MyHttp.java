package com.cqts.kxg.utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.base.BaseValue;
import com.base.http.HttpForVolley;
import com.cqts.kxg.bean.SceneInfo;
import com.cqts.kxg.bean.SigninInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 所有的网络请求
 * Created by Administrator on 2016/5/24.
 */
public class MyHttp {
    private static HashMap<String, String> httpMap = new HashMap<>();
    private static String url = "http://api.kxg99.com/";

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
//                    data = BaseValue.gson.fromJson(response.optString("data"), bean);
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
                             final MyHttpResult myHttpResult) {
        String httpUrl = url + "home/scene";
        httpMap.clear();
        Type type = new TypeToken<List<SceneInfo>>() {}.getType();
        toBean(Request.Method.GET, http, which, httpMap, httpUrl, myHttpResult, type);
    }
}
