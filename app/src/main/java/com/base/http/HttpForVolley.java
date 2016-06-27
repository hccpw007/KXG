package com.base.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
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
    Activity activity;
    Fragment fragment;
    Context context;

    public HttpForVolley(Activity activity) {
        this.activity = activity;
        context = activity;
    }

    public HttpForVolley(Fragment fragment) {
        this.fragment = fragment;
        context = fragment.getActivity();
    }

    public Context getContext() {
        return context;
    }

    /**
     * 正常的请求数据接口
     */
    public void goTo(int Method, Integer which, HashMap<String, String> httpMap, String url,
                     HttpTodo todo) {
        if (null != request && url.equals(request.getUrl())) {
            request.cancel();
        }
        toHttp(Method, which, httpMap, url, todo);
    }

    /**
     * Base64上传图片
     */
    public void postBase64(int Method, Integer which, HashMap<String, String> httpMap,
                           String imgPath, String url, HttpTodo todo) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(imgPath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String encodeToString = Base64.encodeToString(buffer,
                    Base64.DEFAULT);
            httpMap.put("avatar", encodeToString);
            if (null == request) {
                toHttp(Method, which, httpMap, url, todo);
            } else {
                if (url.equals(request.getUrl())) {
                    request.cancel();
                }
                toHttp(Method, which, httpMap, url, todo);
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
                        String url, final HttpTodo todo) {
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
                if (activity != null) {
                    Logger.json(new JSONObject(map).put("url", url).put("activity",
                            activity.getClass().getName()).put("Method", httpMethod).toString());
                } else {
                    Logger.json(new JSONObject(map).put("url", url).put("activity",
                            fragment.getActivity().getClass().getName()).put("Method",
                            httpMethod).toString());
                }
            } catch (JSONException e) {
            }
        }

        //对所有参数进行URL编码,以防中文服务器无法解析
        if (httpMap != null && Method == Request.Method.GET) {
            for (String key : httpMap.keySet()) {
                try {
                    if (!key.equals("token")) {
                        httpMap.put(key, URLEncoder.encode(httpMap.get(key), "utf-8"));
                    }
                } catch (Exception e) {
                }
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
        if (activity != null) {
            request.setTag(activity);
        } else {
            request.setTag(fragment);
        }
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
