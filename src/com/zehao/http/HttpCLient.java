package com.zehao.http;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.zehao.constant.CONSTANT;

public class HttpCLient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void postJson(Context context, String url, HttpEntity entity,
			AsyncHttpResponseHandler responseHandler) {
		client.post(context, getAbsoluteUrl(url), null, entity,
				"application/json", responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return CONSTANT.BASE_URL + relativeUrl;
	}

	// 带参数，获取json对象或者数组
	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.post(getAbsoluteUrl(urlString), params, res);
	}
}