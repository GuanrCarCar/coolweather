package com.coolweather.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.FontRequest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Daily_Forecast;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdataService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;
import java.io.StringReader;
import java.util.prefs.Preferences;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherAcitvity extends AppCompatActivity {
public DrawerLayout drawerLayout;
private Button navButton;
public SwipeRefreshLayout swipeRefresh;
private String mWeatherId;
private ScrollView weatherLayout;//滚动视图对象
    private TextView titleUpdataTime;//基本信息-更新时间
    private TextView titleCity;//基本信息-城市名
    private TextView titleLat;//基本信息-经度
    private TextView titleLon;//基本信息-纬度
    private TextView weatherInfoText;//实时天气信息--天气信息
    private TextView flText;//实时天气信息--体感温度
    private TextView humText;//实时天气信息--相对湿度
    private TextView pcpnText;//实时天气信息--降水量
    private TextView presText;//实时天气信息--大气压强
    private TextView degreeText;//实时天气信息--温度
    private TextView visText;//实时天气信息--能见度
    private TextView dirText;//实时天气信息--风向
    private TextView scText;//实时天气信息--风力
    private TextView spdText;//实时天气信息--风速
    private LinearLayout forecastLayout;//线性布局对象--预报天气
    private TextView aqiText;//空气质量--空气质量指数

    private TextView coText;//空气质量--一氧化碳指数

    private TextView no2Text;//空气质量--二氧化氮指数

    private TextView o3Text;//空气质量--臭氧指数

    private TextView pm10Text;//空气质量--PM10指数

    private TextView pm25Text;//空气质量--PM2.5指数

    private TextView qltyText;//空气质量--空气质量水平

    private TextView so2Text;//空气质量--二氧化硫指数

    private TextView airText;//生活建议--空气质量指数

    private TextView comfortText;//生活建议--舒适度指数

    private TextView carWashText;//生活建议--洗车指数

    private TextView drsgText;//生活建议--穿衣指数

    private TextView fluText;//生活建议--感冒指数

    private TextView sportText;//生活建议--运动指数

    private TextView travText;//生活建议--旅游指数

    private TextView uvText;//生活建议--紫外线指数

    private ImageView bingPicImg;//背景图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//版本控制（当系统版本大于21，也就是5.0以上系统才会执行后面的代码）
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
         setContentView(R.layout.activity_weather);

        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdataTime=(TextView)findViewById(R.id.title_update_time);
        titleLat=(TextView)findViewById(R.id.lat_text);
        titleLon=(TextView)findViewById(R.id.lon_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        flText=(TextView)findViewById(R.id.fl_text);
        humText=(TextView)findViewById(R.id.hum_text);
        pcpnText=(TextView)findViewById(R.id.pcpn_text);
        presText=(TextView)findViewById(R.id.pres_text);
        degreeText=(TextView)findViewById(R.id.degree_text);
        visText=(TextView)findViewById(R.id.vis_text);
        dirText=(TextView)findViewById(R.id.dir_text);
        scText=(TextView)findViewById(R.id.sc_text);
        spdText=(TextView)findViewById(R.id.spd_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        coText=(TextView)findViewById(R.id.co_text);
        no2Text=(TextView)findViewById(R.id.no2_text);
        o3Text=(TextView)findViewById(R.id.o3_text);
        pm10Text=(TextView)findViewById(R.id.pm10_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        qltyText=(TextView)findViewById(R.id.qlty_text);
        so2Text=(TextView)findViewById(R.id.so2_text);
        airText=(TextView)findViewById(R.id.air_text);
        comfortText=(TextView)findViewById(R.id.comfort_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        drsgText=(TextView)findViewById(R.id.drsg_text);
        fluText=(TextView)findViewById(R.id.flu_text);
        sportText=(TextView)findViewById(R.id.sport_text);
        travText=(TextView)findViewById(R.id.trav_text);
        uvText=(TextView)findViewById(R.id.uv_text);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navButton=(Button)findViewById(R.id.nav_button);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        //获取SwipeRefreshLayout的实例
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        //调用setColorSchemeResources()方法来设置下拉刷新进度条的颜色
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if (weatherString!=null){
            //有缓存时直接解析天气数据

            Weather weather=Utility.handleWeatherResponse(weatherString);
            Log.d("MainActivity","ttttt"+weather);

            mWeatherId=weather.basic.weatherId;
        showWeatherInfo(weather);

        }else {
       //无缓存时去服务器查询天气
            mWeatherId=getIntent().getStringExtra("weather_id");
            //在请求数据的时候将ScrollView()隐藏
            weatherLayout.setVisibility(View.INVISIBLE);
            //从服务器请求天气数据
            requestWeather(mWeatherId);
        }
        //设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);//请求天气信息

            }
        });
        String bingPic=prefs.getString("bing_pic",null);
        if (bingPic!=null){
            //如果有缓存数据就直接使用Glide来加载这张图片
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            //如果没有缓存数据就调用loadBingPic（）方法去请求今日的必应背景图
            loadBingPic();
        }
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }




    public void requestWeather(final String weatherId){
//组装接口地址
        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=9d6c0e1ec1184af1b8ce82db3cbe401a";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            final String responseText=response.body().string();
            //将返回的JSON数据转换成Weather对象

                final Weather weather=Utility.handleWeatherResponse(responseText);
                Log.d("MainActivity","ttttt"+responseText);
                Log.d("MainActivity","ttttt"+weather.now.more.info);


                //将当前线程切换到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            //将返回的数据缓存到SharedPreferences当中
                            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(
                                    WeatherAcitvity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId=weather.basic.weatherId;
                            Log.d("MainActivity","tt222"+weather);


                            //调用showWeatherInfo()方法进行内容显示
                            showWeatherInfo(weather);

                        }else{

                            Toast.makeText(WeatherAcitvity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);//刷新事件结束，将进度条隐藏起来
                    }
                });
                //m每次q请求天气信息的时候也会刷新背景图片。
                loadBingPic();
            }
            @Override
            public void onFailure(Call call, IOException e) {
e.printStackTrace();
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        Toast.makeText(WeatherAcitvity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        swipeRefresh.setRefreshing(false);//刷新事件结束，将进度条隐藏起来
    }
});
            }


        });
        loadBingPic();//加载每日一图
    }
    private void loadBingPic() {

        String requestBingPic="http://guolin.tech/api/bing_pic";
        //调用HttpUtil.sendOkHttpRequest()方法获取必应背景图的链接
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
             final String bingPic=response.body().string();
             // 将链接缓存到SharedPreferences当中
                SharedPreferences.Editor editor=PreferenceManager .getDefaultSharedPreferences(WeatherAcitvity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //使用Glide来加载图片
                        Glide.with(WeatherAcitvity.this).load(bingPic).into(bingPicImg);

                    }
                });
            }
        });
    }
    private void showWeatherInfo(Weather weather) {
        String cityName=weather.basic.cityName;
        Log.d("MainActivity","time12"+weather.basic.cityLat);
        Log.d("MainActivity","time21"+weather.basic.cityLon);
        String updataTime="更新时间:"+weather.basic.updataaa.updata;
        Log.d("MainActivity","time25"+weather.basic.tz);
        Log.d("MainActivity","time22"+weather.basic.city);
        Log.d("MainActivity","time23"+weather.basic.cityid);
        Log.d("MainActivity","time24"+weather.basic.updataaa);

//        String updataTime="更新时间:"+weather.basic.updata.split("")[1];
//        String updataTime="更新时间:8:00";
        String lat="经度"+weather.basic.cityLat;
        String lon="纬度"+weather.basic.cityLon;
        titleCity.setText(cityName);
        titleUpdataTime.setText(updataTime);
        titleLat.setText(lat);
        titleLon.setText(lon);
        String weatherInfo=weather.now.more.info;
        String flInfo =weather.now.fl+"℃";
        String humInfo=weather.now.hum;
        String pcpnInfo=weather.now.pcpn+"mm";
        String presInfo=weather.now.pres+"pa";
        String degree= weather.now.temperature+"℃";
        String visInfo=weather.now.vis;
        String dirInfo=weather.now.wind_dir;
        String scInfo=weather.now.wind_sc;
        String spdInfo=weather.now.wind_spd+"m/s";
        weatherInfoText.setText(weatherInfo);
        flText.setText(flInfo);
        humText.setText(humInfo);
        pcpnText.setText(pcpnInfo);
        presText.setText(presInfo);
        degreeText.setText(degree);
        visText.setText(visInfo);
        dirText.setText(dirInfo);
        scText.setText(scInfo);
        spdText.setText(spdInfo);
        forecastLayout.removeAllViews();
        for (Daily_Forecast forecast :weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dataText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);
            TextView dirText=(TextView)view.findViewById(R.id.dir_text);
            TextView scText=(TextView)view.findViewById(R.id.sc_text);
//            String date2="2019-11-30";

            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);

            forecastLayout.addView(view);
        }

        if (weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);



            pm25Text.setText(weather.aqi.city.pm25);
            qltyText.setText(weather.aqi.city.qlty);

        }

//        String air="空气质量"+weather.suggestion.air.info;
        String comfort="舒适度"+weather.suggestion.comfort.info;
        String carWash="洗车指数"+weather.suggestion.carWash.info;
//        String drsg="穿衣指数"+weather.suggestion.drsg.info;
//        String flu="感冒指数"+weather.suggestion.flu.info;
        String sport="运动建议"+weather.suggestion.sport.info;
//        String trav="陆游指数"+weather.suggestion.trav.info;
//        String uv="紫外线指数"+weather.suggestion.uv.info;
//        airText.setText(air);
        comfortText.setText(comfort);
        carWashText.setText(carWash);
//        drsgText.setText(drsg);
//        fluText.setText(flu);
        sportText.setText(sport);
//        travText.setText(trav);
//        uvText.setText("");
//在设置完所有数据后，再将ScrollView设为可见
        weatherLayout.setVisibility(View.VISIBLE);
//激活AutoUpdateService这个服务，只要选中了某个城市并成功更新天气之后，
        // AutoUpdateService就会一直在后台运行，并保证每8个小时更新一次天气
        Intent intent=new Intent(this, AutoUpdataService.class);
        startService(intent);






    }
}
