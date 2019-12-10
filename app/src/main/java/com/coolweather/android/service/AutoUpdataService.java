package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;
import java.util.prefs.Preferences;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdataService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updataBingpic();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHuor=8*60*60*1000;//8小时的毫数秒
        long triggerAtTime= SystemClock.elapsedRealtime()+anHuor;
        Intent i=new Intent(this,AutoUpdataService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }



    //更新天气
    private  void  updateWeather(){
        //将更新后的数据存储在SharedPreferences文件中，打开WeatherActivity的时候会优先从SharedPreferences缓存中读取数据
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =prefs.getString("weather",null);
        if (weatherString!=null){
            //有缓存时直接解析天气数据
           Weather weather= Utility.handleWeatherResponse(weatherString);
            String weatherId=weather.basic.weatherId;
            String weatherUrl="http://guolin.tech/api/weather?cityid=?"+weatherId+"&key=9d6c0e1ec1184af1b8ce82db3cbe401a";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                    Weather weather=Utility.handleWeatherResponse(responseText);

                    if (weather!=null&&"ok".equals(weather.status)){

                    SharedPreferences.Editor editor=
                            PreferenceManager.getDefaultSharedPreferences(AutoUpdataService.this).edit();
                    editor.putString("weather",responseText);
                    editor.apply();
                }
                }
            });
        }
    }
    private void updataBingpic() {
        //将更新后的数据存储在SharedPreferences文件中，打开WeatherActivity的时候会优先从SharedPreferences缓存中读取的数据
        String requestBingPic="Http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
           String bingPic=response.body().string();
           SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdataService.this).edit();
           editor.putString("bing_pic",bingPic);
           editor.apply();
            }
        });
    }

}
