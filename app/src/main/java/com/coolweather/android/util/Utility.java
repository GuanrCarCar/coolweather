package com.coolweather.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.android.WeatherAcitvity;
import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

public static Weather handleWeatherResponse(String response){
    try {
        JSONObject jsonObject=new JSONObject(response);
        JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
        String weatherContent =jsonArray.getJSONObject(0).toString();
        return new Gson().fromJson(weatherContent, Weather.class);
    } catch (JSONException e) {
        e.printStackTrace();
    }

    return null;
}

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)){
            try {

                //将服务器返回的数据传入到JSONArray对象allProvinces中
                JSONArray allProvinces =new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    //循环遍历JSONAray
                    //从中取出的每一个对象都是JSONObject对象
                    JSONObject provinceObject= allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }



    public static boolean handleCityResponse(String response,int provinceId){
        //解析eh和处理服务器返回的省级数据

        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities=new JSONArray(response);
                for (int i= 0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                        city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    //解析和处理返回的县数据
    public  static boolean handleCountyResponse(String  response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }
}
