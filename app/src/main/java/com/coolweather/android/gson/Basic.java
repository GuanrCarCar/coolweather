package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("city")
    public String cityName;//城市名

    @SerializedName("id")
    public String weatherId;//城市对应的天气ID

    @SerializedName("lat")
    public String cityLat;//城市的经度

    @SerializedName("lon")
    public String cityLon;//城市的纬度

    @SerializedName("tz")
    public String tz;//11

    @SerializedName("location")
    public String city;//11

    @SerializedName("cid")
    public String cityid;//11

    @SerializedName("update")
    public Updataa updataaa;

    public class Updataa {
        @SerializedName("loc")
        public String updata;//接口更新时间
        @SerializedName("utc")
        public String updatax;//接口更新时间
    }
}
