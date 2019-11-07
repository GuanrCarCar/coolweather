package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client =new OkHttpClient();
        Request request=new    Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
        //监听OKHttp的请求，如果点击了，反应
    }
}
