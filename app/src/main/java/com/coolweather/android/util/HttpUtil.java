package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        //创建一个OkHttp点击实例
        OkHttpClient client =new OkHttpClient();
        //创建一个request的请求对象，发起一条Http请求，通过url的方式设置网络地址；
        Request request=new    Request.Builder().url(address).build();
        //调用okHttpclient的newcall方法创建一个call
        //调用enqueue将call加入调度队列，然后等待任务执行
        client.newCall(request).enqueue(callback);
        //监听OKHttp的请求，如果点击了，反应
    }
}
