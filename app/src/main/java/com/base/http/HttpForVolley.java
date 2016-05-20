package com.base.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Base64;

import com.android.volley.AuthFailureError;
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

	/** 正常的请求数据接口 */
	public void post(int which, HashMap<String, String> httpMap, String url,
			HttpTodo todo) {
		this.todo = todo;
		if (null != request && url.equals(request.getUrl())) {
			request.cancel();
		}
		toHttp(which, httpMap, url);
	}

	/** Base64上传图片 */
	public void postBase64(int which, HashMap<String, String> httpMap,
			String imgPath, String url, HttpTodo todo) {
		this.todo = todo;
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(new File(imgPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			String encodeToString = Base64.encodeToString(buffer,
					Base64.DEFAULT);
			httpMap.put("base64File", encodeToString);
			httpMap.put("ext", "jpg");
			if (null == request) {
				toHttp(which, httpMap, url);
			} else {
				if (url.equals(request.getUrl())) {
					request.cancel();
				}
				toHttp(which, httpMap, url);
			}
		} catch (Exception e) {
			JSONObject object = new JSONObject();
			try {
				object.put("msg", "发生错误");
				object.put("status", "404");
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

	private void toHttp(final int which, final HashMap<String, String> httpMap,
			final String url) {
		request = new StringRequest(Method.POST, url, new Listener<String>() {

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
					object.put("status", "404");
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
				if (BaseValue.isDebug) {
					Logger.d(url);
					Logger.json(new JSONObject(httpMap).toString());
				}
				return httpMap;
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
		 * @param which
		 *            msg.which 用于标识请求来源 which ==404表示网络错误
		 * @param response
		 *            请求具体结果 JSONObject 格式
		 */
		public void httpTodo(int which, JSONObject response);
	}

}
