package com.base.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.base.BaseValue;
import com.base.utils.Logger;

public class HttpForVolley {
    private StringRequest request;
    HttpTodo todo;
    Activity activity;

    public HttpForVolley(Activity activity) {
        this.activity = activity;
    }

    /**
     * 正常的请求数据接口
     */
    public void goTo(int Method, Integer which, HashMap<String, String> httpMap, String url,
                     HttpTodo todo) {
        this.todo = todo;
        if (null != request && url.equals(request.getUrl())) {
            request.cancel();
        }
        toHttp(Method, which, httpMap, url);
    }

    /**
     * Base64上传图片
     */
    public void postBase64(int Method, Integer which, HashMap<String, String> httpMap,
                           String imgPath, String url, HttpTodo todo) {
        this.todo = todo;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(imgPath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String encodeToString = Base64.encodeToString(buffer,
                    Base64.DEFAULT);
            httpMap.put("avatar", encodeToString);
//            httpMap.put("ext", "jpg");
            if (null == request) {
                toHttp(Method, which, httpMap, url);
            } else {
                if (url.equals(request.getUrl())) {
                    request.cancel();
                }
                toHttp(Method, which, httpMap, url);
            }
        } catch (Exception e) {
            JSONObject object = new JSONObject();
            try {
                object.put("msg", "发生错误");
                object.put("code", "404");
                todo.httpTodo(which, object);
            } catch (JSONException e1) {
            }
            return;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void toHttp(int Method, final Integer which, final HashMap<String, String> httpMap,
                        String url) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (null != httpMap) {
            map = httpMap;
        }
        if (BaseValue.isDebug) {
            try {
                String httpMethod = "";
                if (Method == Request.Method.GET)
                    httpMethod = "GET";
                if (Method == Request.Method.POST)
                    httpMethod = "POST";
                Logger.json(new JSONObject(map).put("url", url).put("activity",
                        activity.getClass().getName()).put("Method",httpMethod).toString());
            } catch (JSONException e) {
            }
        }

        //组装get参数
        if (Method == Request.Method.GET) {
            if (httpMap != null && httpMap.size() > 0) {
                url = url + "?";
                for (String key : httpMap.keySet()) {
                    url = url + key + "=" + httpMap.get(key) + "&";
                }
            }
            if (url.endsWith("&")) {
                url = url.substring(0, url.length() - 1);
                System.out.println(url);
            }
        }


        request = new StringRequest(Method, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    if (BaseValue.isDebug) {
                        Logger.json(response);
                    }
                    JSONObject result = new JSONObject(response);
                    todo.httpTodo(which, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("msg", "网络错误");
                    object.put("code", "404");
                    todo.httpTodo(which, object);

                    if (BaseValue.isDebug) {
                        Logger.json(object.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (null == httpMap) {
                    return new HashMap<String, String>();
                } else {
                    return httpMap;
                }
            }
        };
        request.setTag(activity);
        BaseValue.mQueue.add(request);
    }

    /**
     * 网络请求结束后todo
     */
    public interface HttpTodo {
        /**
         * 网络请求结果返回
         *
         * @param which    msg.which 用于标识请求来源 which ==404表示网络错误
         * @param response 请求具体结果 JSONObject 格式
         */
        public void httpTodo(Integer which, JSONObject response);
    }

}
